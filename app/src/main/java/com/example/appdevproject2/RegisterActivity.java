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
<<<<<<< HEAD
}
=======

    private void registerUser(String firstName, String lastName, String email, String password, String mobileNum) {
        String url = "http://10.0.2.2/cropcart/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(RegisterActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("email", email);
                params.put("password", password);
                params.put("mobileNo", mobileNum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
>>>>>>> 6310549 (Connect app to XAMPP MySQL and implement ERD features)
