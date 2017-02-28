package protooneday.Utilon;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by lenovo on 2016/6/12.
 */

public class RpcEncode extends MessageToByteEncoder {

    private Class<?>   requestCls;

    public  RpcEncode(Class<?> responseCls){
        this.requestCls=responseCls;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
         if(requestCls.isInstance(msg)) {
             byte[] body = SerializationUtil.serialize(msg);
             int dataLength = body.length;
             out.writeInt(dataLength);  // 这里 写入的 就是 解码时的读取的4个字节
             out.writeBytes(body);
         }
    }




}
