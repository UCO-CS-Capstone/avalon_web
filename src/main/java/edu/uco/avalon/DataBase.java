package edu.uco.avalon;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DataBase {

    public interface USER_FLAGS {
        int ACTIVE = 0;
        int LOCKED = 1;
        int DELETED = 2;
        int VERIFIED = 3;
    }

    public static ArrayList<UserBean> allUsers() throws SQLException {

        ArrayList<UserBean> userList = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM users WHERE flagID != ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, USER_FLAGS.DELETED);
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
        }

        return userList;
    }




    /* Method To Fetch The Student Records From Database */

    /* Method Used To Save New Student Record In Database */
    public static void saveStudentDetailsInDB(UserBean user) throws SQLException {

        try (Connection conn = ConnectionManager.getConnection()) {
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
        }

    }


    public static void update(UserBean user) throws SQLException {

        try (Connection conn = ConnectionManager.getConnection()) {
            PasswordHash ph = PasswordHash.getInstance();
            String query = "UPDATE users set first_name=?, last_name=?,email=?, password=?, flagID=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE userID=?";

            PreparedStatement ps = conn.prepareStatement(query);


            ps.setString(1, user.getFirst());
            ps.setString(2, user.getLast());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getFlagID());
            ps.setTimestamp(6, Timestamp.valueOf(user.getLastUpdatedDate()));
            ps.setString(7, user.getLastUpdatedBy());
            ps.setInt(8, user.getUserID());

            ps.executeUpdate();

        }
    }


    public static UserBean readOneUser(int userID) throws SQLException {

        UserBean user = new UserBean();

        try (Connection conn = ConnectionManager.getConnection()) {
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

        }
        return user;
    }

    /**
     * Changes the user flag in the database.
     * See {@link USER_FLAGS}
     * @param userID User to modify
     * @param flag Flag string
     * @throws SQLException
     */
    private static void changeFlag(int userID, int flagID) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "update users set flagID = ? WHERE userID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, flagID);
            ps.setInt(2, userID);
            ps.executeUpdate();
        }
    }

    /**
     * Delete a given user
     * @param userID User to delete
     * @throws SQLException
     */
    public static void deleteUser(int userID) throws SQLException {
        changeFlag(userID, USER_FLAGS.DELETED);
    }

    /**
     * Lock a given user account
     * @param userID User to lock
     * @throws SQLException
     */
    public static void lock(int userID) throws SQLException {
        changeFlag(userID, USER_FLAGS.LOCKED);
    }

    /**
     * Unlock a given user account
     * @param userID User to unlock
     * @throws SQLException
     */
    public static void unlock(int userID) throws SQLException {
        changeFlag(userID, USER_FLAGS.ACTIVE);
    }



}
