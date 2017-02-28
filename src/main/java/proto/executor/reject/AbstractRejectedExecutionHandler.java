package proto.executor.reject;

import common.JvmTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lenovo on 2017/2/25.
 */
public abstract class AbstractRejectedExecutionHandler implements RejectedExecutionHandler {

    protected final Logger logger= LoggerFactory.getLogger(AbstractRejectedExecutionHandler.class);

    private static final ExecutorService dumpExcutor= Executors.newSingleThreadExecutor();

    protected final String threadName;
    private final AtomicBoolean dumpNeeded;
    private final String dumpPrefixName;

    public AbstractRejectedExecutionHandler(String threadName, boolean dumpNeeded, String dumpPrefixName) {
        this.threadName = threadName;
        this.dumpNeeded =new AtomicBoolean(dumpNeeded) ;
        this.dumpPrefixName = dumpPrefixName;
    }

    public void dumpJvmInfo(){
        if(dumpNeeded.getAndSet(false)){
            dumpExcutor.execute(()->{
                String now=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String name=threadName+"_"+now;
                FileOutputStream fileout=null;
                try {

                     fileout=new FileOutputStream(dumpPrefixName+".dump_"+name+".log");
                    List<String> stacks= JvmTools.Jstack();
                    for(String s:stacks){
                        fileout.write(s.getBytes("UTF-8"));
                    }
                    List<String> memoryUsage=JvmTools.memoryUsage();
                    for(String s: memoryUsage){
                        fileout.write(s.getBytes("UTF-8"));
                    }
                    if(JvmTools.memoryUsed()>0.9){
                        JvmTools.Jmap(dumpPrefixName+".dump_"+name+".bin",false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(fileout!=null){
                        try {
                            fileout.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            });


        }
    }


}
