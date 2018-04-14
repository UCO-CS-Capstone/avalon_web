package edu.uco.avalon;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import org.omnifaces.cdi.Cookie;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
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

    public interface messages {
        String shortLoginFailure = "Login Failed.";
        String locked = "Too many login attempts. Please contact the administrator at (555) 867-5309 or click on \"Contact\" in the footer.";
        String loginFailed = "Invalid username or password.";
        String shortRegistrationFail = "Registration Failed.";
        String longRegistrationFail = "Could not create the new user account at this time. Please try again later.";
    }

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
     * Creates a "Remember Me" session
     */
    public void initPersistentSession() {
        try {
            String newUUID = generateUUID();
            Faces.addResponseCookie("UUID", newUUID, UserRepository.PERSISTENT_TIME);
            UserRepository.addPersistentSession(newUUID, userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * If there is a persistent session set then login automatically
     *
     * @return True if successful login, else false.
     */
    public boolean checkPersistentSession() {
        if (loggedIn) return true;
        try {
            if (uuid != null) {
                ++attempts;
                User user = UserRepository.getUserByPersistentSession(uuid);
                if (attempts < 5) {
                    copyOut(user);
                    return true;
                } else {
                    locked = true;
                    UserRepository.lockUserAccount(user.getEmail());
                    FacesContext fc = FacesContext.getCurrentInstance();
                    fc.addMessage("login", new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.shortLoginFailure, messages.locked));
                    Faces.addResponseCookie("UUID", null, 0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Faces.addResponseCookie("UUID", null, 0);
        }
        return false;
    }

    /**
     * Logs a user in with provided credentials
     */
    public void login() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (!locked) {
                if (attempts < 5) {
                    // Attempt login
                    User user = UserRepository.getUserByEmail(email, true);
                    PasswordHash ph = PasswordHash.getInstance();
                    if (user != null && ph.verify(user.getPassword(), password)) {
                        copyOut(user);
                        if (rememberMe) {
                            initPersistentSession();
                        }
                    }
                } else {
                    // Lock out
                    locked = true;
                    UserRepository.lockUserAccount(email);
                }
            }

            if (loggedIn) {
                // Final redirect
                Faces.redirect("dashboard");
            } else {
                // Failure
                password = "";
                if (locked) {
                    fc.addMessage("login", new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.shortLoginFailure, messages.locked));
                } else {
                    fc.addMessage("login", new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.shortLoginFailure, messages.loginFailed));
                    ++attempts;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fc.addMessage("login", new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.shortLoginFailure, messages.loginFailed));
        }
    }

    /**
     * Insert a user into the database
     */
    public void register() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            userID = UserRepository.createUserAccount(this);
            if (rememberMe) {
                initPersistentSession();
            }
            loggedIn = true;
            password = "";
            Faces.redirect("dashboard");
        } catch (SQLException e) {
            e.printStackTrace();
            fc.addMessage("register", new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.shortRegistrationFail, messages.longRegistrationFail));
        }
    }

    /**
     * Destroys the current session to log the user out
     * Then redirects to the login page.
     */
    public void logout() {
        try {
            if (uuid != null) {
                UserRepository.deletePersistentSession(uuid);
            }
            loggedIn = false;
            Faces.addResponseCookie("UUID", null, 0);
            Faces.invalidateSession();
            Faces.redirect("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
