package edu.uco.avalon;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Allows for user login, logout, and registration.
 * Holds data for the current logged in user.
 */
@Named
@SessionScoped
public class LoginBean implements Serializable {

    private int userID;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int attempts = 0;

    private boolean locked = false;
    private boolean loggedIn = false;

    @PostConstruct
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (Exception e) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Logs a user in with provided credentials
     *
     * @return Where to redirect to
     * @throws SQLException
     */
    public String login() throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            // Lock out
            if (attempts >= 5 || locked) {
                email = "";
                password = "";
                loggedIn = false;
                locked = true;

                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage("login", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed.", "Too many login attempts. Please contact the administrator at (555) 867-5309 or click on \"Contact\" in the footer."));
                String sql = "UPDATE users SET flagID = 1 WHERE email = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, email);
                ps.execute();
                return "";
            }

            // Attempt login
            PasswordHash ph = PasswordHash.getInstance();
            String sql = "SELECT * FROM users WHERE email = ?  AND flagID IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.email);
            ResultSet rs = ps.executeQuery();
            String hashed;
            if (rs.next()) {
                hashed = rs.getString("password");

                if (ph.verify(hashed, this.password)) {
                    // Success
                    loggedIn = true;

                    userID = rs.getInt("userID");
                    firstName = rs.getString("first_name");
                    lastName = rs.getString("last_name");
                    email = rs.getString("email");
                    password = "";
                }
            }
        }

        // Failure
        if (!this.loggedIn) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage("login", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed.", "Invalid username or password."));

            password = "";
            loggedIn = false;
            ++attempts;
            return "";
        } else {
            return "/dashboard?faces-redirect=true";
        }
    }

    /**
     * Insert a user into the database
     *
     * @return Where to redirect to
     * @throws SQLException
     */
    public String register() throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            PasswordHash ph = PasswordHash.getInstance();
            String sql = "INSERT INTO users (first_name, last_name, email, password, lastUpdatedDate, lastUpdatedBy) VALUES (?, ?, ?, ?, curdate(), ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, ph.hash(password));
            ps.setInt(5, 0);
            ps.execute();
            loggedIn = true;
            password = "";
        }
        return "/dashboard";
    }

    /**
     * Destroys the current session to log the user out
     * Then redirects to the login page.
     *
     * @throws IOException
     */
    public void logout() throws IOException {
        loggedIn = false;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        locked = locked;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getFullname() {
        return this.firstName + ' ' + this.lastName;
    }
}
