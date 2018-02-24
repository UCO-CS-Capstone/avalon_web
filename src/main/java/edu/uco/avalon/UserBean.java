package edu.uco.avalon;


import java.time.LocalDateTime;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;


import javax.annotation.PostConstruct;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;


@Named
@RequestScoped
public class UserBean implements Serializable{

    private String userName;
    private String password;
    private int userID;
    private int flagID;
    private LocalDateTime lastUpdatedDate;
    private String lastUpdatedBy;


    private ArrayList<UserBean>userListDB;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getUserID() {
        return userID;
    }

    public int getFlagID(){return flagID;}

   public LocalDateTime getLastUpdatedDate(){return lastUpdatedDate;}

    public String getLastUpdatedBy(){return lastUpdatedBy;}




    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    public void setFlagID(int flagID) {
        this.flagID = flagID;
    }
    public void setLastUpdatedBy(String lastUpdatedBy){this.lastUpdatedBy = lastUpdatedBy;}








    @PostConstruct
    public void init() throws SQLException {

            userListDB = DataBase.allUsers();

        }



    /* Method Used To Fetch All Records From The Database */
    public ArrayList<UserBean> userList() {
        return userListDB;
    }

    /* Method Used To Save New Student Record */
    public void saveStudentDetails(UserBean user) throws SQLException {

            DataBase.saveStudentDetailsInDB(user);
        userListDB = DataBase.allUsers();
    }



    /* Method Used To Update Student Record */
    public void updateUser(UserBean user) throws SQLException{
        DataBase.update(user);
        userListDB = DataBase.allUsers();
    }



    public void deleteUserRecord(int id) throws SQLException {
        DataBase.deleteUser(id);
        userListDB = DataBase.allUsers();
    }



}
