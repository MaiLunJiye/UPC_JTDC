package Demo.VideoTransport;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by 15070 on 2017/1/11.
 *
 * 第一次完成视频流在局域网传输，在这里做个笔记
 *
 * 使用方法，打开 Webcam_server和Webcam_client即可
 *
 *
 */
public class Webcam_server {
    BufferedImage bufferedImage;        //图像数据
    Webcam webcam;                      //摄像头

    DatagramSocket dso;                 //UDP套接字（传输用）


    //下面两个是构造函数，传入套接字的使用端口（没有时候就默认用 8888号端口）
    public Webcam_server(int key, String[] myip, int[] myport, String[] aimip, int[] aimport){

        webcam = Webcam.getDefault();       //获取摄像头
        try {
            dso = new DatagramSocket(8888);     //申请一个套接字，并且绑定一个默认端口 8888
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public Webcam_server(int port){
        webcam = Webcam.getDefault();
        try {
            dso = new DatagramSocket(port);         //套接字绑定输出端口
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    //启动摄像头
    public boolean webcam_open(){
        return this.webcam.open();
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

            System.out.println(image_buf.length);

            DatagramPacket dgp = new DatagramPacket(image_buf,image_buf.length,addr);
            dso.send(dgp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Webcam_server server = new Webcam_server(8888);         //实例化一个server类
        server.webcam_open();           //启动摄像头

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
