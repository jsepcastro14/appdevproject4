package com.example.appdevproject2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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

        recyclerView = findViewById(R.id.shoppingList);
        mySpinner = findViewById(R.id.mySpinner);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] categories = {"All", "Vegetables", "Fruits"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(spinnerAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories[position];
                fetchProductsFromServer(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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

        // Corrected ID to match activity_home.xml
        findViewById(R.id.refreshbtn).setOnClickListener(v -> fetchProductsFromServer(mySpinner.getSelectedItem().toString()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProductsFromServer(mySpinner.getSelectedItem().toString());
    }

    private void fetchProductsFromServer(String category) {
        String url = "http://10.0.2.2/cropcart/get_products.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        filteredList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Product product = new Product(
                                    jsonObject.getInt("product_id"),
                                    jsonObject.getInt("user_id"),
                                    jsonObject.getString("productName"),
                                    jsonObject.getString("category"),
                                    jsonObject.getString("Quantity") + " pcs",
                                    "₱" + jsonObject.getString("price")
                            );

                            if (category.equals("All") || product.getCategory().equalsIgnoreCase(category)) {
                                filteredList.add(product);
                            }
                        }

                        adapter = new ProductAdapter(filteredList);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        Toast.makeText(Home.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(Home.this, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
