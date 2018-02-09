package edu.uco.avalon;

import java.io.Serializable;

public class User implements Serializable {

    String title;
    String author;
    int price;
    int quant;
    int sub;


    public User(String title, String author, int price, int quant, int sub) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.quant = quant;
        this.sub = sub;

    }

    public int getPrice() {
        return price;
    }




    public double getSub() {
        return sub;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public void setSub(int sub) {
        this.sub = sub;
    }



    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }





    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
