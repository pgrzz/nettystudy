package common;

import com.sun.management.HotSpotDiagnosticMXBean;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/2/25.
 */
public class JvmTools {

    /**
     * return java stack traces of java threads for the current java process.
     */
    public static List<String> Jstack() throws Exception{
        List<String> stackList=new LinkedList<>();
        Map<Thread,StackTraceElement[]> allStackTraces=Thread.getAllStackTraces();

        for(Map.Entry<Thread,StackTraceElement[]> entry:allStackTraces.entrySet()){
                Thread thread=entry.getKey();
                StackTraceElement[] stackTraceElement=entry.getValue();
                stackList.add(
                        String.format(
                                "\"%s\" tid=%s isDaemon priority=%s"+"%n",
                                thread.getName(),
                                thread.getId(),
                                thread.isDaemon(),
                                thread.getPriority()
                        )
                );
            stackList.add("java lang.Thread State"+thread.getState()+"%n");
            if(allStackTraces!=null){
                for(StackTraceElement s:stackTraceElement){
                        stackList.add("   "+s.toString()+"%n");
                }
            }
        }
        return stackList;
    }

    public static List<String> memoryUsage()throws  Exception{
            MemoryUsage heapMemoryUsage=MXBeanHolder.memoryMxBean.getHeapMemoryUsage();
            MemoryUsage noHeapMemoryUsage=MXBeanHolder.memoryMxBean.getNonHeapMemoryUsage();

        List<String> memoryUsageList=new LinkedList<>();
        memoryUsageList.add("*******************Memory Usage ****************" + "%n");
        memoryUsageList.add("Heap Memory Usage: "+heapMemoryUsage.toString()+"%n");
        memoryUsageList.add("NoHeap Memory Usage :"+noHeapMemoryUsage.toString()+"%n");

        return memoryUsageList;
    }

    public static double memoryUsed()throws Exception{

        MemoryUsage heapMemoryUsage=MXBeanHolder.memoryMxBean.getHeapMemoryUsage();
        return(double)(heapMemoryUsage.getUsed())/heapMemoryUsage.getMax();

    }

    /**
     *Dumps the heap to the outputFile  file in the same format as the hprof heap dump.
     * outputfile  the System-dependent filename
     * live if true dump only  live object i.e objects that are reachable from others
      */
    public static void Jmap(String outputFile,boolean live)throws Exception{

        File file=new File(outputFile);
        if(file.exists()){
            file.delete();
        }
        MXBeanHolder.hotsportDiagnosticMXBean.dumpHeap(outputFile, live);
    }

    private static class MXBeanHolder{
        static final MemoryMXBean memoryMxBean= ManagementFactory.getMemoryMXBean();
        static final HotSpotDiagnosticMXBean hotsportDiagnosticMXBean=ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);

    }


}
