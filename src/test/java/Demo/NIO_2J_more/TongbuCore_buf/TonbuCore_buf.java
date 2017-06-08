package Demo.NIO_2J_more.TongbuCore_buf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by simqin on 3/18/17.
 */
public class TonbuCore_buf implements Runnable {
    public static int __SEND_LENGTH_;
    public static int __RECIVE_LENGTH_;
    private InetSocketAddress[] mysocketaddrs;
    private InetSocketAddress[] othersocketaddrs;
    private String key;
    private Thread tongbu;
    private boolean isclose;
    private LinkedList<ByteBuffer> send_que;
    private LinkedList<ByteBuffer> reciv_eque;

    public String getKey() {
        return key;
    }

    public TonbuCore_buf(InetSocketAddress[] mysocketaddrs, InetSocketAddress[] othersocketaddrs, String key) {
        this.mysocketaddrs = mysocketaddrs;
        this.othersocketaddrs = othersocketaddrs;
        this.key = key;
        this.isclose = false;
        this.send_que = new LinkedList<ByteBuffer>();
        this.reciv_eque = new LinkedList<ByteBuffer>();
    }

    public void run() {
        //同步值
        int JumpValue = 0;
        int preValue = 9;
        long tempKey =  Long.parseLong(key);
        DatagramChannel nowChannel = null;

        try {
            nowChannel = DatagramChannel.open();
            nowChannel.socket().bind(mysocketaddrs[JumpValue % mysocketaddrs.length]);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        if (nowChannel == null) {
            System.exit(-1);
        }

        while(!isclose){
            // 每秒变更
            JumpValue = (int) (System.currentTimeMillis()/10000 % 10000 + tempKey);
            if(JumpValue != preValue) {
                preValue = JumpValue;
                try {
                    nowChannel.close();
                    nowChannel = DatagramChannel.open();
                    nowChannel.socket().bind(mysocketaddrs[JumpValue % mysocketaddrs.length]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (nowChannel == null) {
                System.out.println("channel is null!!!!!!!!!");
                System.exit(-1);
            }

            if (send_que.peek() != null) {
                try {
                    nowChannel.send(send_que.poll(), othersocketaddrs[JumpValue % othersocketaddrs.length]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reciv_eque.size() > __RECIVE_LENGTH_) {
                ByteBuffer buf = reciv_eque.poll();
                buf.clear();
                try {
                    nowChannel.receive(buf);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reciv_eque.offer(buf);
            } else {
//               TODO
            }



            Thread.yield(); //让出cpu
        }
    }


    public Thread start(){
        this.tongbu = new Thread(this);
        tongbu.setPriority(3);
        tongbu.start();
        return tongbu;
    }

    public boolean close() {
        this.isclose = true;
        return this.isclose;
    }


    public void sendData(ByteBuffer data) {
        // 这里不能 data.flip() 不然数据就会被清理掉
        int ret = 0;
        send_que.offer(data);
    }

    public boolean getData(ByteBuffer data) {
        data.clear();
        if(reciv_eque.peek() != null){
            data.put(reciv_eque.poll());
            return  true;
        }  else return false;
    }
}
