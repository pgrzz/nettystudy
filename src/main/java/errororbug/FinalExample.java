package errororbug;

/**
 * Created by lenovo on 2017/3/4.
 *  译者注： final字段所引用的对象里的字段或数组元素可能在后续还会变化，若没有正确同步，其它
 线程也许不能看到最新改变的值，但一定可以看到完全初始化的对象或数组被 final
 字段引用的那个时刻的对象字段值或数组元素。 ）
 */
public class FinalExample {

  static   class  FinalFieldExample  {
        final  int  x;
        int  y;
        static  FinalFieldExample  f;
        public  FinalFieldExample()  {
            x  =  3;
            y  =  4;
        }
        static  void  writer()  {
            f  =  new  FinalFieldExample();
        }
        static  void  reader()  {
            if  (f  !=  null)  {
                int  i  =  f.x;  //  guaranteed  to  see  3
                int  j  =  f.y;  //  could  see  0
            }
        }
    }


}
