package Demo.NIO_2J_more;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by simqin on 4/3/17.
 */
public class Echo_more_client {
    public static void main(String[] args) {
        String otherip = "192.168.31.21";
        String myip = "192.168.31.227";
        int[] myports = { 7000, 7001, 7002, 7003, 7004, 7005, 7006, 7007, 7008, 7009, 7010, 7011, 7012, 7013, 7014, 7015 };
        int[] aimports = { 8000, 8001, 8002, 8003, 8004, 8005, 8006, 8007, 8008, 8009, 8010, 8011, 8012, 8013, 8014, 8015 };
        InetSocketAddress[] myAddr = new InetSocketAddress[myports.length];
        InetSocketAddress[] aimAddr = new InetSocketAddress[aimports.length];

        for(int i=0; i<myports.length; i++) {
            myAddr[i] = new InetSocketAddress(myip, myports[i]);
        }

        for(int i=0; i<aimports.length; i++) {
            aimAddr[i] = new InetSocketAddress(otherip, aimports[i]);
        }
        TonbuCore tonbuCore = new TonbuCore(myAddr,aimAddr,"3");
        tonbuCore.start();

        ByteBuffer buffer = ByteBuffer.allocate(300);

        int sendcount = 1000000;
        for ( int i = 0; i<sendcount; i++) {
            tonbuCore.sendData(buffer);
            System.out.println("+++" + i);
        }

        tonbuCore.close();


    }
}
