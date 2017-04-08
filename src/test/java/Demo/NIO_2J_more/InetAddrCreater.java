package Demo.NIO_2J_more;

import java.net.InetSocketAddress;

/**
 * Created by simqin on 4/8/17.
 */
public class InetAddrCreater {
    public InetSocketAddress[] addr1;
    public InetSocketAddress[] addr2;
    public InetAddrCreater(){
        String[] ip1 = {
                "192.168.43.150",
                "192.168.43.151",
                "192.168.43.152",
                "192.168.43.153",
                "192.168.43.154",
                "192.168.43.155",
                "192.168.43.156",
                "192.168.43.157",
                "192.168.43.158",
                "192.168.43.159",
        };

        String[] ip2 = {
                "192.168.43.170",
                "192.168.43.171",
                "192.168.43.172",
                "192.168.43.173",
                "192.168.43.174",
                "192.168.43.175",
                "192.168.43.176",
                "192.168.43.177",
                "192.168.43.178",
                "192.168.43.179",
        };

        int port1_start = 40000;
        int port1_count = 6500;
        this.addr1 = new InetSocketAddress[ip1.length * port1_count];

        int port2_start = 50000;
        int port2_count = 6500;
        this.addr2 = new InetSocketAddress[ip2.length * port2_count];


        int addrcount = 0;
        int j=0;
        while(j < port2_count) {
            for(int i = 0; i < ip1.length; i++) {
                this.addr1[addrcount] = new InetSocketAddress(ip1[i], j+port1_start);
                addrcount++;
            }
            j++;
        }

        addrcount = 0;
        j=0;
        while(j < port2_count) {
            for(int i = 0; i < ip2.length; i++) {
                this.addr2[addrcount] = new InetSocketAddress(ip2[i], j+port2_start);
                addrcount++;
            }
            j++;
        }

    }
}
