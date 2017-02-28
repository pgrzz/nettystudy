package proto.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import proto.Acknowledge;
import proto.SailHeader;
import proto.Status;

/**
 * Created by lenovo on 2017/2/8.
 */
public class AcKnowledgeEncode extends MessageToByteEncoder<Acknowledge> {



    @Override
    protected void encode(ChannelHandlerContext ctx, Acknowledge msg, ByteBuf out) throws Exception {
                    out.writeShort(SailHeader.MAGIC);
                    out.writeByte(Status.ACK.getValue());
                    out.writeByte(Status.NOTHING.getValue());
                    out.writeLong(msg.getSequece());
                    out.writeInt(Status.NOTHING.getValue());
    }
}
