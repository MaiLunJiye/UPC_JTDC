package Demo.TaskBase;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import Cammer.Webcambasic;

public class webcamtest{
	Webcambasic we;
	static ByteBuffer buf =null;
	static BufferedImage image=null ;
	public static void main(String []args){
		Webcambasic we = new Webcambasic();
		we.webcamrun();
		we.getImageData( buf, image);
		System.out.println("Buffer length: " + buf.capacity());
		System.out.println(image);
	}
}