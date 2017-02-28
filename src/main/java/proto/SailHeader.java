package proto;

/**
 * Created by lenovo on 2017/2/7.
 */
public class SailHeader {

    public static final short MAGIC=(short)0xbabe;
    public static final int    HEAD_LENGTH=16;

    private  short  magic;
    private  byte messageCode;
    private byte  serializer;
    private byte  responseStatus;
    private long id; // <ID,Request,Response>
    private int bodyLength;

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte getMessageCode() {
        return messageCode;
    }
    public Status getMessageCodeEnmu(){
        return Status.parse(messageCode);
    }

    public void setMessageCode(byte messageCode) {
        this.messageCode = messageCode;
    }

    public void sign(byte sign) {
        // sign 低地址4位
        this.messageCode = (byte) (sign & 0x0f);
        // sign 高地址4位, 先转成无符号int再右移4位
        this.serializer = (byte) ((((int) sign) & 0xff) >> 4);
    }

    public byte getSerializer() {
        return serializer;
    }

    public byte getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(byte responseStatus) {
        this.responseStatus = responseStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }
}
