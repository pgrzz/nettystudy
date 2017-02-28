package proto.handler.connect;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.handler.ChannelHandlersHolder;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2017/2/8.
 */
@ChannelHandler.Sharable
public abstract class ConnectionWatchDog extends ChannelInboundHandlerAdapter implements TimerTask,ChannelHandlersHolder{

    private static final Logger logger= LoggerFactory.getLogger(ConnectionWatchDog.class);

    private static final int ST_STARTED=1;
    private static final int ST_STOPED=2;

    private final Bootstrap bootstrap;
    private final Timer timer;
    private final SocketAddress socketAddress;
    private final ChannelGroup channels;

    private volatile int state;
    private  int attempts;

    public ConnectionWatchDog(Bootstrap bootstrap, Timer timer, SocketAddress socketAddress, ChannelGroup channels) {
        this.bootstrap = bootstrap;
        this.timer = timer;
        this.socketAddress = socketAddress;
        this.channels = channels;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if(channels!=null){
            channels.add(ctx.channel());
        }
        attempts=0;
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        boolean doReconnect=isStarted() && (channels ==null || channels.size()<DEFAULT_CAPACITY);
        if(doReconnect){
            if(attempts<12){
                attempts++;
            }
        }
        long timeout=2<<attempts;
        timer.newTimeout(this,timeout, TimeUnit.MILLISECONDS);
    }


    public boolean isStarted(){
        return state==ST_STARTED;
    }
    public void start(){
        state=ST_STARTED;
    }
    public void stop(){
        state=ST_STOPED;
    }

    private static final int DEFAULT_CAPACITY=10;
    @Override
    public void run(Timeout timeout) throws Exception {
        if(channels!=null && channels.size()>=DEFAULT_CAPACITY){
            logger.warn("channel满了");
            return;
        }
        ChannelFuture future;
        synchronized (bootstrap){
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(handlers()); //abstract handlers method
                }
            });
            future=bootstrap.connect(socketAddress);
        }
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                boolean success= future.isSuccess();
                if(!success){
                    future.channel().pipeline().fireChannelInactive();
                }
            }
        });

    }


}
