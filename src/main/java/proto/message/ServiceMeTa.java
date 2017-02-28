package proto.message;

import proto.Directory;

/**
 * Created by lenovo on 2017/2/15.
 */
public class ServiceMeTa extends Directory {

    private String group;       //组名
    private String serviceProviderName;  //服务名
    private String version;     //版本

    public void setGroup(String group) {
        this.group = group;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String getServiceProviderName() {
        return serviceProviderName;
    }

    @Override
    public String getVersion() {
        return version;
    }
}
