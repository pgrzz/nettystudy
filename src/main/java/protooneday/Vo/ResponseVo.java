package protooneday.Vo;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/6/12.
 */
public class ResponseVo implements Serializable {

    private static final long serialVersionUID = 4686274228090335845L;

    private long requestId;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    private int status;
    private String code;
    private Object result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
