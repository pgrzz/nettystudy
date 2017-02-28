package velocity;

import com.sun.org.apache.xpath.internal.operations.Mod;
import redis.clients.jedis.ShardedJedis;
import util.redis.DefaultRedisConnection;
import util.redis.redisPool;
import velocity.model.Model;

/**
 * Created by lenovo on 2016/11/28.
 */
public class ModelDo {



    public void CreateNewModels(Model model){
        //创建模板并且保存
        ShardedJedis shardedJedis= redisPool.getconn();
        DefaultRedisConnection defaultRedisConnection=new DefaultRedisConnection(shardedJedis,Model.class);
            //1创建模板
        VelocityUtil.CreateTemplate(model);
        //存储状态
            defaultRedisConnection.hset(model.getId(),model.getVersion(),model);
         defaultRedisConnection.close();

    }
    //获得内容
    public Object getModel(Class<?> clazz,String id,String version){
        ShardedJedis shardedJedis= redisPool.getconn();
        DefaultRedisConnection defaultRedisConnection=new DefaultRedisConnection(shardedJedis,clazz);
        return  defaultRedisConnection.hget(id,version);
    }

}
