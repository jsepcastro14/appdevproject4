package com.example.appdevproject2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

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

        holder.tvName.setText(item.getName());
        holder.tvQty.setText(item.getQuantity());
        holder.tvPrice.setText(item.getPrice());

        // DITO NATIN ILALAGAY YUNG ADDRESS (Assuming itinatago natin sa Category field temporarily
        // o nag-update ka ng Product class)
        holder.tvAddress.setText("Address: " + item.getCategory());

        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                inventoryList.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvPrice, tvAddress;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.product_name);
            tvQty = itemView.findViewById(R.id.product_quantity);
            tvPrice = itemView.findViewById(R.id.product_price);
            tvAddress = itemView.findViewById(R.id.addresstext);
            btnDelete = itemView.findViewById(R.id.add_to_cart_button); // Ito yung Delete button sa XML mo
        }
    }
}