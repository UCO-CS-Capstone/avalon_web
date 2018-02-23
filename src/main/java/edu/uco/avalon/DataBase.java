package edu.uco.avalon;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    public static Statement stmtObj;
    public static Connection connObj;
    public static ResultSet resultSetObj;
    public static PreparedStatement pstmt;

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

    /* Method To Fetch The User Records From Database */
    public static ArrayList<UserBean> getUserListDB() {
        ArrayList<UserBean> studentsList = new ArrayList<UserBean>();
        try {
            stmtObj = getConnection().createStatement();
            resultSetObj = stmtObj.executeQuery("select * from users");
            while(resultSetObj.next()) {
                UserBean user = new UserBean();
                user.setId(resultSetObj.getInt("user_id"));
                user.setPassword(resultSetObj.getString("password"));
                user.setPassword(resultSetObj.getString("student_password"));
                studentsList.add(user);
            }
            System.out.println("Total Records Fetched: " + studentsList.size());
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return studentsList;
    }

    /* Method Used To Save New User Record In Database */
    public static String saveStudentDetailsInDB(UserBean user) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        try {
            String query = "INSERT INTO users(userName, password)" +
                    " values (?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());


            ps.executeUpdate();
        } finally {
            conn.close();
        }
        return null;
    }

    /* Method Used To Update User Record In Database */
    public static String updateStudentDetailsInDB(UserBean updateStudentObj) {
        try {
            pstmt = getConnection().prepareStatement("update users set username=?, password=?," +
                    "where userID=?");
            pstmt.setString(1,updateStudentObj.getUserName());
            pstmt.setString(2,updateStudentObj.getPassword());
            pstmt.setInt(3,updateStudentObj.getId());
            pstmt.executeUpdate();
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return "/index.xhtml?faces-redirect=true";
    }

    /* Method Used To Delete User Record From Database */
    public static String deleteStudentRecordInDB(int studentId){
        System.out.println("deleteStudentRecordInDB() : Student Id: " + studentId);
        try {
            pstmt = getConnection().prepareStatement("delete from student_record where student_id = "+studentId);
            pstmt.executeUpdate();
            connObj.close();
        } catch(Exception sqlException){
            sqlException.printStackTrace();
        }
        return "/index.xhtml?faces-redirect=true";
    }
}
