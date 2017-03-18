package protooneday.client;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.spi.ServiceRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lenovo on 2016/8/15.
 */
public class CuratorDiscovery {
    private String CONFIGUER;
    private String registryAddress;
    private int timeout;
    private int baseSleepTimeMs;
    private int maxRetries;

    private CuratorFramework curatorFramework;
    private volatile Map<String,List<String>> dataMap=new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    public CuratorDiscovery(String CONFIGUER, String registryAddress, int timeout, int baseSleepTimeMs, int maxRetries) {
        this.CONFIGUER = CONFIGUER;
        this.registryAddress = registryAddress;
        this.timeout = timeout;
        this.baseSleepTimeMs = baseSleepTimeMs;
        this.maxRetries = maxRetries;
        curatorFramework=connectServer();
    }
    public CuratorFramework connectServer() {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries); //重试策略
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(registryAddress)
                .sessionTimeoutMs(timeout)
                .retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();
        return curatorFramework;
    }

    public String  discover(String serviceName) throws Exception {

        List<String>  dataList=dataMap.get(serviceName);

        if(dataList!=null && dataList.size()>0){
            //直接随机算法然后return  先写一个随便的~
            return OnuploadService(dataList);
        }
        dataList=curatorFramework.getChildren().forPath(CONFIGUER+"/"+serviceName);
         dataMap.put(serviceName,dataList);

        PathChildrenCache cache=new PathChildrenCache(curatorFramework,CONFIGUER+"/"+serviceName,true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                List<String> temp=dataMap.get(serviceName);
                switch (event.getType()){
                    case CHILD_ADDED:
                        temp.add(event.getData().getPath().substring(event.getData().getPath().lastIndexOf("/")+1));

                        break;
                    case CHILD_REMOVED:
                        temp.remove(event.getData().getPath().substring(event.getData().getPath().lastIndexOf("/")+1));
                        break;

                }
            }
        });
        return OnuploadService(dataMap.get(serviceName));
    }


    private String  OnuploadService(List<String> dataList){
        Random random=new Random();
        int index=random.nextInt(dataList.size());
        System.out.println("均值"+index);
        return dataList.get(index);
    }


}
