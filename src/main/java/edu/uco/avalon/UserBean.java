package edu.uco.avalon;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named(value="order")
@SessionScoped
public class UserBean implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final Order[] orderList = new Order[]{

            new Order("Gulliermo Ochoa", "jvelasco@uco.edu", 1),
            new Order("Rafael Marquez", "iruiz@uco.edu", 2),
            new Order("Javier Hernandez", "jmarquez@uco.edu", 3),
            new Order("Hector Herrera", "hherrera@uco.edu", 4),
            new Order("Hector Moreno", "hmoreno@uco.edu", 5),
            new Order("Hirvin Lozano", "hlozano@uco.edu", 6),
            new Order("Andres Guardado", "aguardado@uco.edu", 7),
            new Order("Javier Lopez", "jlopez@uco.edu", 8),
            new Order("Carlos Vela", "cvela@uco.edu", 9),
            new Order("Antonio Reyes", "areyesuco.edu", 10),
            new Order("Miguel Layun", "mlayuno@uco.edu", 11),
            new Order("Carlos Salcedo", "csalcedo@uco.edu", 12),
            new Order("Jonathan dos Santos", "jdossantos@uco.edu", 13),
            new Order("Miguel Jimenez", "mjimenez@uco.edu", 14),




    };

public Order[] getOrderList() {

        return orderList;

        }

public static class Order{

    String userName;
    String email;
    int user_id;


    public Order(String userName, String email, int user_id) {
        this.userName = userName;
        this.email = email;
        this.user_id = user_id;

    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

}
}