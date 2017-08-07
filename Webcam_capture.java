package Demo.TaskBase;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.Thread.UncaughtExceptionHandler;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.github.sarxos.webcam.Webcam;
public class Webcam_capture extends JFrame implements Runnable, WebcamListener, WindowListener, UncaughtExceptionHandler, ItemListener, WebcamDiscoveryListener {

	private static final long serialVersionUID = 1L;
	private static BufferedImage image = null;
	private static ByteBuffer buf = null;
	static Webcam webcam = null;
	private WebcamPanel panel = null;
	private WebcamPicker picker = null;
	public void run() {//摄像头display方法

		Webcam.addDiscoveryListener(this);

		setTitle("LYL Java Webcam Capture POC");//设置窗口的表头

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//控制摄像头的关闭

		setLayout(new BorderLayout());

		addWindowListener(this);

		picker = new WebcamPicker();

		picker.addItemListener(this);

		webcam = picker.getSelectedWebcam();//获取网络摄像头

		if (webcam == null) {

			System.out.println("No webcams found...");

			System.exit(1);

		}
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		//设置图片分辨率

		webcam.addWebcamListener(Webcam_capture.this);

		panel = new WebcamPanel(webcam, false);

		panel.setFPSDisplayed(true);

		add(picker, BorderLayout.NORTH);

		add(panel, BorderLayout.CENTER);

		pack();

		setVisible(true);//配置窗口参数


		Thread t = new Thread() {


			public void run() {//自我调用run方法打开摄像头

				panel.start();

			}

		};

		t.setName("example-starter");

		t.setDaemon(true);

		t.setUncaughtExceptionHandler(this);

		t.start();

	}

	public static void main(String[] args)throws IOException {

		SwingUtilities.invokeLater(new Webcam_capture());
		DisplayImage( image,  buf);
		ImageIO.write(image, "JPG", new File("test.jpg"));
	}

	public void webcamOpen(WebcamEvent we) {

		System.out.println("webcam open");

	}

	public void webcamClosed(WebcamEvent we) {

		System.out.println("webcam closed");

	}

	public void webcamDisposed(WebcamEvent we) {

		System.out.println("webcam disposed");

	}
	public static void DisplayImage(BufferedImage image, ByteBuffer buf) {
		System.out.println("hello");
		image = webcam.getImage();//获取参数image
		buf = WebcamUtils.getImageByteBuffer(webcam, "jpg");//活取参数buf
		 
	}

public void webcamImageObtained(WebcamEvent we) {

		BufferedImage image = webcam.getImage();//获取参数image

	}

	public void windowActivated(WindowEvent e) {

	}




	public void windowClosed(WindowEvent e) {

		webcam.close();

	}




	public void windowClosing(WindowEvent e) {

	}
	public void windowOpened(WindowEvent e) {

	}
	public void windowDeactivated(WindowEvent e) {

	}

   public void windowDeiconified(WindowEvent e) {

		System.out.println("webcam viewer resumed");

		panel.resume();

	}


	public void windowIconified(WindowEvent e) {

		System.out.println("webcam viewer paused");

		panel.pause();

	}
	public void uncaughtException(Thread t, Throwable e) {

		System.err.println(String.format("Exception in thread %s", t.getName()));

		e.printStackTrace();

	}

	public void itemStateChanged(ItemEvent e) {

		if (e.getItem() != webcam) {

			if (webcam != null) {

				panel.stop();

				remove(panel);

				webcam.removeWebcamListener(this);

				webcam.close();



				webcam = (Webcam) e.getItem();

				webcam.setViewSize(WebcamResolution.VGA.getSize());//设置图片分辨率

				webcam.addWebcamListener(this);

				System.out.println("selected " + webcam.getName());

				panel = new WebcamPanel(webcam, false);

				panel.setFPSDisplayed(true);

				add(panel, BorderLayout.CENTER);

				pack();

				Thread t = new Thread() {

					public void run() {

						panel.start();

					}

				};

				t.setName("example-stoper");

				t.setDaemon(true);

				t.setUncaughtExceptionHandler(this);

				t.start();

			}

		}

	}



	public void webcamFound(WebcamDiscoveryEvent event) {

		if (picker != null) {

			picker.addItem(event.getWebcam());

		}

	}



	public void webcamGone(WebcamDiscoveryEvent event) {

		if (picker != null) {

			picker.removeItem(event.getWebcam());

		}

	}

	
}