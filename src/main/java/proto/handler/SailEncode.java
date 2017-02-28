package proto.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import proto.*;
import proto.message.ByteHolder;
import proto.message.SailRequestByte;
import proto.message.SailResponseByte;

/**
 * Created by lenovo on 2017/2/7.
 */
@ChannelHandler.Sharable
public class SailEncode extends MessageToByteEncoder<ByteHolder> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ByteHolder msg, ByteBuf out) throws Exception {
        if(msg instanceof SailRequestByte){
            doEncodeRequest((SailRequestByte) msg,out);

        }else if(msg instanceof SailResponseByte){
            doEncodeResponse((SailResponseByte) msg,out);
        }
    }

    private void doEncodeRequest(SailRequestByte request, ByteBuf out){
        byte s_code=request.getSerializer();
        byte sign=(byte) ((s_code<<4)+ Status.REQUEST.getValue());
        long invokedId=request.getInvokedId();
        byte[]bytes=request.getBytes();
        int len=bytes.length;

        out.writeShort(SailHeader.MAGIC);
        out.writeByte(sign);
        out.writeByte(Status.NOTHING.getValue());
        out.writeLong(invokedId);
        out.writeInt(len);
        out.writeBytes(bytes);

    }
    private void doEncodeResponse(SailResponseByte response, ByteBuf out){
        byte s_code=response.getSerializer();
        byte sign=(byte) ((s_code<<4)+Status.RESPONSE.getValue());
        byte status=response.getStatus();
        long invokedId=response.getId();
        byte[]bytes=response.getBytes();
        int len=bytes.length;
        out.writeShort(SailHeader.MAGIC);
        out.writeByte(sign);
        out.writeByte(status);
        out.writeLong(invokedId);
        out.writeInt(len);
        out.writeBytes(bytes);

    }

}
