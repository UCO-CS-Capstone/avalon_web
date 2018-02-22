package edu.uco.avalon;



import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable{


    private DataSource ds;

    public ArrayList<User>userListDB;



    @PostConstruct
    public void init() {
        userListDB = DataBaseOps.getUsers();
    }

    /* Method Used To Fetch All Records From The Database */
    public ArrayList<User> userList() {
        return userListDB;
    }

    /* Method Used To Save New Student Record */
    public String insertUsers(User newStudentObj) {
        return DataBaseOps.insertUser(newStudentObj);
    }

    /* Method Used To Edit Student Record */
    public String editUser(int studentId) {
        return DataBaseOps.edit(studentId);
    }

    /* Method Used To Update Student Record */
    public String updateStudentDetails(User updateStudentObj) {
        return DataBaseOps.updateUser(updateStudentObj);
    }

    /* Method Used To Delete Student Record */
    public String delete(int studentId) {
        return DataBaseOps.deleteUser(studentId);
    }







}
