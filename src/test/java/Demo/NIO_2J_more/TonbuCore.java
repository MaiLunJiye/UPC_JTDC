package Demo.NIO_2J_more;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Created by simqin on 3/18/17.
 */
public class TonbuCore implements Runnable {
    private InetSocketAddress[] mysocketaddrs;
    private int myLeath;
    private int otherLeath;
    private InetSocketAddress[] othersocketaddrs;
    private String key;
    private int JumpValue;
    private DatagramChannel[] mychannels;

    public TonbuCore(InetSocketAddress[] mysocketaddrs, InetSocketAddress[] othersocketaddrs, String key) {
        this.mysocketaddrs = mysocketaddrs;
        this.othersocketaddrs = othersocketaddrs;
        this.myLeath = mysocketaddrs.length;
        this.otherLeath = othersocketaddrs.length;
        this.key = key;

        this.mychannels = new DatagramChannel[mysocketaddrs.length];

        try {
            for( int i = 0; i<mysocketaddrs.length; i++){
                DatagramChannel tempChannel = DatagramChannel.open();
                tempChannel.configureBlocking(false);
                tempChannel.socket().bind(mysocketaddrs[i]);
                mychannels[i] = tempChannel;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        //同步值
        long tempKey =  Long.parseLong(key);
        while(true){
            // 每秒变更
            this.JumpValue = (int) (System.currentTimeMillis() /10 % 10000 + tempKey);
            Thread.yield(); //让出cpu
        }
    }


    public Thread start(){
        Thread th = new Thread(this);
        th.setPriority(3);
        th.start();
        return th;
    }


    public int sendData(ByteBuffer data) {
        // 这里不能 data.flip() 不然数据就会被清理掉
        int ret = 0;
        try {
            int mytemp = JumpValue % myLeath;
            int othertemp = JumpValue % otherLeath;
            ret = mychannels[mytemp]
                    .send(
                            data,
                            othersocketaddrs[othertemp]
                    );
            System.out.println("toport>>>" + othersocketaddrs[othertemp].getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public SocketAddress getData(ByteBuffer data) {
        data.clear();
        SocketAddress ret = null;
        try {
            int mytemp = JumpValue % myLeath;
            ret = mychannels[mytemp].receive( data );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
