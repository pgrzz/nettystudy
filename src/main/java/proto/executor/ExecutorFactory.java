package proto.executor;

import java.util.concurrent.Executor;

/**
 * Created by lenovo on 2017/2/21.
 */
public interface ExecutorFactory extends ConsumerExecutorFactory,ProviderExecutorFactory {

        Executor newExecutor(Target target,String name);


    enum Target{
        Consumer,
        Provider
    }

}
