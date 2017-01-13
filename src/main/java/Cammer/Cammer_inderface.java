package Cammer;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/**
 * Created by 15070 on 2017/1/13.
 */
public interface Cammer_inderface {
    //这个接口要求你把图像信息返回给我传入的两个参数
    public boolean getImageData(ByteBuffer buf, BufferedImage image);
}
