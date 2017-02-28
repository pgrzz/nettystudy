package proto.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lenovo on 2017/2/24.
 */
public class ForkJoinPoolExecutorFactory extends AbstractExecutorFactory {

    private static final Logger logger= LoggerFactory.getLogger(ForkJoinPoolExecutorFactory.class);



    @Override
    public Executor newExecutor(Target target,String name) {

        return new ForkJoinPool(
                coreWorks(target),
                new DefaultForkJoinWorkerThreadFactory(name),
                new DefaultUncaughtExceptionHandler(),true
        );
    }

    private static final  class DefaultForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory{

        private final AtomicInteger idx=new AtomicInteger();
        private final String namePrefix;

        private DefaultForkJoinWorkerThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
           ForkJoinWorkerThread thread=new DefaultForkJoinWorkerThread(pool);
            return thread;
        }
    }

    private static final class DefaultUncaughtExceptionHandler implements  Thread.UncaughtExceptionHandler{
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.error("Uncaught exception in thread :{}" ,t.getName(),e);
        }
    }
    private static final class DefaultForkJoinWorkerThread extends ForkJoinWorkerThread{
        /**
         * Creates a ForkJoinWorkerThread operating in the given pool.
         *
         * @param pool the pool this thread works in
         * @throws NullPointerException if pool is null
         */
        protected DefaultForkJoinWorkerThread(ForkJoinPool pool) {
            super(pool);
        }
    }

}
