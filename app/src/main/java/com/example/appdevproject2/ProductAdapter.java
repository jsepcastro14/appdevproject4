package com.example.appdevproject2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shoppinglist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.category.setText(product.getCategory());
        holder.quantity.setText(product.getQuantity());
        holder.price.setText(product.getPrice());

        holder.itemView.setOnClickListener(v -> {
            showPurchaseDialog(v.getContext(), product);
        });

        if (holder.itemView.getContext() instanceof AddtoCart) {
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }

        holder.btnDelete.setOnClickListener(v -> {
            int actualPosition = holder.getAdapterPosition();
            if (actualPosition != RecyclerView.NO_POSITION) {
                productList.remove(actualPosition);
                notifyItemRemoved(actualPosition);
                notifyItemRangeChanged(actualPosition, productList.size());
            }
        });
    }

    @Override
    public int getItemCount() { return productList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, category, quantity, price, btnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            category = itemView.findViewById(R.id.product_category);
            quantity = itemView.findViewById(R.id.product_quantity);
            price = itemView.findViewById(R.id.product_price);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private void showPurchaseDialog(Context context, Product product) {
        com.google.android.material.bottomsheet.BottomSheetDialog bottomSheetDialog =
                new com.google.android.material.bottomsheet.BottomSheetDialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.activity_purchaseorbuy, null);

        TextView name = view.findViewById(R.id.dialogProductName);
        TextView category = view.findViewById(R.id.dialogProductCategory);
        TextView price = view.findViewById(R.id.dialogProductPrice);
        TextView totalPrice = view.findViewById(R.id.tvTotalPrice);
        ImageButton returnBtn = view.findViewById(R.id.returnbtn);

        TextView tvQty = view.findViewById(R.id.tvQuantity);
        ImageButton btnPlus = view.findViewById(R.id.plus);
        ImageButton btnMinus = view.findViewById(R.id.minus);
        Button btnAddToCart = view.findViewById(R.id.btnAddToCart);
        Button btnBuyNow = view.findViewById(R.id.btnBuyNow);

        name.setText(product.getName());
        category.setText(product.getCategory());
        price.setText(product.getPrice());
        totalPrice.setText("Total: " + product.getPrice());

        String cleanPrice = product.getPrice().replaceAll("[^0-9.]", "");
        double basePrice = Double.parseDouble(cleanPrice);
        final int[] count = {1};

        Runnable updatePrice = () -> {
            tvQty.setText(String.valueOf(count[0]));
            double total = basePrice * count[0];
            totalPrice.setText(String.format("Total: ₱%.2f", total));
        };

        btnPlus.setOnClickListener(v -> {
            count[0]++;
            updatePrice.run();
        });

        btnMinus.setOnClickListener(v -> {
            if (count[0] > 1) {
                count[0]--;
                updatePrice.run();
            }
        });

        updatePrice.run();
        returnBtn.setOnClickListener(v -> bottomSheetDialog.dismiss());

        btnAddToCart.setOnClickListener(v -> {
            addToCartServer(context, product, count[0], basePrice * count[0]);
            bottomSheetDialog.dismiss();
        });

        btnBuyNow.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            double total = basePrice * count[0];
            String finalPrice = String.format("₱%.2f", total);
            showPaymentMethodDialog(context, finalPrice, product, count[0]);
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void addToCartServer(Context context, Product product, int quantity, double totalPrice) {
        String url = "http://10.0.2.2/cropcart/add_to_cart.php";
        SharedPreferences sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(context, "Added to Cart!", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, AddtoCart.class));
                    } else {
                        Toast.makeText(context, "Failed: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("product_id", String.valueOf(product.getProductId()));
                params.put("productName", product.getName());
                params.put("category", product.getCategory());
                params.put("Quantity", String.valueOf(quantity));
                params.put("price", String.format("%.2f", totalPrice));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void showPaymentMethodDialog(Context context, String totalPriceString, Product currentProduct, int quantity) {
        BottomSheetDialog paymentDialog = new BottomSheetDialog(context);
        View paymentView = LayoutInflater.from(context).inflate(R.layout.activity_payment_method, null);
        RadioGroup rgPayment = paymentView.findViewById(R.id.rgPaymentMethods);
        Button btnPayNow = paymentView.findViewById(R.id.btnBuyNow);
        TextView tvTotal = paymentView.findViewById(R.id.dialogProductPrice);
        EditText etAddress = paymentView.findViewById(R.id.etShippingAddress);

        tvTotal.setText(totalPriceString);

        paymentView.findViewById(R.id.returnbtn).setOnClickListener(v -> paymentDialog.dismiss());

        btnPayNow.setOnClickListener(v -> {
            String address = etAddress.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(context, "Please enter address", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedId = rgPayment.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(context, "Please select payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton rb = paymentView.findViewById(selectedId);
            String paymentMethod = rb.getText().toString();
            
            // For Buy Now, we directly place order
            placeOrderServer(context, currentProduct, quantity, totalPriceString, address + " (" + paymentMethod + ")");
            paymentDialog.dismiss();
        });
        paymentDialog.setContentView(paymentView);
        paymentDialog.show();
    }

    private void placeOrderServer(Context context, Product product, int quantity, String price, String address) {
        // This is a direct buy, but we can reuse the place_order logic or make a simple buy script
        // For simplicity, let's assume we use add_to_cart then immediately place_order, 
        // OR we can create a direct_buy.php. Let's stick to the flow or provide instructions.
        // Actually, your ERD has tblOrderHistory. Let's just post to a new script: buy_now.php
        
        String url = "http://10.0.2.2/cropcart/buy_now.php"; 
        // Note: You'll need to create buy_now.php similar to place_order.php but for one item
        
        SharedPreferences sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(context, "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, OrderHistory.class));
                    } else {
                        Toast.makeText(context, "Error: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("product_id", String.valueOf(product.getProductId()));
                params.put("productName", product.getName());
                params.put("category", product.getCategory());
                params.put("Quantity", String.valueOf(quantity));
                params.put("price", price.replace("₱", ""));
                params.put("address", address);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }
}
