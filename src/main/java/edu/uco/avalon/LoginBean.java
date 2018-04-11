package edu.uco.avalon;

import org.omnifaces.cdi.Cookie;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mariadb.jdbc.internal.com.send.ed25519.Utils.bytesToHex;

/**
 * Allows for user login, logout, and registration.
 * Holds data for the current logged in user.
 */
@Named
@SessionScoped
public class LoginBean extends User implements Serializable {

    private boolean loggedIn = false;

    private int attempts = 0;

    private boolean rememberMe = true;

    @Inject @Cookie(name = "UUID")
    private String uuid = null;

    @PostConstruct
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (Exception e) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Helper to copy a User object's variables to the current Bean
     *
     * @param user
     */
    private void copyOut(User user) {
        if (user != null) {
            setUserID(user.getUserID());
            setFirstName(user.getFirstName());
            setLastName(user.getLastName());
            setEmail(user.getEmail());
            setPassword(user.getPassword());
            setLocked(user.isLocked());
            loggedIn = true;
        }
    }

    /**
     * Generate a random SHA-256 string for remember me functionality
     *
     * @return digest
     */
    private String generateUUID() {
        MessageDigest salt = null;
        try {
            salt = MessageDigest.getInstance("SHA-256");
            salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String digest = bytesToHex(salt.digest());
        return digest;
    }

    /**
     * If there is a persistent session set then login automatically
     *
     * @throws SQLException
     */
    public void checkPersistentSession() throws SQLException {
        if (uuid != null) {
            int userID = UserRepository.getPersistentSession(uuid);
            if (userID != -1) {
                User user = UserRepository.getUserByID(userID, true);
                copyOut(user);
            }
        }
    }

    /**
     * Creates a "Remember Me" session
     *
     * @throws SQLException
     */
    public void initPersistentSession() throws SQLException {
        String newUUID = generateUUID();
        Faces.addResponseCookie("UUID", newUUID, 31536000);
        UserRepository.addPersistentSession(newUUID, userID);
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
                locked = true;

                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage("login", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed.", "Too many login attempts. Please contact the administrator at (555) 867-5309 or click on \"Contact\" in the footer."));
                UserRepository.lockUserAccount(email);
                return "";
            }

            // Attempt login
            User user = UserRepository.getUserByEmail(email, true);
            PasswordHash ph = PasswordHash.getInstance();
            if (user != null) {
                if (ph.verify(user.getPassword(), password)) {
                    copyOut(user);
                }
            }
        }

        // Failure
        if (!loggedIn) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage("login", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed.", "Invalid username or password."));

            password = "";
            loggedIn = false;
            ++attempts;
            return "";
        } else {
            if (rememberMe) {
                initPersistentSession();
            }
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
            UserRepository.createUserAccount(this);
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
    public void logout() throws SQLException {
        if (uuid != null) {
            UserRepository.deletePersistentSession(uuid);
        }
        loggedIn = false;
        Faces.addResponseCookie("UUID", null, 0);
        Faces.invalidateSession();
        Faces.redirect(Faces.getRequestContextPath() + "/");
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
