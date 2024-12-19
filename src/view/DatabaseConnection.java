package view;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection initializeDatabaze() {
        String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://localhost:1433";

        String dbName = "Laptrinh";
        String dbUsername = "sa";
        String dbPassword = "13051980";
        String connectionURL = dbURL + ";databaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";

        Connection conn = null;

        try {
            // Load the SQL Server JDBC driver
            Class.forName(dbDriver);
            // Create the connection
            conn = DriverManager.getConnection(connectionURL, dbUsername, dbPassword);
            System.out.println("Connect successfully");
        } catch (Exception ex) {
            System.out.println("Connect failure: " + ex.getMessage());
            ex.printStackTrace();
        }
        // Return the connection object
        return conn;
    }
}
