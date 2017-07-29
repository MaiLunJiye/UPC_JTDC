package Audio;

import java.nio.ByteBuffer;

public interface GetDataAble {
    //获取声音（保存在我传入的 bytebuffer 里面）
    public boolean getSoundData(ByteBuffer buf);

    // 播放我传入的声音 bytebuffer
    public boolean outputSound(ByteBuffer buf);
}
