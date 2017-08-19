package Cammer;
import java.awt.image.BufferedImage;

import java.nio.ByteBuffer;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamUtils;

//这里帮你新建了一个你自己决定的接口函数  不然编译失败
public class Webcam_basic implements Cammer_int_new {
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
