package Transport.JumpValueCounter;

public interface CanCountJumpValue {
    /**
     * 计算跳变值借口
     * @param key 参与计算跳变值的Key
     * @return 大于等于0的整数
     */
    public int countJumpValue(String key);
}
