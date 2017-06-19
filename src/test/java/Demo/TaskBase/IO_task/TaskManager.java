package Demo.TaskBase.IO_task;

import java.nio.ByteBuffer;

/**
 * Created by honghem on 6/10/17.
 */
public class TaskManager {
    public static int __DEFAULT_MAX_SIZE__ = 10;
    protected int size;
    protected int head,tail;
    protected ByteBuffer[] taskArray;    //循环队列

    public TaskManager(int size) {
        this.size = size;
        head = tail = 0;
        ByteBuffer[] taskArray= new ByteBuffer[size];
        for(int i = 0; i < taskArray.length; i++) {
            taskArray[i] = ByteBuffer.allocate(70000);
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
        head = (head + 1)%taskArray.length;
        return true;
    }

}
