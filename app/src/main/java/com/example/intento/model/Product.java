// Product.java
package com.example.intento.model;

public class Product {
    private int id;
    private String pname;
    private String description;
    private String price;
    private String image;
    private String category;
    private String date;
    private String time;

    // Constructor
    public Product(int id, String pname, String description, String price, String image, String category, String date, String time) {
        this.id = id;
        this.pname = pname;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.date = date;
        this.time = time;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public String getPname() {
        return pname;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
