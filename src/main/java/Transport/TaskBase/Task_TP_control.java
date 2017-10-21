package Transport.TaskBase;

import Transport.JumpValueCounter.CanCountJumpValue;
import Transport.JumpValueCounter.JVCounterBySysTime;
import Transport.TaskBase.IO_task.TaskManager;
import Transport.Transport_interface;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Task_TP_control implements Transport_interface, Runnable{
    /**
     　* 基于事件的跳变控制类,是外部程序调用跳变模块的入口的实现
     　* @ClassName：Task_TP_control
     　*/

    protected DatagramChannel mychannels[];
    protected InetSocketAddress aimAddress[];

    protected String mykey;
    protected String aimkey;
    protected CanCountJumpValue jumpValueCounter = new JVCounterBySysTime();


    protected DatagramChannel nowchannel;

    protected TaskManager outputTask;
    protected TaskManager inputTask;

    protected Thread optThread;
    protected boolean treadClose;

    protected ReentrantReadWriteLock intasklock;
    protected ReentrantReadWriteLock outtasklock;

    /**
     * 构造函数
     *      双方的IP地址簇顺序敏感
     * @param myaddrs 我的可用跳变IP地址簇
     * @param aimAddress 目标的可用跳变IP地址簇
     * @param mykey 我放的Key
     * @param aimkey 目标的Key
     */
    public Task_TP_control(InetSocketAddress[] myaddrs, InetSocketAddress[] aimAddress, String mykey, String aimkey) {
        DatagramChannel datagramChannel[] = new DatagramChannel[myaddrs.length];

        for(int i = 0; i<myaddrs.length; i++) {
            try {
                datagramChannel[i] = DatagramChannel.open();
                datagramChannel[i].configureBlocking(false);
                datagramChannel[i].socket().bind(myaddrs[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        this.mychannels = datagramChannel;
        this.aimAddress = aimAddress;
        this.mykey = mykey;
        this.aimkey = aimkey;

        nowchannel = mychannels[0];
        outputTask = new TaskManager(TaskManager.__DEFAULT_MAX_SIZE__);
        inputTask = new TaskManager(TaskManager.__DEFAULT_MAX_SIZE__);

        intasklock = new ReentrantReadWriteLock();
        outtasklock = new ReentrantReadWriteLock();

        optThread = new Thread(this);
        optThread.setPriority(3);
        optThread.start();
        System.out.println("start");
        treadClose = false;
    }

    /**
     * 自定义同步值生成器
     * @param jumpValueCounter 同步值生成器
     */
    public void setJumpValueCounter(CanCountJumpValue jumpValueCounter) {
        this.jumpValueCounter = jumpValueCounter;
    }

    @Override
    public boolean writeData(ByteBuffer buffer) {
        outputTask.addTask(buffer);
        return true;
    }

    @Override
    public boolean readData(ByteBuffer buffer) {
        boolean ret = inputTask.popTask(buffer);
        return ret;
    }


    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(inputTask.getLimite());
        InetSocketAddress sendTo = null;
        SocketAddress dataSource = null;

        int MyPrepJumpValue = 0;
        int AimPrepJumpValue = 0;

        int MyNowJumpValue = 0;
        int AimNowJumpValue = 0;

        int tmpJumpValue = 0;

        while(!treadClose) {

            //============== receive =================
            tmpJumpValue = jumpValueCounter.countJumpValue(mykey);
            if (tmpJumpValue != MyNowJumpValue) {
                MyPrepJumpValue = MyNowJumpValue;
                MyNowJumpValue = tmpJumpValue;
            }

            tmpJumpValue = jumpValueCounter.countJumpValue(aimkey);
            if (tmpJumpValue != AimNowJumpValue) {
                AimPrepJumpValue = AimNowJumpValue;
                AimNowJumpValue = tmpJumpValue;
            }

            nowchannel = mychannels[MyPrepJumpValue % mychannels.length];
            try {
                buffer.clear();
                dataSource = nowchannel.receive(buffer);
                if (dataSource != null) {
                    System.out.println(nowchannel.getLocalAddress() + " rec <--" + dataSource + " : " + buffer);
                    buffer.flip();
                    inputTask.addTask(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //=================send 发送====================
            tmpJumpValue = jumpValueCounter.countJumpValue(mykey);
            if (tmpJumpValue != MyNowJumpValue) {
                MyPrepJumpValue = MyNowJumpValue;
                MyNowJumpValue = tmpJumpValue;
            }
            tmpJumpValue = jumpValueCounter.countJumpValue(aimkey);
            if (tmpJumpValue != AimNowJumpValue) {
                AimPrepJumpValue = AimNowJumpValue;
                AimNowJumpValue = tmpJumpValue;
            }
            nowchannel = mychannels[MyPrepJumpValue % mychannels.length];

            buffer.clear();
            try {
                if (outputTask.popTask(buffer)){
                    sendTo = aimAddress[AimPrepJumpValue % aimAddress.length];
                    System.out.println("send:" + nowchannel.getLocalAddress() + "-->" + sendTo);
                    nowchannel.send(buffer,sendTo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //================clean======================
            tmpJumpValue = jumpValueCounter.countJumpValue(mykey);
            if (tmpJumpValue != MyNowJumpValue) {
                MyPrepJumpValue = MyNowJumpValue;
                MyNowJumpValue = tmpJumpValue;
            }
            tmpJumpValue = jumpValueCounter.countJumpValue(aimkey);
            if (tmpJumpValue != AimNowJumpValue) {
                AimPrepJumpValue = AimNowJumpValue;
                AimNowJumpValue = tmpJumpValue;
            }

            try {
                buffer.clear();
                mychannels[MyNowJumpValue % mychannels.length].receive(buffer);
                buffer.clear();
                mychannels[MyNowJumpValue % mychannels.length].receive(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }


//            try {
//                Thread.sleep(20);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            Thread.yield();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        treadClose = true;
        optThread.join();
        for(int i = 0; i<mychannels.length; i++){
            mychannels[i].close();
        }
    }
}
