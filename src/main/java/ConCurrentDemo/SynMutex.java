package ConCurrentDemo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by lenovo on 2017/1/28.
 */
public class SynMutex implements Lock {

    private static class Sync extends AbstractQueuedSynchronizer{

        // report whether in locked state


        @Override
        protected boolean isHeldExclusively() {
            return  getState()==1;
        }
        public boolean  tryAcquire(int acquire){
            if(compareAndSetState(0,1)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }
        //realse the lock by setting state to zero
        protected  boolean tryRelease(int release){
            assert release==1;
            if(compareAndSetState(1,0)){
                return true;
            }else{
                throw new IllegalArgumentException();
            }
        }
        Condition newCondition(){
            return new ConditionObject();
        }
        private void readObject(ObjectInputStream s)throws IOException,ClassNotFoundException{
            s.defaultReadObject();
            setState(0);
        }
    }

private final Sync sync=new Sync();
    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
            sync.isHeldExclusively();
    }

    @Override
    public boolean tryLock() {
      return   sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
