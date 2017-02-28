package proto;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**

 * {@link SystemClock} is a optimized substitute of {@link System#currentTimeMillis()} for avoiding context switch overload.
 *
 * Every instance would start a thread to update the time, so it's supposed to be singleton in application context.
 *
 * Forked from <A>https://github.com/zhongl/jtoolkit/blob/master/common/src/main/java/com/github/zhongl/jtoolkit/SystemClock.java</A>
 */
public class SystemClock {

    private static final SystemClock millisClock=new SystemClock(1);
    private final long precision;
    private final AtomicLong now;
    public static SystemClock getMillisClock(){
        return millisClock;
    }
    private SystemClock(long precision){
        this.precision=precision;
        now=new AtomicLong(System.currentTimeMillis());

    }
    private void scheduleClockUpdating(){
        ScheduledExecutorService scheduledExecutorService= Executors.newSingleThreadScheduledExecutor((runnable)->{
            Thread t=new Thread(runnable,"system clock");
            t.setDaemon(true);
            return t;
        });
        scheduledExecutorService.scheduleAtFixedRate(()->{
          now.set(System.currentTimeMillis());
        },precision,precision, TimeUnit.MILLISECONDS);
    }
    public long now(){
        return now.get();
    }
    public long getPrecision(){
        return precision;
    }

}
