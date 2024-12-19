package view;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
    public static Connection getSQLServerConnection() {
        String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://localhost:1433";
        String dbName = "Laptrinh";
        String dbUsername = "sa";
        String dbPassword = "486905";
        String connectionURL = dbURL + ";databaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";

        Connection conn = null;

        try {
            // Load SQL Server JDBC driver
            Class.forName(dbDriver);
            // Establish connection
            conn = DriverManager.getConnection(connectionURL, dbUsername, dbPassword);
            System.out.println("Connection successful!");
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to establish a connection to the database.");
            e.printStackTrace();
        }

        return conn;
    }
}
