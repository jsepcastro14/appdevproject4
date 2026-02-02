package com.example.appdevproject2;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private static List<Product> items = new ArrayList<>();
    public static List<Product> getItems() { return items; }
    public static void addItem(Product p) { items.add(p); }
}
