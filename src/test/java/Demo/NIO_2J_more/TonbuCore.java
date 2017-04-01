package Demo.NIO_2Jump_More;

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
    private InetSocketAddress[] othersocketaddrs;
    private String key;
    private long JumpValue;
    private DatagramChannel[] mychannels;

    public TonbuCore(InetSocketAddress[] mysocketaddrs, InetSocketAddress[] othersocketaddrs, String key) {
        this.mysocketaddrs = mysocketaddrs;
        this.othersocketaddrs = othersocketaddrs;
        this.key = key;

        try {
            for( int i = 0; i<mysocketaddrs.length; i++){
                DatagramChannel tempChannel = DatagramChannel.open();
                tempChannel.socket().bind(mysocketaddrs[i]);
            }

            for( int i = 0; i<othersocketaddrs.length; i++){
                DatagramChannel tempChannel = DatagramChannel.open();
                tempChannel.socket().bind(othersocketaddrs[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        //同步值
        long tempKey =  Long.parseLong(key);
        while(true){
            // 每秒变更10次
            this.JumpValue =System.currentTimeMillis() /100 % 10000 + tempKey;
            Thread.yield(); //让出cpu
        }
    }



    public int sendData(ByteBuffer data) {
        data.flip();
        try {
            return mychannels[(int) (JumpValue % mychannels.length)]
                    .send(
                            data,
                            othersocketaddrs[(int) (JumpValue % othersocketaddrs.length)]
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public SocketAddress getData(ByteBuffer data) {
        data.clear();
        try {
            return mychannels[(int) (JumpValue % mychannels.length)] .receive( data );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
