package com.example.appdevproject2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class addProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);


        // Mapping ng IDs mula sa activity_add_product.xml
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

        // FIX DITO: Ginawa nating 'view' para hindi mag-error
        findViewById(R.id.returnbtn).setOnClickListener(view -> {
            finish();
        });

        btnAdd.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            String qty = etQty.getText().toString().trim() + " pcs";
            String price = "₱" + etPrice.getText().toString().trim();

            if (!name.isEmpty() && !etQty.getText().toString().isEmpty()) {
                // I-save sa ProductManager
                ProductManager.addProduct(new Product(name, category, qty, price));

                Toast.makeText(this, "Product Added Successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Isasara ang activity at babalik sa Home
            } else {
                Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


