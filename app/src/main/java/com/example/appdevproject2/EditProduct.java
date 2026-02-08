package com.example.appdevproject2;

import android.os.Bundle;
import android.widget.ArrayAdapter; // Idagdag ito
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner; // Idagdag ito
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class EditProduct extends AppCompatActivity {

    private EditText etName, etQuantity, etPrice;
    private Spinner mySpinner;
    private ImageButton returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // I-link ang mga Views
        etName = findViewById(R.id.etProductName);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        mySpinner = findViewById(R.id.mySpinner); // Mula sa xml mo
        returnBtn = findViewById(R.id.returnbtn);

        // 1. Gawa ng listahan ng categories
        List<String> categories = new ArrayList<>();
        categories.add("Fruits");
        categories.add("Vegetables");

        // 2. I-setup ang ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 3. I-apply ang adapter sa spinner
        mySpinner.setAdapter(adapter);

        // Kunin ang data mula sa Intent (mula sa ProductAdapter)
        String name = getIntent().getStringExtra("PRODUCT_NAME");
        String qty = getIntent().getStringExtra("PRODUCT_QUANTITY");
        String price = getIntent().getStringExtra("PRODUCT_PRICE");
        String currentCategory = getIntent().getStringExtra("PRODUCT_CATEGORY");

        // I-set ang data sa UI
        if (name != null) etName.setText(name);
        if (qty != null) etQuantity.setText(qty);
        if (price != null){
            // Tatanggalin ang ₱ at comma para numero lang ang maiwan sa EditText
            String cleanPrice = price.replaceAll("[^0-9.]", "");
            etPrice.setText(cleanPrice);
        }

        // 4. I-set ang Spinner sa tamang category base sa product data
        if (currentCategory != null) {
            int spinnerPosition = adapter.getPosition(currentCategory);
            mySpinner.setSelection(spinnerPosition);
        }
        Button btnApplyChanges = findViewById(R.id.btnApplyChanges);
        int productId = getIntent().getIntExtra("PRODUCT_ID", -1); // Kunin ang ID

        btnApplyChanges.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            String newQty = etQuantity.getText().toString().trim();
            String newPrice = etPrice.getText().toString().trim();
            String newCategory = mySpinner.getSelectedItem().toString();

            if (newName.isEmpty() || newQty.isEmpty() || newPrice.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            updateProductInDatabase(productId, newName, newQty, newPrice, newCategory);
        });

        returnBtn.setOnClickListener(v -> finish());
    }

    private void updateProductInDatabase(int id, String name, String qty, String price, String category) {
        String url = "http://10.0.2.2/cropcart/update_product.php"; // Palitan ang IP kung real device

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equalsIgnoreCase("success")) {
                        Toast.makeText(this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Babalik sa main screen pagkatapos ma-update
                    } else {
                        Toast.makeText(this, "Update failed: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", String.valueOf(id));
                params.put("product_name", name);
                params.put("category", category);
                params.put("quantity", qty);
                params.put("price", price);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}