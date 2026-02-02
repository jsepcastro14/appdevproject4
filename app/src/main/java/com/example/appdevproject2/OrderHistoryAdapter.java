package com.example.appdevproject2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<Product> historyList;

    public OrderHistoryAdapter(List<Product> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderhistory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = historyList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvQty.setText(item.getQuantity());
        holder.tvPrice.setText(item.getPrice());

        // Dito natin ilalagay ang Address + Payment Method na kinuha natin kanina
        holder.tvAddress.setText(item.getCategory());

        // Delete button logic para sa history
        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                historyList.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
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
            btnDelete = itemView.findViewById(R.id.add_to_cart_button); // Base sa ID sa XML mo
        }
    }
}