package Transport;

/**
 * Created by root on 1/16/17.
 */
public class Jump_UDP implements Transport_interface{
    private int key;
    private int[] myport;
    private int[] aimport;


    public boolean sendData(byte[] data) {
        return false;
    }
}
