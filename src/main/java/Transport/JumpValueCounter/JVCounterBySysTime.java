package Transport.JumpValueCounter;

import java.security.NoSuchAlgorithmException;


public class JVCounterBySysTime implements CanCountJumpValue{
    protected int JumpCycle = 100;

    /**
     * 修改跳变周期
     * @param jumpCycle 跳变周期(ms)
     */
    public void setJumpCycle(int jumpCycle) {
        JumpCycle = jumpCycle;
    }

    @Override
    public int countJumpValue(String key) {
        int SystemTime = (int) (System.currentTimeMillis() / JumpCycle);
        String source = String.valueOf(SystemTime);
        source += key;

        java.security.MessageDigest md = null;
        try {
            md = java.security.MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return 0;
        }

        md.update(source.getBytes());

        byte[] src = md.digest();
        int offset = 0;

        //bit[] to in
        int retvalue = (int) ((src[offset] & 0xFF)
                | ((src[offset+1] & 0xFF)<<8)
                | ((src[offset+2] & 0xFF)<<16)
                | ((src[offset+3] & 0xFF)<<24));

        return  Math.abs(retvalue);
    }

}
