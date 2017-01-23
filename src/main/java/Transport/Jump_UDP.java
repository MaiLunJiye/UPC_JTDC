package Transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Created by root on 1/16/17.
 */
public class Jump_UDP implements Transport_interface, Runnable{
    static final int __MY_ADDR_ = 0;
    static final int __AIM_ADDR_ = 1;
    static final int __MAX_VALUE__ = 100000;

    private int value;
    private boolean jumping_flag;
    private Thread jumpThread;


    private int key;

    private String[] myip;
    private int[] myport;

    private String[] aimip;
    private int[] aimport;

    private DatagramChannel datagramChannel;

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

        //通过时间计算跳边值
        int time = (int)System.currentTimeMillis();
        System.out.println(time);
        value = (key + time/10) % __MAX_VALUE__;

        return (new InetSocketAddress(toip[value % myip.length], toport[value % myport.length] ));
    }

    //重新计算跳变值
    private void regetValue(){
        int time = (int)System.currentTimeMillis();
        System.out.println(time);
        value = (key + time/1000) % __MAX_VALUE__;
        value = value < 0? -value : value;
    }

    public int writeData(ByteBuffer buffer) throws IOException {
        return datagramChannel.write(buffer);
    }

    public int readData(ByteBuffer buffer) throws IOException {
        return datagramChannel.write(buffer);
    }

    /**
     * 子线程跳变：
     *      开启一个子线程，子线程负责连接的同步
     */
    public void run() {
        regetValue();           //从新计算跳变值
        int now = value;        //记录当前跳变值
        jumping_flag = true;    //设置是否继续标志

        //先取得第一次的 ip，port值
        String now_myip = myip[value % myip.length];
        int now_myport = myport[value % myport.length];

        while (jumping_flag) {  //一直做死循环

            //每隔一段时间计算 从新计算跳变值，查看跳变值是否相等
            while (now == value){
                //如果相等了就等待一段时间，然后再次判断
                Thread.yield();     //让出cpu
                regetValue();   //再次刷新
            }


            try {
                datagramChannel.socket().bind(new InetSocketAddress( myip[value % myip.length], myport[value % myport.length] ));
                datagramChannel.connect(new InetSocketAddress( aimip[value % myip.length], myport[value % myport.length] ));
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
