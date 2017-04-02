package Demo.NIO_2Jump_More;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

/**
 * Created by 15070 on 2017/1/11.
 * 基于NIO的视频传送
 *
 * 基本原理和 IO 的一样
 */
public class NIO_2J_client extends JPanel{
    //这个类继承JPanel，所以可以渲染图像

    static int ByteLength = 60000;      //这个是用来接收数据的缓冲区大小，以后需要根据代码来改
    private BufferedImage bufferedImage;    //用来装 转换后的图像

    private HashMap<String, DatagramChannel> dChannelMap;

    private byte[] bs;                  //用来接收数据的缓冲区
    private ByteBuffer byteBuffer;      //也是缓冲区
    String jumpValue;

    int defaultPort = 7777;

    //构造函数，传入端口表示监听哪个端口数据，

    public NIO_2J_client(HashMap<String, DatagramChannel> dgmap){     //上面哪个的有参构造（参数就是监听的端口号）
        dChannelMap = dgmap;
        jumpValue = "Default";

        //设置默认套接字
        try {
            // 设置默认套接字
            DatagramChannel defChannel = DatagramChannel.open();
            defChannel.socket().bind( new InetSocketAddress(defaultPort));
            defChannel.configureBlocking(false);
            dChannelMap.put(jumpValue, defChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }


        bs = new byte[2*ByteLength];        //申请一个字节数组，长度是 2 * 预先设置好的缓冲区长度
        byteBuffer = ByteBuffer.wrap(bs);
    }



    public BufferedImage getImage(){        //接收UDP数据，然后转化为图像保存在bufferedImage
        try {
            byteBuffer.clear();
            SocketAddress rec = dChannelMap.get(jumpValue).receive(byteBuffer);            //接收 收到的数据

            if (rec == null) return null;
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
        //先新建一个类，然后先获取一下图像，初始化一下bufferedImage
        //构建哈希表
        String[] keys = new String[]{
                "J2_one",
                "J2_two",
                "J2_three",
                "J2_four",
                "J2_five",
                "J2_six",
                "J2_seven",
                "J2_eight",
                "J2_nine",
                "J2_ten",
        };

        InetSocketAddress myAddr[] = new InetSocketAddress[keys.length];

        for(int i = 0; i < keys.length; i++) {
            myAddr[i] = new InetSocketAddress(8000 + i);
        }

        HashMap<String, DatagramChannel> myChannels = new HashMap<String, DatagramChannel>();

        for (int i = 0; i < keys.length; i++){
            try {
                DatagramChannel dags = DatagramChannel.open();
                dags.socket().bind(myAddr[i]);
                myChannels.put(keys[i], dags);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        NIO_2J_client wclient = new NIO_2J_client(myChannels);
        //新建一个画板，并且初始化
        JFrame w = new JFrame();
        w.setSize(200,200);

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
