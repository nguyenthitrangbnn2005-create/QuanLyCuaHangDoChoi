package com.example.baitaplon;

public class Order {

    int    id;
    String username;
    String productName;
    int    quantity;
    double total;
    String status;
    String orderTime;

    public Order(int id, String username, String productName,
                 int quantity, double total, String status, String orderTime) {
        this.id          = id;
        this.username    = username;
        this.productName = productName;
        this.quantity    = quantity;
        this.total       = total;
        this.status      = status;
        this.orderTime   = orderTime;
    }

    // Constructor cũ giữ tương thích
    public Order(String username, String productName, int quantity, double total) {
        this(-1, username, productName, quantity, total, "Chờ xử lý", "");
    }

    public int    getId()          { return id; }
    public String getUsername()    { return username; }
    public String getProductName() { return productName; }
    public int    getQuantity()    { return quantity; }
    public double getTotal()       { return total; }
    public String getStatus()      { return status; }
    public String getOrderTime()   { return orderTime; }
}
