package proto.provider;

import io.netty.channel.Channel;
import proto.Processor;
import proto.Status;
import proto.handler.example.SimpleVo;
import proto.message.SailRequestByte;
import proto.message.SailResponseByte;
import util.SerializationUtils;

import java.util.concurrent.Executor;

/**
 * Created by lenovo on 2017/2/15.
 */
public class ProviderProcessor extends Processor {

    private final Executor executor;

    public ProviderProcessor(Executor executor) {
        this.executor = executor;
    }


    @Override
    public void handlerRequest(SailRequestByte request, Channel channel) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                SimpleVo vo= SerializationUtils.deserialize(request.getBytes(),SimpleVo.class);
                System.out.println(vo.getName());
                SailResponseByte responseWarpper=new SailResponseByte(request.getInvokedId());
                responseWarpper.setStatus(Status.OK.getValue());
                responseWarpper.setSerializer((byte)0x9);
                vo.setName("success");
                vo.setAge(2);
                responseWarpper.setBytes(SerializationUtils.serialize(vo));

                channel.writeAndFlush(responseWarpper);
            }
        });

    }

    @Override
    public void handlerResponse(SailResponseByte response, Channel channel) {

    }

    @Override
    public void handlerException(Throwable cause, Status status, Channel channel) {

    }
}
