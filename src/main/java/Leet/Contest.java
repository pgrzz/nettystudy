package Leet;

import java.util.*;

/**
 * Created by lenovo on 2017/2/5.
 */
public class Contest {



    public String[] findWords(String[] words) {
         int[]one=new int[]{'q','w','e','r','t','y','u','i','o','p'};
        int[]two=new int[]{'a','s','d','f','g','h','j','k','l'};
        int[]three=new int[]{'z','x','c','v','b','n','m'};
        int ss[]=new int[128];
        for(int i=0;i<one.length;i++){
            ss[one[i]]=1;
        }
        for(int i=0;i<two.length;i++){
            ss[two[i]]=2;
        }
        for(int i=0;i<three.length;i++){
            ss[three[i]]=3;
        }
        List<String> array=new ArrayList<>();
        for(int i=0;i<words.length;i++){
            String temp=words[i].toLowerCase();
            int len=temp.length();
            int status=ss[temp.charAt(0)];
            boolean need=true;
            for(int j=1;j<len;j++){
              if(ss[temp.charAt(j)]!=status){
                  need=false;
                  break;
              }
            }
            if(need){
                array.add(words[i]);
            }
        }
        String result[]=new String[array.size()];
        return  array.toArray(result);

    }
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        public     TreeNode(int x) { val = x; }
    }

    public static void main(String[] args){
        TreeNode root=new TreeNode(5);
        TreeNode left=new TreeNode(2);
        TreeNode right=new TreeNode(-3);
        root.left=left;
       root.right=right;
        new Contest().findFrequentTreeSum(root);
    }

    int max=0;
    public int[] findFrequentTreeSum(TreeNode root) {
        if(root==null)return new int[]{};
        Map<Integer,Integer> map=new LinkedHashMap<>();
        afterTraversalHelper(root,map);
       List<Integer> list=new ArrayList<>();
        for(Map.Entry<Integer,Integer> i: map.entrySet()){
              if(i.getValue()==max){
                  list.add(i.getKey());
              }
        }
        int []result=new int[list.size()];
        for(int i=0;i<result.length;i++){
            result[i]=list.get(i);
        }
         return result;
    }

    //定义后序遍历方式

    private int afterTraversalHelper(TreeNode root, Map<Integer,Integer> map){
       int left=root.left==null?0:afterTraversalHelper(root.left,map);
        int right=root.right==null?0:afterTraversalHelper(root.right,map);
        int value=left+right+root.val;
        map.put(value,map.getOrDefault(value,0)+1);
       max= Math.max(max,map.get(value));
        return value;
    }



}
