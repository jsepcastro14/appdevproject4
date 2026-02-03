package com.example.appdevproject2;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private static List<Product> allProducts = new ArrayList<>();

    // Static block: Tatakbo ito pagbukas ng app


    public static List<Product> getAllProducts() {
        return allProducts;
    }

    public static void addProduct(Product product) {
        allProducts.add(product);
    }
}