package ConCurrentDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * Created by lenovo on 2017/3/22.
 */
public class ByteBufTest {

    public static void main(String args[]){

        ByteBuf buf= UnpooledByteBufAllocator.DEFAULT.buffer(1024);

            assert  buf.refCnt()==1;
            boolean destoryed=buf.release();
            assert  destoryed;
            assert buf.refCnt()==0;

         //   buf.retain();     //   can't be used  when  buf refCount =0;
          //assert  buf.refCnt()==1;




    }


}
