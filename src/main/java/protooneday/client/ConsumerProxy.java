package protooneday.client;


import protooneday.Vo.RequestVo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lenovo on 2016/5/31.
 * 同步调用，Future,异步callback 都由这个来做
 */
public class ConsumerProxy implements InvocationHandler {

    //同步调用 默认
    public static final int GENERAL=1;
    //future调用;
    public static final int FUTURE=2;
    //异步
    public static final int ASYNC=3;

    //
    /**
     * ThreadLocal 只是为了保护当前的现场
     * 这里的ThreadLocal 的ConsumerProxy 会被Spring Ioc 持有
     * 所以申不声明 为static都不存在 ThreadLocal的弱引用链被断了存在 Null的key 导致访问不到
     * 不过 按照规范我们还是应该把它申明为 static
     */
    private static     ThreadLocal<Callback>  callbacks=new ThreadLocal<>();
    //ThreadLocal 保护当前状态
    private static      ThreadLocal<Integer> status=new ThreadLocal<>();

    public ConsumerProxy setCallBack(Callback callBack){
         callbacks.set(callBack);
        return this;
    }
    public ConsumerProxy setStatus(int Status){
        this.status.set(Status);
        return this;
    }



    ExecutorService executors=Executors.newFixedThreadPool(2);  // 其实这里也可以交给spring 管理的


    private CuratorDiscovery curatorDiscovery;


    public ConsumerProxy(CuratorDiscovery curatorDiscovery){
        this.curatorDiscovery=curatorDiscovery;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //情况判断

        final int sta=status.get();
        final Callback callback=callbacks.get();

        RequestVo requestVo=new RequestVo();
        String interfaceName=method.getDeclaringClass().getName();
        requestVo.setClassName(interfaceName);
        requestVo.setMethodName(method.getName());
        requestVo.setArgs(args);
        requestVo.setParameTypes(method.getParameterTypes());
        if(sta==GENERAL){
        //当前线程直接调用;
            String  ip="";
            int port=0;
            //默认采用zookeeper去获取服务
            if(curatorDiscovery!=null){
                String address=curatorDiscovery.discover(interfaceName);
                String temp[]=address.split(":");
                ip=temp[0];
                port=Integer.parseInt(temp[1]);
            }
            return new RpcClient().connect(port,ip,requestVo);

        }else if(sta==FUTURE){
            Future  future=  executors.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    String  ip="";
                    int port=0;
                    //默认采用zookeeper去获取服务
                    if(curatorDiscovery!=null){
                        String address=curatorDiscovery.discover(interfaceName);
                        String temp[]=address.split(":");
                        ip=temp[0];
                        port=Integer.parseInt(temp[1]);
                    }
                    return new RpcClient().connect(port,ip,requestVo); // 这里开始搞服务发现
                }
            });
            return future;
        }else if(sta== ASYNC){
            executors.submit(new Runnable() {
                @Override
                public void run()  {
                    String  ip="";
                    int port=0;
                    //默认采用zookeeper去获取服务
                    if(curatorDiscovery!=null){
                        String address= null;
                        try {
                            address = curatorDiscovery.discover(interfaceName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String temp[]=address.split(":");
                        ip=temp[0];
                        port=Integer.parseInt(temp[1]);
                    }
                    Object result= null;
                    try {
                        result = new RpcClient().connect(port,ip,requestVo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    callback.ResultListener(result);
                }
            });
        }
        return null;
    }



    @SuppressWarnings("unchecked")
    public <T> T proxy(Class<T> interfaceClass) throws Throwable {
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException(interfaceClass.getName()
                    + " is not an interface");
        }
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[] { interfaceClass }, this);
    }


}
