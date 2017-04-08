package Demo.NIO_2J_more;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * Created by simqin on 4/3/17.
 */
public class Echo_more_server {
    public static void main(String[] args) {
        InetAddrCreater ic = new InetAddrCreater();

        TonbuCore tonbuCore = new TonbuCore(ic.addr1,ic.addr2,"0");
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
