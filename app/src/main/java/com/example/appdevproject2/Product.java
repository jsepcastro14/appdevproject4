package com.example.appdevproject2;

public class Product {
<<<<<<< HEAD
    String name, category, quantity, price;
    // Idagdag itong variable para ma-track kung available pa ang product
    private boolean isAvailable = true;

    public Product(String name, String category, String quantity, String price) {
=======
    private int productId;
    private int userId;
    private String name, category, quantity, price;

    public Product(int productId, int userId, String name, String category, String quantity, String price) {
        this.productId = productId;
        this.userId = userId;
>>>>>>> 6310549 (Connect app to XAMPP MySQL and implement ERD features)
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.isAvailable = true; // Default na true kapag ginawa ang product
    }

    public int getProductId() { return productId; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getQuantity() { return quantity; }
    public String getPrice() { return price; }

    // Getter para malaman kung available pa
    public boolean isAvailable() {
        return isAvailable;
    }

    // Setter para palitan ang status (hal. gawing false pagkatapos i-checkout)
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
