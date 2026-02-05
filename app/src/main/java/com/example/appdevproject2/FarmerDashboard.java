package com.example.appdevproject2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.ImageButton;
import android.widget.TextView;
=======
>>>>>>> 6310549 (Connect app to XAMPP MySQL and implement ERD features)
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FarmerDashboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SuccesfulOrdersAdapter adapter;
    private List<Product> successfulOrdersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farmer_dashboard);

<<<<<<< HEAD
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
=======
        Button btnClickHere = findViewById(R.id.btnClickHere);
        recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchSuccessfulOrders();
>>>>>>> 6310549 (Connect app to XAMPP MySQL and implement ERD features)

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

<<<<<<< HEAD
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
=======
    private void fetchSuccessfulOrders() {
        String url = "http://10.0.2.2/cropcart/get_successful_orders.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        successfulOrdersList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            
                            // Map database columns to Product object
                            Product p = new Product(
                                    obj.getInt("product_id"),
                                    obj.getInt("user_id"),
                                    obj.getString("productName"),
                                    "Rate: " + obj.getString("rate") + "/5",
                                    obj.getString("address"), // We show address in quantity temporarily or similar
                                    "Confirmed" // Placeholder for price
                            );
                            successfulOrdersList.add(p);
                        }
                        adapter = new SuccesfulOrdersAdapter(successfulOrdersList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error fetching successful orders", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
>>>>>>> 6310549 (Connect app to XAMPP MySQL and implement ERD features)
