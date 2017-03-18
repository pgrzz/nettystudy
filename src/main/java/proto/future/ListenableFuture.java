package proto.future;

/**
 * Created by lenovo on 2017/3/6.
 * accepts completion listeners.
 */
@SuppressWarnings("unchecked")
public interface ListenableFuture<V> {



    ListenableFuture<V> addListener(SailListener<V>  listeners);

    ListenableFuture<V> addListeners(SailListener<V>  ... listeners);

    ListenableFuture<V> removeListener(SailListener<V>  listeners);

    ListenableFuture<V> removeListeners(SailListener<V> ... listeners );

}
