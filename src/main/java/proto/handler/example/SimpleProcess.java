package proto.handler.example;

import io.netty.channel.Channel;
import proto.Processor;
import proto.Status;
import proto.message.SailRequestByte;
import proto.message.SailResponseByte;
import util.SerializationUtils;

/**
 * Created by lenovo on 2017/2/9.
 */
public class SimpleProcess extends Processor {
    @Override
    public void handlerRequest(SailRequestByte request, Channel channel) {
        SimpleVo vo= SerializationUtils.deserialize(request.getBytes(),SimpleVo.class);
        System.out.println(vo.getName());
        SailResponseByte responseWarpper=new SailResponseByte(request.getInvokedId());
        responseWarpper.setStatus(Status.OK.getValue());
        responseWarpper.setSerializer((byte)0x9);
        vo.setName("success");
        vo.setAge(1);
        responseWarpper.setBytes(SerializationUtils.serialize(vo));

        channel.writeAndFlush(responseWarpper);
    }

    @Override
    public void handlerResponse(SailResponseByte response, Channel channel) {
        SimpleVo vo= SerializationUtils.deserialize(response.getBytes(),SimpleVo.class);
        assert vo.getName().equals( "success");

        // do some business.
        System.out.println(vo.getName());
    }

    @Override
    public void handlerException(Throwable cause, Status status, Channel channel) {
            channel.close();
    }
}
