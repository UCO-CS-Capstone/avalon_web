package edu.uco.avalon;


import java.time.LocalDateTime;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;


import javax.annotation.PostConstruct;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;


@Named
@RequestScoped
public class UserBean implements Serializable{


    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private int userID;
    private int flagID;
    private LocalDateTime lastUpdatedDate;
    private String lastUpdatedBy;


    private ArrayList<UserBean>userListDB;

    public String getFirst() {
        return first_name;
    }
    public String getLast() {
        return last_name;
    }



    public String getEmail() {
        return email;
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



    public void setFirst(String first_name) {
        this.first_name = first_name;
    }
    public void setLast(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
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



    public String beforeEdit(int userID) throws Exception{
        UserBean user = DataBase.readOneUser(userID);
        this.userID = userID;
        this.first_name = user.getFirst();
        this.last_name = user.getLast();
        this.email = user.getEmail();
        this.password  = user.getPassword();
        this.flagID = user.getFlagID();
        this.lastUpdatedBy = user.getLastUpdatedBy();
        return "/admin/update_User";
    }

    /* Method Used To Update Student Record */
    public String updateUser() throws SQLException{
        UserBean oldInfo = new UserBean();
        oldInfo.setUserID(this.userID);
        oldInfo.setFirst(this.first_name);
        oldInfo.setLast(this.last_name);
        oldInfo.setEmail(this.email);
        oldInfo.setPassword(this.password);
        oldInfo.setFlagID(this.flagID);
        oldInfo.setLastUpdatedDate(LocalDateTime.now());
        oldInfo.setLastUpdatedBy("admin");
        DataBase.update(oldInfo);
        userListDB = DataBase.allUsers();
        return "/admin/index";
    }



    public void deleteUserRecord(int id) throws SQLException {
        DataBase.deleteUser(id);
        userListDB = DataBase.allUsers();
    }
    public void lock(int id) throws SQLException {
        DataBase.lock(id);
        userListDB = DataBase.allUsers();
    }
    public void unlock(int id) throws SQLException {
        DataBase.unlock(id);
        userListDB = DataBase.allUsers();
    }





}
