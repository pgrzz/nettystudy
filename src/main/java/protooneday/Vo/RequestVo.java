package protooneday.Vo;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lenovo on 2016/6/12.
 */
public class RequestVo implements Serializable{


    private static final long serialVersionUID = 4686274228090335845L;

    private long requestId;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    private String className;
    private String methodName;
    private Class<?>[] parameTypes;
    private Object[] args;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameTypes() {
        return parameTypes;
    }

    public void setParameTypes(Class<?>[] parameTypes) {
        this.parameTypes = parameTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }


}
