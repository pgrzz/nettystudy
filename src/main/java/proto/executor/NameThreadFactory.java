package proto.executor;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lenovo on 2017/2/25.
 */
public class NameThreadFactory implements ThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(NameThreadFactory.class);

    private static final AtomicInteger poolId=new AtomicInteger();

    private final AtomicInteger nextId=new AtomicInteger();
    private final String prefix;
    private final boolean isDaemon;
    private final int priority;
    private final ThreadGroup group;


    public NameThreadFactory(String prefix) {
        this(prefix,false,Thread.NORM_PRIORITY);
    }

    public NameThreadFactory(String prefix, boolean isDaemon, int priority) {
        this.prefix = prefix;
        this.isDaemon = isDaemon;
        this.priority = priority;
        SecurityManager manager=System.getSecurityManager();
        this.group = (manager==null)?Thread.currentThread().getThreadGroup(): manager.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Preconditions.checkNotNull(r);
        String name=prefix+nextId.getAndIncrement();
        Thread t=new Thread(group,r,name,0);
        try {
            if(isDaemon){
                t.setDaemon(true);
            }else{
                t.setDaemon(false);
            }
            if(t.getPriority()!=priority){
                t.setPriority(priority);
            }
        }catch (Exception ignored){}

        return t;
    }



    public ThreadGroup getThreadGroup(){
        return group;
    }

}
