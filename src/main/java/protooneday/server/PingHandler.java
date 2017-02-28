package protooneday.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import sun.plugin2.message.HeartbeatMessage;

/**
 * Created by lenovo on 2016/8/16.
 */
public class PingHandler extends IdleStateHandler {
    public PingHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }



    private static final ByteBuf ping= Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("HEATBEAT", CharsetUtil.UTF_8));

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){

            IdleStateEvent event=(IdleStateEvent)evt;
            switch (event.state()){
                case READER_IDLE:ctx.close();
                break;
                case WRITER_IDLE:ctx.writeAndFlush(HeartbeatMessage.DEFAULT_INTERVAL);

            }

        }else {
            super.userEventTriggered(ctx,evt);
        }
    }
}
