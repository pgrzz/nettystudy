package proto.message;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lenovo on 2017/2/15.
 */
public class SailRequestByte extends ByteHolder {

        private static final AtomicLong invokedIdGenerator=new AtomicLong(0);

        private final long invokedId;

        private transient long timestamp;

    public SailRequestByte() {
        this.invokedId = invokedIdGenerator.getAndIncrement();
    }
    public SailRequestByte(long invokedId){this.invokedId=invokedId;}

    public long getInvokedId() {
        return invokedId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
