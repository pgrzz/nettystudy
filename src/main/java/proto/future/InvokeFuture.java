package proto.future;

/**
 * Created by lenovo on 2017/3/6.
 */
public interface InvokeFuture<V> extends ListenableFuture<V>  {

    Class<V> returnType();

    V getResult()throws Throwable;

}
