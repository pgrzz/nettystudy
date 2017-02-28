package proto.handler.accept;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.Processor;
import proto.Status;
import proto.message.SailRequestByte;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lenovo on 2017/2/8.
 */
@ChannelHandler.Sharable
public class AcceptHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger= LoggerFactory.getLogger(AcceptHandler.class);

    private  static final AtomicInteger channelCounter=new AtomicInteger(0);

    private Processor processor;

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            if(msg instanceof SailRequestByte){
                try {
                    processor.handlerRequest((SailRequestByte) msg,ctx.channel()); //这里要做还没有做消息的释放
                }catch (Throwable e){
                    processor.handlerException(e, Status.SERVER_ERROR,ctx.channel());
                }

            }else{
                logger.warn("can't process this type of msg : {}" ,msg.getClass());
                ReferenceCountUtil.release(msg);
            }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        int count=channelCounter.decrementAndGet();
        logger.info("the numbers channels {}:"+count);
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int count=channelCounter.incrementAndGet();
        logger.info("the numbers channels {}:"+count);
        super.channelActive(ctx);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel channel=ctx.channel();

        if(!channel.isWritable()) { //写缓满了
            // 高水位线: ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK
            // 低水位线: ChannelOption.WRITE_BUFFER_LOW_WATER_MARK
            channel.config().setAutoRead(false);
        }else {
            channel.config().setAutoRead(true);
        }


    }
}
