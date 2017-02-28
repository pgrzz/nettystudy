package proto.message;



/**
 * Created by lenovo on 2017/2/15.
 */
public class SailRequest {

 private    SailRequestByte requestByte;
 private    MessageWrapper messageWrapper;


    public SailRequest(){
        this(new SailRequestByte());
    }
    private SailRequest(SailRequestByte sailRequestByteWrapper){
        this.requestByte=sailRequestByteWrapper;
    }
    public SailRequestByte getSailRequestByteWrapper() {
        return requestByte;
    }

    public void setSailRequestByteWrapper(SailRequestByte sailRequestByteWrapper) {
        this.requestByte = sailRequestByteWrapper;
    }

    public MessageWrapper getMessageWrapper() {
        return messageWrapper;
    }

    public void setMessageWrapper(MessageWrapper messageWrapper) {
        this.messageWrapper = messageWrapper;
    }
}
