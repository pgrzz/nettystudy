package protooneday.Utilon;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;


import java.util.List;

/**
 * Created by lenovo on 2016/6/12.
 */

public class RpcDecode extends ByteToMessageDecoder {

    private Class<?>   responseCls;

    public  RpcDecode(Class<?> responseCls){
        this.responseCls=responseCls;
    }

    private final int HEAD_LENGTH=4; //如果是sail的Header 就是 16


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes()<HEAD_LENGTH)  // 读取的是一个int
            return ;

//        in.markReaderIndex(); //标记 当前的 readIndex 位置
//
//        int dataLength=in.readInt();
//
//        if(dataLength<0)
//           ctx.close();
//        if(in.readableBytes()<dataLength){  // 可读的小于实际的 重置 dataLength
//            in.resetReaderIndex();
//            return;
//        }

        /**
         *   改用get  具体的参考 {@link ByteToMessageDecoder} doc
         */

        int dataLength=in.getInt(0);
        if(dataLength<0){
            ctx.close();
        }
        if(in.readableBytes()<dataLength){
            return;
        }

        byte []body=new byte[dataLength];
        in.readBytes(body);
        Object obj=SerializationUtil.deserialize(body,responseCls);
        out.add(obj);
    }




}
