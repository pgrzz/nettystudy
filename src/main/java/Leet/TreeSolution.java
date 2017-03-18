package Leet;

import scala.Int;

import java.util.*;

/**
 * Created by lenovo on 2016/12/26.
 */
public class TreeSolution {


     static class TreeNode {
             int val;
             TreeNode left;
             TreeNode right;
         public     TreeNode(int x) { val = x; }
         }


    public TreeNode invertTree(TreeNode root) {
        if(root==null)
            return  null;


        invertTree(root.left);
        invertTree(root.right);
            TreeNode temp=root.left;
            root.left=root.right;
            root.right=temp;


        return root;
    }

    public static void main(String[]args){
        new TreeSolution().isSameTree(new TreeNode(0),new TreeNode(0));
    }


    public boolean isSameTree(TreeNode p, TreeNode q) {
        boolean result=false;
       if(p!=null && q!=null && p.val==q.val){
            result=isSameTree(p.left,q.left);
           if(!result)
               return false;
       }
        if(p ==null && q==null){
            result= true;
        }
        return result;

    }

    private void exchangeAndEqules(TreeNode root){
        if(root==null)
        return;



    }


    public boolean isSymmetric(TreeNode root) {
        if(root==null)
            return true;
        return mirror(root.left,root.right);

    }
    private boolean mirror(TreeNode p,TreeNode q){
        if(p==null && q==null) return true;
        if(p==null || q==null) return false;
            return p.val==q.val && mirror(p.left,q.right) && mirror(p.right,q.left);
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> list=new ArrayList<>();
        Queue<List<TreeNode>> queue=new LinkedList<>();
       if(root==null)
           return list;

        List first=new ArrayList();
        first.add(root);
        queue.offer(first);
        while(queue.size()>0){
                List<TreeNode> temp=queue.poll(); //当前层
            List<Integer> results=new ArrayList<>();   //结果层
            List<TreeNode> tt=new ArrayList<>();       //下一层
            for(TreeNode node:temp){
                   results.add(node.val);

                if(node.left!=null){
                    tt.add(node.left);
                }
                if(node.right!=null){
                    tt.add(node.right);
                }
            }
            if(tt.size()>0){
                queue.offer(tt);
            }
            list.add(results);
        }
        return list;
    }
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> list=new ArrayList<>();
        Queue<List<TreeNode>> queue=new LinkedList<>();
        if(root==null)
            return list;

        List first=new ArrayList();
        first.add(root);

        queue.offer(first);

        while(queue.size()>0){
            List<TreeNode> temp=queue.poll(); //当前层
            List<Integer> results=new ArrayList<>();   //结果层
            List<TreeNode> tt=new ArrayList<>();       //下一层
            for(TreeNode node:temp){
                results.add(node.val);

                if(node.left!=null){
                    tt.add(node.left);
                }
                if(node.right!=null){
                    tt.add(node.right);
                }
            }
            if(tt.size()>0){
                queue.offer(tt);
            }
            list.add(0,results);
        }
        return list;
    }



    public int maxDepth(TreeNode root) {
        if(root==null)return 0;
        Stack<TreeNode> key=new Stack<>();
        Stack<Integer> value=new Stack<>();
        key.push(root);
        value.push(0);
        int max=0;
        while(!key.isEmpty()){
            TreeNode temp=key.pop();
            int  tempvalue=value.pop();
            if(tempvalue>max)
                max=tempvalue;

            if(temp.left!=null){
                key.push(temp.left);
                value.push(tempvalue+1);
            }
            if(temp.right!=null){
                key.push(temp.right);
                value.push(tempvalue+1);
            }
        }
    return max+1;
    }
    public int maxDepth2(TreeNode root){
        if (root==null) return 0;
        int left=maxDepth2(root.left)+1;
        int right=maxDepth2(root.right)+1;
        return left>right?left:right;
    }

    public int maxDepthBfs(TreeNode root){
        if(root==null)return 0;
        Queue<TreeNode> queue=new LinkedList<>();
        queue.offer(root);
        int max=0;
        while(!queue.isEmpty()){
            ++max;
            int nowlen=queue.size();
            for(int i=0;i<nowlen;i++){
                TreeNode temp=queue.poll();
                if(temp.left!=null)queue.offer(temp.left);
                if(temp.right!=null)queue.offer(temp.right);
            }
        }
        return max;
    }


    public int minDepth(TreeNode root) {
        if(root==null)return 0;
        if(root.left==null||root.right==null)
       return 1+Math.max(minDepth(root.left),minDepth(root.right));
       return 1+Math.min(minDepth(root.left),minDepth(root.right));

    }

    public boolean hasPathSum(TreeNode root, int sum) {
            if(root==null)
                return false;
            if(root.left==null && root.right==null && sum-root.val==0)return true;

            return hasPathSum(root.left,sum-root.val)||hasPathSum(root.right,sum-root.val);

    }

    private int Dfs(TreeNode treeNode){
        if (treeNode==null)return 0;
        return Math.max(Dfs(treeNode.left),Dfs(treeNode.right))+1;

    }

    public boolean isBalanced(TreeNode root) {
        if(root==null)return true;
        int left=Dfs(root.left);
        int right=Dfs(root.right);

       return  Math.abs(left-right)<=1 && isBalanced(root.left) && isBalanced(root.right);

    }

    public int sumOfLeftLeaves(TreeNode root) {
       if (root==null)return 0;
        return leftDfs(root.left,true)+leftDfs(root.right,false);
    }
    private int leftDfs(TreeNode root,boolean isleft){
        if(root==null)return 0;
            if(root.left==null && root.right==null &&isleft )
            return root.val;

        return leftDfs(root.left,true)+leftDfs(root.right,false);
    }



    public List<String> binaryTreePaths(TreeNode root) {

            List<String> list=new ArrayList<>();
            if(root!=null){
                dfsLeftPath(root,"",list);
            }

        return list;
    }

    public void dfsLeftPath(TreeNode root,String path,List<String> list){
         if(root.left==null && root.right==null){
             list.add(path+root.val);
         }
        if(root.left!=null){
            dfsLeftPath(root.left,path+root.val+"->",list);
        }
        if(root.right!=null){
            dfsLeftPath(root.right,path+root.val+"->",list);
        }
    }


    public TreeNode deleteNode(TreeNode root, int key) {
            TreeNode p=null;
        TreeNode delete=root;
        while(delete!=null && delete.val!=key){
            p=delete;
          if(key>delete.val){
              delete=delete.right;
          }else{
              delete=delete.left;
          }
        }
        if(p==null){
            return deleteRootNode(delete);
        }
        if(p.left==delete){
            p.left=deleteRootNode(delete);
        }
        if(p.right==delete){
            p.right=deleteRootNode(delete);
        }
        return root;
    }


    TreeNode deleteRootNode(TreeNode root){

        if(root==null)
            return null;
        if(root.left==null)
           return root.right;
        if(root.right==null)
            return root.left;
        //双孩子删除情况
        TreeNode next=root.right;
        TreeNode pre=null;
        while(next.left!=null){     //找后继节点
            pre=next;
            next=next.left;
        }
        next.left=root.left;
        if(pre!=null){              //如果后继节点就是 删除节点的右孩子则 只用后继节点充当删除节点。且后继节点左孩子就是删除节点左孩子.
            pre.left=next.right;
            next.right=root.right;
        }
        return next;
    }


    public int pathSum(TreeNode root, int sum) {
        HashMap<Integer, Integer> pre=new HashMap<>();
        pre.put(0,1);
        return helper(root,0,sum,pre);

    }

    public int helper(TreeNode root,int sum,int target,HashMap<Integer,Integer> preSum){
        if(root==null)
            return 0;
        sum+=root.val;
        int res=preSum.getOrDefault(sum-target,0);
        preSum.put(sum,preSum.getOrDefault(sum,0)+1);
        res+=helper(root.left,sum,target,preSum)+helper(root.right,sum,target,preSum);
        preSum.put(sum,preSum.get(sum)-1);
        return res;
    }


    int start=0;
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if(root==null)
            return "#";
        return String.valueOf(root.val)+"|"+serialize(root.left)+"|"+serialize(root.right);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if(data.charAt(start)=='#'){
            start++;
            return null;
        }
        int end=start;
        while(end==data.length() || data.charAt(end)!='|'){
            end++;
        }
        String num=data.substring(start,end);
        TreeNode node=new TreeNode(Integer.valueOf(num));
        start=end;
        start++; node.left=deserialize(data);
        start++;node.right=deserialize(data);
            return node;
    }


    public int rob(TreeNode root){
        if(root==null)return 0;
        //BFS
        Queue<TreeNode> queue=new LinkedList<>();
        int[]result=new int[2];

        queue.offer(root);
        int cen=0;
        while(!queue.isEmpty()){
            int value=0;
            int size=queue.size();
            for(int i=0;i<size;i++){
                TreeNode treeNode=queue.poll();
                value+=treeNode.val;
                if(treeNode.left!=null)queue.offer(treeNode.left);
                if(treeNode.right!=null)queue.offer(treeNode.right);
            }
            int other=cen==0?1:0;
            result[cen]=Math.max(result[other],result[cen]+value);

            cen=cen==0?1:0;
        }
        return Math.max(result[0],result[1]);
    }

    public int rob2(TreeNode root){
        //dfs
        if(root==null)
            return 0;
         int[] nums=robHelper(root);
        return Math.max(nums[0],nums[1]);
    }

    public int[] robHelper(TreeNode root){
        if(root==null)
            return new int[2];
        int []left=robHelper(root.left);   //自底向上
        int []right=robHelper(root.right);

        int[] res=new int[2];
        res[0]=left[1]+right[1]+root.val;    //这里保存各层相加的值
        res[1]=Math.max(left[0],left[1])+Math.max(right[0],right[1]); //这里保存相邻层大的那个值.
        return res;

    }

    /**
     * 递归版 中序
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result=new ArrayList<>();
        if(root==null)
            return result;

//        result.add(root.val);
        inorderTraversalHelper(root,result);
        return result;
    }

    private void  inorderTraversalHelper(TreeNode root,List<Integer> list) {
        if(root.left!=null){
            inorderTraversalHelper(root.left,list);
        }
        list.add(root.val);
        if(root.right!=null){
            inorderTraversalHelper(root.right,list);
        }
    }

    /**
     *非递归版 中序
     */
    public List<Integer> inorderTraversalTwo(TreeNode root){
        List<Integer> result=new ArrayList<>();
        if(root==null)
            return result;
        Stack<TreeNode> stack=new Stack<>();
        stack.push(root);
        TreeNode cur=root;
        while(!stack.empty()){
            while(cur!=null){
                cur=cur.left;
                stack.add(cur);
            }
            cur=stack.pop();
            result.add(cur.val);
            cur=cur.right;
        }
        return result;
    }


    /**
     * 锯齿层次遍历
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        if(root==null) return new ArrayList<>();
        List<List<Integer>> result=new ArrayList<>();
        int status=0;
        Queue<TreeNode> queue=new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            int size=queue.size();
            List<Integer> list=new ArrayList<>(size);
            for(int i=0;i<size;i++){
                TreeNode temp=queue.poll();
                list.add(temp.val);
                if(temp.left!=null){ queue.add(temp.left); }
                if(temp.right!=null){queue.add(temp.right);}
            }
            if(status==1){
                Collections.reverse(list);
                status=0;
            }else{
                status=1;
            }
            result.add(list);
        }

        return result;
    }

    /**
     * 先序遍历  中左右
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> list=new ArrayList<>();
        preorderTraversalHelper(root,list);
        return list;
    }
    public void preorderTraversalHelper(TreeNode root,List<Integer> list){
        if(root==null){return;}
        list.add(root.val);
        if(root.left!=null)preorderTraversalHelper(root.left,list);
        if(root.right!=null)preorderTraversalHelper(root.right,list);
    }


}
