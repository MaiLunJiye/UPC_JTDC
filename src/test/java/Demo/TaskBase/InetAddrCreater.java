package Demo.TaskBase;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;

/**
 * Created by simqin on 4/8/17.
 */
public class InetAddrCreater {
    public InetSocketAddress[] addr1;
    public InetSocketAddress[] addr2;

    public InetAddrCreater(String confPath) {

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(confPath));
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject);
            JSONArray ip1 = (JSONArray) jsonObject.get("ip1");
            JSONArray ip2 = (JSONArray) jsonObject.get("ip2");


//        String[] ip1 = { };
//        String[] ip2 = { };

            int port1_start = 7000;
            int port1_count = 100;
            this.addr1 = new InetSocketAddress[ip1.size() * port1_count];

            int port2_start = 7500;
            int port2_count = 100;
            this.addr2 = new InetSocketAddress[ip2.size() * port2_count];


            Iterator<String> ip1Iterator = ip1.iterator();
            Iterator<String> ip2Iterator = ip2.iterator();
            int addrcount = 0;
            while(ip1Iterator.hasNext()) {
                int j = 0;
                String nowIP = ip1Iterator.next();
                while (j < port2_count) {
                    this.addr1[addrcount] = new InetSocketAddress(nowIP, j + port1_start);
                    addrcount++;
                    j++;
                }
                System.out.println(nowIP);
            }

            addrcount = 0;
            while(ip2Iterator.hasNext()) {
                int j = 0;
                String nowIP = ip2Iterator.next();
                while (j < port2_count) {
                    this.addr2[addrcount] = new InetSocketAddress(nowIP, j + port1_start);
                    addrcount++;
                    j++;
                }
                System.out.println(nowIP);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        InetAddrCreater ic = new InetAddrCreater("./iplist_MP.json");
        for(int i = 0; i < ic.addr2.length; i++)
            System.out.println(ic.addr2[i]);

        for(int i = 0; i < ic.addr1.length; i++)
            System.out.println(ic.addr1[i]);
    }
}
