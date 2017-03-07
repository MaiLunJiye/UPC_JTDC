package Demo.VideoTransport;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by 15070 on 2017/1/11.
 *
 * 这个是客户端，接收来自服务端的数据，然后转化为可以输出的图像，接着输出到屏幕
 *
 * 输出图像用到JPanel
 */
public class IO_tp_client extends JPanel{
    //这个类继承JPanel，所以可以渲染图像

    static int ByteLength = 60000;      //这个是用来接收数据的缓冲区大小，以后需要根据代码来改
    private DatagramSocket dgs;         //UDP套接字
    private BufferedImage bufferedImage;    //用来装 转换后的图像

    private byte[] bs;                  //用来接收数据的缓冲区
    private DatagramPacket dp;          //封装了上面哪个 bs， 可以用来装载UDP收到的数据

    //构造函数，传入端口表示监听哪个端口数据，
    public IO_tp_client(){
        try {
            dgs = new DatagramSocket(8889);     //默认监听8889端口
        } catch (SocketException e) {
            e.printStackTrace();
        }
        init();     //解决了端口问题，执行这个方法实现初始化
    }

    public IO_tp_client(int port){     //上面哪个的有参构造（参数就是监听的端口号）
        try {
            dgs = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        init();
    }

    //初始化设置，把缓冲区设置好
    private void init(){
        bs = new byte[2*ByteLength];        //申请一个字节数组，长度是 2 * 预先设置好的缓冲区长度
        dp = new DatagramPacket(bs, bs.length);     //把bs封装成 UDP数据包类
    }


    public BufferedImage getImage(){        //接收UDP数据，然后转化为图像保存在bufferedImage
        try {
            dgs.receive(dp);            //接收 收到的数据
            ByteArrayInputStream in = new ByteArrayInputStream(bs);     //通过流处理，转化为数据流
            bufferedImage = ImageIO.read(in);       //通过ImageIO的read方法转化为 图像

            //输出来源地址与 端口
            System.out.println("souce is " + dp.getAddress().getHostAddress()+":" + dp.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
            return bufferedImage;
    }

    public void paint(Graphics g)       //绘制函数
    {
        super.paint(g);

        //把bufferedImage绘制出来
        g.drawImage(bufferedImage,0,0,
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight(),
                null);
    }

    public static void main(String[] args) {
        //先新建一个类，然后先获取一下图像，初始化一下bufferedImage
        IO_tp_client wclient = new IO_tp_client(8889);
        wclient.getImage();

        //新建一个画板，并且初始化
        JFrame w = new JFrame();
        w.setSize(wclient.getImage().getWidth(), wclient.getImage().getHeight());

        //把 wclient 添加到画板上面，并且让画板的属性设置成可见
        w.add(wclient);
        w.setVisible(true);

        while(true){
            //循环
            wclient.getImage();     //接收数据
            wclient.repaint();      //刷新画板

            //休眠50毫秒
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
