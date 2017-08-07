package Demo.TaskBase;
import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
public interface  Webcamtext {
	BufferedImage image = null;
	 ByteBuffer buf = null;
	static Webcam webcam = null;
	/*public static void DisplayImage(){
		 image = webcam.getImage();//获取参数image
		 buf = WebcamUtils.getImageByteBuffer(webcam, "jpg");//活取参数buf
	}*/
	 public static void main(String[] args) throws InterruptedException{
		 Webcam webcam = Webcam.getDefault();
		 //webcam.setViewSize(new Dimension(1024, 768));//设置图片分辨率
		webcam.setViewSize(WebcamResolution.VGA.getSize());//自动获取分辨率
		//public static  DisplayImage(BufferedImage image,BufferedImage image);
		/* BufferedImage image = webcam.getImage();//获取参数image
			ByteBuffer buf = WebcamUtils.getImageByteBuffer(webcam, "jpg");//活取参数buf
		 WebcamUtils.capture(webcam, "test1", ImageUtils.FORMAT_JPG);
			WebcamUtils.capture(webcam, "test2", "jpg");	
			System.out.println("Buffer length: " + buf.capacity());*/
		/*public  DisplayImage(BufferedImage image,ByteBuffer buf){
			 image = webcam.getImage();//获取参数image
			 buf = WebcamUtils.getImageByteBuffer(webcam, "jpg");//活取参数buf
		}*/
		WebcamUtils.capture(webcam, "test1", ImageUtils.FORMAT_JPG);
		WebcamUtils.capture(webcam, "test2", "jpg");	
		WebcamPanel panel = new WebcamPanel(webcam); 

			panel.setFPSDisplayed(true);

			panel.setDisplayDebugInfo(true);

			panel.setImageSizeDisplayed(true);

			panel.setMirrored(true);

			JFrame window = new JFrame("LYL webcam panel");

			window.add(panel);

			window.setResizable(true);

			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			window.pack();

			window.setVisible(true); 
			DisplayImage( image,  buf);
	 }
	public static void DisplayImage(BufferedImage image, ByteBuffer buf) {
		System.out.println("hello");
		image = webcam.getImage();//获取参数image
		buf = WebcamUtils.getImageByteBuffer(webcam, "jpg");//活取参数buf
		 
	}
	 
}
