package proto.future;

import java.util.EventListener;

/**
 * Created by lenovo on 2017/3/6.
 */

public interface SailListener<V> extends EventListener {

    void complete(V result);
    void failure(Throwable cause);
}
