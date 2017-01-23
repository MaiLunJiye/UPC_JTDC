package ReferenceCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by 15070 on 2016/8/29.
 *	经过修改的闪避球游戏
 *	求不会闪烁， 用了新的类 JFrame   JPanel
 */
public class MouseBallshan {
    public static void main(String[] args) {
        Game mp = new Game(20);

        JFrame w = new JFrame();
        w.setSize(mp.getWinWidth(),mp.getWinHeight());

        w.add(mp);

        w.addMouseMotionListener(mp);
        mp.addMouseMotionListener(mp);

        Thread t = new Thread(mp);
        t.start();

        w.setVisible(true);
    }
}

class Game extends JPanel implements Runnable,MouseMotionListener
{
    private int winHeight = 400;
    private int winWidth = 300;

    private int x[] = null;
    private int y[] = null;

    private boolean drawcir = false;
    private int radius = 20;
    private int rx = winHeight;
    private int ry = winWidth/2;

    public Game(int maxnum) {
        x = new int[maxnum];
        y = new int[maxnum];

        for(int i=0; i<maxnum; i++)
        {
            x[i] = (int)(Math.random()*winWidth);
            y[i] = -1*(int)(Math.random()*winHeight);
        }

    }

    public int getWinHeight() {
        return winHeight;
    }

    public void setWinHeight(int winHeight) {
        this.winHeight = winHeight;
    }

    public int getWinWidth() {
        return winWidth;
    }

    public void setWinWidth(int winWidth) {
        this.winWidth = winWidth;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        for(int i = 0; i <x.length; i++)
        {
            if(y[i]<0) continue;
            g.drawString("*",x[i],y[i]);
        }
        g.setColor(Color.orange);
        g.fillOval(rx-radius,ry-radius,2*radius,2*radius);
    }

    private void resetBlock(int index)
    {
        x[index] = (int)(Math.random()*winWidth);
        y[index] = -1*(int)(Math.random()*winHeight*0.2);
    }

    private void Check()
    {
        for(int i=0; i<x.length;i++)
        {
            if((x[i]-rx)*(x[i]-rx)+(y[i]-ry)*(y[i]-ry) < radius*radius)
            {
                resetBlock(i);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        rx = e.getX();
        ry = e.getY();

        Check();
    }
    public void run() {
        while (true)
        {
            for(int i=0;i < x.length;i++)
            {
                y[i]+=5;

                if(y[i] > winHeight)
                {
                    resetBlock(i);
                }
            }
            repaint();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
