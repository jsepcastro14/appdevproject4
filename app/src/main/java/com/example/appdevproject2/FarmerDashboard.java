package com.example.appdevproject2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FarmerDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farmer_dashboard);

        Button btnClickHere = findViewById(R.id.btnClickHere);

        btnClickHere.setOnClickListener(v -> {
            // Lipat sa AddProduct activity
            Intent intent = new Intent(FarmerDashboard.this, addProduct.class);
            startActivity(intent);
        });

        findViewById(R.id.returnbtn).setOnClickListener(v -> finish());
    }
}