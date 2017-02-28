package Leet.stl;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lenovo on 2016/12/24.
 */
public class PriorityQueue<T extends  Comparable> {

    private ArrayList<T> arrays;

    private  int size=1;

    public PriorityQueue() {
       arrays=new ArrayList<T>(0);

    }

    private T parent(int i){
        return arrays.get(i>>1);
    }
    private T left(int i){
        return arrays.get(i<<1);
    }
    private T right(int i){
        return arrays.get(i<<1+1);
    }
    private void swap(int i,T iNode,int lagset,T lagestNode){
        arrays.set(i,lagestNode);
        arrays.set(lagset,iNode);
    }

    private void fix_heap(int i){
        if(i<0 || i>arrays.size())
         throw new IndexOutOfBoundsException();
        T leftNode=left(i);
        T rightNode=right(i);
        int lagest=i;
        T nowNode=arrays.get(i);
        if(leftNode.compareTo(nowNode)>0){  // left > lagest
            lagest=i<<1;
            nowNode=leftNode;
        }
        if(rightNode.compareTo(nowNode)>0){ // right>lagest
            lagest=i<<1+1;
            nowNode=rightNode;
        }
        if(i!=lagest){
            swap(i,arrays.get(i),lagest,nowNode);
        }
    }

    private ArrayList<T> buid_heap(T[] arrays){
        this.arrays=(ArrayList<T>) Arrays.asList(arrays);
         int len=arrays.length<<1;
        for(int i=len;i>0;i++){
            fix_heap(i);
        }
        return this.arrays;
    }

   private T peek(){
       return arrays.get(1);
   }

    private T poll(){
        T max=this.arrays.get(1);
        T last=this.arrays.get(this.arrays.size()-1);
        this.arrays.set(1,last);
        arrays.remove(1);
        fix_heap(1);
        return max;
    }

//    private T push(){
//
//    }

    private void heap_increse(int i,T key) throws Exception {
        if(key.compareTo(arrays.get(i))<0){
            throw new Exception("");
        }
        arrays.set(i+1,key);
        while(i>1 && parent(i).compareTo(arrays.get(i))<0){
            swap(i,arrays.get(i),i>>1,arrays.get(i>>1));
        }

    }


    public static void main(String args[]){

    }




}
