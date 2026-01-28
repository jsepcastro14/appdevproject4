package com.example.appdevproject2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.shoppingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample Data
        List<Product> list = new ArrayList<>();
        list.add(new Product("Carrots", "Vegetables", "10kg", "₱500.00"));
        list.add(new Product("Rice", "Grains", "50kg", "₱2,500.00"));

        ProductAdapter adapter = new ProductAdapter(list);
        recyclerView.setAdapter(adapter);

        // Sa loob ng onCreate method ng Home.java
        ImageButton navSettings = findViewById(R.id.nav_settings);

        navSettings.setOnClickListener(v -> {
            // Intent para lumipat mula Home patungong Settings
            Intent intent = new Intent(Home.this, Settings.class);
            startActivity(intent);
        });

        ImageButton navCart = findViewById(R.id.nav_cart);
        navCart.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, AddtoCart.class);
            startActivity(intent);
        });

        ImageButton navOrderHistory = findViewById(R.id.nav_orderhistory);
        navOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, OrderHistory.class);
            startActivity(intent);
        });

    }
}