package com.example.appdevproject2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
<<<<<<< HEAD
import android.widget.ImageButton; // Siguraduhin na i-import ito
=======
import android.widget.ImageButton;
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

public class Inventory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private List<Product> inventoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory);

        recyclerView = findViewById(R.id.inventoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.returnbtn).setOnClickListener(v -> finish());
        
        // Refresh button logic
        findViewById(R.id.refreshbtn).setOnClickListener(v -> fetchInventory());

<<<<<<< HEAD
        returnBtn.setOnClickListener(v -> {
            finish();
        });

        ImageButton refreshBtn = findViewById(R.id.refreshbtn);
        refreshBtn.setOnClickListener(v -> {
            InventoryAdapter newAdapter = new InventoryAdapter(InventoryManager.getItems());
            rv.setAdapter(newAdapter);
            Toast.makeText(this, "Inventory updated", Toast.LENGTH_SHORT).show();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
=======
        fetchInventory();
>>>>>>> 6310549 (Connect app to XAMPP MySQL and implement ERD features)
    }

    private void fetchInventory() {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/cropcart/get_inventory.php?user_id=" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        inventoryList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            
                            Product p = new Product(
                                    obj.getInt("product_id"),
                                    obj.getInt("user_id"),
                                    obj.getString("productName"),
                                    obj.getString("category"),
                                    obj.getString("Quantity") + " pcs",
                                    "₱" + obj.getString("price")
                            );
                            inventoryList.add(p);
                        }
                        adapter = new InventoryAdapter(inventoryList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error fetching inventory", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
