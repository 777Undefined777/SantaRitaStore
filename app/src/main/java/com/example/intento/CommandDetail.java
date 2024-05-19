package com.example.intento;

public class CommandDetail {
    private long id;
    private long commandId;
    private String productName;
    private String price;
    private int quantity;
    private double totalPrice;

    public CommandDetail(long id, long commandId, String productName, String price, int quantity, double totalPrice) {
        this.id = id;
        this.commandId = commandId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getters
    public long getId() {
        return id;
    }

    public long getCommandId() {
        return commandId;
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
