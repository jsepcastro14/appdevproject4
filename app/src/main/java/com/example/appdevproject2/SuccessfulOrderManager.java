package com.example.appdevproject2;

import java.util.ArrayList;
import java.util.List;

public class SuccessfulOrderManager {
    private static List<Product> successfulList = new ArrayList<>();

    public static List<Product> getSuccessfulOrders() {
        return successfulList;
    }

    public static void addOrder(Product product) {
        successfulList.add(product);
    }
}