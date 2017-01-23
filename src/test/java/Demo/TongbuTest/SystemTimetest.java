package Demo.TongbuTest;

/**
 * Created by root on 1/20/17.
 */
public class SystemTimetest {
    public static void main(String[] args) {
        while (true){
            long time;
            time = System.currentTimeMillis() / 1000;

            System.out.println(time);
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
