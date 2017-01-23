package Transport;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * Created by 15070 on 2017/1/16.
 */
public interface Transport_interface {
    public int writeData(ByteBuffer buffer) throws IOException;
    public SocketAddress readData(ByteBuffer buffer) throws IOException;
}
