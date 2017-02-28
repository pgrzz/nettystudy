package protooneday.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import protooneday.Vo.RequestVo;
import protooneday.Vo.ResponseVo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lenovo on 2016/6/11.
 */
public class SimpleHander extends SimpleChannelInboundHandler {


      private final Map<String,Object>   map;
     public  SimpleHander(Map<String,Object> map){
         this.map=map;
     }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        RequestVo requestVo=(RequestVo)msg;
        Object obj =new Invoke(map).invoke(requestVo);
        ResponseVo responseVo=new ResponseVo();
        responseVo.setCode("成功");
        responseVo.setStatus(1);
        responseVo.setResult(obj);
        ctx.writeAndFlush(responseVo);


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();

    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
           cause.printStackTrace();  //当服务器出现异常和关闭时应该 取消注册 释放资源
        ctx.close();
    }
}
