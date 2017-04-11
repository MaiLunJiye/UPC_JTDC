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
                "192.168.31.150",
                "192.168.31.151",
                "192.168.31.152",
                "192.168.31.153",
                "192.168.31.154",
                "192.168.31.155",
                "192.168.31.156",
                "192.168.31.157",
                "192.168.31.158",
                "192.168.31.159",
        };

        String[] ip2 = {
                "192.168.31.170",
                "192.168.31.171",
                "192.168.31.172",
                "192.168.31.173",
                "192.168.31.174",
                "192.168.31.175",
                "192.168.31.176",
                "192.168.31.177",
                "192.168.31.178",
                "192.168.31.179",
        };

        int port1_start = 7000;
        int port1_count = 135;
        this.addr1 = new InetSocketAddress[ip1.length * port1_count];

        int port2_start = 7500;
        int port2_count = 135;
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
