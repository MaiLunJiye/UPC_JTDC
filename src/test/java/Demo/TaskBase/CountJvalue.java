package Demo.TaskBase;

/**
 * Created by honghem on 6/19/17.
 */
public class CountJvalue {
    public static int getvalue(String key){
        int k = Integer.parseInt(key);
        return (int) (System.currentTimeMillis() / 2000 + k);
    }
}
