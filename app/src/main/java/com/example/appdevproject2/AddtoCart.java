package com.example.appdevproject2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddtoCart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addto_cart);

        String name = getIntent().getStringExtra("prodName");
        String category = getIntent().getStringExtra("prodCategory");
        String qty = getIntent().getStringExtra("prodQuantity");
        String price = getIntent().getStringExtra("prodPrice");

        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dito mo ilalagay ang listahan ng items na nasa cart
        List<Product> cartList = new ArrayList<>();
        // Halimbawa: cartList.add(selectedProduct);

        if (name != null) {
            cartList.add(new Product(name, category, qty, price));
        }

        CartProductAdapter adapter = new CartProductAdapter(CartManager.getCartItems());
        recyclerView.setAdapter(adapter);

        // Return button logic
        findViewById(R.id.returnbtn).setOnClickListener(v -> finish());
    }
}