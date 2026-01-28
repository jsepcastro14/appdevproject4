package com.example.appdevproject2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_profile_settings);

        // Maaari mo ring lagyan ng return logic ang returnbtn dito
        findViewById(R.id.returnbtn).setOnClickListener(v -> finish());

        TextView tvInventory = findViewById(R.id.btn_inventory);
        tvInventory.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, Inventory.class);
            startActivity(intent);
        });

        TextView tvAccountSettings = findViewById(R.id.btn_accountsettings);
        tvAccountSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, AccountSettings.class);
            startActivity(intent);
        });

        // 3. Sign Off Navigation
        TextView tvSignOff = findViewById(R.id.btn_signoff);
        tvSignOff.setOnClickListener(v -> {
            // Babalik sa LoginActivity at lilinisin ang history
            Intent intent = new Intent(Settings.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.returnbtn).setOnClickListener(v -> finish());

        TextView btnFarmerDash = findViewById(R.id.tvGoToFarmerDashboard);

        // 2. Set ang Click Listener
        btnFarmerDash.setOnClickListener(v -> {
            // Intent para lumipat mula Settings patungong FarmerDashboard class
            Intent intent = new Intent(Settings.this, FarmerDashboard.class);
            startActivity(intent);
        });


        ImageButton navCart = findViewById(R.id.nav_cart);
        navCart.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, AddtoCart.class);
            startActivity(intent);
        });

        ImageButton navOrderHistory = findViewById(R.id.nav_orderhistory);
        navOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, OrderHistory.class);
            startActivity(intent);
        });

        ImageButton navHome = findViewById(R.id.nav_home);
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, Home.class);
            startActivity(intent);
        });
    }
}