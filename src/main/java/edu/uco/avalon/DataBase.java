package edu.uco.avalon;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DataBase {

    public static ArrayList<UserBean> allUsers() throws SQLException {
        Connection conn = ConnectionManager.getConnection();

        ArrayList<UserBean> userList = new ArrayList<>();

        try {
            String query = "SELECT * FROM users";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserBean user = new UserBean();
                user.setFirst(rs.getString("first_name"));
                user.setLast(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setUserID(rs.getInt("userID"));
                //user.setPassword(rs.getString("password"));
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
        Connection conn = ConnectionManager.getConnection();

        try {
            PasswordHash ph = PasswordHash.getInstance();
            String query = "INSERT INTO users( first_name, last_name, email, password,flagID, lastUpdatedDate,lastUpdatedBy)" +
                    " values (?, ? , ?, ?, ?, ?,?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user.getFirst());
            ps.setString(2, user.getLast());
            ps.setString(3, user.getEmail());
            ps.setString(4, ph.hash(user.getPassword()));
            ps.setInt(5, 0);
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, "admin");



            ps.executeUpdate();
        } finally {
            conn.close();
        }

    }


    public static void update(UserBean user) throws SQLException {
        Connection conn = ConnectionManager.getConnection();

        try {
            PasswordHash ph = PasswordHash.getInstance();
            String query = "UPDATE users set first_name=?, last_name=?,email=?, password=?, flagID=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE userID=?";

            PreparedStatement ps = conn.prepareStatement(query);


            ps.setString(1,user.getFirst());
            ps.setString(2, user.getLast());
            ps.setString(3,user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getFlagID());
            ps.setTimestamp(6, Timestamp.valueOf(user.getLastUpdatedDate()));
            ps.setString(7, user.getLastUpdatedBy());
            ps.setInt(8, user.getUserID());

            ps.executeUpdate();

        } finally {
            conn.close();
        }
    }


    public static UserBean readOneUser(int userID) throws SQLException {
        Connection conn = ConnectionManager.getConnection();

        UserBean user = new UserBean();

        try {
            String query = "SELECT * FROM users WHERE userID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user.setFirst(rs.getString("first_name"));
                user.setLast(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFlagID(rs.getInt("flagID"));
                user.setLastUpdatedBy(rs.getString("lastUpdatedBy"));

            }

        } finally {
            conn.close();
        }
        return user;
    }

    public static void deleteUser(int user) throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        try {
            String query = "delete from users WHERE userID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user);
            ps.executeUpdate();
        } finally {
            conn.close();
        }
    }




    public static void lock(int user) throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        try {
            String query = "update users set flagID = 1 WHERE userID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user);
            ps.executeUpdate();
        } finally {
            conn.close();
        }
    }
    public static void unlock(int user) throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        try {
            String query = "update users set flagID = 0 WHERE userID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user);
            ps.executeUpdate();
        } finally {
            conn.close();
        }
    }



}
