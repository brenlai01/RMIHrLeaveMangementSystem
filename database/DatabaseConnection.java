package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Protocol: JDBC over TCP/IP to communicate with MySQL database
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/hrm_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password"; // TODO: change to your MySQL password

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        // TODO: Load MySQL JDBC driver - Class.forName("com.mysql.cj.jdbc.Driver")
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("[DB] Error connecting to database: " + e.getMessage());
            return null;
        }
    }
}
