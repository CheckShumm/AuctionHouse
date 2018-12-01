import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private boolean isAuth;

    public User() {
        this.isAuth = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }
}
