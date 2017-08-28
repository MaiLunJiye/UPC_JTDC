package Cammer;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamUtils;

public class WebcamNew implements webcam_interfacenew{
	Webcam webcam = Webcam.getDefault();
	public BufferedImage getimageData(BufferedImage image) {
		image = webcam.getImage();
	    return image ;       
	}

	public ByteBuffer getbufData(ByteBuffer buf) {
		buf = WebcamUtils.getImageByteBuffer(webcam, "JPG");
		return buf;
	}

	
	

}
