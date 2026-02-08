package com.example.appdevproject2;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        holder.image.setImageResource(item.getImageResourceId());



        holder.itemView.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                showRateDialog(v.getContext(), item, currentPos);
            }
        });

        // Sa loob ng onBindViewHolder:
        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // Ito ay tatawag sa getOrderId() na naglalaman na ng order_history_id
                int orderIdToDelete = item.getOrderId();
                deleteOrderFromServer(v.getContext(), orderIdToDelete, currentPos);
            }
        });
    }


    private void showRateDialog(Context context, Product product, int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.activity_rate_product, null);

        TextView title = sheetView.findViewById(R.id.dialogProductCategory);
        EditText etRating = sheetView.findViewById(R.id.etRating);
        Button btnRate = sheetView.findViewById(R.id.btnBuyNow);

        title.setText("Rate " + product.getName());

        btnRate.setOnClickListener(v -> {
            String rating = etRating.getText().toString();
            if (!rating.isEmpty()) {
                rateProductOnServer(context, product, rating, position, bottomSheetDialog);
            } else {
                Toast.makeText(context, "Please enter a rating", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void rateProductOnServer(Context context, Product product, String rating, int position, BottomSheetDialog dialog) {
        String url = "http://10.0.2.2/cropcart/rate_product.php";
        SharedPreferences sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(context, "Rated Successfully!", Toast.LENGTH_SHORT).show();
                        if (position < historyList.size()) {
                            historyList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, historyList.size());
                        }
                        dialog.dismiss();
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
                params.put("address", product.getCategory()); // Assuming address is in the category field for history
                params.put("rate", rating);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    // Idagdag itong bagong method sa loob ng OrderHistoryadapter class
    private void deleteOrderFromServer(Context context, int orderId, int position) {
        String url = "http://10.0.2.2/cropcart/delete_order.php"; // Siguraduhing tama ang URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // I-check kung ang tugon mula sa server ay "success"
                    if (response.trim().equalsIgnoreCase("success")) {
                        Toast.makeText(context, "Order deleted successfully!", Toast.LENGTH_SHORT).show();

                        // Tanggalin ang item sa listahan at i-update ang RecyclerView
                        // Siguraduhing may laman pa rin ang list bago mag-remove
                        if (position < historyList.size()) {
                            historyList.remove(position);
                            notifyItemRemoved(position);
                            // Mahalaga ito para ma-update ang positions ng ibang items
                            notifyItemRangeChanged(position, historyList.size());
                        }
                    } else {
                        // Kung may error na nireturn ang PHP script
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
                // Ipadala ang order_id sa PHP script
                params.put("order_id", String.valueOf(orderId));
                return params;
            }
        };

        // Idagdag ang request sa Volley queue
        Volley.newRequestQueue(context).add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return historyList.size();
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
            btnDelete = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}
