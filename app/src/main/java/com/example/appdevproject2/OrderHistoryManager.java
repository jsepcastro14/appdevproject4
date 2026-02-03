package com.example.appdevproject2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderHistoryManager {
    private static final List<Product> items = Collections.synchronizedList(new ArrayList<>());

    // Huwag nang magbalik ng bagong ArrayList para direct ang reference,
    // O kaya gumamit ng remove method. Mas safe ang remove method:
    public static List<Product> getItems() {
        return items; // Direct reference para sync ang Adapter at Manager
    }

    public static void addItem(Product p) {
        if (p != null) {
            items.add(p);
        }
    }

    // Dagdagan ito para safe ang pag-delete
    public static void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }
}