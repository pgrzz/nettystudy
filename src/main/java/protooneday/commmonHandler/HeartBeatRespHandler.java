package protooneday.commmonHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protooneday.Vo.RpcContent;

/**
 * Created by lenovo on 2016/6/14.
 */
public class HeartBeatRespHandler extends SimpleChannelInboundHandler {

    private RpcContent buildHeatBeat(){
        RpcContent rpcContent=new RpcContent();
        RpcContent.Head head=rpcContent.HeadFactory();
         head.setType(RpcContent.MessageType.HEARTBEAT_RESP);
          rpcContent.setHeader(head);
          return  rpcContent;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcContent rpcContent=(RpcContent) msg;

          if(rpcContent.getHeader()!=null && rpcContent.getHeader().getType()==RpcContent.MessageType.HEARTBEAT_REQ){
              // 判断是不是 服务器 发过来的 请求 然后  如果是 心跳请求的话 就处理 否则的话就 正常交给下个 handler
                 RpcContent beatContext=buildHeatBeat();
               ctx.writeAndFlush(beatContext);
          }else {
              ctx.fireChannelRead(msg);

          }

    }
}
