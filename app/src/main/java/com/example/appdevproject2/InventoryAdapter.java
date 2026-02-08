package com.example.appdevproject2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private List<Product> inventoryList;

    public InventoryAdapter(List<Product> inventoryList) {
        this.inventoryList = inventoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = inventoryList.get(position);
        holder.image.setImageResource(item.getImageResourceId());
        holder.tvName.setText(item.getName());
        holder.tvQty.setText(item.getQuantity());
        holder.tvPrice.setText(item.getPrice());
        holder.tvAddress.setText("Category: " + item.getCategory());

        String category = item.getCategory().toLowerCase();
        if (category.contains("fruits")) {
            holder.image.setImageResource(R.drawable.fruits);
        } else if (category.contains("vegetables")) {
            holder.image.setImageResource(R.drawable.vegetables);
        } else {
            holder.image.setImageResource(R.drawable.emptyimage); // Fallback image
        }


        // --- UPDATED DELETE BUTTON LOGIC ---
        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // Kunin ang product_id ng item na ide-delete.
                // Ginamit natin ang getProductId() dahil ito ang unique ID sa tblinventory
                int inventoryIdToDelete = item.getProductId();

                // Tawagin ang method para mag-delete sa server
                deleteItemFromServer(v.getContext(), inventoryIdToDelete, currentPos);
            }
        });
    }

    // --- NEW METHOD ADDED ---
    // Ito ay kinopya at in-adapt mula sa iyong OrderHistoryadapter
    private void deleteItemFromServer(Context context, int inventoryId, int position) {
        // Ang URL ay papunta sa bagong PHP script para sa pag-delete ng inventory
        String url = "http://10.0.2.2/cropcart/delete_inventory.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // I-check kung ang tugon mula sa server ay "success"
                    if (response.trim().equalsIgnoreCase("success")) {
                        Toast.makeText(context, "Item deleted successfully!", Toast.LENGTH_SHORT).show();

                        // Kung matagumpay ang pag-delete sa server, saka pa lang tatanggalin sa UI
                        if (position < inventoryList.size()) {
                            inventoryList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, inventoryList.size());
                        }
                    } else {
                        // Kung may error message na galing sa PHP, ipakita ito
                        Toast.makeText(context, "Failed to delete: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    // Para sa network errors (hal. walang internet, maling URL)
                    Toast.makeText(context, "Network Error: Could not connect to server.", Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Ipadala ang 'inventory_id' sa PHP script. Ito dapat tumugma sa inaasahan ng PHP.
                params.put("inventory_id", String.valueOf(inventoryId));
                return params;
            }
        };

        // Idagdag ang request sa Volley queue para ma-execute
        Volley.newRequestQueue(context).add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvPrice, tvAddress;
        Button btnDelete;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_image);
            tvName = itemView.findViewById(R.id.product_name);
            tvQty = itemView.findViewById(R.id.product_quantity);
            tvPrice = itemView.findViewById(R.id.product_price);
            tvAddress = itemView.findViewById(R.id.addresstext);
            btnDelete = itemView.findViewById(R.id.add_to_cart_button); // Ito yung Delete button sa XML mo
        }
    }
}