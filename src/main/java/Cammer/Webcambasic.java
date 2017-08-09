package Cammer;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import javax.swing.JFrame;


import com.github.sarxos.webcam.Webcam;

import com.github.sarxos.webcam.WebcamPanel;

import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;

import Cammer.Cammer_inderface;

public class Webcambasic implements Cammer_inderface{
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
	public static void main(String[] args) throws InterruptedException {
		
		webcamrun();

	}

	public boolean getImageData(ByteBuffer buf, BufferedImage image) {
		//****将图像信息储存为jpg格式，并实现接口返回 buf，image参数*****//
		Webcam webcam = Webcam.getDefault();
		buf = WebcamUtils.getImageByteBuffer(webcam, "jpg");//将图像保存为JPG格式，并返回为buf
		System.out.println("Buffer length: " + buf.capacity());
		image = webcam.getImage();
		System.out.println(image);
		return false;
	}

}