package proto.future;

import sun.misc.Unsafe;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 *
 * Created by lenovo on 2017/2/28.
 */
public abstract   class AbstractFuture<V> {

    private static final int NEW=0;
    private static final int COMPLETING=1;
    private static final int NORMAL=2;
    private static final int EXCEPTIONAL=3;

    // volatile 保证可见性
    private Object outCome;



    /**
     *NEW  ---->  COMPLETING --->  NORMAL
     *NEW  ---->  COMPLETING --->  EXCEPTIONAL
     */
    private volatile int state;
    private volatile WaitNode waiters;


    public AbstractFuture(){this.state=NEW;}
    public boolean isDone(){return state!=  NEW;}
    protected  int state(){return state;}

    public V get()throws Throwable{
        int s=state;
        if(s<=COMPLETING){
            awitNode(false,0L); // 直接 park
        }
        return report(s);
    }

    protected V get(long timeout, TimeUnit unit)throws Throwable{
            if(unit==null){
                throw new NullPointerException("unit");
            }
        int s=state;
        if(s<COMPLETING && (s=awitNode(true,unit.toNanos(timeout)))<=COMPLETING){
             throw  new Exception("time_out");
        }
        return report(s);

    }

    protected Object outCome(){
        return outCome;
    }

    protected void set(V v){

        if(UNSAFE.compareAndSwapInt(this,stateOffset,NEW,COMPLETING)){
            outCome=v;
            //putOrderedInt在JIT后会通过intrinsic优化掉StoreLoad屏障，不保证可见性
            UNSAFE.putOrderedInt(this,stateOffset,EXCEPTIONAL);
            completion(v);
        }

    }

    protected void setException(Throwable x){
        if(UNSAFE.compareAndSwapInt(this,state,NEW,COMPLETING)){
            outCome=x;
            //putOrderedInt在JIT后会通过intrinsic优化掉StoreLoad屏障，不保证可见性
            UNSAFE.putOrderedInt(this,stateOffset,EXCEPTIONAL);
            completion(x);
        }
    }

    /**
     * 返回正常执行的结果或者异常
     */
    private V report(int s)throws Throwable{
        Object x=outCome;
        if(s==NORMAL){
            return (V)x;
        }
        throw (Throwable)x;
    }



    /**
     *  1 唤醒并移除 Treiber Stack 中所有等待的线程
     *  2 调用钩子函数done()
     */
    private void completion(Object x){
        // assert state>COMPLETING;
        for(WaitNode q;(q=waiters)!=null;){
            if(UNSAFE.compareAndSwapObject(this,waitersOffset,q,null)){
                for(;;){
                    Thread t=q.thread;
                    if(t!=null){
                        q.thread=null;
                        LockSupport.unpark(t);
                    }
                    WaitNode next=q.next;
                    if(next==null){
                        break;
                    }
                    q.next=null; // unlink to help gc
                    q=next;
                }
                break;
            }
        }

        done(state,x);
    }

     abstract void done(int state,Object x);

    private int awitNode(boolean timed, long nanos){

        final long deadline=timed?System.nanoTime()+nanos:0L;
        WaitNode q=null;
        boolean queued=false;
        for(;;){
            int s=state;
            if(s>COMPLETING){       //任务执行完成
                if(q!=null) {
                    q.thread = null;
                }
                return  s; //返回任务状态
            }else if(s==COMPLETING){
                Thread.yield(); //正在执行 让出cpu
            }else if(q ==null){     //创建一个等待节点
                q=new WaitNode();
            }else if(!queued){ //将当前等待的节点入队
                queued=UNSAFE.compareAndSwapObject(this,waitersOffset,q.next=waiters,q);
            }else if(timed){
                nanos=deadline-System.nanoTime();
                if(nanos<=0L){
                    removeWaiter(q);
                    return state;
                }
                LockSupport.parkNanos(this,nanos);
            }else {
                LockSupport.park(this);
            }


        }

    }

    private void removeWaiter(WaitNode node){
        if(node !=null){
            node.thread=null;
            // 将node 从等待队列移除，以node.thread==null 为依据 发生竞争时重试

            retry:
            for(;;){

                for(WaitNode pred=null,q=waiters ,s;q!=null;q=s){
                    s=q.next;
                    if(q.thread!=null){
                        pred=q;
                    }else if(pred!=null){
                        pred.next=s;
                        if(pred.thread==null){
                            continue retry;
                        }
                    }else if(!UNSAFE.compareAndSwapObject(this,waitersOffset,q,s)){
                        continue retry;
                    }
                    break ;
                }

            }
        }

    }


    static final class WaitNode {

        volatile Thread thread;
        volatile WaitNode next;

        WaitNode() {
            thread = Thread.currentThread();
        }
    }

    // unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE = Unsafe.getUnsafe();
    private static final long stateOffset;
    private static final long waitersOffset;

    static {
        try {
            Class<?> k = AbstractFuture.class;
            stateOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("state"));
            waitersOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("waiters"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}