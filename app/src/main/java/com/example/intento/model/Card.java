package com.example.intento.model;

public class Card {
    private String pid;
    private String pname;
    private String price;
    private String discount;
    private String quantity;
    private byte[] image; // Añadir el campo de la imagen

    public Card(String pid, String pname, String price, String discount, String quantity, byte[] image) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.image = image; // Inicializar el campo de la imagen
    }

    // Getters y Setters para todos los campos

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
