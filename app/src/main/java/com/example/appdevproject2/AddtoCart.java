package com.example.appdevproject2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddtoCart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Product> cartList = new ArrayList<>();
    private CartProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addto_cart);
        Button checkoutBTN = findViewById(R.id.checkoutBTN);

        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchCartFromServer();

        checkoutBTN.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            double grandTotal = 0;
            for (Product p : cartList) {
                String cleanPrice = p.getPrice().replaceAll("[^0-9.]", "");
                grandTotal += Double.parseDouble(cleanPrice);
            }

            String finalGrandTotal = String.format("₱%.2f", grandTotal);
            showPaymentMethodDialog(this, finalGrandTotal);
        });

        findViewById(R.id.returnbtn).setOnClickListener(v -> finish());
    }

    // Sa loob ng AddtoCart.java
    private void fetchCartFromServer() {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        String url = "http://10.0.2.2/cropcart/get_cart.php?user_id=" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        cartList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            // --- UPDATED CONSTRUCTOR ---
                            // Siguraduhing ang get_cart.php mo ay naglalabas ng "cart_id"
                            cartList.add(new Product(
                                    obj.getInt("cart_id"), // 1. Dito natin ilalagay ang cart_id
                                    obj.getInt("product_id"),
                                    obj.getInt("user_id"),
                                    obj.getString("productName"),
                                    obj.getString("category"),
                                    obj.getString("Quantity") + " pcs",
                                    "₱" + obj.getString("price"),
                                    0 // imageResourceId (default/no image)
                            ));
                        }
                        // Gagamitin na natin ang CartProductAdapter na in-update
                        adapter = new CartProductAdapter(cartList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Parsing Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error fetching cart", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }


    private void showPaymentMethodDialog(Context context, String totalAmount) {
        BottomSheetDialog paymentDialog = new BottomSheetDialog(context);
        View paymentView = LayoutInflater.from(context).inflate(R.layout.activity_payment_method, null);

        RadioGroup rgPayment = paymentView.findViewById(R.id.rgPaymentMethods);
        Button btnPayNow = paymentView.findViewById(R.id.btnBuyNow);
        TextView tvTotal = paymentView.findViewById(R.id.dialogProductPrice);
        EditText etAddress = paymentView.findViewById(R.id.etShippingAddress);

        tvTotal.setText(totalAmount);

        btnPayNow.setOnClickListener(v -> {
            String addressText = etAddress.getText().toString().trim();
            if (addressText.isEmpty()) {
                Toast.makeText(context, "Please enter shipping address", Toast.LENGTH_SHORT).show();
                return;
            }

            int checkedId = rgPayment.getCheckedRadioButtonId();
            if (checkedId == -1) {
                Toast.makeText(context, "Please select payment method", Toast.LENGTH_SHORT).show();
                return;
            }
            
            RadioButton rb = paymentView.findViewById(checkedId);
            String fullInfo = addressText + " (" + rb.getText() + ")";

            placeOrderOnServer(context, fullInfo, paymentDialog);
        });
        
        paymentView.findViewById(R.id.returnbtn).setOnClickListener(v -> paymentDialog.dismiss());

        paymentDialog.setContentView(paymentView);
        paymentDialog.show();
    }

    private void placeOrderOnServer(Context context, String address, BottomSheetDialog dialog) {
        String url = "http://10.0.2.2/cropcart/place_order_from_cart.php";
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(context, "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        // Mag-redirect sa Home.java at i-clear ang back stack
                        // para ma-refresh ang product list.
                        Intent intent = new Intent(AddtoCart.this, Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // Isara ang AddtoCart activity

                    } else {
                        Toast.makeText(context, "Checkout Error: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, "Network Error on checkout", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("address", address);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
