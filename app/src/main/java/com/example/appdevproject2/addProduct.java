package com.example.appdevproject2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class addProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        EditText etName = findViewById(R.id.etProductName);
        EditText etQty = findViewById(R.id.etQuantity);
        EditText etPrice = findViewById(R.id.etPrice);
        Spinner spinnerCategory = findViewById(R.id.mySpinner);
        Button btnAdd = findViewById(R.id.btnSubmitProduct);

        String[] categories = {"Vegetables", "Fruits"};
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        findViewById(R.id.returnbtn).setOnClickListener(view -> {
            finish();
        });

        btnAdd.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            String qty = etQty.getText().toString().trim();
            String price = etPrice.getText().toString().trim();

            if (!name.isEmpty() && !qty.isEmpty() && !price.isEmpty()) {
                addProductToServer(name, category, qty, price);
            } else {
                Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProductToServer(String name, String category, String qty, String price) {
        String url = "http://10.0.2.2/cropcart/add_product.php";

        // Get user_id from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(addProduct.this, "Product Added Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(addProduct.this, "Error: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(addProduct.this, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("productName", name);
                params.put("category", category);
                params.put("Quantity", qty);
                params.put("price", price);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
