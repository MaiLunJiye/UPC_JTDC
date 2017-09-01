package Demo.TaskBase;

import Transport.TaskBase.Task_TP_control;
import Transport.Transport_interface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by 15070 on 2017/1/11.
 *
 * 这个是客户端，接收来自服务端的数据，然后转化为可以输出的图像，接着输出到屏幕
 *
 *
 * 输出图像用到JPanel
 */
public class NIO_more_Client extends JPanel{
    //这个类继承JPanel，所以可以渲染图像

    static int ByteLength = 60000;      //这个是用来接收数据的缓冲区大小，以后需要根据代码来改
    Transport_interface tonbuCore;
    private BufferedImage bufferedImage;    //用来装 转换后的图像

    private byte[] bs;                  //用来接收数据的缓冲区
    private ByteBuffer byteBuffer;      //也是缓冲区


    //初始化设置，把缓冲区设置好
    private NIO_more_Client(Transport_interface tonbuCore){
        this.tonbuCore = tonbuCore;
        bs = new byte[2*ByteLength];        //申请一个字节数组，长度是 2 * 预先设置好的缓冲区长度
        byteBuffer = ByteBuffer.wrap(bs);
    }


    public BufferedImage getImage(){        //接收UDP数据，然后转化为图像保存在bufferedImage
        try {
            byteBuffer.clear();
            boolean rec = tonbuCore.readData(byteBuffer);            //接收 收到的数据
//            System.out.println(rec);
            byteBuffer.flip();
            ByteArrayInputStream in = new ByteArrayInputStream(bs);     //通过流处理，转化为数据流
            bufferedImage = ImageIO.read(in);       //通过ImageIO的read方法转化为 图像
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public void paint(Graphics g)       //绘制函数
    {
        super.paint(g);
        if(bufferedImage == null) return;

        //把bufferedImage绘制出来
        g.drawImage(bufferedImage,0,0,
                bufferedImage.getWidth(),
                bufferedImage.getHeight(),
                null);
    }

    public static void main(String[] args) {
        InetAddrCreater ic = new InetAddrCreater("./config/iplist_MP.json");
        Transport_interface tongbuCore = new Task_TP_control(ic.addr2, ic.addr1, "ClientKey", "ServerKey");
        NIO_more_Client client = new NIO_more_Client(tongbuCore);


        //新建一个画板，并且初始化
        JFrame w = new JFrame();
        w.setSize(200,200);

        //把 wclient 添加到画板上面，并且让画板的属性设置成可见
        w.add(client);
        w.setVisible(true);

        while(true){
            //循环
            client.getImage() ;     //接收数据
            client.repaint();      //刷新画板

            Thread.yield();

            //休眠50毫秒
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
