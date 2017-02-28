package proto;

/**
 * Created by lenovo on 2017/2/8.
 */
public class Acknowledge {
    private final long sequece; //序号

    public Acknowledge(long sequece) {
        this.sequece = sequece;
    }

    public long getSequece() {
        return sequece;
    }
}
