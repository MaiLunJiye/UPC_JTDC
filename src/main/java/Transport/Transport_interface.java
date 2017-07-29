package Transport;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * Created by 15070 on 2017/1/16.
 */
public interface Transport_interface {
    public boolean writeData(ByteBuffer buffer);
    public boolean readData(ByteBuffer buffer);
}
