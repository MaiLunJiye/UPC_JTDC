package Demo.Video_IO_more;

import com.github.sarxos.webcam.Webcam;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by simqin on 3/7/17.
 * 保留套接字形式的跳变
 * 预先保存套接字, 跳变基于使用不同的套接字实现
 * 目前还没有考虑阻塞问题,内存占用问题
 */
public class VideoServer {
    Webcam webcam;
    DatagramSocket datagramSockets[];

    BufferedImage bufferedImage;
    int jvalue;

    public VideoServer(DatagramSocket[] datagramSockets){
        webcam = Webcam.getDefault();
        webcam.open();
        this.datagramSockets = datagramSockets;
        jvalue = 0;
    }

    public boolean capture() {
        bufferedImage = webcam.getImage();
        return bufferedImage == null ? false : true;
    }

    public void sendImage(InetSocketAddress addr){
        while(jvalue >= datagramSockets.length){
            jvalue -= datagramSockets.length;
        }

        try{
            ByteArrayOutputStream os = new ByteArrayOutputStream();     //必须新建,不然那数据有问题
            ImageIO.write(bufferedImage, "PNG", os);
            byte[] image_buf = os.toByteArray();

            DatagramPacket dpg = new DatagramPacket( image_buf, image_buf.length, addr );

            System.out.println("send_dg: "+datagramSockets[jvalue].getLocalPort());

            datagramSockets[jvalue].send(dpg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        jvalue++;
    }

    public static void main(String[] args) {
        String myip = new String("127.0.0.1");
        int baseport = 4000;
        InetSocketAddress clientaddr = new InetSocketAddress("127.0.0.1", 8889);
        DatagramSocket datags[] = new DatagramSocket[10];
        try {
            for(int i = 0; i < datags.length; i++) {
                datags[i] = new DatagramSocket(i + baseport);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        VideoServer vserver = new VideoServer(datags);

        while(true){
            vserver.capture();
            vserver.sendImage(clientaddr);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
