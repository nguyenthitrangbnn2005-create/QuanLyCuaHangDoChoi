package com.example.baitaplon;

public class Product {

    int id;
    String productCode;
    String productName;
    String category;
    int quantity;
    double price;

    public Product(int id,
                   String productCode,
                   String productName,
                   String category,
                   int quantity,
                   double price) {

        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}