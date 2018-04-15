package edu.uco.avalon;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import org.omnifaces.util.Faces;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    /**
     * Common time values as seconds
     */
    public interface secondsIn {
        /**
         * 1 day in seconds
         */
        int day = 60 * 60 * 24;

        /**
         * 1 week in seconds
         */
        int week = 60 * 60 * 24 * 7;

        /**
         * 1 month in seconds
         */
        int month = 60 * 60 * 24 * 31;

        /**
         * 1 year in seconds
         */
        int year = 60 * 60 * 24 * 365;
    }

    /**
     * The length of time that a "Remember Me" session should last
     */
    public static final int PERSISTENT_TIME = secondsIn.month;

    /**
     * Creates a user from a ResultSet
     *
     * @param rs ResultSet
     * @return User
     * @throws SQLException
     */
    private static User createUserFromResultSet(ResultSet rs) throws SQLException {
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
                sql += " AND flagID = 0";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = createUserFromResultSet(rs);
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
                sql += " AND flagID = 0";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = createUserFromResultSet(rs);
                return user;
            }
        }
        return null;
    }

    /**
     * Gets a User from a persistent session ID (Remeber Me)
     *
     * @param sessionID persistent session ID
     * @return User
     * @throws SQLException
     */
    public static User getUserByPersistentSession(String sessionID) throws SQLException {
        int userID = UserRepository.getPersistentSession(sessionID);
        User user = UserRepository.getUserByID(userID, true);
        return user;
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
            ps.executeUpdate();
        }
    }

    /**
     * Insert a new user account into the database
     *
     * @param user User
     * @throws SQLException
     */
    public static int createUserAccount(User user) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            PasswordHash ph = PasswordHash.getInstance();
            String sql = "INSERT INTO users (first_name, last_name, email, password, lastUpdatedDate, lastUpdatedBy) VALUES (?, ?, ?, ?, curdate(), ?)";
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, ph.hash(user.getPassword()));
            ps.setString(5, "registration");
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt("userID");
            }
        }
        throw new SQLException("Could not create new user account");
    }

    public static String getUserCountry() {
        try {
            File database = new File(Faces.getRealPath("WEB-INF") + "/GeoLite2-Country.mmdb");
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
            InetAddress ipAddress = InetAddress.getByName(Faces.getRemoteAddr());
            CountryResponse response = dbReader.country(ipAddress);
            return response.getCountry().getName();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception ignored) {}
        return "unknown";
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
            String sql = "INSERT INTO persistent_session (sessionID, userID, ip, country, timestamp) VALUES (?, ?, ?, ?, NOW())";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sessionID);
            ps.setInt(2, userID);
            ps.setString(3, Faces.getRemoteAddr());
            ps.setString(4, getUserCountry());
            ps.executeUpdate();
        }
    }

    /**
     * Gets a persistent session from the database (Remember Me) within the last PERSISTENT_TIME seconds
     *
     * @param sessionID Random session ID
     * @return Associated User ID
     * @throws SQLException
     */
    public static int getPersistentSession(String sessionID) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM persistent_session WHERE sessionID = ? AND active = TRUE AND country = ? AND timestamp > DATE_SUB(NOW(), INTERVAL ? SECOND)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sessionID);
            ps.setString(2, getUserCountry());
            ps.setInt(3, PERSISTENT_TIME);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("userID");
            }
        }
        throw new SQLException("Entry not found for given session ID");
    }

    /**
     * Removes a persistent session entry from the database (Remember Me)
     *
     * @param sessionID Random session ID
     * @throws SQLException
     */
    public static void deletePersistentSession(String sessionID) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "UPDATE persistent_session SET active = FALSE WHERE sessionID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sessionID);
            ps.executeUpdate();
        }
    }
}
