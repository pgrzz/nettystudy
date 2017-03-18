package proto.future;

import com.google.common.base.Preconditions;

import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2017/3/6.
 */
public abstract class AbstractInvokeFuture<V> extends AbstractFuture<V> implements InvokeFuture<V> {

    private Object listeners;


    public AbstractInvokeFuture() {
        super();
    }



    @Override
    void done(int state, Object x) {
            notifyListeners(state,x);
    }


    @Override
    public ListenableFuture<V> addListener(SailListener<V> listeners) {
        Preconditions.checkNotNull(listeners);

        synchronized (this){
            addListener0(listeners);
        }
        if(isDone()){
            notifyListeners(state(),outCome());
        }
        return this;
    }

    @Override
    public ListenableFuture<V> addListeners(SailListener<V>... listeners) {
            Preconditions.checkNotNull(listeners);

            synchronized (this){
                for(SailListener<V> listener:listeners){
                    if(listener==null){
                        continue;
                    }
                    addListener0(listener);
                }
            }
        if(isDone()){
            notifyListeners(state(),outCome());
        }
        return this;
    }

    @Override
    public ListenableFuture<V> removeListener(SailListener<V> listeners) {
            Preconditions.checkNotNull(listeners);
        synchronized (this){
            removeListener0(listeners);
        }
        return this;
    }

    @Override
    public ListenableFuture<V> removeListeners(SailListener<V>... listeners) {
        Preconditions.checkNotNull(listeners);
        synchronized (this){
            for(SailListener<V> listener:listeners){
                if(listener==null){
                    continue;
                }
                removeListener0(listener);
            }
        }
        return this;
    }

    protected void notifyListeners(int state, Object x){
        Object listener;
        synchronized (this){
            if(this.listeners==null){
                return;
            }
            listener=this.listeners;
            this.listeners=null;
        }

        if(listener instanceof DefaultListeners){
            SailListener<V>[]array=((DefaultListeners) listener).listeners();
            int size=((DefaultListeners) listener).size();
            for(int i=0;i<size;i++){
                notifyListen0(array[i],state,x);
            }
        }else{
            notifyListen0((SailListener<V>) listener,state,x);
        }

    }


    abstract void notifyListen0(SailListener<V> listener,int state,Object x);


    private void addListener0(SailListener<V> listener){
        if(listeners==null){
            listeners=listener;
        }else     if(listener instanceof  DefaultListeners){
            ((DefaultListeners) listener).add(listener);
        }else {
            listeners=DefaultListeners.with((SailListener<V>) listeners,listener);
        }


    }

    private void removeListener0(SailListener<V> listener){
        if(listener instanceof  DefaultListeners){
            ((DefaultListeners) listener).remove(listener);
        }else if(listeners==listener){
            listeners=null;
        }


    }


}
