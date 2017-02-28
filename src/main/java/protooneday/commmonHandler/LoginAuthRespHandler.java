package protooneday.commmonHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protooneday.Vo.ResponseVo;
import protooneday.Vo.RpcContent;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lenovo on 2016/6/13.
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    private Map<String,Boolean>  nodeCheak=new ConcurrentHashMap<>();

    private String[] whiteList ={"127.0.0.1"};

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
          nodeCheak.remove(ctx.channel().remoteAddress().toString());
          ctx.close();
          ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ResponseVo responseVo=null;
        RpcContent rpcContent=(RpcContent)msg;
          RpcContent.Head header=  rpcContent.getHeader();
           if(header.getType()==RpcContent.MessageType.LOGIN_REQ){
               String nodeIndex=ctx.channel().remoteAddress().toString();
                if(nodeCheak.containsKey(nodeIndex)){
                    // 防止重复登录
                    responseVo=new ResponseVo();
                     responseVo.setStatus(500);
                    responseVo.setCode("你已经登录");
                     responseVo.setResult(null);


                }else {
                    InetSocketAddress address=(InetSocketAddress) ctx.channel().remoteAddress();
                    String ip=address.getAddress().getHostAddress();
                    boolean isOk=false;
                     for(String WIP:whiteList){
                         if(WIP.equals(ip)){
                             isOk=true;
                             break;
                         }
                     }
                    responseVo=new ResponseVo();
                          if(isOk){
                              responseVo.setStatus(200);
                              nodeCheak.put(nodeIndex,true);

                          }else {
                              responseVo.setStatus(500);
                          }
                       //传递 content
                     rpcContent.setResponseVo(responseVo);
                    ctx.writeAndFlush(rpcContent);
                }
           }else{
                ctx.fireChannelRead(msg);
           }

    }
}
