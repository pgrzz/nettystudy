package proto;

/**
 * Created by lenovo on 2017/2/15.
 */
public abstract class Directory {

    private String directoryCache;

    /** 服务所属组别 */
    public abstract String getGroup();

    /** 服务名称 */
    public abstract String getServiceProviderName();

    /** 服务版本号 */
    public abstract String getVersion();

  public String direcotry(){
      if(directoryCache!=null){
          return directoryCache;
      }
      StringBuilder builder=new StringBuilder("");
      builder.append(getGroup())
              .append("-")
              .append(getServiceProviderName())
              .append("-")
              .append(getVersion());

      directoryCache=builder.toString();
      return directoryCache;
  }
    public void clear(){
        directoryCache=null;
    }
}
