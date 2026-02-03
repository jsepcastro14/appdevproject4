package com.example.appdevproject2;

public class Product {
    String name, category, quantity, price;
    public Product(String name, String category, String quantity, String price) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getQuantity() { return quantity; }
    public String getPrice() { return price; }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}


