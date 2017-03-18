package proto.executor.reject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by lenovo on 2017/3/6.
 */
public class DiscardTaskPolicy extends  AbstractRejectedExecutionHandler {

    public DiscardTaskPolicy(String threadName){
        super(threadName,false,"");
    }

    public DiscardTaskPolicy(String threadName, boolean dumpNeeded, String dumpPrefixName) {
        super(threadName, dumpNeeded, dumpPrefixName);
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        logger.error("thread pool exhausted :{}",threadName, executor.toString());
        if(!executor.isShutdown()){
            BlockingQueue<Runnable> queue=executor.getQueue();
            int discardSize=queue.size()>>1;
            for(int i=0;i<discardSize;i++){
                queue.poll();
            }
            queue.offer(r);
        }

    }
}
