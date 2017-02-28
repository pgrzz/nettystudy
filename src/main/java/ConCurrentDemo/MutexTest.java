package ConCurrentDemo;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by lenovo on 2017/1/28.
 * 利用 FifoMutex 保证了 test被访问的正确性
 */
public class MutexTest {
    private static final    Logger logger= LoggerFactory.getLogger(MutexTest.class);
    public static int test=10;
    private static CountDownLatch countDownLatch=new CountDownLatch(1);
    private static CountDownLatch downLatch=new CountDownLatch(2);
    public static void main(String[] args){
     long start=System.currentTimeMillis();
        FIFOMutex fifoMutex=new FIFOMutex();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<1000000;i++){
                    fifoMutex.lock();
                    try {
                        test--;
                    }finally {
                        fifoMutex.unlock();
                    }

                }
                downLatch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                countDownLatch.countDown();
                for(int  i=0;i<1000000;i++){
                    fifoMutex.lock();
                    try {
                        test++;
                    }finally {
                        fifoMutex.unlock();
                    }
                }
                downLatch.countDown();
            }
        }).start();
        try {
            downLatch.await();
            assert test==10;
            long second=(System.currentTimeMillis()-start)/1000;
           logger.warn("time:"+second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}
