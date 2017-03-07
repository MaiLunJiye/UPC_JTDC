package Demo.VideoConnect;

import Demo.VideoTransport.IO_tp_client;
import Demo.VideoTransport.IO_tp_server;

import javax.swing.*;
import java.net.InetSocketAddress;

/**
 * Created by 15070 on 2017/1/12.
 *  这个好像是端口绑定失败了
 */
public class VideoMC implements Runnable{
    private IO_tp_client wc;
    private IO_tp_server ws;

    private int tp_port=9999;

    public VideoMC(int inport,int outport){
        wc = new IO_tp_client(inport);
        ws = new IO_tp_server(outport);

        ws.webcam_open();

    }

    private InetSocketAddress getclientaddr(){
        return (new InetSocketAddress(tp_port));
    }


    public void run() {
        while(true){
            ws.capture();
            ws.sendImage(getclientaddr());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void showImg(){
        JFrame w = new JFrame();
        w.setSize(999,800);

        w.add(wc);

        w.setVisible(true);

        while(true){
            wc.getImage();
            wc.repaint();

            try{
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        VideoMC videoMC = new VideoMC(9999,5555);
        videoMC.run();
        videoMC.showImg();

    }

}
