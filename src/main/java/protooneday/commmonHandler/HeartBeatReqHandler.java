package protooneday.commmonHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;
import protooneday.Vo.RpcContent;

import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2016/6/14.
 *  server give client a beat  once 5 mins
 */
public class HeartBeatReqHandler extends SimpleChannelInboundHandler{

    private volatile ScheduledFuture<?> heartBeat;

    private class HeartBeatTask implements  Runnable {

         private final ChannelHandlerContext ctx;

        public HeartBeatTask (final  ChannelHandlerContext ctx){
            this.ctx=ctx;
        }
        private RpcContent buildHeatBeat(){
            RpcContent rpcContent=new RpcContent();
            RpcContent.Head head=rpcContent.HeadFactory();
                  head.setType(RpcContent.MessageType.HEARTBEAT_REQ);
             rpcContent.setHeader(head);
            return  rpcContent;
        }


        @Override
        public void run() {
            RpcContent rpcContent=buildHeatBeat();
             ctx.writeAndFlush(rpcContent);

        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcContent rpcContent= (RpcContent) msg;
        RpcContent.Head header=rpcContent.getHeader();

          if(header!=null && header.getType()==RpcContent.MessageType.LOGIN_RESP){
                  heartBeat=ctx.executor().scheduleAtFixedRate(
                   new HeartBeatTask(ctx),0,500, TimeUnit.MILLISECONDS
                  );

          }else if(rpcContent.getHeader()!=null && rpcContent.getHeader().getType()==RpcContent.MessageType.HEARTBEAT_RESP){
              System.out.println("Client beta bessage");
          }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
         if(heartBeat==null){
             heartBeat.cancel(true);
             heartBeat=null;
         }
        ctx.fireExceptionCaught(cause);
    }
}
