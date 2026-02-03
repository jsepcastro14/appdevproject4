package com.example.appdevproject2;

import android.widget.Toast;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class OrderHistoryadapter extends RecyclerView.Adapter<OrderHistoryadapter.ViewHolder> {
    private List<Product> historyList;

    public OrderHistoryadapter(List<Product> historyList) {
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
        holder.tvAddress.setText(item.getCategory());

        // FIX: Ginagamit na ang tamang method at ipinapasa ang context
        holder.itemView.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                showRateDialog(v.getContext(), item, currentPos);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                historyList.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, historyList.size());
            }
        });
    }

    // FIX: Dinagdagan ng Context at position parameters
    private void showRateDialog(Context context, Product product, int position) {
        // Gumamit ng context mula sa View, hindi null!
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.activity_rate_product, null);

        TextView title = sheetView.findViewById(R.id.dialogProductCategory);
        EditText etRating = sheetView.findViewById(R.id.etRating);
        Button btnRate = sheetView.findViewById(R.id.btnBuyNow);

        title.setText("Rate " + product.getName());

        btnRate.setOnClickListener(v -> {
            String rating = etRating.getText().toString();
            if (!rating.isEmpty()) {
                String updatedDetails = product.getCategory() + " | Rate: " + rating + "/5";
                Product ratedProduct = new Product(product.getName(), updatedDetails, product.getQuantity(), product.getPrice());

                // I-save ang rated product
                SuccessfulOrderManager.addOrder(ratedProduct);

                // Alisin sa history list pagkatapos i-rate
                if (position < historyList.size()) {
                    historyList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, historyList.size());
                }

                Toast.makeText(context, "Rated Successfully!", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(context, "Please enter a rating", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
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
            btnDelete = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}