package util.redis;

import redis.clients.jedis.ShardedJedis;
import util.SerializationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by lenovo on 2016/11/28.
 */
public class DefaultRedisConnection implements  RedisConnection{


    ShardedJedis jedis;
    Class<?>  target;

    public DefaultRedisConnection(ShardedJedis jedis,Class clazz) {
        this.jedis = jedis;
        target=clazz;
    }

    @Override
    public void close() {
        this.jedis.close();
    }

    @Override
    public Boolean isConnected() {
      return false;
    }

    @Override
    public Long hset(String key, String field, Serializable object) {
        return jedis.hset(key.getBytes(),field.getBytes(),SerializationUtils.serialize(object));
    }

    @Override
    public Object hget(String key, String field) {

        return SerializationUtils.deserialize( jedis.hget(key.getBytes(),field.getBytes()),target);
    }

    @Override
    public boolean del(String key) {
            jedis.del(key.getBytes());

        return true;
    }

    @Override
    public Long hdel(String key, String  fields) {
      return  jedis.hdel(key.getBytes() ,fields.getBytes());
    }

    @Override
    public Long expire(String key, int seconds) {
        return jedis.expire(key,seconds);
    }

    @Override
    public Set<String> hkeys(String key) {
        return jedis.hkeys(key);
    }

    @Override
    public Boolean exists(String key) {
        return jedis.exists(key);
    }



}
