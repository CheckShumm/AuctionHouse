import java.sql.*;
import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private boolean isAuth;

    public User() {
        this.isAuth = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() {

    }

    public static void main(String args[]) {
        User user = new User();
    }

}
