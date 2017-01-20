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

        int time = (int)System.currentTimeMillis();
        System.out.println(time);
        value = (key + time/10) % __MAX_VALUE__;

        return (new InetSocketAddress(toip[value % myip.length], toport[value % myport.length] ));
    }

    private void regetValue(){
        int time = (int)System.currentTimeMillis();
        System.out.println(time);
        value = (key + time/10) % __MAX_VALUE__;
        value = value < 0? -value : value;
    }

    public int writeData(ByteBuffer buffer) throws IOException {
        return datagramChannel.write(buffer);
    }

    public int readData(ByteBuffer buffer) throws IOException {
        return datagramChannel.write(buffer);
    }

    public void run() {
        regetValue();
        int now = value;
        jumping_flag = true;
        while (jumping_flag) {

            while (now == value){
                regetValue();

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                String now_myip = myip[value % myip.length];
                int now_myport = myport[value % myport.length];
                System.out.println(now_myip + now_myport);
                datagramChannel.socket().bind(new InetSocketAddress( myip[value % myip.length], myport[value % myport.length] ));
                datagramChannel.connect(new InetSocketAddress( aimip[value % myip.length], myport[value % myport.length] ));
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void jump_stop(){
        jumping_flag = false;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jump_stop();
    }
}
