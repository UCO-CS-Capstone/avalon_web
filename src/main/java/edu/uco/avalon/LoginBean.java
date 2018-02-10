package edu.uco.avalon;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

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

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int attempts = 0;

    private boolean locked;
    private boolean loggedIn = false;

    @PostConstruct
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (Exception e) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, e);
        }
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

    @Override
    public String toString() {
        return "LoginBean{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
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

    public String login() throws SQLException {
        if (attempts >= 5) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage("form", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed.", "Too many login attempts."));
            return "";
        }
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }
        try {
            Argon2 argon2 = Argon2Factory.create();
            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.email);
            ResultSet rs = ps.executeQuery();
            String hashed;
            if (rs.next()) {
                hashed = rs.getString("password");

                if (argon2.verify(hashed, this.password)) {
                    this.loggedIn = true;

                    this.userID = rs.getInt("userID");
                    this.firstName = rs.getString("first_name");
                    this.lastName = rs.getString("last_name");
                    this.locked = rs.getBoolean("isLocked");
                }
            }
        } finally {
            conn.close();
        }

        if (!this.loggedIn) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage("form", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed.", "Invalid username or password."));

            this.email = "";
            this.loggedIn = false;
            ++this.attempts;
            return "";
        } else {
            return "/dashboard?faces-redirect=true";
        }
    }

    public void logout() throws IOException {
        this.loggedIn = false;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/");
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
