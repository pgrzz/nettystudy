package proto;

import io.netty.channel.Channel;
import proto.message.SailRequestByte;
import proto.message.SailResponseByte;

/**
 *
 * Created by lenovo on 2017/2/8.
 */
public abstract class Processor {

 public   abstract void handlerRequest(SailRequestByte request, Channel channel);
    public abstract void handlerResponse(SailResponseByte response, Channel channel);
    public   abstract void handlerException(Throwable cause,Status status,Channel channel);
}
