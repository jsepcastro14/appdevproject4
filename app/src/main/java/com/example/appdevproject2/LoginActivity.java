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
<<<<<<< HEAD
        // ... rest of your code (ViewCompat listeners)
=======
    }

    private void loginUser(String email, String password) {
        String url = "http://10.0.2.2/cropcart/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if (status.equals("success")) {
                            JSONObject user = jsonObject.getJSONObject("user");

                            // Save user details to SharedPreferences - matching new ERD column names
                            SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("userId", user.getInt("user_id"));
                            editor.putString("firstName", user.getString("firstName"));
                            editor.putString("lastName", user.getString("lastName"));
                            editor.putString("email", user.getString("email"));
                            editor.putString("mobileNo", user.getString("mobileNo"));
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, Home.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(LoginActivity.this, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
>>>>>>> 6310549 (Connect app to XAMPP MySQL and implement ERD features)
    }
}
