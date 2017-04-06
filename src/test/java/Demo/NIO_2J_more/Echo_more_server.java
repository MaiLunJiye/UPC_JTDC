package Demo.NIO_2J_more;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * Created by simqin on 4/3/17.
 */
public class Echo_more_server {
    public static void main(String[] args) {
        String myip = "192.168.31.21";
        String otherip = "192.168.31.227";

        int myportleath = 2000;
        int[] myports = new int[myportleath];
        int[] aimports = new int[myportleath];

        for(int i=0; i<myportleath; i++) {
            aimports[i] = 13000 + i;
            myports[i] = 19000 + i;
        }



        InetSocketAddress[] myAddr = new InetSocketAddress[myports.length];
        InetSocketAddress[] aimAddr = new InetSocketAddress[aimports.length];

        for(int i=0; i<myports.length; i++) {
            myAddr[i] = new InetSocketAddress(myip, myports[i]);
        }

        for(int i=0; i<aimports.length; i++) {
            aimAddr[i] = new InetSocketAddress(otherip, aimports[i]);
        }
        TonbuCore tonbuCore = new TonbuCore(myAddr,aimAddr,"0");
        tonbuCore.start();


        ByteBuffer buffer = ByteBuffer.allocate(4000);
        int count = 0;

        System.out.println("lister start");
        while (true) {
            if (  tonbuCore.getData(buffer)  != null ) {
//                tonbuCore.sendData(buffer);
                buffer.clear();
                count++;
                System.out.println(" >> " + count);
            }
        }
    }
}
