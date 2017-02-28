package proto.handler.accept;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.IOException;

/**
 * Created by lenovo on 2017/2/8.
 */
@ChannelHandler.Sharable
public class AcceptIdleTrigger extends ChannelInboundHandlerAdapter {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if(evt instanceof IdleStateEvent){
                IdleState state=((IdleStateEvent) evt).state();
                if(state == IdleState.READER_IDLE){
                    throw new IOException("");
                }else{
                    super.userEventTriggered(ctx,evt);
                }
            }

    }
}
