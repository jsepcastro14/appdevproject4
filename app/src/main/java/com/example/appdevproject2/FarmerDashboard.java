package com.example.appdevproject2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FarmerDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farmer_dashboard);

        // 1. I-initialize ang mga TextViews para sa counters
        TextView tvTotalProducts = findViewById(R.id.tvTotalProductsCount);
        TextView tvSuccessfulOrders = findViewById(R.id.tvSuccessfulOrdersCount);

        // 2. Kunin ang size ng lists mula sa Managers
        int totalProducts = ProductManager.getAllProducts().size();
        int successfulSales = SuccessfulOrderManager.getSuccessfulOrders().size();

        // 3. I-set ang text sa UI
        tvTotalProducts.setText(String.valueOf(totalProducts));
        tvSuccessfulOrders.setText(String.valueOf(successfulSales));

        // --- Existing code mo sa ibaba ---
        Button btnClickHere = findViewById(R.id.btnClickHere);
        RecyclerView rv = findViewById(R.id.ordersRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        SuccesfulOrdersAdapter adapter = new SuccesfulOrdersAdapter(SuccessfulOrderManager.getSuccessfulOrders());
        rv.setAdapter(adapter);

        btnClickHere.setOnClickListener(v -> {
            Intent intent = new Intent(FarmerDashboard.this, addProduct.class);
            startActivity(intent);
        });

        // Sa loob ng onCreate
        ImageButton refreshBtn = findViewById(R.id.refreshbtn);
        refreshBtn.setOnClickListener(v -> {
            // 1. I-update ang mga Counters
            tvTotalProducts.setText(String.valueOf(ProductManager.getAllProducts().size()));
            tvSuccessfulOrders.setText(String.valueOf(SuccessfulOrderManager.getSuccessfulOrders().size()));

            // 2. I-update ang RecyclerView
            SuccesfulOrdersAdapter newAdapter = new SuccesfulOrdersAdapter(SuccessfulOrderManager.getSuccessfulOrders());
            rv.setAdapter(newAdapter);

            Toast.makeText(this, "Dashboard updated", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.returnbtn).setOnClickListener(v -> finish());
    }

    // Opsyonal: I-refresh ang numbers kapag bumalik ang user sa dashboard
    @Override
    protected void onResume() {
        super.onResume();
        TextView tvTotalProducts = findViewById(R.id.tvTotalProductsCount);
        TextView tvSuccessfulOrders = findViewById(R.id.tvSuccessfulOrdersCount);

        tvTotalProducts.setText(String.valueOf(ProductManager.getAllProducts().size()));
        tvSuccessfulOrders.setText(String.valueOf(SuccessfulOrderManager.getSuccessfulOrders().size()));
    }
}