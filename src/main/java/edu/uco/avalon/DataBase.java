package edu.uco.avalon;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DataBase {

    public static ArrayList<UserBean> allUsers() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null");
        }

        ArrayList<UserBean> userList = new ArrayList<>();

        try {
            String query = "SELECT * FROM users";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserBean user = new UserBean();
                user.setUserID(rs.getInt("userID"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setFlagID(rs.getInt("flagID"));
                user.setLastUpdatedDate(rs.getTimestamp("lastUpdatedDate").toLocalDateTime());
                user.setLastUpdatedBy(rs.getString("lastUpdatedBy"));

                userList.add(user);
            }
        } finally {
            conn.close();
        }

        return userList;
    }




    /* Method To Fetch The Student Records From Database */

    /* Method Used To Save New Student Record In Database */
    public static void saveStudentDetailsInDB(UserBean user) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        try {
            String query = "INSERT INTO users(userName, password,flagID, lastUpdatedDate,lastUpdatedBy)" +
                    " values (?, ?, ?, ?,?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getFlagID());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(5, user.getLastUpdatedBy());



            ps.executeUpdate();
        } finally {
            conn.close();
        }

    }


    public static void update(UserBean user) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null");
        }

        try {
            String query = "UPDATE users SET username=?, password=?, flagID=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE userID=?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getFlagID());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(5, user.getLastUpdatedBy());
            ps.setInt(6, user.getUserID());

            ps.executeUpdate();
        } finally {
            conn.close();
        }
    }




    public static void deleteUser(int user) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null");
        }
        try {
            String query = "delete from users WHERE userID=" + user;
            PreparedStatement ps = conn.prepareStatement(query);

            ps.executeUpdate();
        } finally {
            conn.close();
        }
    }






}