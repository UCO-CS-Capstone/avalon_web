package edu.uco.avalon;

import java.io.Serializable;

public class User implements Serializable {

    private String userName;
    private String userType;
    private String email;
    private int age;


    public User(String userName, String userType, String email, int age) {
        this.userName = userName;
        this.userType = userType;
        this.email = email;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserType() {
        return userType;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setUserType(int age) {
        this.age = age;
    }




}
