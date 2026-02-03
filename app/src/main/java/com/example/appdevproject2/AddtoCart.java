package com.example.appdevproject2;

import android.content.Context;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class AddtoCart extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addto_cart);
        Button checkoutBTN = findViewById(R.id.checkoutBTN);

        String name = getIntent().getStringExtra("prodName");
        String category = getIntent().getStringExtra("prodCategory");
        String qty = getIntent().getStringExtra("prodQuantity");
        String price = getIntent().getStringExtra("prodPrice");

        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dito mo ilalagay ang listahan ng items na nasa cart
        List<Product> cartList = new ArrayList<>();
        // Halimbawa: cartList.add(selectedProduct);

        if (name != null) {
            cartList.add(new Product(name, category, qty, price));
        }

        CartProductAdapter adapter = new CartProductAdapter(CartManager.getCartItems());
        recyclerView.setAdapter(adapter);

        // Return button logic
        checkoutBTN.setOnClickListener(v -> {
            double grandTotal = 0;

            // Loop sa lahat ng items sa cart para makuha ang kabuuang presyo
            for (Product p : CartManager.getCartItems()) {
                // Linisin ang string (₱500.00 -> 500.00) bago i-parse sa double
                String cleanPrice = p.getPrice().replaceAll("[^0-9.]", "");
                grandTotal += Double.parseDouble(cleanPrice);
            }

            String finalGrandTotal = String.format("₱%.2f", grandTotal);
            showPaymentMethodDialog(this, finalGrandTotal, cartList.get(0));
        });

        findViewById(R.id.returnbtn).setOnClickListener(v -> finish());
    }

        private void showPaymentMethodDialog(Context context, String totalAmount, Product currentProduct) {
        BottomSheetDialog paymentDialog = new BottomSheetDialog(context);
        View paymentView = LayoutInflater.from(context).inflate(R.layout.activity_payment_method, null);

        RadioGroup rgPayment = paymentView.findViewById(R.id.rgPaymentMethods);
        Button btnPayNow = paymentView.findViewById(R.id.btnBuyNow);
        TextView tvTotal = paymentView.findViewById(R.id.dialogProductPrice);
        EditText etAddress = paymentView.findViewById(R.id.etShippingAddress);

        tvTotal.setText(totalAmount);

            btnPayNow.setOnClickListener(v -> {
                // 1. Kunin ang Address at RadioButton Selection
                String addressText = etAddress.getText().toString().trim();
                RadioButton rb = paymentView.findViewById(rgPayment.getCheckedRadioButtonId());
                String fullInfo = "Address: " + addressText + " | Method: " + rb.getText();

                // 2. I-save sa parehong Managers
                if (context instanceof AddtoCart) {
                    for (Product p : CartManager.getCartItems()) {
                        // Gumagawa tayo ng bagong Product object para hindi ma-affect ang original reference
                        Product entry = new Product(p.getName(), fullInfo, p.getQuantity(), p.getPrice());
                        InventoryManager.addItem(entry);
                        OrderHistoryManager.addItem(entry);
                    }
                } else {
                    Product entry = new Product(currentProduct.getName(), fullInfo, currentProduct.getQuantity(), currentProduct.getPrice());
                    InventoryManager.addItem(entry);
                    OrderHistoryManager.addItem(entry);
                }

            Toast.makeText(context, "Payment Successful!", Toast.LENGTH_SHORT).show();

                CartManager.clearCart();

                if (context instanceof AddtoCart) {
                    AddtoCart activity = (AddtoCart) context;
                    // Gumawa ng bagong adapter na walang laman o i-notify ang existing adapter
                    RecyclerView rv = activity.findViewById(R.id.cartRecyclerView);
                    CartProductAdapter emptyAdapter = new CartProductAdapter(CartManager.getCartItems());
                    rv.setAdapter(emptyAdapter);
                }

            paymentDialog.dismiss();
        });
        paymentView.findViewById(R.id.returnbtn).setOnClickListener(v -> paymentDialog.dismiss());

        paymentDialog.setContentView(paymentView);
        paymentDialog.show();
    }
}