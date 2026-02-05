package com.example.appdevproject2;

public class Product {
    private int productId;
    private int userId;
    private String name, category, quantity, price;
    private boolean isAvailable = true;

    public Product(int productId, int userId, String name, String category, String quantity, String price) {
        this.productId = productId;
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.isAvailable = true;
    }

    public int getProductId() { return productId; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getQuantity() { return quantity; }
    public String getPrice() { return price; }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
