package proto.channel;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * Created by lenovo on 2017/2/11.
 */
public class SailChannel {

    private static final AttributeKey<SailChannel> NETTY_CHANNEL_KEY=AttributeKey.valueOf("netty.channel");



   private Channel channel;

}
