package velocity.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/11/28.
 */
public class Model implements Serializable {

    private static final long serialVersionUID = -6849794470754667710L;

    private  String templateurl;
    private  String id;
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private  String tittle;
    private  String createTime;
    private String author;
    private String content;

    public String getTemplateurl() {
        return templateurl;
    }

    @Override
    public String toString() {
        return tittle+content;
    }

    public void setTemplateurl(String templateurl) {
        this.templateurl = templateurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
