package Demo.Video_IO_more;

import com.github.sarxos.webcam.Webcam;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * Created by simqin on 3/7/17.
 * 保留套接字形式的跳变
 * 预先保存套接字, 跳变基于使用不同的套接字实现
 * 目前还没有考虑阻塞问题,内存占用问题
 */


/**
 * 思路:
 * 预先创建一个套接字池, 然后通过不同时间选择选择不同的套接字进行通信实现跳边
 */

public class NIO_more_Server {
    Webcam webcam;

    BufferedImage bufferedImage;

    TonbuCore tonbuCore;



    public NIO_more_Server(TonbuCore tonbuCore){
        webcam = Webcam.getDefault();
        webcam.open();
        this.tonbuCore = tonbuCore;
        new Thread(tonbuCore).start();
    }

    public boolean capture() {
        bufferedImage = webcam.getImage();
        return bufferedImage == null ? false : true;
    }

    public void sendImage(InetSocketAddress addr){

        try{
            ByteArrayOutputStream os = new ByteArrayOutputStream();     //必须新建,不然那数据越变越长
            ImageIO.write(bufferedImage, "PNG", os);
            byte[] image_buf = os.toByteArray();
            ByteBuffer byteBuffer = ByteBuffer.wrap(image_buf);
            tonbuCore.sendData(byteBuffer);

/*            DatagramPacket dpg = new DatagramPacket( image_buf, image_buf.length, addr );

            System.out.println("send_dg: "+datagramSockets[jvalue].getLocalPort());

            datagramSockets[jvalue].send(dpg);      //根据状态码选择发送数据的socket*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
