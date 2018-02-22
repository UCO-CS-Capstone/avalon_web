package edu.uco.avalon;


import java.util.ArrayList;
import javax.annotation.PostConstruct;

public class User {


    private int id;
    private String userName;
    private String password;



    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
       return password;
    }





    public void setUserName(String userName) {
        this.userName = userName;
    }
    //public void setUserType(String userType) {
    //    this.userType = userType;
   // }
    public void setPassword(String password) {
        this.password = password;
    }





}
