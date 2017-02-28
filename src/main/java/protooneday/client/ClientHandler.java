package protooneday.client;


import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protooneday.Vo.RequestVo;
import protooneday.Vo.ResponseVo;
import protooneday.Vo.RpcContent;


/**
 * Created by lenovo on 2016/6/17.
 */
public class ClientHandler extends SimpleChannelInboundHandler<ResponseVo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private RpcContent rpcContent;

    public ClientHandler(RpcContent rpcContent){
        this.rpcContent=rpcContent;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseVo msg) throws Exception {


     // 读到请求时加锁写入
            synchronized (rpcContent){
                rpcContent.setResponseVo(msg);
                rpcContent.RESULT_OK=true;
                  rpcContent.notifyAll();  // java的所有对象锁机制带给了JVM的复杂性
            }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

                   RequestVo requestVo=rpcContent.getRequestVo();
       // ctx.writeAndFlush(requestVo); //发送请求 out、 // 新增了 addListener

            while(true) {
                if (ctx.channel().isActive()) {
                    if (ctx.channel().isWritable()) {
                        ctx.writeAndFlush(requestVo);
                        break;
                    } else {
                            Thread.sleep(100);
                    }
                } else {
                    LOGGER.error("channel active active", requestVo.getRequestId(), requestVo.getArgs());
                }
            }


    }




    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        // 在最后一个 handler 要进行异常处理 不让异常继续传递


    }
}
