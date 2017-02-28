package velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import velocity.model.Model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * Created by lenovo on 2016/11/28.
 */
public class VelocityUtil {
    private static  VelocityEngine engine;
    static {
        engine=new VelocityEngine();
        Properties properties=new Properties();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER,"classpath");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        engine.init();
    }

    public static void CreateTemplate(Model model){
        VelocityContext context=new VelocityContext();
        context.put("id",model.getId());
        context.put("author",model.getAuthor());
        context.put("content",model.getContent());
        context.put("createTime",model.getCreateTime());
        context.put("tittle",model.getTittle());

        Template template=engine.getTemplate(model.getTemplateurl());
        try (FileOutputStream outputStream=new FileOutputStream("F://"+"abc.html");
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
