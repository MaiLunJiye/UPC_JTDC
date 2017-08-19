package Demo.TaskBase;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import javax.swing.JFrame;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import Cammer.Webcam_basic;
public class Webacam_test {
	Webcam_basic we;
	static ByteBuffer buf = null;
    static BufferedImage image =null;

    //这部分代码有必要 但是 不应该这么使用
		public static void webcamrun(){//摄像机调用方法
		 Webcam webcam = Webcam.getDefault();//发现捕获的摄像机
			webcam.setViewSize(WebcamResolution.VGA.getSize());//设置图像分辨率
			WebcamPanel panel = new WebcamPanel(webcam);//panel 是显示面板，所有和panel有关的操作都是在面板上显示相关的参数
	//*****在面板上显示FPS参数，image参数等********//
			panel.setFPSDisplayed(true);

			panel.setDisplayDebugInfo(true);

			panel.setImageSizeDisplayed(true);

			panel.setMirrored(true);
	//*****设置面板框*******//
			JFrame window = new JFrame("upc webcam panel");

			window.add(panel);

			window.setResizable(true);

			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			window.pack();

			window.setVisible(true);

	}
	public static void main (String args[]){
		/** 下面是伪代码, 具体实现要你自己实现
		 *  模块测试应该侧重 测试接口本身一般步骤是
         *  把你这个模块需要初始化的环境都 封装到构造函数里面
		 */


		Webcam_basic webcam_basic = new Webcam_basic(参数 , 参数);

		window = EvriomentInit()  // 一个函数,建立一个验证模块运行的环境
		// 比如这里, 应该在这个函数里面构造好一个 窗户,一个渲染图片的地方

		while (true) { // 防止只能显示一次
			// 如果确定了使用返回值返回, 那么接口设计时候就不要参数了, 这个要改
			BufferedImage bufferedImage = webcam_basic.getimageData();
			window.showImage(BufferedImage);  // 通过窗口显示一下图片看看是否可以
		}

		/**
		 * 上面的代码可以看到  我的摄像头获取图片 以及图片显示都是经过接口函数来实现的,
		 * 对比一下  你的接口函数只是输出了一下缓冲区大小, 这样就失去了测试接口的意义了
		 *
		 * 也许你会有个疑问, 为什么我这么侧重单元测试,  实际上这样可以提高代码的质量
		 * 但是要记住,不要为了测试而测试
		 * 测试不需要漂亮的界面, 只需要确保接口函数功能 完善就可以了
		 */


		// 对比一下你的
		webcamrun();
		Webcam_basic we = new Webcam_basic();	
		while (true){
			// 只是拿到了图片,并没有验证这张图片是否能播放, 能使用
			image=we.getimageData(image);
			System.out.println(image);
			buf=we.getbufData(buf);
			System.out.println("Buffer length: " + buf.capacity());
		}	
	}
 
}