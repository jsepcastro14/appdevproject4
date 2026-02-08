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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Product> productList;
    private int currentUserId;

    public ProductAdapter(Context context, List<Product> productList) {
        this.productList = productList;
        SharedPreferences sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        this.currentUserId = sharedPref.getInt("userId", -1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        holder.image.setImageResource(product.getImageResourceId());

        // Logic para sa sariling produkto
        if (product.getUserId() == currentUserId) {
            holder.itemView.setClickable(false);
            holder.itemView.setOnClickListener(null);
            holder.name.setText(product.getName() + " (Your own product)");
            holder.itemView.setAlpha(0.6f);

            // Ipakita ang Edit button dahil sa user ito
            holder.btnEdit.setVisibility(View.VISIBLE);

            // FUNCTION NG EDIT BUTTON
            holder.btnEdit.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, EditProduct.class);

                // Ipasa ang mga detalye ng produkto para may laman ang Edit Screen
                intent.putExtra("PRODUCT_ID", product.getProductId());
                intent.putExtra("PRODUCT_NAME", product.getName());
                intent.putExtra("PRODUCT_CATEGORY", product.getCategory());
                intent.putExtra("PRODUCT_QUANTITY", product.getQuantity());
                intent.putExtra("PRODUCT_PRICE", product.getPrice());

                context.startActivity(intent);
            });

        } else {
            // Kapag hindi sa user, itago ang Edit button at payagan ang pagbili
            holder.btnEdit.setVisibility(View.GONE);
            holder.itemView.setClickable(true);
            holder.itemView.setAlpha(1.0f);
            holder.name.setText(product.getName());
            holder.itemView.setOnClickListener(v -> {
                showPurchaseDialog(v.getContext(), product, holder.getAdapterPosition());
            });
        }

        holder.btnDelete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void deleteProductFromDatabase(Context context, int productId, int position) {
        String url = "http://10.0.2.2/cropcart/delete_product.php";
        StringRequest deleteRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equalsIgnoreCase("success")) {
                        if (position != RecyclerView.NO_POSITION && position < productList.size()) {
                            productList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, productList.size());
                        }
                    } else {
                        Toast.makeText(context, "Order processed, but failed to remove product from list: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(context, "Order processed. Network error on final product removal.", Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", String.valueOf(productId));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(deleteRequest);
    }

    private void showPurchaseDialog(Context context, Product product, int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.activity_purchaseorbuy, null);

        // (Walang pagbabago sa pag-initialize ng views)
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
        btnPlus.setOnClickListener(v -> { count[0]++; updatePrice.run(); });
        btnMinus.setOnClickListener(v -> { if (count[0] > 1) { count[0]--; updatePrice.run(); } });
        updatePrice.run();
        returnBtn.setOnClickListener(v -> bottomSheetDialog.dismiss());

        btnAddToCart.setOnClickListener(v -> {
            addToCartServer(context, product, count[0], basePrice * count[0], bottomSheetDialog);
        });

        btnBuyNow.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            double total = basePrice * count[0];
            String finalPrice = String.format("₱%.2f", total);
            showPaymentMethodDialog(context, finalPrice, product, count[0], position);
        });



        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    // --- BAGONG VERSION NG addToCartServer (WALA NANG 'position' at 'delete' call) ---
    private void addToCartServer(Context context, Product product, int quantity, double totalPrice, BottomSheetDialog dialog) {
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
                        dialog.dismiss();
                        // WALA NANG DELETE AT WALA NANG REDIRECT.
                        // Ang item ay mananatili sa Home screen.
                    } else {
                        Toast.makeText(context, "Failed to add to cart: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Add to cart network Error", Toast.LENGTH_SHORT).show()) {
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

    private void showPaymentMethodDialog(Context context, String totalPriceString, Product currentProduct, int quantity, int position) {
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
                Toast.makeText(context, "Please enter address", Toast.LENGTH_SHORT).show(); return;
            }
            int selectedId = rgPayment.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(context, "Please select payment method", Toast.LENGTH_SHORT).show(); return;
            }
            RadioButton rb = paymentView.findViewById(selectedId);
            String paymentMethod = rb.getText().toString();

            placeOrderServer(context, currentProduct, quantity, totalPriceString, address + " (" + paymentMethod + ")", position, paymentDialog);
        });
        paymentDialog.setContentView(paymentView);
        paymentDialog.show();
    }

    // --- Ito ay para sa "Buy Now" - mayroon pa rin itong delete function ---
    private void placeOrderServer(Context context, Product product, int quantity, String price, String address, int position, BottomSheetDialog dialog) {
        String url = "http://10.0.2.2/cropcart/buy_now.php";
        SharedPreferences sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(context, "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        // PAGKATAPOS MAG-BUY NOW, TAWAGIN ANG DELETE FUNCTION
                        deleteProductFromDatabase(context, product.getProductId(), position);

                    } else {
                        Toast.makeText(context, "Failed to place order: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Place order network Error", Toast.LENGTH_SHORT).show()) {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, category, quantity, price, btnDelete;
        ImageView image;
        Button btnEdit; // Idagdag ang Button object dito

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            category = itemView.findViewById(R.id.product_category);
            quantity = itemView.findViewById(R.id.product_quantity);
            price = itemView.findViewById(R.id.product_price);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit); // I-link sa ID mula sa shoppinglist.xml
        }
    }
}
