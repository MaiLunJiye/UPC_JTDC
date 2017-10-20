package Demo.TaskBase;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import Cammer.WebcamNew;

public class TestNew extends JPanel {
	/**
	 * 
	 */
	private static BufferedImage image = null;
    private static Webcam webcam = null;
    //***构造方法，摄像头初始化****//
    public TestNew (){
        webcam = Webcam.getDefault();
        webcam.open();
    }
   //**** 绘制获取的image信息 ***//
  public void paint(Graphics g){

	    int pw = getWidth();
		int ph = getHeight();
		int iw = image.getWidth();
		int ih = image.getHeight();
		int x = 0;
		int y = 0;
		int w = 0;
		int h = 0;
		int dx1, dx2, dy1, dy2; 
		int sx1, sx2, sy1, sy2;
		double s = Math.max((double) iw / pw, (double) ih / ph);
		double niw = iw / s;
		double nih = ih / s;
		double dx = (pw - niw) / 2;
		double dy = (ph - nih) / 2;
		w = (int) niw;
		h = (int) nih;
		x = (int) dx;
		y = (int) dy;
		dx1 = x;
		dy1 = y;
		dx2 = x + w;
		dy2 = y + h;
		sx1 = iw;
		sy1 = 0;
		sx2 = 0;
		sy2 = ih;
        super.paint(g);
        g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }
  //****使用接口方法****//
  public void  show(BufferedImage image,Component comp) throws InterruptedException{
	  TestNew.image=image;
	  comp.repaint();
	  Thread.sleep(50);
  }
    public static void main(String[] args) throws InterruptedException {
    	TestNew te= new TestNew();
    	 WebcamNew we=new WebcamNew();
    	 JFrame w = new JFrame();
         w.setSize(WebcamResolution.VGA.getSize());
         w.add(te);
         w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//控制开关
         w.setVisible(true);
          while (true){
        	  te.show(we.getimageData(image),te);
          }
    }
}