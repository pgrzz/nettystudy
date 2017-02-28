package protooneday.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import protooneday.Utilon.RpcDecode;
import protooneday.Utilon.RpcEncode;
import protooneday.Vo.RequestVo;
import protooneday.Vo.ResponseVo;
import protooneday.Vo.RpcContent;
import protooneday.server.PingHandler;

import java.util.concurrent.*;

/**
 * Created by lenovo on 2016/6/12.
 */
public class RpcClient  {



  private static  final  EventLoopGroup eventExecutors=new NioEventLoopGroup();


    public Object connect(int port, String host, final RequestVo requestVo) throws InterruptedException, ExecutionException {


        RpcContent   rpcContent =new RpcContent();
         rpcContent.setRequestVo(requestVo);
        ResponseVo responseVo=null;
        try {
            Bootstrap bootstrap=new Bootstrap();

            bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcEncode(RequestVo.class));
                            ch.pipeline().addLast(new RpcDecode(ResponseVo.class));
                            ch.pipeline().addLast(new ClientHandler(rpcContent));

                        }
                    });

            ChannelFuture channelFuture=bootstrap.connect(host,port).sync(); // 主机选择交给发现中心去解决 然后 可以在发现中心实现负载均衡 并且在客服端缓存服务器列表
              for(;;){
                  if(!rpcContent.RESULT_OK){
                      synchronized (rpcContent) {
                          rpcContent.wait();
                      }
                  }
                  responseVo=rpcContent.getResponseVo();
                  break;
              }

            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    // 对该 channel 注册 关心的事件
                    if(!future.isSuccess()){
                        // 失败 处理
                    }
                    else if(future.isCancelled()){
                    }
                    else {
                        future.channel().close();
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();
               return  responseVo.getResult();

        }finally {

            eventExecutors.shutdownGracefully();

        }

    }

}
