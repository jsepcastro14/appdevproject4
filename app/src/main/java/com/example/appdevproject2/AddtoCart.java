package com.example.appdevproject2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

<<<<<<< HEAD
        if (name != null) {
            cartList.add(new Product(name, category, qty, price));
        }

        CartProductAdapter adapter = new CartProductAdapter(CartManager.getCartItems());
        recyclerView.setAdapter(adapter);

        ImageButton refreshBtn = findViewById(R.id.refreshbtn);
        refreshBtn.setOnClickListener(v -> {
            CartProductAdapter newAdapter = new CartProductAdapter(CartManager.getCartItems());
            recyclerView.setAdapter(newAdapter);
            Toast.makeText(this, "Cart refreshed", Toast.LENGTH_SHORT).show();
        });

        // Return button logic
=======
>>>>>>> 6310549 (Connect app to XAMPP MySQL and implement ERD features)
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
                            cartList.add(new Product(
                                    obj.getInt("product_id"),
                                    obj.getInt("user_id"),
                                    obj.getString("productName"),
                                    obj.getString("category"),
                                    obj.getString("Quantity") + " pcs",
                                    "₱" + obj.getString("price")
                            ));
                        }
                        adapter = new CartProductAdapter(cartList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
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

<<<<<<< HEAD
            btnPayNow.setOnClickListener(v -> {
                String addressText = etAddress.getText().toString().trim();
                int selectedId = rgPayment.getCheckedRadioButtonId();

                if (addressText.isEmpty() || selectedId == -1) {
                    Toast.makeText(context, "Please complete details", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton rb = paymentView.findViewById(selectedId);
                String fullInfo = "Address: " + addressText + " | Method: " + rb.getText();

                // Loop sa lahat ng binili sa cart
                for (Product p : CartManager.getCartItems()) {
                    Product entry = new Product(p.getName(), fullInfo, p.getQuantity(), p.getPrice());
                    InventoryManager.addItem(entry);
                    OrderHistoryManager.addItem(entry);

                    // LOGIC: Hanapin ang product sa main list at markahan bilang sold
                    for (Product mainProd : ProductManager.getAllProducts()) {
                        if (mainProd.getName().equals(p.getName())) {
                            mainProd.setAvailable(false); // Dito ito mawawala sa Home list
                            break;
                        }
                    }
                }

                Toast.makeText(context, "Payment Successful!", Toast.LENGTH_SHORT).show();
                CartManager.clearCart();

                // I-update ang UI ng Cart (para maging empty)
                if (context instanceof AddtoCart) {
                    AddtoCart activity = (AddtoCart) context;
                    RecyclerView rv = activity.findViewById(R.id.cartRecyclerView);
                    CartProductAdapter emptyAdapter = new CartProductAdapter(CartManager.getCartItems());
                    rv.setAdapter(emptyAdapter);
                }

                paymentDialog.dismiss();
            });
=======
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
        
>>>>>>> 6310549 (Connect app to XAMPP MySQL and implement ERD features)
        paymentView.findViewById(R.id.returnbtn).setOnClickListener(v -> paymentDialog.dismiss());

        paymentDialog.setContentView(paymentView);
        paymentDialog.show();
    }

    private void placeOrderOnServer(Context context, String address, BottomSheetDialog dialog) {
        String url = "http://10.0.2.2/cropcart/place_order.php";
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(context, "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
                        cartList.clear();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        startActivity(new Intent(AddtoCart.this, OrderHistory.class));
                        finish();
                    } else {
                        Toast.makeText(context, "Error: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()) {
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
