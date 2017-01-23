package Demo.Video_Jump;

import Demo.VideoTransport.Webcam_client;
import Transport.Jump_UDP;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by root on 1/16/17.
 */

class ClientTest{
    public static void main(String[] args) {
        int key = 123;
        int[] aimport = {8888,8889,8890};
        String[] aimip = { "127.0.0.1"};
        int[] myport = {7777,7778,7779};
        String[] myip = {"127.0.0.1"};

        Webcam_clientJump wclient = new Webcam_clientJump(key, myip, myport, aimip, aimport);

        wclient.start();
        while (true){
            try {
                wclient.getImg();
                wclient.repaint();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

public class Webcam_clientJump extends JPanel{
    static int ByteLength = 60000;

    private BufferedImage bufferedImage;
    private byte[] bs;

    private ByteBuffer byteBuffer;
    private ByteArrayInputStream bais;
    private Jump_UDP judp;


    public Webcam_clientJump(int key, String[] myip, int[] myport, String[] aimip, int[] aimport){
        bs = new byte[2*ByteLength];
        byteBuffer = ByteBuffer.wrap(bs);
        bais = new ByteArrayInputStream(bs);
        judp = new Jump_UDP(key, myip, myport, aimip, aimport);
    }

    public void start(){
        judp.jump_open();

        //新建一个画板，并且初始化
        JFrame w = new JFrame();
        w.setSize(500,700);

        //把 wclient 添加到画板上面，并且让画板的属性设置成可见
        w.add(this);
        w.setVisible(true);

    }

    public void stop(){
        judp.jump_stop();
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        g.drawImage(bufferedImage,
                    0,
                    0,
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight(),
                    null);

    }

    public void getImg() throws IOException {
        judp.readData(byteBuffer);
        ImageIO.read(bais);
    }


}
