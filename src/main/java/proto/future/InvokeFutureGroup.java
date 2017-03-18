package proto.future;

/**
 * Created by lenovo on 2017/3/6.
 */
public interface InvokeFutureGroup<V> extends  InvokeFuture<V>{

    InvokeFuture<V>[] futures();
}
