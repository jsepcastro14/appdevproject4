package com.example.appdevproject2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

        // Delete button logic para sa individual item
        holder.btnDelete.setOnClickListener(v -> {
            // Kunin ang current position para hindi magkamali sa pag-delete
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                CartManager.removeItem(currentPos);
                notifyItemRemoved(currentPos);
                notifyItemRangeChanged(currentPos, productList.size());
                Toast.makeText(v.getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

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
}