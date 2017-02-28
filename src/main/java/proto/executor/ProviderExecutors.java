package proto.executor;

import util.SailServiceLoader;

import java.util.concurrent.Executor;

/**
 * Created by lenovo on 2017/2/27.
 */
public class ProviderExecutors {

    private static final Executor executor;

    static {

        ExecutorFactory factory=(ExecutorFactory) SailServiceLoader.loadFirst(ProviderExecutorFactory.class);
        executor= factory.newExecutor(ExecutorFactory.Target.Provider,"provider");
    }

    public static Executor getExecutor(){
        return executor;
    }

}
