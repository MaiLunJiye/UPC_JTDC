package Demo.Video_Jump;

import java.net.InetSocketAddress;

/**
 * Created by root on 1/16/17.
 */
public class Jump_tp {
    int key;
    String aimip;
    int myport[];
    int aimport[];

    public Jump_tp(int key, String aimip, int[] myport, int[] aimport) {
        this.key = key;
        this.aimip = aimip;
        this.myport = myport;
        this.aimport = aimport;
    }

    private InetSocketAddress getAimAddr(){
        int cnt = (int) (key+System.currentTimeMillis())
                            / 100
                            % aimport.length;
        return (new InetSocketAddress(aimip,aimport[cnt]));
    }

    private int getMyport(){
        int cnt = (int) (key+System.currentTimeMillis())
                / 100
                % aimport.length;
        return myport[cnt];
    }
}
