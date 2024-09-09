package DB_Conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    // Database credentials
    private String url = "jdbc:postgresql://localhost:5432/WorkPal";
    private String user = "postgres";
    private String password = "0";

    // Method to establish a connection
    public Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        if (connection != null) {
            
        } else {
            System.out.println("Failed to make connection!");
        }
        return connection;
    }
}
