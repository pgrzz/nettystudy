package proto.handler;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import proto.SystemClock;

import java.util.concurrent.TimeUnit;

/**
 *  1. Netty4.x默认的链路检测使用的是eventLoop的delayQueue, delayQueue是一个优先级队列, 复杂度为O(log n),
 *      每个worker处理自己的链路监测, 可能有助于减少上下文切换, 但是网络IO操作与idle会相互影响.
 *  2. 这个实现使用{@link HashedWheelTimer}的复杂度为O(1), 而且网络IO操作与idle不会相互影响, 但是有上下文切换.
 *  3. 如果连接数小, 比如几万以内, 可以直接用Netty4.x默认的链路检测 {@link io.netty.handler.timeout.IdleStateHandler},
 *      如果连接数较大, 建议使用这个实现.
 *
 */
public class IdleStateCheckHandler extends ChannelDuplexHandler{

    private static final long MIN_TIMEOUT_MILLIS=1;



    private final HashedWheelTimer timer;
    private final long readerIdleTimeMillis;
    private final long writerIdleTimeMillis;
    private final long allIdleTimeMillis;

    private volatile int state; // 0 - none, 1 - initialized, 2 - destoryed

    private volatile boolean reading;

    private volatile Timeout readerIdleTimeout;
    private volatile Timeout writeIdleTimeout;
    private volatile Timeout allIdleTimeout;
    private volatile long   lastReadTime;
    private volatile long lastWriteTime;

    private boolean firstReaderIdleEvent=true;
    private boolean firstWriteIdleEvent=true;
    private boolean firstAllIdleEvent=true;

    public IdleStateCheckHandler(HashedWheelTimer timer, long readerIdleTimeMillis, long writerIdleTimeMillis, long allIdleTimeMillis) {
        this.timer = timer;
        this.readerIdleTimeMillis = readerIdleTimeMillis;
        this.writerIdleTimeMillis = writerIdleTimeMillis;
        this.allIdleTimeMillis = allIdleTimeMillis;
    }

    public IdleStateCheckHandler(HashedWheelTimer timer, long readerIdleTimeMillis, long writerIdleTimeMillis, long allIdleTimeMillis, TimeUnit unit) {
        Preconditions.checkNotNull(unit);
        this.timer=timer;
        if(readerIdleTimeMillis<=0){
            this.readerIdleTimeMillis=0;
        }else{
            this.readerIdleTimeMillis=Math.max(unit.toMillis(readerIdleTimeMillis),MIN_TIMEOUT_MILLIS);
        }
        if(writerIdleTimeMillis<=0){
            this.writerIdleTimeMillis=0;
        }else{
            this.writerIdleTimeMillis=Math.max(unit.toMillis(writerIdleTimeMillis),MIN_TIMEOUT_MILLIS);
        }
        if(allIdleTimeMillis<=0){
            this.allIdleTimeMillis=0;
        }else {
            this. allIdleTimeMillis=Math.max(unit.toMillis(allIdleTimeMillis),MIN_TIMEOUT_MILLIS);
        }

    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
       if(ctx.channel().isActive() && ctx.channel().isRegistered()){
           // channelActive() event has been fired already, which means this.channelActive() will
           // not be invoked. We have to initialize here instead.
           initalizer(ctx);
       }else{
           // channelActive() event has not been fired yet.  this.channelActive() will be invoked
           // and initialization will occur there.

       }
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        destory();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        if(ctx.channel().isActive()){
            initalizer(ctx);
        }
        super.channelRegistered(ctx);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        initalizer(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        destory();
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(readerIdleTimeMillis>0 || allIdleTimeMillis >0){
            firstReaderIdleEvent=firstAllIdleEvent=true;
            reading=true;
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if(readerIdleTimeMillis>0 || allIdleTimeMillis> 0){
            lastReadTime=SystemClock.getMillisClock().now();
            reading=false;
        }
       ctx.fireChannelReadComplete();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(writerIdleTimeMillis>0 || allIdleTimeMillis>0){
            firstWriteIdleEvent=firstAllIdleEvent=true;
            lastWriteTime=SystemClock.getMillisClock().now();
        }else {
            promise.addListener(writeListener);
        }
        ctx.write(msg,promise);
    }
    // do not create a new ChannelFutureListener per write operation to reduce GC pressure.
    private final ChannelFutureListener writeListener=(future -> {
        firstWriteIdleEvent=firstAllIdleEvent=true;
        lastWriteTime= SystemClock.getMillisClock().now(); // make hb for firstWriterIdleEvent and firstAllIdleEvent
    });

    public long getWriterIdleTimeMillis() {
        return writerIdleTimeMillis;
    }

    public long getAllIdleTimeMillis() {
        return allIdleTimeMillis;
    }

    public long getReaderIdleTimeMillis() {
        return readerIdleTimeMillis;
    }

    private void initalizer(ChannelHandlerContext ctx){
        // Avoid the case where destroy() is called before scheduling timeouts.
        // See: https://github.com/netty/netty/issues/143
        switch (state){
            case 1:
            case 2:
                return;
        }
        state=1;
        lastReadTime=lastWriteTime=SystemClock.getMillisClock().now();
        if(readerIdleTimeMillis>0){
            readerIdleTimeout=timer.newTimeout(new ReaderIdleTimeoutTask(ctx),readerIdleTimeMillis,TimeUnit.MILLISECONDS);
        }
        if(writerIdleTimeMillis>0){
            writeIdleTimeout=timer.newTimeout(new WriterTimeOutTask(ctx),writerIdleTimeMillis,TimeUnit.MILLISECONDS);
        }
        if(allIdleTimeMillis>0){
            allIdleTimeout=timer.newTimeout(new AllIdleTimeoutTask(ctx),allIdleTimeMillis,TimeUnit.MILLISECONDS);
        }
    }
    private void destory(){
        state=2;
        if(readerIdleTimeout!=null){
            readerIdleTimeout.cancel();
            readerIdleTimeout=null;
        }
        if(writeIdleTimeout!=null){
            writeIdleTimeout.cancel();
            writeIdleTimeout=null;
        }
        if(allIdleTimeout!=null){
            allIdleTimeout.cancel();
            allIdleTimeout=null;
        }
    }

    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt)throws Exception{
        ctx.fireUserEventTriggered(evt);
    }

    private final class ReaderIdleTimeoutTask implements TimerTask{
        private final ChannelHandlerContext ctx;

        public ReaderIdleTimeoutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }


        @Override
        public void run(Timeout timeout) throws Exception {
            if(timeout.isCancelled() || !ctx.channel().isOpen()){
                return;
            }

            long lastReadTime=IdleStateCheckHandler.this.lastReadTime;
            long nextDelay=readerIdleTimeMillis;
            if(!reading){
                nextDelay -= SystemClock.getMillisClock().now()-lastReadTime;
            }
            if(nextDelay<=0){
                // Reader is idle - set a new timeout and notify the callback.
                try {
                    readerIdleTimeout=timer.newTimeout(this,readerIdleTimeMillis,TimeUnit.MILLISECONDS);
                    IdleStateEvent evt;
                    if(firstReaderIdleEvent){
                        firstReaderIdleEvent=false;
                        evt=IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT;
                    }else {
                        evt=IdleStateEvent.READER_IDLE_STATE_EVENT;
                    }
                    channelIdle(ctx,evt);
                }catch (Throwable t){
                    ctx.fireExceptionCaught(t);
                }

            }else{
                // Read occurred before the timeout - set a new timeout with shorter delay.
                readerIdleTimeout=timer.newTimeout(this,nextDelay,TimeUnit.MILLISECONDS);
            }
        }
    }
    private final class WriterTimeOutTask implements TimerTask{
        private final ChannelHandlerContext ctx;

        public WriterTimeOutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            if (timeout.isCancelled() || !ctx.channel().isOpen()) {
                return;
            }
            long lastWriteTime=IdleStateCheckHandler.this.lastWriteTime;
            long nextDelay=writerIdleTimeMillis-(SystemClock.getMillisClock().now()-lastWriteTime);
            if(nextDelay<=0){
                writeIdleTimeout=timer.newTimeout(this,writerIdleTimeMillis,TimeUnit.MILLISECONDS);
                try {
                    IdleStateEvent event;
                    if(firstWriteIdleEvent){
                        firstWriteIdleEvent=false;
                        event=IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT;
                    }else{
                        event=IdleStateEvent.WRITER_IDLE_STATE_EVENT;
                    }
                    channelIdle(ctx,event);
                }catch (Throwable t){
                    ctx.fireExceptionCaught(t);
                }
            }else{
                writeIdleTimeout=timer.newTimeout(this,nextDelay,TimeUnit.MILLISECONDS);
            }
        }
    }

    private final class AllIdleTimeoutTask implements TimerTask{
        private final ChannelHandlerContext ctx;

        public AllIdleTimeoutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            if (timeout.isCancelled() || !ctx.channel().isOpen()) {
                return;
            }
            long nextDelay=allIdleTimeMillis;
            if(!reading){
                long lastTime=Math.max(lastReadTime,lastWriteTime);
                nextDelay -= SystemClock.getMillisClock().now()-lastTime;
            }
            if(nextDelay<=0){
                allIdleTimeout=timer.newTimeout(this,allIdleTimeMillis,TimeUnit.MILLISECONDS);
                try {
                    IdleStateEvent evt;
                    if(firstAllIdleEvent){
                        firstAllIdleEvent=false;
                        evt=IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT;
                    }else{
                        evt=IdleStateEvent.ALL_IDLE_STATE_EVENT;
                    }
                    channelIdle(ctx,evt);
                }catch (Throwable t){
                    ctx.fireExceptionCaught(t);
                }
            }else{
                // Either read or write occurred before the timeout - set a new
                // timeout with shorter delay.
                allIdleTimeout=timer.newTimeout(this,nextDelay,TimeUnit.MILLISECONDS);
            }
        }
    }

}
