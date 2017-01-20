package Demo.Video_Jump;

import Demo.VideoTransport.Webcam_client;
import Demo.VideoTransport.Webcam_server;
import Transport.Jump_UDP;
import com.github.sarxos.webcam.Webcam;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by root on 1/16/17.
 */

class ServerTest{
    public static void main(String[]args){
        int key = 123;
        int[] myport = {8888,8889,8890};
        String[] myip = { "127.0.0.1"};
        int[] aimport = {7777,7778,7779};
        String[] aimip = {"127.0.0.1"};

       Webcam_serverJump wserver = new Webcam_serverJump(key, myip, myport, aimip, aimport);

       wserver.start();
       while (true){
           try {
               wserver.sendImg();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }
}

public class Webcam_serverJump {
    static int ByteLength = 60000;

    BufferedImage bufferedImage;        //图像数据
    Webcam webcam;                      //摄像头
    ByteArrayOutputStream os;

    byte[] bs;
    ByteBuffer buffer;


    Jump_UDP judp;

    public Webcam_serverJump(int key, String[] myip, int[] myport, String[] aimip, int[] aimport){
        bs = new byte[2 * ByteLength];
        webcam = Webcam.getDefault();
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        buffer = ByteBuffer.wrap(bs);
        judp = new Jump_UDP(key, myip,myport, aimip, aimport);
    }

    public boolean capture() throws IOException {
        bufferedImage = webcam.getImage();      //拍照
        ImageIO.write(bufferedImage,"PNG",os);  //写入流类
        bs = os.toByteArray();

        return bufferedImage==null?false:true;
    }

    public void start(){
        webcam.open();      //开启摄像头服务
        new Thread(judp).start();   //开启跳变服务
    }

    public void sendImg() throws IOException {
        capture();      //先拍照
        judp.writeData(buffer);     //后传输
    }


}
