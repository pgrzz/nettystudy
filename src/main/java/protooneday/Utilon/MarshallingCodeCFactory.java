package protooneday.Utilon;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * Created by lenovo on 2016/6/12.
 */
public class MarshallingCodeCFactory {

    public static MarshallingDecoder createMarshallingDecoder(){
        //实例化java序列化MarshallingFactory工厂，其中参数serial表示的就是java版的
        final MarshallerFactory mf = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration mc = new MarshallingConfiguration();
        mc.setVersion(5);
        UnmarshallerProvider up = new DefaultUnmarshallerProvider(mf, mc);
        //这里面的1024表示的是单个消息序列化后的最大长度
        MarshallingDecoder decoder =  new MarshallingDecoder(up,1024);
        return decoder;
    }
    /**
     * @Title: createMarshallingEncoder
     * @Description:创建Marshalling编码器
     * @return
     * @author Administroter
     * @date 2016年3月7日
     */
    public static MarshallingEncoder createMarshallingEncoder(){
        final MarshallerFactory mf = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration mc = new MarshallingConfiguration();
        mc.setVersion(5);
        MarshallerProvider up = new DefaultMarshallerProvider(mf, mc);
        //对象序列化成二进制数组
        MarshallingEncoder me = new MarshallingEncoder(up);
        return me;
    }



}
