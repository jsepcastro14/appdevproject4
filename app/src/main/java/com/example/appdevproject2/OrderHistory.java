package com.example.appdevproject2;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Setup Return Button
        ImageButton returnBtn = findViewById(R.id.returnbtn);
        returnBtn.setOnClickListener(v -> finish());

        // Setup RecyclerView para sa History
        RecyclerView rv = findViewById(R.id.inventoryRecyclerView); // Siguraduhin na ito ang ID sa XML mo
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Gamitin ang bagong OrderHistoryAdapter
        OrderHistoryAdapter adapter = new OrderHistoryAdapter(OrderHistoryManager.getItems());
        rv.setAdapter(adapter);
    }
}