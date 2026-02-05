package com.example.appdevproject2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView tvLoginRedirect = findViewById(R.id.tvLoginRedirect);

        // Ang string na gagamit ng HTML tags para sa blue at underline
        String text = "Have an account? <font color='#0000FF'><u>Log in</u></font>";

        // I-set ang formatted text
        tvLoginRedirect.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));

        // Click listener para bumalik sa Login screen
        tvLoginRedirect.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        Button btnSignUp = findViewById(R.id.btnSignUp);

        // 2. Set ang Click Listener para sa navigation
        btnSignUp.setOnClickListener(v -> {
            // 3. Intent para lumipat sa LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);

            finish();
        });
    }
}