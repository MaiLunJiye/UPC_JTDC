package Transport.JumpValueCounter;

public interface CanCountJumpValue {
    /**
     * 跳变值计算接口,实现这个接口可以自定义同步策略
     *      系统会不断请求这个借口,根据每次返回值确定使用的信道
     * @ClassName :CanCountJumpValue
     */

    /**
     * 计算跳变值借口
     * @param key 参与计算跳变值的Key
     * @return 大于等于0的整数
     */
    public int countJumpValue(String key);
}
