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
    private List<User> userList;









    @PostConstruct
    private void init() {
        userList = new ArrayList<>();
        userList.add(new User("John Travolta", "Admin", "john.doe@example.com", 32));
        userList.add(new User("Peter Griffin", "User", "peter.smith@example.com", 25));
        userList.add(new User("Mary Poppins", "User", "mary.jane@example.com", 27));
        userList.add(new User("Mike Tyson", "User", "mike.skeet@example.com", 35));
    }

    public List<User> getUserList() {
        return userList;
    }

    public int getUserCount() {
        return userList.size();
    }




}
