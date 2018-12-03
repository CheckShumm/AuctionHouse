import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Auth {

    Database db;
    Connection connect;
    Statement statement;
    private User user;

    public Auth() throws SQLException {
        db = new Database();
        Connection connect = db.getConnection();
        statement = connect.createStatement();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean register() {
        try {
            String registerUser = "insert into users (username,password, created_at)" // need a space
                    + " values ('"+user.getUsername()+"', '"+user.getPassword()+"','" + Help.getCurrentTime() + "')";
            int countInserted = statement.executeUpdate(registerUser);
            if(login()){
                return true;
            }

            return false;
        } catch (MySQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            System.out.println("Unable to register user because of duplicate username");
            return false;
        } catch(SQLException ex) {
            System.out.println("Unable to connect to database");
            ex.printStackTrace();
            return false;
        }
    }

    public boolean login() throws SQLException {
//        System.out.println("attempt to login with username: " + user.getUsername() + ", password: " + user.getPassword() + "\n");
        try {
            String findUser = "SELECT * FROM users WHERE username='" +
                    user.getUsername() +
                    "' AND password='" + user.getPassword() +"'";

            ResultSet rset = statement.executeQuery(findUser);
            while(rset.next()) {
                user.setId(rset.getInt("id"));
                user.setUsername(rset.getString("username"));
                user.setPassword(rset.getString("password"));
                user.setAuth(true);
                return true;
                }

            return false;
        } catch(SQLException ex) {
            System.out.println("Unable to connect to database");
            ex.printStackTrace();
            return false;
        }
    }

    public boolean logout() {
        if (!user.isAuth()) {
            return false;
        }

        user.setAuth(false);
        return true;
    }

    public User getUser() {
        return user;
    }
}
