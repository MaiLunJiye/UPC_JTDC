package Transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;

/**
 * Created by honghem on 4/27/17.
 */
abstract public class TB_Core {
    protected InetSocketAddress[] mysocketaddrs;        //我的可用地址
    protected InetSocketAddress[] othersocketaddrs;     //目标可用地址
    protected DatagramChannel[] mychannels;             //我的可用通道
    protected Object key;                       // 同步秘钥

    /**
     * 构造函
     * 传入的地址数组顺序敏感
     * @param mysocketaddrs     自己可用地址数组
     * @param othersocketaddrs  目标可用地址数组
     * @param key               同步秘钥
     */
    public TB_Core(InetSocketAddress[] mysocketaddrs, InetSocketAddress[] othersocketaddrs, Object key) {
        this.mysocketaddrs = mysocketaddrs;
        this.othersocketaddrs = othersocketaddrs;
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
            System.exit(1);
        }
    }

    abstract public void start();
    abstract public int sendData(ByteBuffer data);
    abstract public SocketAddress getData(ByteBuffer data);

    public boolean close() {
        boolean ret = false;
        for (Channel ch: mychannels
             ) {
            try {
                ch.close();
            } catch (IOException e) {
                e.printStackTrace();
                ret = false;
            }
        }
        return ret;
    }
}
