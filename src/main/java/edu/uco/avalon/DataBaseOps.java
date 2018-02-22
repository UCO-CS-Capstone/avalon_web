package edu.uco.avalon;


import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;


import javax.faces.context.FacesContext;



public class DataBaseOps implements Serializable {


    public static Statement stmtObj;
    public static Connection connObj;
    public static ResultSet resultSetObj;
    public static PreparedStatement ps;

    /* Method To Establish Database Connection */
    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String db_url ="jdbc:mariadb://localhost:3306/avalon_db",
                    db_userName = "root",
                    db_password = "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv";
            connObj = DriverManager.getConnection(db_url,db_userName,db_password);
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return connObj;
    }

    /* Method To Fetch The Student Records From Database */
    public static ArrayList<User> getUsers(){
        ArrayList<User> userList = new ArrayList<>();
        try {
            stmtObj = getConnection().createStatement();
            resultSetObj = stmtObj.executeQuery("select * from users");
            while(resultSetObj.next()) {
                User stuObj = new User();
                stuObj.setId(resultSetObj.getInt("id"));
                stuObj.setUserName(resultSetObj.getString("userName"));
                stuObj.setPassword(resultSetObj.getString("password"));

                userList.add(stuObj);
            }
            System.out.println("Total Records Fetched: " + userList.size());
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return userList;
    }




    /* Method Used To Save New Student Record In Database */
    public static String insertUser(User newStudentObj) {
        int saveResult = 0;
        String navigationResult = "";
        try {
            ps = getConnection().prepareStatement("insert into users (username, password, flag_ID) values (?, ?, ?, ?, ?)");
            ps.setString(1, newStudentObj.getUserName());
            ps.setString(2, newStudentObj.getPassword());

            saveResult = ps.executeUpdate();
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        if(saveResult !=0) {
            navigationResult = "index.xhtml?faces-redirect=true";
        } else {
            navigationResult = "new_User.xhtml?faces-redirect=true";
        }
        return navigationResult;
    }

    /* Method Used To Edit Student Record In Database */
    public static String edit(int studentId) {
        User editRecord = null;
        System.out.println("editStudentRecordInDB() : Student Id: " + studentId);

        /* Setting The Particular Student Details In Session */
        Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

        try {
            stmtObj = getConnection().createStatement();
            resultSetObj = stmtObj.executeQuery("select * from users where student_id = "+studentId);
            if(resultSetObj != null) {
                resultSetObj.next();
                editRecord = new User();
                editRecord.setId(resultSetObj.getInt("id"));
                editRecord.setUserName(resultSetObj.getString("userName"));
                editRecord.setPassword(resultSetObj.getString("password"));

            }
            sessionMapObj.put("editRecordObj", editRecord);
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return "/index.xhtml?faces-redirect=true";
    }

    /* Method Used To Update Student Record In Database */
    public static String updateUser(User updateStudentObj) {
        try {
            ps = getConnection().prepareStatement("update users set userName=?, password=?  where id=?");
            ps.setString(1,updateStudentObj.getUserName());
            ps.setString(3,updateStudentObj.getPassword());
            ps.setInt(2,updateStudentObj.getId());
            ps.executeUpdate();
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return "/index.xhtml?faces-redirect=true";
    }

    /* Method Used To Delete Student Record From Database */
    public static String deleteUser(int id){
        System.out.println("deleteStudentRecordInDB() : Student Id: " + id);
        try {
            ps = getConnection().prepareStatement("delete from users where id = "+id);
            ps.executeUpdate();
            connObj.close();
        } catch(Exception sqlException){
            sqlException.printStackTrace();
        }
        return "/index.xhtml?faces-redirect=true";
    }


}
