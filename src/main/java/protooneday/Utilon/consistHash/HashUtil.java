package protooneday.Utilon.consistHash;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by lenovo on 2016/8/16.
 */
public class HashUtil {
    public HashUtil(List<String> serverList){
        this.serverList=serverList;
    }

    private List<String> serverList;

    private List<String> realNodes=new LinkedList<>();

    private SortedMap<Integer,String> virtualNodes=new TreeMap<Integer, String>();

    private int  numOfVir;

    public void init(){

        for(String service:serverList){
            realNodes.add(service);
        }

        for(String str:realNodes){
            for(int i=0;i<realNodes.size();i++){

                for(int j=0;j<numOfVir;j++){
                    String virtualNodeName=str+"&&VN"+String.valueOf(j);
                    int hash=getHash(virtualNodeName);

                            virtualNodes.put(hash,virtualNodeName);

                }
            }
        }

    }
    public void addServer(String str){

        //对节点先虚拟化
        for(int i=0;i<numOfVir;i++){
            String virtualNodeName=str+"&&VN"+String.valueOf(i);
            int hash=getHash(virtualNodeName);

            virtualNodes.put(hash,virtualNodeName);

        }

    }

    public void removeServer(String str){
        //把对应的节点从 真实节点删除再把虚拟节点删除
        realNodes.remove(str);
        for(int i=0;i<numOfVir;i++){
            String virtualNodeName=str+"&&VN"+String.valueOf(i);
            int hash=getHash(virtualNodeName);
            virtualNodes.remove(hash);
        }

    }


    public String getServer(String node){
     int hash=getHash(node);
        SortedMap<Integer,String> submap=virtualNodes.tailMap(hash);
        Integer i=submap.firstKey();
        String virtualNode=submap.get(i);
        return  virtualNode.substring(0,virtualNode.indexOf("&&"));

    }

    public int getHash(String str){
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

}
