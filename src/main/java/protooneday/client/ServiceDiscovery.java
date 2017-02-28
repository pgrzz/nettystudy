//package protooneday.client;
//
//import org.I0Itec.zkclient.IZkDataListener;
//import org.I0Itec.zkclient.ZkClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutionException;
//
///**
// * Created by lenovo on 2016/6/21.
// */
//public class ServiceDiscovery {
//
//    private final String CONFIGUER="/configcenter";
//
//   private ZkClient zkClient;
//
//    public ServiceDiscovery(String registryAddress){
//        this.registryAddress=registryAddress;
//        this.zkClient=connectServer();
//    }
//
//
//
//    //传递 的服务名 去zookeeper中 返回一个 String
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);
//
//    private String registryAddress;
//
//    private volatile Map<String,List<String>> dataMap=new ConcurrentHashMap<>();
//
//
//
//    public String getRegistryAddress() {
//        return registryAddress;
//    }
//
//    public void setRegistryAddress(String registryAddress) {
//        this.registryAddress = registryAddress;
//    }
//
//
//    //首先来分析 要些什么 填充 list  1连接
//    private ZkClient connectServer(){
//
//        ZkClient zkClient=new ZkClient(registryAddress,5000);
//
//        return zkClient;
//    }
//
//    //查找节点 负载均衡在这里也可以 并且缓存到list 中去
//
//    public String  discover(String serviceName) throws Exception {
//
//         // 1 应该先去 缓存的节点中拿如果没有再去 zookeeper 拿
//         List<String>  dataList=dataMap.get(serviceName);
//
//          if(dataList!=null && dataList.size()>0){
//               //直接随机算法然后return  先写一个随便的~
//           return OnuploadService(dataList);
//          }
//           List<String> list= zkClient.getChildren(CONFIGUER+"/"+serviceName); //得到 ip 然后对该感兴趣的service进行监听
//         if(list.size()>0) {
//             zkClient.subscribeDataChanges(CONFIGUER + "/" + serviceName, new IZkDataListener() {
//                 @Override
//                 public void handleDataChange(String s, Object o) throws Exception {
//                     // 从新加载 map对应的 protooneday.service
//                     List<String> temp=zkClient.getChildren(s);
//                     dataMap.put(serviceName,temp);
//                 }
//                 @Override
//                 public void handleDataDeleted(String s) throws Exception {
//                     dataMap.remove(s);
//                 }
//             });
//             dataMap.put(serviceName, list);
//             return OnuploadService(list);
//         }else{
//             throw new Exception("no provider the protooneday.service+"+serviceName);
//         }
//
//    }
//
//public String  OnuploadService(List<String> dataList){
//    Random random=new Random();
//    int index=random.nextInt(dataList.size());
//    return dataList.get(index);
//}
//
//}
