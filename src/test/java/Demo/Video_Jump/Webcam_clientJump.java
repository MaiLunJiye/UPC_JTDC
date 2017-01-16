package Demo.Video_Jump;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by root on 1/16/17.
 */
public class Webcam_clientJump extends JPanel{
    static int ByteLength = 60000;

    private BufferedImage bufferedImage;
    private byte[] bs;

    public Webcam_clientJump(){
        bs = new byte[2*ByteLength];
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


}
