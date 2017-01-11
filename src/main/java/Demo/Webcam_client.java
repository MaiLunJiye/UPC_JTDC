package Demo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by 15070 on 2017/1/11.
 */
public class Webcam_client extends JPanel{
    static int ByteLength = 60000;
    private DatagramSocket dgs;
    private BufferedImage bufferedImage;

    private DatagramPacket dp;
    private byte[] bs;

    public Webcam_client(){
        try {
            dgs = new DatagramSocket(8889);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        init();
    }

    public Webcam_client(int port){
        try {
            dgs = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        init();
    }
    private void init(){
        bs = new byte[2*ByteLength];
        dp = new DatagramPacket(bs, bs.length);
    }


    public BufferedImage getImage(){
        try {
            dgs.receive(dp);
            ByteArrayInputStream in = new ByteArrayInputStream(bs);
            bufferedImage = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
            return bufferedImage;
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(bufferedImage,0,0,
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight(),null);
    }

    public static void main(String[] args) {
        Webcam_client wclient = new Webcam_client(8889);
        wclient.getImage();

        JFrame w = new JFrame();
        w.setSize(wclient.getImage().getWidth(),wclient.getImage().getHeight());

        w.add(wclient);
        w.setVisible(true);

        while(true){
            wclient.getImage();
            wclient.repaint();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
