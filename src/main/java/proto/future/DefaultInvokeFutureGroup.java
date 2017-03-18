package proto.future;

import com.google.common.base.Preconditions;

/**
 * Created by lenovo on 2017/3/6.
 */
public class DefaultInvokeFutureGroup<V> implements  InvokeFutureGroup<V> {

    private final InvokeFuture<V>[] futures;

    public static <T> DefaultInvokeFutureGroup<T> with(InvokeFuture<T>[] futures){
        return  new DefaultInvokeFutureGroup<T>(futures);
    }

    private DefaultInvokeFutureGroup(InvokeFuture<V>[]futures){
        Preconditions.checkArgument(futures!=null && futures.length>0,"empty futures");
        this.futures=futures;
    }

    @Override
    public InvokeFuture<V>[] futures() {
        return futures;
    }

    @Override
    public Class<V> returnType() {
        return futures[0].returnType();
    }

    @Override
    public V getResult() throws Throwable {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListenableFuture<V> addListener(SailListener<V> listeners) {
        return null;
    }

    @Override
    public ListenableFuture<V> addListeners(SailListener<V>... listeners) {
        return null;
    }

    @Override
    public ListenableFuture<V> removeListener(SailListener<V> listeners) {
        return null;
    }

    @Override
    public ListenableFuture<V> removeListeners(SailListener<V>... listeners) {
        return null;
    }
}
