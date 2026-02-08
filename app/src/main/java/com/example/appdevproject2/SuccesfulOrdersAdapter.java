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

public class SuccesfulOrdersAdapter extends RecyclerView.Adapter<SuccesfulOrdersAdapter.ViewHolder> {
    private List<Product> successList;

    public SuccesfulOrdersAdapter(List<Product> successList) {
        this.successList = successList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_successful_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = successList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvQty.setText(item.getQuantity());
        holder.tvAddress.setText(item.getCategory());
        // Kung may image ka para dito
        // holder.image.setImageResource(item.getImageResourceId());

        // --- UPDATED DELETE BUTTON LOGIC ---
        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // Kunin ang rate_id mula sa Product object.
                // Ang `getOrderId()` ay ginagamit natin para sa rate_id dito.
                int rateIdToDelete = item.getOrderId();

                // Tawagin ang method para mag-delete sa server
                deleteSuccessfulOrderFromServer(v.getContext(), rateIdToDelete, currentPos);
            }
        });
    }

    // --- NEW METHOD PARA MAG-DELETE SA DATABASE ---
    private void deleteSuccessfulOrderFromServer(Context context, int rateId, int position) {
        // URL ng bagong PHP script
        String url = "http://10.0.2.2/cropcart/delete_successful_order.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // I-check kung ang tugon mula sa server ay "success"
                    if (response.trim().equalsIgnoreCase("success")) {
                        Toast.makeText(context, "Record deleted successfully!", Toast.LENGTH_SHORT).show();

                        // Kung successful, saka pa lang alisin sa UI (RecyclerView)
                        if (position < successList.size()) {
                            successList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, successList.size());
                        }
                    } else {
                        // Ipakita ang error message mula sa PHP script
                        Toast.makeText(context, "Failed to delete: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    // Para sa network errors
                    Toast.makeText(context, "Network Error: Could not connect to server.", Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Ipadala ang 'rate_id' sa PHP script.
                params.put("rate_id", String.valueOf(rateId));
                return params;
            }
        };

        // Idagdag ang request sa Volley queue
        Volley.newRequestQueue(context).add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return successList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvAddress;
        Button btnDelete;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.product_name);
            tvQty = itemView.findViewById(R.id.product_quantity);
            tvAddress = itemView.findViewById(R.id.addresstext);
            image = itemView.findViewById(R.id.product_image);
            btnDelete = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}
