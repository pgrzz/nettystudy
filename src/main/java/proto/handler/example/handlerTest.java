package proto.handler.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.HashedWheelTimer;
import io.netty.util.concurrent.GlobalEventExecutor;
import proto.Processor;
import proto.channel.Directory;
import proto.executor.ConsumerExecutors;
import proto.executor.ProviderExecutors;
import proto.handler.IdleStateCheckHandler;
import proto.handler.SailDecode;
import proto.handler.SailEncode;
import proto.handler.accept.AcceptHandler;
import proto.handler.accept.AcceptIdleTrigger;
import proto.handler.connect.ConnectHandler;
import proto.handler.connect.ConnectionWatchDog;
import proto.message.SailRequestByte;
import proto.provider.ConsumerProcessor;
import proto.provider.ProviderProcessor;
import util.SerializationUtils;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
/**
 *
 * Created by lenovo on 2017/2/9.
 */
public class handlerTest {

    private Bootstrap bootstrap;
    private EventLoopGroup work=new NioEventLoopGroup(); // default cpu *2
    private EventLoopGroup boss=new NioEventLoopGroup();
    private final HashedWheelTimer timer=new HashedWheelTimer();
    private final WriteBufferWaterMark waterMark=WriteBufferWaterMark.DEFAULT;

    private ServerBootstrap serverBootstrap;





    /**
     * server handlers
     */
    private final AcceptIdleTrigger idleTrigger=new AcceptIdleTrigger();
    private final SailEncode encode=new SailEncode();
    private final AcceptHandler acceptHandler=new AcceptHandler();

    /**
     * connect handler
     */
    private final ConnectHandler connectHandler=new ConnectHandler();

    //对于服务来说 维护一个Map<Directory,group>的channels集合.
    private static  final ChannelGroup group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public ChannelGroup getGroup() {
        return group;
    }

    public void initServer(){
      serverBootstrap=new ServerBootstrap();
        serverBootstrap.group(boss,work).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,32768)
                .option(ChannelOption.SO_REUSEADDR,true)
                .childOption(ChannelOption.SO_REUSEADDR,true)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,false)
                .childOption(ChannelOption.ALLOW_HALF_CLOSURE,true)
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,waterMark);

    }
    public ChannelFuture bind() throws InterruptedException {
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(
                        new IdleStateCheckHandler(timer,60,0,0),
                        idleTrigger,
                        new SailDecode(),
                        encode,
                        acceptHandler
                );
            }
        });
       return serverBootstrap.bind("localhost",8888).sync();
    }


    public void setComsumerProcess(Processor processor){
        //当执行请求时应该 应该IO线程释放控制权转化给业务线程做CPU密集型的工作处理.
        connectHandler.setProcessor(processor);
    }
    public void setProviderProcess(Processor processor){
        acceptHandler.setProcessor(processor);
    }

    public void initConnect(){
        bootstrap=new Bootstrap();
        bootstrap.group(work)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true);
    }
    public ChannelFuture connect() throws InterruptedException {
        final SocketAddress socketAddress = InetSocketAddress.createUnresolved("localhost",8888);
        final ConnectionWatchDog watchDog=new ConnectionWatchDog(bootstrap,timer,socketAddress,group) {
            @Override
            public ChannelHandler[] handlers() {
                return new ChannelHandler[]{
                        this,
                        new IdleStateCheckHandler(timer,60,0,0),
                        idleTrigger,
                        new SailDecode(),
                        encode,
                        connectHandler
                };
            }
        };
        watchDog.start();
        ChannelFuture future;
        synchronized (bootstrap){
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(watchDog.handlers());
                }
            });
        }
        future=bootstrap.connect("localhost",8888).sync();

        return future;
    }

    public void destory(){
        work.shutdownGracefully();
        boss.shutdownGracefully();
    }


    private static final AttributeKey<Directory> CHANNEL_ATTRIBUTE_KEY=AttributeKey.valueOf("netty.channel");

    /**
     * 只要把这部分分离开来再加一些些抽象就可以变成完整一些的传输层
     */
    public static void main(String[]args) throws InterruptedException {
        handlerTest test=new handlerTest();
        //server
        test.initServer();
        test.bind();
        // client
        test.initConnect();

        ProviderProcessor providerProcessor=new ProviderProcessor(ProviderExecutors.getExecutor());
        ConsumerProcessor consumerProcessor=new ConsumerProcessor(ConsumerExecutors.executor());
        test.setProviderProcess(providerProcessor);
        test.setComsumerProcess(consumerProcessor);

        ChannelFuture future=test.connect();
        ChannelGroup group=test.getGroup();
        if(group.size()==0){
            group.add(future.channel());
        }
        Channel[] arrays=new Channel[group.size()];
        group.toArray(arrays);
        Channel c=arrays[0];

        SailRequestByte request=new SailRequestByte(1);
        SimpleVo vo=new SimpleVo("aa",18);
        request.setBytes(SerializationUtils.serialize(vo));
        request.setSerializer((byte)0x9);
        request.setTimestamp(1000L);
        for(int i=0;i<100000;i++){
            c.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("成功");
                    }else {
                        System.out.println("失败");
                    }
                }
            });

        }



       // test.destory();

    }



}
