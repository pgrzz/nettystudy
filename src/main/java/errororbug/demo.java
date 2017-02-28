package errororbug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lenovo on 2017/1/12.
 */
public class demo {

    public static  void main(String[] args){
        new demo().test1();
    }


    /**
     *
     * 2017_1_12  list 迭代器
     * 只有两个元素的情况非常特殊（可以理解为尾节点的前一个节点这种情况） 当你把元素1 删除以后
     * 迭代器是先判断的 是否还有下一个也就是 cursor != size; 所以到这里就退出了不会抛出ConcurrentModificationException(); 异常
     * 当  1 元素被删除后 size改变了  hashNext 现在 会判断 cursor != size; 因为已经等于 size了所以退出
     * 2 元素。 首先 迭代器中的迭代数量为modCount
     * 什么是modCount   The number of times this list has been <i>structurally modified</i>.
     * 当前时刻list拥有的元素个数
     * 当把第2个删除后 到第三次遍历 modCount != expectedModCount   throw new ConcurrentModificationException();
     * 也就是把尾元素删除会导致  modCount 改变
     */
    public void test1(){
        List<String> a = new ArrayList<String>();
        a.add("1");
        a.add("2");
        for (String temp : a) {
//            if("1".equals(temp)){
//                a.remove(temp);
//            }
            if("1".equals(temp)){
                a.remove(temp);
            }
        }
    }





}
