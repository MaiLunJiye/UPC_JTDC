package Demo.VideoTransport;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Created by 15070 on 2017/1/11.
 *
 * 基于NIO的视频传输
 * 基本原理和 IO 一样
 */
public class NIO_tp_server {
    BufferedImage bufferedImage;        //图像数据
    Webcam webcam;                      //摄像头

    DatagramChannel dso;                 //UDP管道


    //下面两个是构造函数，传入套接字的使用端口（没有时候就默认用 8888号端口）

    public NIO_tp_server(int port){
        webcam = Webcam.getDefault();
        try {
            dso = DatagramChannel.open();
            dso.configureBlocking(false);   //非阻塞模式
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        webcam.open();
    }

    public BufferedImage getBufferedImage(){
        return bufferedImage;
    }

    //让摄像头拍摄一张照片，并且把照片存放到 bufferedImage里面
    public boolean capture(){
        bufferedImage = webcam.getImage();
        return bufferedImage==null?false:true;
    }

    //发送 bufferedImage ， 传入的参数是发送目标的地址
    public void sendImage(InetSocketAddress addr){
        ByteArrayOutputStream os=new ByteArrayOutputStream();       //java io技术，申请一个处理流
        try {
            ImageIO.write(bufferedImage,"PNG",os);
            byte[] image_buf = os.toByteArray();
            ByteBuffer byteBuffer = ByteBuffer.wrap(image_buf);
            dso.send(byteBuffer, addr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NIO_tp_server server = new NIO_tp_server(8888);         //实例化一个server类

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
