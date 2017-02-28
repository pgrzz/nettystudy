package util.redis;

import redis.clients.jedis.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2016/11/28.
 */
public class redisPool {

     private static  ShardedJedisPool shardedJedisPool;
     static {
        JedisPoolConfig config=new JedisPoolConfig();
            config.setMaxTotal(2);
            config.setMaxIdle(1);
            config.setMaxWaitMillis(2000);
            config.setTestOnBorrow(false);
            config.setTestOnReturn(false);

        String host="127.0.0.1";
        JedisShardInfo shardInfo1=new JedisShardInfo(host,6379,500);
        List<JedisShardInfo> shardInfos= Arrays.asList(shardInfo1);
         shardedJedisPool=new ShardedJedisPool(config,shardInfos);
    }

    public static ShardedJedis getconn(){
        return shardedJedisPool.getResource();
    }




}
