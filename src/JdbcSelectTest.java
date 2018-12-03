import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.*;   // Use 'Connection', 'Statement' and 'ResultSet' classes in java.sql package
import java.text.SimpleDateFormat;
import java.util.Date;

// JDK 1.7 and above
public class JdbcSelectTest {   // Save as "JdbcSelectTest.java"
    public static void main(String[] args) {
        try (

                // Step 1: Allocate a database 'Connection' object
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/auctionhouse?useSSL=false", "root", "//3dvER :0.28()~!");
                // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"

                // Step 2: Allocate a 'Statement' object in the Connection
                Statement stmt = conn.createStatement();
        ) {
            // Step 3: Execute a SQL SELECT query, the query result
            //  is returned in a 'ResultSet' object.
//            String strSelect = "select * from users";
//            System.out.println("The SQL query is: " + strSelect); // Echo For debugging
//            System.out.println();

            // INSERT a record
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
            String currentDateTime = format.format(date);

            String sqlInsert = "insert into users (username,password, created_at)" // need a space
                    + " values ('homan', '123','" + currentDateTime + "')";
            System.out.println("The SQL query is: " + sqlInsert);  // Echo for debugging
            int countInserted = stmt.executeUpdate(sqlInsert);
            System.out.println(countInserted + " records inserted.\n");

//            ResultSet rset = stmt.executeQuery(strSelect);

            // Step 4: Process the ResultSet by scrolling the cursor forward via next().
            //  For each row, retrieve the contents of the cells with getXxx(columnName).
//            System.out.println("The records selected are:");
//            int rowCount = 0;
//            while(rset.next()) {   // Move the cursor to the next row, return false if no more row
//                int id = rset.getInt("id");
//                String username = rset.getString("username");
//                String password   = rset.getString("password");
//                System.out.println(id + ", " + username + ", " + password);
//                ++rowCount;
//            }
//            System.out.println("Total number of records = " + rowCount);

        }
        catch(MySQLIntegrityConstraintViolationException e) {
            System.out.println("Cannot register because of duplicate username");
        }
        catch(SQLException ex) {
//            System.out.println("Cannot register because of duplicate username");
            ex.printStackTrace();
        }
        // Step 5: Close the resources - Done automatically by try-with-resources
    }
}