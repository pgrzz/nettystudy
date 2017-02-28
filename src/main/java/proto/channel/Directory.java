package proto.channel;



/**
 * 服务的目录：<服务组别，服务名称，服务版本>
 * Created by lenovo on 2017/1/23.
 */
public abstract class Directory {

    private String directoryCahce;

    /**
     * 服务所属的组别
     */
    public abstract String getGroup();

    /**
     * 服务名称
     */
    public abstract String getServiceProviderName();

    /**
     * 服务的版本
     */
    public abstract String getVersion();

    public String directory(){
        if(directoryCahce!=null)
            return directoryCahce;

        StringBuilder buf= new StringBuilder("");
        buf.append(getGroup())
                .append('-')
                .append(getServiceProviderName())
                .append('-')
                .append(getVersion());
        directoryCahce=buf.toString();
        return directoryCahce;
    }


}
