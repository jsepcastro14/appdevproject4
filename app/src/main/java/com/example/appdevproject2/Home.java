package com.example.appdevproject2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner mySpinner;
    private ProductAdapter adapter;
    private List<Product> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // UI Initialization
        recyclerView = findViewById(R.id.shoppingList);
        mySpinner = findViewById(R.id.mySpinner); // Mula sa activity_home.xml
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 1. Setup Spinner Options
        String[] categories = {"All", "Vegetables", "Fruits"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(spinnerAdapter);

        // 2. Spinner Listener para sa Filtering
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories[position];
                updateUI(selectedCategory); // Tawagin ang refresh na may filter
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Navigation Buttons
        setupNavigation();
    }

    private void setupNavigation() {
        findViewById(R.id.nav_settings).setOnClickListener(v -> {
            startActivity(new Intent(Home.this, Settings.class));
        });

        findViewById(R.id.nav_cart).setOnClickListener(v -> {
            startActivity(new Intent(Home.this, AddtoCart.class));
        });

        findViewById(R.id.nav_orderhistory).setOnClickListener(v -> {
            startActivity(new Intent(Home.this, OrderHistory.class));
        });

        // Refresh button logic
        findViewById(R.id.returnbtn).setOnClickListener(v -> updateUI(mySpinner.getSelectedItem().toString()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // I-refresh ang listahan base sa kasalukuyang pili sa spinner
        updateUI(mySpinner.getSelectedItem().toString());
    }

    // 3. Inayos na updateUI method
    private void updateUI(String category) {
        List<Product> allProducts = ProductManager.getAllProducts();
        filteredList.clear();

        if (category.equals("All")) {
            filteredList.addAll(allProducts);
        } else {
            // I-filter ang mga products base sa category string
            for (Product p : allProducts) {
                if (p.getCategory().equalsIgnoreCase(category)) {
                    filteredList.add(p);
                }
            }
        }

        // I-set ang adapter gamit ang filtered list
        adapter = new ProductAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }
}