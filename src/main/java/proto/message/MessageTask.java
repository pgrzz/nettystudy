package proto.message;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.Processor;

/**
 * Created by lenovo on 2017/2/15.
 */
public class MessageTask {

    private static final Logger logger= LoggerFactory.getLogger(MessageTask.class);

    private final Processor processor;
    private final Channel channel;
    private final SailRequest request;

    public MessageTask(Processor processor, Channel channel, SailRequest request) {
        this.processor = processor;
        this.channel = channel;
        this.request = request;
    }


    private void process(ServiceMeTa service){

        final SailRequest _result=this.request;

        MessageWrapper message=_result.getMessageWrapper();
        String methodName=message.getMethodName();


    }


}
