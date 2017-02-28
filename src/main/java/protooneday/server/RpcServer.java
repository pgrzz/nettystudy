package protooneday.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import protooneday.Utilon.RpcDecode;
import protooneday.Utilon.RpcEncode;
import protooneday.Vo.RequestVo;
import protooneday.Vo.ResponseVo;
import protooneday.server.annotation.RpcService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Handler;


/**
 * Created by lenovo on 2016/6/11.
 */
public class RpcServer  implements ApplicationContextAware,InitializingBean{

    @SuppressWarnings("resource")
    public static void main(String args[]){
        new ClassPathXmlApplicationContext("spring-server.xml");
    }


    private String serverAddress;

    private CuratorRegistry curatorRegistry;
   private final  Map<String,Object> map=new HashMap<String, Object>();
    public RpcServer(String serverAddress, CuratorRegistry curatorRegistry) {
        this.serverAddress = serverAddress;
        this.curatorRegistry = curatorRegistry;
    }

  private static  final   EventLoopGroup bossGroup=new NioEventLoopGroup();
    private static  final   EventLoopGroup workGroup=new NioEventLoopGroup();

    public void afterPropertiesSet() throws Exception {

        ServerBootstrap serverBootstrap=new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)  //新加的不知道会对内存怒有影响不
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler());
                            ch.pipeline().addLast(new RpcEncode(ResponseVo.class));
                            ch.pipeline().addLast(new RpcDecode(RequestVo.class));
                            ch.pipeline().addLast(new SimpleHander(map));
                        }
                    });

            int port=Integer.parseInt(serverAddress.split(":")[1]);
            ChannelFuture f = serverBootstrap.bind(port).sync();
             curatorRegistry.register(serverAddress,map.keySet());
            f.channel().closeFuture().sync();
        }finally {

            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
              Map<String,Object> mapservice= applicationContext.getBeansWithAnnotation(RpcService.class);

                      if(mapservice.size()>0) {
                          for (Object obj : mapservice.values()) {
                  //实例化的Impl Bean 和 接口名
                              String serviceName=obj.getClass().getAnnotation(RpcService.class).value().getName();

                              map.put(serviceName,obj);
                          }
                      }

    }


    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }


}
