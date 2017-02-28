package protooneday.server;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import protooneday.Vo.RequestVo;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by lenovo on 2016/6/12.
 */
public class Invoke {


    private final Map<String,Object> map;

    public  Invoke(Map<String,Object> map){
        this.map=map;
    }


    public Object invoke(RequestVo requestVo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {

        String interfaceName=requestVo.getClassName();
        String methodName=requestVo.getMethodName();
        Class<?>[] parameTypes=requestVo.getParameTypes();
        Object[] arguments=requestVo.getArgs();

        Object obj=map.get(interfaceName);
        FastClass clazz = FastClass.create(obj.getClass());
        FastMethod  serviceMethod=clazz.getMethod(methodName,parameTypes);
        Object resutl=   serviceMethod.invoke(obj,arguments); //方法的执行对象和执行参数


        return  resutl;

    }

}
