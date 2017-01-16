package Demo.Video_Jump;

import com.github.sarxos.webcam.Webcam;

import java.awt.image.BufferedImage;

/**
 * Created by root on 1/16/17.
 */
public class Webcam_serverJump {
    BufferedImage bufferedImage;        //图像数据
    Webcam webcam;                      //摄像头

    public Webcam_serverJump(){
        webcam = Webcam.getDefault();
    }

    public boolean capture(){
        bufferedImage = webcam.getImage();
        return bufferedImage==null?false:true;
    }


}
