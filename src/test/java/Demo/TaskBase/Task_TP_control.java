package Demo.TaskBase;

import Transport.Transport_interface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;

/**
 * Created by honghem on 6/10/17.
 */
public class Task_TP_control implements Transport_interface{
    protected DatagramChannel mychannels[];
    protected InetAddress aimAddress[];
    protected String key;
    protected DatagramChannel nowchannel;



    public int writeData(ByteBuffer buffer) throws IOException {
        return 0;
    }

    public SocketAddress readData(ByteBuffer buffer) throws IOException {
        return null;
    }
}
