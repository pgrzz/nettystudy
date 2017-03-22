package proto.handler.connect;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.Processor;
import proto.message.SailResponseByte;

/**
 * Created by lenovo on 2017/2/8.
 */
@ChannelHandler.Sharable
public class ConnectHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger= LoggerFactory.getLogger(ConnectHandler.class);

    private Processor processor;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //这里还应该打印异常的原因
        ctx.channel().close();
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel channel=ctx.channel();
        if(!channel.isWritable()){
            channel.config().setAutoRead(false);
        }else{
            channel.config().setAutoRead(true);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof SailResponseByte){
            try {
                processor.handlerResponse((SailResponseByte) msg,ctx.channel());
            }catch (Throwable t){
                logger.error("unhandler exception {}, on channelRead()",t.getCause(),ctx.channel());
            }
        }else{
            logger.info("can't processor this type :{}"+msg.getClass());
        }
        ReferenceCountUtil.release(msg);
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
}
