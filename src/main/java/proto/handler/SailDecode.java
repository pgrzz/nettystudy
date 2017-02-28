package proto.handler;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import proto.*;
import proto.message.SailRequestByte;
import proto.message.SailResponseByte;

import java.util.List;

/**
 * Created by lenovo on 2017/2/7.
 */
public class SailDecode extends ReplayingDecoder<SailDecode.State>{

    private static final int MAX_BODY_LENGTH=1024*1024*5;

    private static final boolean USE_COMPOSITE_BUF=false;

    public SailDecode() {
        super(State.HEADER_MAGIC);
        if(USE_COMPOSITE_BUF){
        setCumulator(COMPOSITE_CUMULATOR);
        }
    }
    private final SailHeader header=new SailHeader();

    /**
     *  the decode method will be call more than once see {@link ReplayingDecoder}
     *  design like the demo in {@link ReplayingDecoder}
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()){
            case HEADER_MAGIC:
                   checkMagic(in.readShort());  //magic
                checkpoint(State.HEADER_SIGN);
            case HEADER_SIGN:
                header.sign(in.readByte()); //请求类型和序列化类型
                checkpoint(State.HEADER_STATUS);
            case HEADER_STATUS:
                header.setResponseStatus(in.readByte());// 请求状态
                checkpoint(State.HEADER_ID);
            case HEADER_ID:
                header.setId(in.readLong());
                checkpoint(State.HEADER_BODY_LENGTH);
            case HEADER_BODY_LENGTH:
                header.setBodyLength(in.readInt());
                checkpoint(State.BODY);
            case BODY:
                switch (header.getMessageCodeEnmu()){
                    case HEARTBEAT:
                     break;
                    case REQUEST:{
                        int len=checkBodyLength(header.getBodyLength());
                        byte[]bytes=new byte[len];
                        in.readBytes(bytes);
                        SailRequestByte sailRequestWarpper=new SailRequestByte(header.getId());
                        sailRequestWarpper.setTimestamp(SystemClock.getMillisClock().now());
                        sailRequestWarpper.setSerializer(header.getSerializer());
                        sailRequestWarpper.setBytes(bytes);
                        out.add(sailRequestWarpper);
                        break;
                    }
                    case RESPONSE:{
                        int len=checkBodyLength(header.getBodyLength());
                        byte[]bytes=new byte[len];
                        in.readBytes(bytes);
                        SailResponseByte sailResponseWarpper=new SailResponseByte(header.getId());
                        sailResponseWarpper.setStatus(header.getResponseStatus());
                        sailResponseWarpper.setSerializer(header.getSerializer());
                        sailResponseWarpper.setBytes(bytes);
                        out.add(sailResponseWarpper);
                        break;
                    }
                    default:
                        throw new Exception("IO Signals");
                }
                checkpoint(State.HEADER_MAGIC);
                break;
            default:
                throw new Error("Shouldn't reach here.");

        }


    }

    enum State{
        HEADER_MAGIC,
        HEADER_SIGN,
        HEADER_STATUS,
        HEADER_ID,
        HEADER_BODY_LENGTH,
        BODY
    }
    private static void checkMagic(short magic)throws Exception{
        if(magic!=SailHeader.MAGIC){
            throw new IllegalArgumentException("magic");
        }
    }
    private static int checkBodyLength(int size)throws Exception{
        if(size>MAX_BODY_LENGTH){
            throw new IllegalArgumentException("body size");
        }
        return size;
    }
}
