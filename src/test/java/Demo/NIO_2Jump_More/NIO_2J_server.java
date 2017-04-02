package Demo.NIO_2Jump_More;

import com.github.sarxos.webcam.Webcam;
import com.sun.jna.Structure;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

/**
 * Created by 15070 on 2017/1/11.
 *
 * 基于通道池的方法双向跳边
 */
public class NIO_2J_server {
    BufferedImage bufferedImage;        //图像数据
    Webcam webcam;                      //摄像头

    HashMap<String, DatagramChannel> dchannelMap;                 //我方通道哈希
    HashMap<String, InetSocketAddress> aimAddr;                 //目标地址哈希

    String jumpValue;

    int defaultPort = 8888;


    public BufferedImage getBufferedImage(){
        return bufferedImage;
    }

    //让摄像头拍摄一张照片，并且把照片存放到 bufferedImage里面
    public boolean capture(){
        bufferedImage = webcam.getImage();
        return bufferedImage==null?false:true;
    }

    //构造函数
    public NIO_2J_server(HashMap<String, DatagramChannel> dchannelMap, HashMap<String, InetSocketAddress> aimAddr){
        webcam = Webcam.getDefault();
        this.dchannelMap = dchannelMap;
        this.aimAddr = aimAddr;
        webcam.open();

        jumpValue = "Default";

        try {
            // 设置默认套接字
            DatagramChannel defChannel = DatagramChannel.open();
            defChannel.socket().bind( new InetSocketAddress(defaultPort));
            defChannel.configureBlocking(false);

            dchannelMap.put(jumpValue, defChannel);

            aimAddr.put(jumpValue, new InetSocketAddress("127.0.0.1",7777));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //发送 bufferedImage ， 传入的参数是发送目标的地址
    public void sendImage(InetSocketAddress addr){
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();       //java io技术，申请一个处理流
            ImageIO.write(bufferedImage,"PNG",os);
            byte[] image_buf = os.toByteArray();
            ByteBuffer byteBuffer = ByteBuffer.wrap(image_buf);

            dchannelMap.get(jumpValue).send(byteBuffer, aimAddr.get(jumpValue));      //通过 jvalue值 查找对应的通道

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        //构建哈希表
        String[] keys = new String[]{
                "J2_one",
                "J2_two",
                "J2_three",
                "J2_four",
                "J2_five",
                "J2_six",
                "J2_seven",
                "J2_eight",
                "J2_nine",
                "J2_ten",
                };

        InetSocketAddress myAddr[] = new InetSocketAddress[keys.length];
        InetSocketAddress aimAddr[] = new InetSocketAddress[keys.length];

        for(int i = 0; i < keys.length; i++) {
            myAddr[i] = new InetSocketAddress(7000 + i);
            aimAddr[i] = new InetSocketAddress(8000 + i);
        }

        HashMap<String, DatagramChannel> myChannels = new HashMap<String, DatagramChannel>();
        HashMap<String, InetSocketAddress> aimAddrMap = new HashMap<String, InetSocketAddress>();

        for (int i = 0; i < keys.length; i++){
            try {
                DatagramChannel dags = DatagramChannel.open();
                dags.socket().bind(myAddr[i]);

                myChannels.put(keys[i], dags);
                aimAddrMap.put(keys[i], aimAddr[i]);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        NIO_2J_server server = new NIO_2J_server(myChannels, aimAddrMap);         //实例化一个server类

        //设置传输地址
        InetSocketAddress clientaddr = new InetSocketAddress("127.0.0.1",8889);

        while (true){
            //循环发送
            server.capture();       //拍摄
            server.sendImage(clientaddr);       //发送

            //系统休眠50毫秒
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
