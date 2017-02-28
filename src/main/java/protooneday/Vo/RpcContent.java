package protooneday.Vo;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/6/13.
 */
public class RpcContent implements Serializable {


    public volatile boolean RESULT_OK=false;

    private RequestVo requestVo;
    private ResponseVo responseVo;
    private Head header;

    public Head getHeader() {
        return header;
    }

    public void setHeader(Head header) {
        this.header = header;
    }

    public ResponseVo getResponseVo() {
        return responseVo;
    }

    public void setResponseVo(ResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public RequestVo getRequestVo() {
        return requestVo;
    }

    public void setRequestVo(RequestVo requestVo) {
        this.requestVo = requestVo;
    }


    public Head  HeadFactory(){

        return new Head();  // 这个 外部类是不能够直接访问内部类对象的 必须通过  对象去构造 还有在 内部类中传递参数的时候是要final
    }


    public class Head{

        private byte type;
        private long sessionID;
        private byte priority;

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public long getSessionID() {
            return sessionID;
        }

        public void setSessionID(long sessionID) {
            this.sessionID = sessionID;
        }

        public byte getPriority() {
            return priority;
        }

        public void setPriority(byte priority) {
            this.priority = priority;
        }
    }
    public class MessageType{
        public static  final int LOGIN_REQ=1;
         public static final int LOGIN_RESP=2;
        public static final byte  HEARTBEAT_REQ=3;
         public static final byte HEARTBEAT_RESP=4;

    }
}
