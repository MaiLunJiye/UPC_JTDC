package ReferenceCode;



import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Created by root on 12/11/16.
 **/


public class CameraServer_webcom extends JPanel {
    private BufferedImage image = null;
    private Webcam webcam = null;


    private int winHeight = 500;
    private int winWidth = 700;

    public int getWinHeight() {
        return winHeight;
    }

    public int getWinWidth() {
        return winWidth;
    }

    public CameraServer_webcom (){
        webcam = Webcam.getDefault();
        webcam.open();
    }

    public boolean Close(){
        return webcam.close();
    }

    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(image , 0, 0, image.getWidth()*2, image.getHeight()*2, null);
    }

    public void grab(){
        image = webcam.getImage();
    }

    public static void main(String[] args) throws InterruptedException {
        CameraServer_webcom cwb = new CameraServer_webcom();
        JFrame w = new JFrame();
        w.setSize(cwb.getWinWidth(),cwb.getWinHeight());

        w.add(cwb);

        w.setVisible(true);

        while(true){
            //循环拍照 刷新
            cwb.grab();
            cwb.repaint();
            Thread.sleep(50);
        }

    }

}
