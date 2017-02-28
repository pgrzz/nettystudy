package ConCurrentDemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lenovo on 2017/1/28.
 */
public class ConditionTest {

    private static int a=10;
    private static final ReentrantLock lock=new ReentrantLock();
    private static final Condition fullLock=lock.newCondition();
    private static final Condition emptyLock=lock.newCondition();
    public static void main(String[]args) {

        //消费者 没有消费则等待
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){

                    final ReentrantLock lock=ConditionTest.lock;
                    lock.lock();
                    try {
                    while(a==0){
                        try {
                            emptyLock.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                        a--;
                        System.out.println("消费a了当前a为"+a);
                        fullLock.signalAll();

                    }finally {
                        lock.unlock();
                    }
                }

            }
        }).start();
        //生产者
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                final ReentrantLock lock=ConditionTest.lock;
                    lock.lock();
                    try {
                        while(a==10){
                            try {
                                fullLock.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        a++;
                        System.out.println("生产了a现在为:"+a);
                        emptyLock.signalAll();
                    }finally {
                        lock.unlock();
                    }


                }


            }
        }).start();

    }


}
