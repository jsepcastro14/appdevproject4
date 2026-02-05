package com.example.appdevproject2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
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

public class OrderHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderHistoryadapter adapter;
    private List<Product> historyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.orderHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.returnbtn).setOnClickListener(v -> finish());
        
        // Corrected ID: refreshbtn (lowercase) matching your XML
        findViewById(R.id.refreshbtn).setOnClickListener(v -> fetchOrderHistory());

        fetchOrderHistory();
    }

    private void fetchOrderHistory() {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/cropcart/get_order_history.php?user_id=" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        historyList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            
                            // Map database columns to Product object
                            Product p = new Product(
                                    obj.getInt("product_id"),
                                    obj.getInt("user_id"),
                                    obj.getString("productName"),
                                    obj.getString("address"), // We show address in the category field for history
                                    obj.getString("Quantity") + " pcs",
                                    "₱" + obj.getString("price")
                            );
                            historyList.add(p);
                        }
                        adapter = new OrderHistoryadapter(historyList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
