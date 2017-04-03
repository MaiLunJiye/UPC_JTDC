package Demo.NIO_2J_more;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;


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

    public void sendImage(){
        ByteArrayOutputStream os = new ByteArrayOutputStream();     //必须新建,不然那数据越变越长
        try{
            ImageIO.write(bufferedImage, "PNG", os);
            byte[] image_buf = os.toByteArray();
            ByteBuffer byteBuffer = ByteBuffer.wrap(image_buf);
            tonbuCore.sendData(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TonbuCore getTonbuCore() {
        return tonbuCore;
    }

    public static void main(String[] args) {
        String ipAddr = "127.0.0.1";
        int[] aimports = { 7000, 7001, 7002, 7003, 7004, 7005, 7006, 7007, 7008, 7009, 7010, 7011, 7012, 7013, 7014, 7015 };
        int[] myports = { 8000, 8001, 8002, 8003, 8004, 8005, 8006, 8007, 8008, 8009, 8010, 8011, 8012, 8013, 8014, 8015 };
        InetSocketAddress[] myAddr = new InetSocketAddress[myports.length];
        InetSocketAddress[] aimAddr = new InetSocketAddress[aimports.length];

        for(int i=0; i<myports.length; i++) {
            myAddr[i] = new InetSocketAddress(ipAddr, myports[i]);
        }

        for(int i=0; i<aimports.length; i++) {
            aimAddr[i] = new InetSocketAddress(ipAddr, aimports[i]);
        }
        NIO_more_Server server = new NIO_more_Server(new TonbuCore(myAddr, aimAddr, "3"));

        Thread th = server.getTonbuCore().start();

        while(true) {
            server.capture();
            server.sendImage();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
