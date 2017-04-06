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
        int myportleath = 2000;
        int[] myports = new int[myportleath];
        int[] aimports = new int[myportleath];

        for(int i=0; i<myportleath; i++) {
            myports[i] = 13000 + i;
            aimports[i] = 19000 + i;
        }

        InetSocketAddress[] myAddr = new InetSocketAddress[myports.length];
        InetSocketAddress[] aimAddr = new InetSocketAddress[aimports.length];

        for(int i=0; i<myports.length; i++) {
            myAddr[i] = new InetSocketAddress(myip, myports[i]);
        }

        for(int i=0; i<aimports.length; i++) {
            aimAddr[i] = new InetSocketAddress(otherip, aimports[i]);
        }
        TonbuCore tonbuCore = new TonbuCore(myAddr,aimAddr,"2000");
        tonbuCore.start();

        ByteBuffer buffer = ByteBuffer.allocate(300);

        int sendcount = 1000000;
        for ( int i = 0; i<sendcount; i++) {
            tonbuCore.sendData(buffer);
            System.out.println("+++" + i);
        }
        tonbuCore.close();
        System.out.println("key is " + tonbuCore.getKey());


    }
}
