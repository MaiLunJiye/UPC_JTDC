package Transport;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;



public interface Transport_interface {
    /**
     　* 传输接口, 提供外部程序对跳变模块的IO调用借口
     　* @ClassName：Transport_interface
     　*/

    /**
     * 把缓冲区数据写入消息队列
     * @param buffer 载有消息的缓冲区
     * @return 写入是否成功
     */
    public boolean writeData(ByteBuffer buffer);

    /**
     * 从消息队列里读取消息
     * @param buffer 用于装载消息的缓冲区
     * @return 读取是否成功
     */
    public boolean readData(ByteBuffer buffer);
}
