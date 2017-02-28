package proto.message;

import proto.handler.accept.intercepter.AccepterIntercepter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Created by lenovo on 2017/2/15.
 */
public class ServiceWrapper implements Serializable {

    /**
     * 服务元组
     */
    private final ServiceMeTa metaData;

    /**
     * 服务对象
     */
    private final Object serviceProvider;

    /**
     * 服务拦截器
     */
    private final AccepterIntercepter[] intercepters;

    /**
     * provider 中所有接口的参数类型（用于JLS规则实现方法调用静态分配）
     */
    private final Map<String,List<Class<?>[]>> methodsParamterTypes;

    /**
     * 权重 hashcode(),与equals() 不把weight计算在内。
     */
    private int weight=50;

    private Executor executor;

    public ServiceWrapper(ServiceMeTa metaData, Object serviceProvider, AccepterIntercepter[] intercepters, Map<String, List<Class<?>[]>> methodsParamterTypes) {
        this.metaData = metaData;
        this.serviceProvider = serviceProvider;
        this.intercepters = intercepters;
        this.methodsParamterTypes = methodsParamterTypes;
    }



}
