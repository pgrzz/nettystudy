package proto.executor.reject;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by lenovo on 2017/3/6.
 */
public class CallerRunPolicy extends AbstractRejectedExecutionHandler {

    public CallerRunPolicy(String threadName){
        super(threadName,false,"");
    }


    public CallerRunPolicy(String threadName, boolean dumpNeeded, String dumpPrefixName) {
        super(threadName, dumpNeeded, dumpPrefixName);
    }


    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        logger.error("Thread Pool {}",threadName);
            if(!executor.isShutdown()){
                r.run();
            }
    }
}
