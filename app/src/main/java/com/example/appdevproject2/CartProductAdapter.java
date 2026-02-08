package com.example.appdevproject2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {
    List<Product> productList;

    public CartProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addtocartproduct, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.category.setText(product.getCategory());
        holder.quantity.setText(product.getQuantity());
        holder.price.setText(product.getPrice());
        // holder.image.setImageResource(product.getImageResourceId()); // Uncomment kung may image

        // --- UPDATED DELETE BUTTON LOGIC ---
        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // Kunin ang cart_id mula sa Product object (na inilagay natin sa getOrderId())
                int cartIdToDelete = product.getOrderId();
                deleteItemFromCart(v.getContext(), cartIdToDelete, currentPos);
            }
        });
    }

    // --- NEW METHOD PARA MAG-DELETE SA DATABASE ---
    private void deleteItemFromCart(Context context, int cartId, int position) {
        // URL ng bagong PHP script
        String url = "http://10.0.2.2/cropcart/delete_from_cart.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // I-check kung ang tugon mula sa server ay "success"
                    if (response.trim().equalsIgnoreCase("success")) {
                        Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();

                        // Kung successful, saka pa lang alisin sa UI (RecyclerView)
                        if (position < productList.size()) {
                            productList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, productList.size());
                        }
                    } else {
                        // Ipakita ang error message mula sa PHP script
                        Toast.makeText(context, "Failed to remove: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    // Para sa network errors
                    Toast.makeText(context, "Network Error: Could not connect to server.", Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Ipadala ang 'cart_id' sa PHP script.
                params.put("cart_id", String.valueOf(cartId));
                return params;
            }
        };

        // Idagdag ang request sa Volley queue
        Volley.newRequestQueue(context).add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, category, quantity, price, btnDelete;
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            category = itemView.findViewById(R.id.product_category);
            quantity = itemView.findViewById(R.id.product_quantity);
            price = itemView.findViewById(R.id.product_price);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
