package Transport.TaskBase.IO_task;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by honghem on 6/10/17.
 */
public class TaskManager {
    public static int __DEFAULT_LIMITE__ = 65400;
    public static int __DEFAULT_MAX_SIZE__ = 10;
    protected int size;
    protected int limite;
    protected int head,tail;
    protected ByteBuffer[] taskArray;    //循环队列


    public TaskManager(int size) {
        initTaskManager(size, __DEFAULT_LIMITE__);
    }

    public TaskManager(int size, int limite) {
        initTaskManager(size, limite);
    }

    public int getLimite() {
        return limite;
    }

    protected void initTaskManager(int size, int limite) {
        this.limite = limite;
        this.size = size;
        head = tail = 0;
        this.taskArray= new ByteBuffer[size];
        for(int i = 0; i < taskArray.length; i++) {
            taskArray[i] = ByteBuffer.allocate(__DEFAULT_LIMITE__);
        }

    }

    public void addTask(ByteBuffer buffer) {
        taskArray[tail].clear();
        taskArray[tail].put(buffer);
        tail = (tail + 1) % taskArray.length;
        if ( head == tail)
            head = (head + 1) % taskArray.length;
    }

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
