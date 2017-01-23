package Transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Created by root on 1/16/17.
 */
public class Jump_UDP implements Transport_interface, Runnable{
    //取得地址时候的参数 的 定义
    static final int __MY_ADDR_ = 0;            //取得我的地址
    static final int __AIM_ADDR_ = 1;           //取得目标地址
    static final int __MAX_VALUE__ = 100000;    //跳变值的最大值（大于他就取模），放置溢出

    //跳变相关
    private boolean jumping_flag;       //跳变标志（用来控制跳变同步进程是否继续）
    private Thread jumpThread;          //同步进程


    //同步相关
    private int key;                //互相的秘钥

    private String[] myip;          //我的地址集
    private int[] myport;           //我的端口集

    private String[] aimip;         //目标的地址集
    private int[] aimport;          //目标的端口集

    private DatagramChannel datagramChannel;        //通信用的UDP管道

    public Jump_UDP(int key, String[] myip, int[] myport, String[] aimip, int[] aimport) {
        this.key = key;
        this.myip = myip;
        this.myport = myport;
        this.aimip = aimip;
        this.aimport = aimport;

        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //取得 地址的函数，传入参数为 谁,使用相同的跳变策略
    private InetSocketAddress getAddr(int Who){
        String[] toip;
        int[] toport;
        if(Who == __MY_ADDR_){
            toip = this.myip;
            toport = this.myport;
        }else if(Who == __AIM_ADDR_){
            toip = this.aimip;
            toport = this.aimport;
        }else{
            System.out.println("Imput error in getAddr");
            return null;
        }

        //通过时间计算跳变值
        int time = (int)System.currentTimeMillis();
        int value = (key + time/1000) % __MAX_VALUE__;

        value = value<0 ? -value:value;

        System.out.println(value);

        return (new InetSocketAddress(toip[value % toip.length],
                                        toport[value % toport.length])
                                    );

    }


    public int writeData(ByteBuffer buffer) throws IOException {
        return datagramChannel.send(buffer, getAddr(__AIM_ADDR_) );
    }

    public SocketAddress readData(ByteBuffer buffer) throws IOException {
        return datagramChannel.receive(buffer);
    }

    /**
     * 子线程跳变：
     *      开启一个子线程，子线程负责连接的同步
     */
    public void run() {
        InetSocketAddress nowAddr = getAddr(__MY_ADDR_);
        InetSocketAddress currentAddr = nowAddr;
        jumping_flag = true;    //设置是否继续标志

        while (jumping_flag) {  //一直做死循环

            //每隔一段时间计算 从新计算自己的地址，查看跳变值是否相等
            while (nowAddr == (currentAddr = getAddr(__MY_ADDR_)) ){
                //如果一样就让出cpu，下次获得cpu再搞
                Thread.yield();     //让出cpu
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            try {
                //更改自己目前监听状态
                datagramChannel.socket().bind(currentAddr);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            nowAddr = currentAddr;      //刷新当前值
            Thread.yield();     //让出cpu
        }
    }

    public void jump_open() {
        jumpThread = new Thread(this);  //开启新的进程
        jumpThread.start();
    }

    //跳变停止，
    public void jump_stop(){
        jumping_flag = false;      //设置run（）里面的循环标志，让他跳出循环
        try {
            jumpThread.join();      //等待跳变进程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jump_stop();
    }
}
