package Transport.TaskBase;

import Transport.TaskBase.IO_task.TaskManager;
import Transport.Transport_interface;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by honghem on 6/10/17.
 */
public class Task_TP_control implements Transport_interface, Runnable{
    protected DatagramChannel mychannels[];
    protected InetSocketAddress aimAddress[];

    protected String mykey;
    protected String aimkey;

    protected DatagramChannel nowchannel;

    protected TaskManager outputTask;
    protected TaskManager inputTask;

    protected Thread optThread;
    protected boolean treadClose;

    protected ReentrantReadWriteLock intasklock;
    protected ReentrantReadWriteLock outtasklock;

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

    public boolean writeData(ByteBuffer buffer) {
        outputTask.addTask(buffer);
        return true;
    }

    public boolean readData(ByteBuffer buffer) {
        boolean ret = inputTask.popTask(buffer);
        return ret;
    }


    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(inputTask.getLimite());
        InetSocketAddress sendTo = null;
        SocketAddress dataSource = null;
        while(!treadClose) {
            nowchannel = mychannels[ CountJvalue.getvalue(mykey) % mychannels.length ];
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

            //send 发送
            nowchannel = mychannels[ CountJvalue.getvalue(mykey) % mychannels.length ];
            buffer.clear();
            try {
                if (outputTask.popTask(buffer)){
                    sendTo = aimAddress[CountJvalue.getvalue(aimkey) % aimAddress.length];
                    System.out.println("send:" + nowchannel.getLocalAddress() + "-->" + sendTo);
                    nowchannel.send(buffer,sendTo);
                }
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
