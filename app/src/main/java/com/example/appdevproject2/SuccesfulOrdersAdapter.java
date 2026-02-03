package com.example.appdevproject2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Siguraduhing naka-import ito
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SuccesfulOrdersAdapter extends RecyclerView.Adapter<SuccesfulOrdersAdapter.ViewHolder> {
    private List<Product> successList;

    public SuccesfulOrdersAdapter(List<Product> successList) {
        this.successList = successList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ginagamit ang item_successful_orders.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_successful_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = successList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvQty.setText(item.getQuantity());
        holder.tvAddress.setText(item.getCategory());

        // FUNCTION NG DELETE BUTTON
        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // 1. Alisin ang item sa listahan
                successList.remove(currentPos);

                // 2. I-notify ang adapter sa pagbabago
                notifyItemRemoved(currentPos);

                // 3. I-refresh ang position ng natitirang items para maiwasan ang crash
                notifyItemRangeChanged(currentPos, successList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return successList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvAddress;
        Button btnDelete; // Idinagdag na variable para sa button

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.product_name);
            tvQty = itemView.findViewById(R.id.product_quantity);
            tvAddress = itemView.findViewById(R.id.addresstext);

            // I-bind ang button gamit ang ID mula sa XML
            btnDelete = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}