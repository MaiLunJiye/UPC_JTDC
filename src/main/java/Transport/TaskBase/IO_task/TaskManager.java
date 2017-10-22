package Transport.TaskBase.IO_task;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskManager {
    /**
     　* 消息队列管理类,用于管理消息队列
       *    每个消息管理类里面都有两个消息队列,一个用于发,一个用于收
     　* @ClassName：TaskManager
     　*/

    public static int __DEFAULT_LIMITE__ = 65400;
    public static int __DEFAULT_MAX_SIZE__ = 10;
    protected int size;
    protected int limite;
    protected int head,tail;
    protected ByteBuffer[] taskArray;    //循环队列


    /**
     * 初始化消息队列,并指定队列长度
     * @param size 队列长度
     */
    public TaskManager(int size) {
        initTaskManager(size, __DEFAULT_LIMITE__);
    }

    /**
     * 初始化消息队列,并指定长度,消息队列缓冲区大小
     * @param size 队列长度
     * @param limite 消息队列的单个缓冲区大小
     */
    public TaskManager(int size, int limite) {
        initTaskManager(size, limite);
    }

    /**
     * 获取消息队列的单个缓冲区大小
     * @return 返回缓冲区大小
     */
    public int getLimite() {
        return limite;
    }

    /**
     * 初始化消息队列,并指定长度,消息队列缓冲区大小
     * @param size 队列长度
     * @param limite 消息队列的单个缓冲区大小
     */
    protected void initTaskManager(int size, int limite) {
        this.limite = limite;
        this.size = size;
        head = tail = 0;
        this.taskArray= new ByteBuffer[size];
        for(int i = 0; i < taskArray.length; i++) {
            taskArray[i] = ByteBuffer.allocate(__DEFAULT_LIMITE__);
        }

    }

    /**
     * 添加消息到消息队列
     * @param buffer 载有消息的缓冲区
     */
    public void addTask(ByteBuffer buffer) {
        taskArray[tail].clear();
        try {
            taskArray[tail].put(buffer);
        } catch ( Exception e) {
            System.err.println("buffer too big = [" + buffer + "]");
            return;
        }
        tail = (tail + 1) % taskArray.length;
        if ( head == tail)
            head = (head + 1) % taskArray.length;
    }

    /**
     * 从消息队列里获取消息
     * @param buffer 用于装载消息的缓冲区
     * @return 读取是否成功
     */
    public boolean popTask(ByteBuffer buffer) {
        if(head == tail)
            return false;
        buffer.clear();
        taskArray[head].flip();
        buffer.put(taskArray[head]);
        buffer.flip();
        head = (head + 1)%taskArray.length;
        return true;
    }

}
