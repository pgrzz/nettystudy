package proto;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import proto.SailHeader;
import proto.Status;

/**
 * Created by lenovo on 2017/2/7.
 */
public class HeartBeat {

    private static final ByteBuf HEART_BEART;

    static {
        ByteBuf buf= UnpooledByteBufAllocator.DEFAULT.buffer(SailHeader.HEAD_LENGTH);
        buf.writeShort(SailHeader.MAGIC);
        buf.writeByte(Status.HEARTBEAT.getValue());
        buf.writeByte(Status.NOTHING.getValue());
        buf.writeByte(Status.NOTHING.getValue());
        buf.writeLong(Status.NOTHING.getValue());
        buf.writeInt(Status.NOTHING.getValue());
        HEART_BEART=buf;
    }

    public static ByteBuf getHeartBeart() {
        return HEART_BEART;
    }
}
