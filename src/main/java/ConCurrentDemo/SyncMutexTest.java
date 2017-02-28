package ConCurrentDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by lenovo on 2017/1/28.
 */
public class SyncMutexTest {

    private static final Logger LOGGER= LoggerFactory.getLogger(SyncMutexTest.class);

    public static int test=10;
    private static CountDownLatch countDownLatch=new CountDownLatch(1);
    private static CountDownLatch downLatch=new CountDownLatch(2);
    private static final SynMutex synMutex=new SynMutex();
    public static void main(String[] args){

        long start= System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<1000000;i++){
                    synMutex.lock();
                    try {
                        test--;
                    }finally {
                        synMutex.unlock();
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
                    synMutex.lock();
                    try {
                        test++;
                    }finally {
                        synMutex.unlock();
                    }
                }
                downLatch.countDown();
            }
        }).start();
        try {
            downLatch.await();
            assert test==10;
            long second=( System.currentTimeMillis()-start)/1000;
            LOGGER.warn("time:"+second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}
