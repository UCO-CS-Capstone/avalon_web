package edu.uco.avalon;




import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;



import javax.sql.DataSource;
import javax.annotation.PostConstruct;

import javax.inject.Named;


@Named
public class UserBean implements Serializable{

    private String userName;
    private String password;
    private int id;


    public ArrayList<UserBean>userListDB;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }



    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setId(int id) {
        this.id = id;
    }




    @PostConstruct
    public void init() {
        userListDB = DataBase.getUserListDB();
    }

    /* Method Used To Fetch All Records From The Database */
    public ArrayList<UserBean> userList() {
        return userListDB;
    }

    /* Method Used To Save New Student Record */
    public String saveStudentDetails(UserBean user) throws SQLException {

            return DataBase.saveStudentDetailsInDB(user);
    }



    /* Method Used To Update Student Record */
    public String updateStudentDetails(UserBean updateStudentObj) {
        return DataBase.updateStudentDetailsInDB(updateStudentObj);
    }

    /* Method Used To Delete Student Record */
    public String deleteStudentRecord(int studentId) {
        return DataBase.deleteStudentRecordInDB(studentId);
    }

}
