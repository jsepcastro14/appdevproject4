package com.example.appdevproject2;

import android.content.Intent;
import android.graphics.Paint; // Idagdag ito para sa underline
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.text.Html;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // 1. Hanapin ang TextView
        TextView tvCreateAccount = findViewById(R.id.tvCreateAccount);

        // Dito natin ise-set na yung "Create Account" lang ang may style
        String text = "New here? <font color='#0000FF'><u>Create Account</u></font>";
        tvCreateAccount.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));

        // Navigation logic papuntang RegisterActivity
        tvCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        Button btnLogIn = findViewById(R.id.btnLogIn);

        // 2. Click listener para sa navigation
        btnLogIn.setOnClickListener(v -> {
            // 3. Intent para lumipat sa Home activity
            Intent intent = new Intent(LoginActivity.this, Home.class);
            startActivity(intent);

            finish();
        });
        // ... rest of your code (ViewCompat listeners)
    }
}