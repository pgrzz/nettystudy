package proto.provider;

import io.netty.channel.Channel;
import proto.Processor;
import proto.Status;
import proto.message.SailRequestByte;
import proto.message.SailResponseByte;

import java.util.concurrent.Executor;

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



}

    @Override
    public void handlerException(Throwable cause, Status status, Channel channel) {

    }
}
