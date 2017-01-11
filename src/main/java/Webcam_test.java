import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by 15070 on 2017/1/11.
 */
public class Webcam_test {
    public static void main(String[] args) {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        try {
            ImageIO.write(webcam.getImage(), "PNG", new File("hello-world.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
