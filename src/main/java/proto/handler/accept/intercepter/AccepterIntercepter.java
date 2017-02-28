package proto.handler.accept.intercepter;

/**
 * Created by lenovo on 2017/2/15.
 */
public interface AccepterIntercepter {

    void beforeInvoke(Object provider,String methodName,Object[] args);
    void afterInover(Object privider,String methodName,Object[]args,Object result);

}
