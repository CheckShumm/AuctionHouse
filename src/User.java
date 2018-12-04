import java.sql.*;
import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private boolean isAuth;
    private int requestCount;
    private int itemCount;
    
    public User() {
        this.isAuth = false;
        this.itemCount = 0;
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

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public void incRequestCount() {
        this.requestCount++;
    }
    
    
    public void login() {

    }

    public void incItemCount() {
        this.itemCount++;
    }

    public void decItemCount() {
        this.itemCount--;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public static void main(String args[]) {
        User user = new User();
    }

}
