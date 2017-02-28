package proto.executor;

/**
 * Created by lenovo on 2017/2/21.
 */
public abstract class AbstractExecutorFactory implements ExecutorFactory {

    protected int coreWorks(Target target){
        int defaultValue=ExecutorConstants.PROCESSORS<<1; //IO 密集型
        switch (target){
            case Consumer:                  //能够在这里再对consumer2次加工
                return defaultValue;
            case Provider:                  //能够对provider2次加工
                return defaultValue;
            default:
                return defaultValue;
        }

    }

    protected int maxWorks(Target target){
        int defaultValue=ExecutorConstants.MAX_WORKS;
        switch (target){
            case Consumer:
                return defaultValue;
            case Provider:
                return defaultValue;
            default:
                return defaultValue;
        }
    }

    protected int queueCapacity(Target target){
        int defaultValue=ExecutorConstants.PROCESSOR_WORKER_QUEUE_CAPACITY;
        switch (target){
            case Consumer:
                return defaultValue;
            case Provider:
                return defaultValue;
            default:
                return defaultValue;
        }
    }



}
