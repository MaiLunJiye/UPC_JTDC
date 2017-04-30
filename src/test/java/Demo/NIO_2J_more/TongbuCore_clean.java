package Demo.NIO_2J_more;

import Transport.TB_Core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by honghem on 4/27/17.
 * 第二代同步core，通过使用清空接收缓冲区的方法释放缓冲区
 */
public class TongbuCore_clean extends TB_Core implements Runnable{
    static long jumppinglv = 1;
    static long GET_W_LOCK_TIMEOUT = 2;
    static long GET_R_LOCK_TIMEOUT = 5;


    private boolean isclose;
    protected DatagramChannel nowChannel;
    protected DatagramChannel nextChannel;
    private Thread tongbu;
    private ReentrantReadWriteLock lock;
    private long jumpValue;

    /**
     * 构造函数
     * 传入的地址数组顺序敏感
     * @param mysocketaddrs    自己可用地址数组
     * @param othersocketaddrs 目标可用地址数组
     * @param key              同步秘钥
     */
    public TongbuCore_clean(InetSocketAddress[] mysocketaddrs, InetSocketAddress[] othersocketaddrs, String key) {
        super(mysocketaddrs, othersocketaddrs, key);
        this.isclose = true;
        this.nowChannel = mychannels[0];
        this.nextChannel = this.nowChannel;
        lock = new ReentrantReadWriteLock();
    }

    public void run() {
        jumpValue = 0;
        long jcode_next = 0;
        ByteBuffer buffer = ByteBuffer.allocate(70000);
        while(isclose) {
            jcode_next = System.currentTimeMillis() / jumppinglv % 100000;
            //如果值没有改变，则清理下一个channel
            if (jcode_next == jumpValue) {
                try {
                    nextChannel.receive(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buffer.clear();
                Thread.yield(); //让出cpu

            } else {    //已经改变
                jumpValue = jcode_next;
                try {
                    if (lock.writeLock().tryLock(this.GET_W_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                            nowChannel = nextChannel;
                            nextChannel = mychannels[
                                    ((int)jumpValue + Integer.parseInt((String)key))
                                            % mychannels.length];
                            lock.writeLock().unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("tongbu Thread cant change");
                }
            }

            Thread.yield();
        }
    }

/*    protected DatagramChannel updataSelectChannel(int jumpvalue) {
        try {
            if (lock.writeLock().tryLock(this.GET_W_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                try {
                    nowChannel = nextChannel;
                    nextChannel = mychannels[
                            (jumpvalue + Integer.parseInt((String)key))
                                    % mychannels.length];
                } finally {
                    lock.writeLock().unlock();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return this.nowChannel;
    }*/

    public boolean close() {
        isclose = false;

        try {
            tongbu.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.close();
        return isclose;
    }

    public void start() {
        isclose = true;
        tongbu = new Thread(this);
        tongbu.start();
    }


    public int sendData(ByteBuffer data) {
        int ret = 0;
        try {
            if (lock.readLock().tryLock(this.GET_R_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                try {
                    nowChannel.send(data, othersocketaddrs[
                            (int)jumpValue + Integer.parseInt((String) key)
                                    % othersocketaddrs.length]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lock.readLock().unlock();
            } else {
                System.out.println("send error,cant get channel");
                ret = -1;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            ret = -1;
        }
        return  ret;
    }

    public SocketAddress getData(ByteBuffer data) {
        return null;
    }

}
