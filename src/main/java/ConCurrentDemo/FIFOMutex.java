package ConCurrentDemo;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by lenovo on 2017/1/28.
 */
public class FIFOMutex {

    private final AtomicBoolean locked= new AtomicBoolean(false);
    private final Queue<Thread> waiters=new ConcurrentLinkedQueue<>();

    public void lock(){
        boolean wasInterrupted=false;
        Thread thread=Thread.currentThread();
        waiters.add(thread);

        while(waiters.peek()!=thread || ! locked.compareAndSet(false,true)){
            LockSupport.park(this);
            if(Thread.interrupted()){
                wasInterrupted=true;
            }
        }

        waiters.remove(); // only differ form poll in that if empty throw exception
        if(wasInterrupted){
            thread.interrupt();
        }
    }

    public void unlock(){
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }


}
