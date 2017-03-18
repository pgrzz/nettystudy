package proto.consumer;

import io.netty.channel.Channel;
import proto.Processor;
import proto.Status;
import proto.handler.example.SimpleVo;
import proto.message.SailRequestByte;
import proto.message.SailResponseByte;
import util.SerializationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

/**
 * Created by lenovo on 2017/2/27.
 */
public class ConsumerProcessor extends Processor {


    private  final Executor executor;

    public ConsumerProcessor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void handlerRequest(SailRequestByte request, Channel channel) {

    }

    @Override
    public void handlerResponse(SailResponseByte response, Channel channel) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                SimpleVo vo= SerializationUtils.deserialize(response.getBytes(),SimpleVo.class);
                assert vo.getName().equals( "success");

                // do some business.
                System.out.println(vo.getName());
            }
        });



 }

    @Override
    public void handlerException(Throwable cause, Status status, Channel channel) {

    }
}
