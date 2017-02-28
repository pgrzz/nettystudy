package protooneday.Vo;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/6/12.
 */
public class TestUser implements Serializable {

    //private static final long serialVersionUID = -849794470754667710L;
    private static final long serialVersionUID = 4686274228090335845L;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
