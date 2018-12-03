import java.sql.*;

public class Database {

    private String host;
    private String port;
    private String name;
    private String user;
    private String password;

    private Connection connection;

    Environment env = new Environment();

    public Database(String host,String port, String user, String password) throws SQLException {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        connection = getConnection();
    }

    public Database() throws SQLException {
        this.host = env.get("DATABASE_ADDRESS", "localhost");
        this.port = env.get("DATABASE_PORT", "3306");
        this.name = env.get("DATABASE_NAME", "auctionhouse");
        this.user = env.get("DATABASE_USER", "root");
        this.password = env.get("DATABASE_PASSWORD", "password");
        connection = getConnection();

    }

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://"+host+":"+port+"/"+name+"?useSSL=false", user, password
        );
        return conn;
    }


}
