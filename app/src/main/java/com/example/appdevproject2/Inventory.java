package com.example.appdevproject2;

import android.os.Bundle;
import android.widget.ImageButton; // Siguraduhin na i-import ito

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Inventory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory);

        ImageButton returnBtn = findViewById(R.id.returnbtn);
        // Sa loob ng onCreate
        RecyclerView rv = findViewById(R.id.inventoryRecyclerView); // Siguraduhin na may ID sa XML
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Gumamit ng ProductAdapter pero gamit ang InventoryManager list
        InventoryAdapter adapter = new InventoryAdapter(InventoryManager.getItems());
        rv.setAdapter(adapter);

        returnBtn.setOnClickListener(v -> {
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}