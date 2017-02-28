package proto.message;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by lenovo on 2017/2/15.
 */
public class MessageWrapper implements Serializable{

    private static final long serialVersionUID = 1009813828866652852L;

    private String name;                        //应用名
    private final   ServiceMeTa serviceMeTa;    //目标服务元数据
    private String methodName;                  //目标方法
    private Object[]args;                       //参数

    public MessageWrapper(ServiceMeTa serviceMeTa) {
        this.serviceMeTa = serviceMeTa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceMeTa getServiceMeTa() {
        return serviceMeTa;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "MessageWrapper{" +
                "name='" + name + '\'' +
                ", serviceMeTa=" + serviceMeTa +
                ", methodName='" + methodName + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
