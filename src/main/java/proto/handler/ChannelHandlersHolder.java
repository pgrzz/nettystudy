package proto.handler;

import io.netty.channel.ChannelHandler;

/**
 * Created by lenovo on 2017/2/8.
 */
public interface ChannelHandlersHolder {



    ChannelHandler[] handlers();

}
