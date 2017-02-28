package proto.message;

/**
 * Created by lenovo on 2017/2/7.
 */
public abstract class ByteHolder {
    private byte serializer;
    private byte[]bytes;

    public byte getSerializer() {
        return serializer;
    }

    public void setSerializer(byte serializer) {
        this.serializer = serializer;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public void nullBytes(){
        bytes=null;//help gc
    }
    public int size(){
        return bytes==null?0:bytes.length;
    }
}
