package edu.uco.avalon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    /**
     * Creates a user from a ResultSet
     *
     * @param rs ResultSet
     * @return User
     * @throws SQLException
     */
    private static User createUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserID(rs.getInt("userID"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        int flagID = rs.getInt("flagID");
        if (flagID == 0) {
            user.setLocked(false);
        } else if (flagID == 1) {
            user.setLocked(true);
        }
        return user;
    }

    /**
     * Gets a User from a user ID
     *
     * @param userID Where clause
     * @param onlyActive Only pull active accounts
     * @return User
     * @throws SQLException
     */
    public static User getUserByID(int userID, boolean onlyActive) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM users WHERE userID = ?";
            if (onlyActive) {
                sql += " AND flagID IS NULL";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = createUser(rs);
                return user;
            }
        }
        return null;
    }

    /**
     * Gets a User from a user ID
     *
     * @param email Where clause
     * @param onlyActive Only pull active accounts
     * @return User
     * @throws SQLException
     */
    public static User getUserByEmail(String email, boolean onlyActive) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ?";
            if (onlyActive) {
                sql += " AND flagID IS NULL";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = createUser(rs);
                return user;
            }
        }
        return null;
    }

    /**
     * Set a user's flag to locked
     *
     * @param email Where clause
     * @throws SQLException
     */
    public static void lockUserAccount(String email) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "UPDATE users SET flagID = 1 WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.execute();
        }
    }

    /**
     * Insert a new user account into the database
     *
     * @param user User
     * @throws SQLException
     */
    public static void createUserAccount(User user) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            PasswordHash ph = PasswordHash.getInstance();
            String sql = "INSERT INTO users (first_name, last_name, email, password, lastUpdatedDate, lastUpdatedBy) VALUES (?, ?, ?, ?, curdate(), ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, ph.hash(user.getPassword()));
            ps.setInt(5, 0);
            ps.execute();
        }
    }

    /**
     * Add a persistent session into the database (Remember Me)
     *
     * @param sessionID Random session ID
     * @param userID User ID to tie the session to
     * @throws SQLException
     */
    public static void addPersistentSession(String sessionID, int userID) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "INSERT INTO persistent_session (sessionID, userID, timestamp) VALUES (?, ?, NOW())";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sessionID);
            ps.setInt(2, userID);
            ps.executeQuery();
        }
    }

    /**
     * Gets a persistent session from the database (Remember Me)
     *
     * @param sessionID Random session ID
     * @return Associated User ID
     * @throws SQLException
     */
    public static int getPersistentSession(String sessionID) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM persistent_session WHERE sessionID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sessionID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("userID");
            }
        }
        return -1;
    }

    /**
     * Removes a persistent session entry from the database (Remember Me)
     *
     * @param sessionID Random session ID
     * @throws SQLException
     */
    public static void deletePersistentSession(String sessionID) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "DELETE FROM persistent_session WHERE sessionID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sessionID);
            ps.execute();
        }
    }
}
