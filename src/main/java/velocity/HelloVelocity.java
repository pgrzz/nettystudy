package velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by lenovo on 2016/11/28.
 * 忘记了 classses 下不能够写入==。。。
 */
public class HelloVelocity {

    private static final String path="F:/"+"ab.html";

    public static void main(String args[]){
        VelocityEngine velocityEngine=new VelocityEngine();
        Properties properties=new Properties();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER,"classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
        Template template=velocityEngine.getTemplate("hello.vm");
        VelocityContext context=new VelocityContext();
        context.put("name","pgr");
        context.put("date",new Date());
        List list=new ArrayList();
        list.add(1);
        list.add(2);
        context.put("list",list);
        try (FileOutputStream outputStream=new FileOutputStream(path);
             PrintWriter printWriter=new PrintWriter(outputStream)
        ){
            outputStream.write(new byte[]{});
            template.merge(context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
