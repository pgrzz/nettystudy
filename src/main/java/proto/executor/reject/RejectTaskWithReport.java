package proto.executor.reject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *  * 如果当前任务实现了 {@link RejectRunnable} 接口, 那么交给用户去实现拒绝任务的逻辑,
 * 否则以FIFO的方式抛弃队列中一部分现有任务.
 *
 * Created by lenovo on 2017/2/21.
 */
public class RejectTaskWithReport  extends AbstractRejectedExecutionHandler {

    private static final Logger logger= LoggerFactory.getLogger(RejectTaskWithReport.class);


    public RejectTaskWithReport(String threadName, boolean dumpNeeded, String dumpPrefixName, String threadName1) {
        super(threadName, dumpNeeded, dumpPrefixName);
    }

   public RejectTaskWithReport(String threadPoolName){
       super(threadPoolName,true,"");

   }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
         logger.error("Thread pool :{}",threadName,executor.toString());
        dumpJvmInfo();
        if(r instanceof RejectRunnable){
            ((RejectRunnable) r).rejected(); //用户处理
        }else{
            if(!executor.isShutdown()){
                BlockingQueue<Runnable> queue= executor.getQueue();
                int discardSize=queue.size()>>1;
                for(int i=0;i<discardSize;i++){
                    queue.poll();
                }

                try {
                    queue.put(r);
                } catch (InterruptedException ignore) {
                     //should not be interrupted
                }

            }

        }

    }
}
