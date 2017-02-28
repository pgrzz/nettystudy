package protooneday.server;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.spi.ServiceRegistry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * Created by lenovo on 2016/8/15.
 */
public class CuratorRegistry {


   private String CONFIGUER;
    private String registryAddress;
    private int timeout;
    private int baseSleepTimeMs;
    private int maxRetries;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    public CuratorRegistry(String CONFIGUER,String registryAddress,int timeout,int baseSleepTimeMs,int maxRetries){
        this.CONFIGUER=CONFIGUER;
        this.registryAddress=registryAddress;
        this.timeout=timeout;
        this.baseSleepTimeMs=baseSleepTimeMs;
        this.maxRetries=maxRetries;

    }


    public CuratorFramework connectServer() {


        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries); //重试策略
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(registryAddress)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(timeout)
                .build();

        curatorFramework.start();
        return curatorFramework;
    }

    public void register(String serverAddress, Set<String> serviceNames) {

        CuratorFramework curatorFramework=connectServer();
        for (String service:serviceNames
             ) {
            try {
                createNode(service,curatorFramework,serverAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public  void createNode(String serviceName,CuratorFramework curatorFramework,String serverAddress) throws Exception { //这里要自定义异常不然不知道什么问题

               curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(CONFIGUER+"/"+serviceName+"/"+serverAddress);


    }

}
