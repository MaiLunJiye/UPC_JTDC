package Demo.NIO_2J_more;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by simqin on 4/3/17.
 */
public class Echo_more_client {
    public static void main(String[] args) {
        InetAddrCreater ic = new InetAddrCreater();

        TonbuCore tonbuCore = new TonbuCore(ic.addr2,ic.addr1,"0");
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
