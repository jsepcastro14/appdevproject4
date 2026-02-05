package com.example.appdevproject2;

import android.os.Bundle;
import android.widget.ImageButton; // Siguraduhin na i-import ito
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);

        ImageButton returnBtn = findViewById(R.id.returnbtn);
        // Sa loob ng onCreate
        RecyclerView rv = findViewById(R.id.orderHistoryRecyclerView); // Siguraduhin na may ID sa XML
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Gumamit ng ProductAdapter pero gamit ang InventoryManager list
        OrderHistoryadapter adapter = new OrderHistoryadapter(OrderHistoryManager.getItems());
        rv.setAdapter(adapter);

        returnBtn.setOnClickListener(v -> {
            finish();
        });

        ImageButton refreshBtn = findViewById(R.id.refreshbtn);
        refreshBtn.setOnClickListener(v -> {
            OrderHistoryadapter newAdapter = new OrderHistoryadapter(OrderHistoryManager.getItems());
            rv.setAdapter(newAdapter);
            Toast.makeText(this, "Order history updated", Toast.LENGTH_SHORT).show();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}