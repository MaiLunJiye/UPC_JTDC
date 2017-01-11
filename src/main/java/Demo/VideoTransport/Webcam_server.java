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
 */
public class Webcam_server {
    BufferedImage bufferedImage;
    Webcam webcam;

    DatagramSocket dso;


    public Webcam_server(){

        webcam = Webcam.getDefault();
        try {
            dso = new DatagramSocket(8888);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public Webcam_server(int port){
        webcam = Webcam.getDefault();
        try {
            dso = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public boolean webcam_open(){
        return this.webcam.open();
    }

    public BufferedImage getBufferedImage(){
        return bufferedImage;
    }

    public boolean capture(){
        bufferedImage = webcam.getImage();
        return bufferedImage==null?false:true;
    }

    public void sendImage(InetSocketAddress addr){
        ByteArrayOutputStream os=new ByteArrayOutputStream();
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
        Webcam_server server = new Webcam_server(8888);
        server.webcam_open();

        InetSocketAddress clientaddr = new InetSocketAddress("127.0.0.1",8889);

        while (true){
            server.capture();
            server.sendImage(clientaddr);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

}
