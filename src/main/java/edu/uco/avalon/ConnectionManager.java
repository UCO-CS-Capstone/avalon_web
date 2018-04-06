package edu.uco.avalon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    /**
     * Gets the database connection with parameters
     * @return DriverManager database connection
     * @throws SQLException If connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }
        return conn;
    }
}
