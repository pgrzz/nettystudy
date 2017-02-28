package proto.executor;

import util.SailServiceLoader;

import java.util.concurrent.Executor;

/**
 * Created by lenovo on 2017/2/24.
 */
public class ConsumerExecutors {

    private static final Executor executor;

    static {
        ExecutorFactory factory=(ExecutorFactory) SailServiceLoader.loadFirst(ConsumerExecutorFactory.class);
            executor=factory.newExecutor(ExecutorFactory.Target.Consumer,"consumer");
    }
    public static Executor executor(){
        return executor;
    }
    public static void excute(Runnable command){
        executor.execute(command);
    }
}
