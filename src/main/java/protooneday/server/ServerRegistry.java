//package protooneday.server;
//
//
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.zookeeper.ZooKeeper;
//import org.apache.zookeeper.data.Stat;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.imageio.spi.ServiceRegistry;
//import java.net.Socket;
//import java.util.Set;
//import java.util.concurrent.CountDownLatch;
//
///**
// * Created by lenovo on 2016/6/19.
// * 注册中心应该不依赖于具体的客服端 于是乎抽象出来哈哈哈哈
// *
// */
//public class ServerRegistry {
//
//
//
//
//    private final String CONFIGUER="/configcenter";
//
//
//    public ServerRegistry(String registryAddress) {
//        this.registryAddress = registryAddress;
//    }
//
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
//
//    private CountDownLatch latch = new CountDownLatch(1);
//
//    private String registryAddress;
//
//
//
//    private ZkClient connectServer(){
//
//        ZkClient zkClient=new ZkClient(registryAddress,5000);
//        return zkClient;
//    }
//
//
//
//
//
//
//    public void register(String serverAddress,Set<String> serviceNames){
//
//        try{
//            ZkClient zkClient=connectServer();
//
//            for(String seName:serviceNames) {
//
//                createNode(zkClient,serverAddress,seName);
//            }
//
//
//        }catch (Exception e){
//            LOGGER.error("zkClient has fall  to create Node");
//        }
//
//    }
//
//
//   private void createNode(ZkClient zkClient,String address,String serviceName){  //这里应该还要有服务名  节点为  configuer/serviceName/ip:port
//
//       //创建根节点 上层 服务为 null
//       boolean exists=zkClient.exists(CONFIGUER);
//       if(!exists) {
//           zkClient.createPersistent(CONFIGUER);
//       }
//
//       // 注册服务
//
//         boolean serviceexits=zkClient.exists(CONFIGUER+"/"+serviceName);
//            if(!serviceexits){
//                zkClient.createPersistent(CONFIGUER+"/"+serviceName);
//            }
//           zkClient.createEphemeral(CONFIGUER+"/"+serviceName+"/"+address);
//   }
//
//
//}
