package Cammer;
import java.awt.image.BufferedImage;

import java.nio.ByteBuffer;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamUtils;

public class Webcam_basic implements Cammer_inderface {
	    Webcam webcam = Webcam.getDefault();
	
	public BufferedImage getimageData(BufferedImage image){
	    image = webcam.getImage();
		return image;	
	}
	public ByteBuffer getbufData(ByteBuffer buf){
		buf = WebcamUtils.getImageByteBuffer(webcam, "JPG");
		return buf;
	}
	  	
}
