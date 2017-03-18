package proto.handler.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.HashedWheelTimer;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.PlatformDependent;
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
import proto.consumer.ConsumerProcessor;
import proto.provider.ProviderProcessor;
import util.SerializationUtils;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 *





 *
 *
 * 在Jupiter 中
 *              定义(udp,tcp)
 * 连接层  Transporter(interface) ---->
 * 定义 ip+port,启动，config,关闭映射到 Netty bootstrap(start,shutdown) 处理者(Processor)
 * JAccept(interface)  中间的NettyAcceptor 可以不要 层数太多了 不过这样通用的 启动和关闭等方法
 * 就子类都要扩展 JDK1.8可以考虑 default void start(){},default void shutdown(){}.
 *        --->
 *        定义了 epoll ET?LT ,Protocol=TCP,服务Netty配置（boss,work Option）,IoRatio(业务与IO的线程处理比例默认 50%),start(Netty->bind),shutdown.
 *        NettyTcpAcceptor(Impl---->JAccept)
 *        除了 没有 epoll 模式定义,Protocol=UDP 其他的和TCP大同小异
 *        NettyUdtAcceptor(Impl---->JAccept)
 *        定义 连接(ip+port),config,Processor,groups(Directory->group), 是否直连(决定了是否从注册中心测试有用),连接管理，启动关闭
 *        JConnector(interface) NettyConnector 可以default扩展
 *             -->
 *             添加了handler,idle,encoder,AccepterHandler(Processor),init(Options),bind
 *             JNettyTcpAcceptor(Extend--->NettyTcpAcceptor )
 *              和TCP一样init协议不同Options不同
 *              JNettyUdtAcceptor(Extend--->NettyUdyAcceptor)
 *         --->
 *         定义 epoll ET?LT, IoRatio(默认50%)  groups, Protocol=TCP ,Config,时间轮(但是放这里感觉上下文没联系放在子类会好一些?), 连接管理connectManager,
 *         NettyTcpConnector(Impl-->NConnector)
 *         除了 epoll ET?LT,模式为UDP 其他大同小异
 *         NettyUdtConnector
 *              -->
 *              添加了 idle,encoder,Connector(Processor) intit(ET?LT,Options) connect(定义 watchDog ，watchDog里面复用watchDog连接)
 *              JNettyTcpConnector(Extend--->NettyTcpConnector)
 *              和TCP一样 除了Option不同
 *              jNettyUdtConnector
 *
 *      在Jupiter中 应该用内部类来 减少一些类 比如 NativeETSupport 这些可以放到Config里面其实.
 *
 *
 * Created by lenovo on 2017/2/9.
 */
public class handlerTest {

    private  static  final CountDownLatch latch=new CountDownLatch(1);


    private Bootstrap bootstrap;
    private EventLoopGroup work=new NioEventLoopGroup(); // default cpu *2
    private EventLoopGroup boss=new NioEventLoopGroup();
    private static final HashedWheelTimer timer=new HashedWheelTimer();
    private final WriteBufferWaterMark waterMark=WriteBufferWaterMark.DEFAULT;

    private ServerBootstrap serverBootstrap;



    private final SailEncode encode=new SailEncode();

    /**
     * server handlers
     */
    private final AcceptIdleTrigger idleTrigger=new AcceptIdleTrigger();
    private final AcceptHandler acceptHandler=new AcceptHandler();

    /**
     * connect handler
     */
    private final ConnectHandler connectHandler=new ConnectHandler();

    /**
     *
    对于服务来说 维护一个Map<Directory,group>的channels集合.
     它是线程安全的 内部由ConCurrentMap 来维护所以WatchDog中的失效触发的HashWheelTimer恢复策略不会有问题
     */
   private static  final ChannelGroup group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public ChannelGroup getGroup() {
        return group;
    }

    public void initServer(){
      serverBootstrap=new ServerBootstrap();
        serverBootstrap.group(boss,work).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.ALLOCATOR,new PooledByteBufAllocator(PlatformDependent.directBufferPreferred()))
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
        Channel c=null;

        if(group.size()>0) {
            Channel[] arrays = new Channel[group.size()];
            group.toArray(arrays);
             c = arrays[0];
        }else{
            c=future.channel();
        }

        SailRequestByte request=new SailRequestByte();
        SimpleVo vo=new SimpleVo("aa",18);
        request.setBytes(SerializationUtils.serialize(vo));
        request.setSerializer((byte)0x9);
        request.setTimestamp(1000L);
        long time=System.currentTimeMillis();
        for(int i=0;i<=100000;i++){
            c.writeAndFlush(request);
            if(i==100000){
                latch.countDown();
            }
        }
        latch.await();
        //总时间
        System.out.println("花费:"+(System.currentTimeMillis()-time));


        test.destory();

    }



}
