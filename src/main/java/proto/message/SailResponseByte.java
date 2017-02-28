package proto.message;

/**
 * Created by lenovo on 2017/2/15.
 */
public class SailResponseByte extends  ByteHolder {

        private final long id;
        private byte status;

    public SailResponseByte(long id) {
        this.id = id;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public byte getStatus() {
        return status;
    }
}
