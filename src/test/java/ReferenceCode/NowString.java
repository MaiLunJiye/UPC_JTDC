package ReferenceCode;

import java.util.Date;
import java.text.SimpleDateFormat;

public class NowString {
    public static void main(String[] args) throws InterruptedException {
        while(true) {
            long time = System.currentTimeMillis();
            time /= 100;
            System.out.println(time);
            Thread.sleep(100);

        }
    }
}
