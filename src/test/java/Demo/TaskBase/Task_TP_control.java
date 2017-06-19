package Demo.TaskBase;

import Demo.TaskBase.IO_task.TaskManager;
import Transport.Transport_interface;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by honghem on 6/10/17.
 */
public class Task_TP_control implements Transport_interface, Runnable{
    protected DatagramChannel mychannels[];
    protected InetSocketAddress aimAddress[];
    protected String key;
    protected DatagramChannel nowchannel;

    protected TaskManager outputTask;
    protected TaskManager inputTask;

    protected Thread optThread;
    protected boolean treadClose;

    protected ReentrantReadWriteLock intasklock;
    protected ReentrantReadWriteLock outtasklock;

    public Task_TP_control(DatagramChannel[] mychannels, InetSocketAddress[] aimAddress, String key) {
        this.mychannels = mychannels;
        this.aimAddress = aimAddress;
        this.key = key;
        nowchannel = mychannels[0];
        outputTask = new TaskManager(TaskManager.__DEFAULT_MAX_SIZE__);
        inputTask = new TaskManager(TaskManager.__DEFAULT_MAX_SIZE__);

        intasklock = new ReentrantReadWriteLock();
        outtasklock = new ReentrantReadWriteLock();

        optThread = new Thread(this);
        optThread.start();
        treadClose = false;
    }

    public boolean writeData(ByteBuffer buffer) {
        outtasklock.writeLock().lock();
        outputTask.addTask(buffer);
        outtasklock.writeLock().unlock();
        return true;
    }

    public boolean readData(ByteBuffer buffer) {
        intasklock.writeLock().lock();
        boolean ret = inputTask.popTask(buffer);
        intasklock.writeLock().unlock();
        return ret;
    }

    public void run() {
        int prepjvalue = CountJvalue.getvalue(key) % mychannels.length;
        int now;
        ByteBuffer buffer = ByteBuffer.allocate(70000);
        while(!treadClose) {
            now = CountJvalue.getvalue(key) % mychannels.length;
            if (now != prepjvalue) {
                nowchannel = mychannels[prepjvalue];
                prepjvalue = now;
                continue;
            }

            //clean next channl
            try {
                buffer.clear();
                mychannels[now].receive(buffer);
                buffer.clear();
                mychannels[now].receive(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //receive
            if(intasklock.writeLock().tryLock()){
                try {
                    buffer.clear();
                    nowchannel.receive(buffer);
                    buffer.flip();
                    inputTask.addTask(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    intasklock.writeLock().unlock();
                }
            }

            //send
            buffer.clear();
            if (outtasklock.writeLock().tryLock()) {
                try {
                    if (!outputTask.popTask(buffer)){
                        nowchannel.send(buffer,aimAddress[prepjvalue]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    outtasklock.writeLock().unlock();
                }
            }

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
