package com.example.appdevproject2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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
    private TextView tvTotalProducts, tvSuccessfulOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farmer_dashboard);

        // Initialize UI Counters
        tvTotalProducts = findViewById(R.id.tvTotalProductsCount);
        tvSuccessfulOrders = findViewById(R.id.tvSuccessfulOrdersCount);

        Button btnClickHere = findViewById(R.id.btnClickHere);
        recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchSuccessfulOrders();

        btnClickHere.setOnClickListener(v -> {
            Intent intent = new Intent(FarmerDashboard.this, addProduct.class);
            startActivity(intent);
        });

        // Refresh button logic
        ImageButton refreshBtn = findViewById(R.id.refreshbtn);
        refreshBtn.setOnClickListener(v -> {
            fetchSuccessfulOrders();
            Toast.makeText(this, "Dashboard updated", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.returnbtn).setOnClickListener(v -> finish());
    }

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
                                    obj.getString("address"), 
                                    "Confirmed" 
                            );
                            successfulOrdersList.add(p);
                        }
                        adapter = new SuccesfulOrdersAdapter(successfulOrdersList);
                        recyclerView.setAdapter(adapter);

                        // Update counters based on fetched data
                        if (tvSuccessfulOrders != null) {
                            tvSuccessfulOrders.setText(String.valueOf(successfulOrdersList.size()));
                        }
                        // Note: Total products counter would ideally need another fetch from tblproduct
                        
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error fetching successful orders", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
