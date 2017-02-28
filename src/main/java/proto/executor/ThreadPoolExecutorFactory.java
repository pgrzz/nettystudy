package proto.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.executor.reject.RejectTaskWithReport;

import java.util.concurrent.*;

/**
 * Created by lenovo on 2017/2/21.
 */
public class ThreadPoolExecutorFactory extends AbstractExecutorFactory {

    private static final Logger logger= LoggerFactory.getLogger(ThreadPoolExecutorFactory.class);

    @Override
    public Executor newExecutor(Target target,String name) {
        return new ThreadPoolExecutor(
                coreWorks(target),
                maxWorks(target),
                120L,
                TimeUnit.SECONDS,
                workQueue(target,WorkerQueueType.ARRAY_BLOCKING_QUEUE),
                new NameThreadFactory(name),
                createRejectedPolicy(target,new RejectTaskWithReport(name))
        );
    }



    private BlockingQueue<Runnable> workQueue(Target target,WorkerQueueType queueType){
        BlockingQueue<Runnable> workQueue=null;
        switch (queueType){
            case LINKED_BLOCKING_QUEUE:
                workQueue=new LinkedBlockingDeque<>(ExecutorConstants.PROCESSOR_WORKER_QUEUE_CAPACITY);
            case ARRAY_BLOCKING_QUEUE:
                workQueue=new ArrayBlockingQueue<Runnable>(ExecutorConstants.PROCESSOR_WORKER_QUEUE_CAPACITY);
        }
    return workQueue;
    }
    private RejectedExecutionHandler createRejectedPolicy(Target target,RejectedExecutionHandler defaultHandler){
            RejectedExecutionHandler handler=null;
        handler=new RejectTaskWithReport(Thread.currentThread().getName(),false,"providerExecutor","thread");
        return handler;
    }
     enum WorkerQueueType{
        LINKED_BLOCKING_QUEUE,
        ARRAY_BLOCKING_QUEUE;

        static WorkerQueueType parse(String name){
            for(WorkerQueueType type:values()){
                if(type.name().equalsIgnoreCase(name)){
                    return type;
                }
            }
            return null;
        }

    }



}
