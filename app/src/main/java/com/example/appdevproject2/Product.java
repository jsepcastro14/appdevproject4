package com.example.appdevproject2;

public class Product {
    private int orderId;
    private int productId;
    private int userId;
    private String name, category, quantity, price;

    private boolean isAvailable = true;
    private int imageResourceId; // <-- BAGONG FIELD NA IDINAGDAG

    // Constructor na may imageResourceId
    public Product(int orderId, int productId, int userId, String name, String category, String quantity, String price, int imageResourceId) {
        this.orderId = orderId;
        this.productId = productId;
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.isAvailable = true;
        this.imageResourceId = imageResourceId; // <-- I-INITIALIZE ANG BAGONG FIELD
    }

    // Overloaded na constructor para sa backward compatibility (kung may code ka na gumagamit ng lumang constructor)
    public Product(int orderId, int productId, int userId, String name, String category, String quantity, String price) {
        this(orderId, productId, userId, name, category, quantity, price, 0); // 0 as default/no image
    }
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    // Getters
    public int getProductId() { return productId; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getQuantity() { return quantity; }
    public String getPrice() { return price; }
    public int getImageResourceId() { return imageResourceId; } // <-- GETTER PARA SA IMAGE

    // Setters at iba pang methods
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
