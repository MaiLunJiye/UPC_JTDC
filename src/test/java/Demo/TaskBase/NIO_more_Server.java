package Demo.TaskBase;

import Transport.TaskBase.Task_TP_control;
import Transport.Transport_interface;
import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 思路:
 * 预先创建一个套接字池, 然后通过不同时间选择选择不同的套接字进行通信实现跳边
 */

public class NIO_more_Server {
    Webcam webcam;
    BufferedImage bufferedImage;
    Transport_interface tonbuCore;

    public NIO_more_Server(Transport_interface tonbuCore){
        webcam = Webcam.getDefault();
        webcam.open();
        this.tonbuCore = tonbuCore;
    }

    public boolean capture() {
        bufferedImage = webcam.getImage();
        return bufferedImage == null ? false : true;
    }

    public void sendImage(){
        ByteArrayOutputStream os = new ByteArrayOutputStream();     //必须新建,不然那数据越变越长
        try{
            ImageIO.write(bufferedImage, "PNG", os);

            byte[] image_buf = os.toByteArray();
            ByteBuffer byteBuffer = ByteBuffer.wrap(image_buf);
            tonbuCore.writeData(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Transport_interface getTonbuCore() {
        return tonbuCore;
    }

    public static void main(String[] args) {

        InetAddrCreater ic = new InetAddrCreater("./config/iplist.json");
        Task_TP_control tonbuCore = new Task_TP_control(ic.addr2, ic.addr1, "0");
        NIO_more_Server server = new NIO_more_Server(tonbuCore);


        while(true) {
            if (server.capture())
                server.sendImage();

            Thread.yield();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
