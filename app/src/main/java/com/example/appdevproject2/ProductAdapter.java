package com.example.appdevproject2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.content.Context;
import android.widget.Toast;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        holder.itemView.setOnClickListener(v -> {
            showPurchaseDialog(v.getContext(), product);
        });

        if (holder.itemView.getContext() instanceof AddtoCart) {
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }

        // DELETE LOGIC
        holder.btnDelete.setOnClickListener(v -> {
            int actualPosition = holder.getAdapterPosition();
            if (actualPosition != RecyclerView.NO_POSITION) {
                productList.remove(actualPosition); // Tanggalin sa listahan
                notifyItemRemoved(actualPosition);   // I-update ang UI
                notifyItemRangeChanged(actualPosition, productList.size());
            }
            });

        // Ang existing click listener mo para sa pop-up ay mananatili
        holder.itemView.setOnClickListener(v -> {
            showPurchaseDialog(v.getContext(), product);
            });
    }

    @Override
    public int getItemCount() { return productList.size(); }

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
    private void updateProductInventory(Product boughtProduct) {
        List<Product> allProducts = ProductManager.getAllProducts();

        for (int i = 0; i < allProducts.size(); i++) {
            Product p = allProducts.get(i);
            if (p.getName().equals(boughtProduct.getName())) {

                // Kunin ang kasalukuyang numero mula sa string (halimbawa: "10 pcs" -> 10)
                int currentQty = Integer.parseInt(p.getQuantity().replaceAll("[^0-9]", ""));
                int boughtQty = Integer.parseInt(boughtProduct.getQuantity().replaceAll("[^0-9]", ""));

                int remainingQty = currentQty - boughtQty;

                if (remainingQty <= 0) {
                    // Burahin sa listahan kung ubos na
                    allProducts.remove(i);
                } else {
                    // I-update ang quantity kung may natira pa
                    p.setQuantity(remainingQty + " pcs");
                }
                break;
            }
        }
    }

    private void showPurchaseDialog(Context context, Product product) {
        // 1. Create ang BottomSheetDialog
        com.google.android.material.bottomsheet.BottomSheetDialog bottomSheetDialog =
                new com.google.android.material.bottomsheet.BottomSheetDialog(context);

        // 2. I-inflate ang layout na purchaseorbuy
        View view = LayoutInflater.from(context).inflate(R.layout.activity_purchaseorbuy, null);

        // 3. I-bind ang views mula sa XML (I-check ang IDs sa activity_purchaseorbuy.xml)
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

        // 4. I-set ang data na galing sa Home list
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

        // Gagana na ang click listeners kahit ImageButton sila
        btnPlus.setOnClickListener(v -> {
            count[0]++;
            updatePrice.run();
        });

        btnMinus.setOnClickListener(v -> {
            if (count[0] > 1) {
                count[0]--;
                updatePrice.run();
            }
        });

        updatePrice.run();
        returnBtn.setOnClickListener(v -> bottomSheetDialog.dismiss());

        btnAddToCart.setOnClickListener(v -> {
            // Isara muna ang dialog bago lumipat
            bottomSheetDialog.dismiss();

            // Intent para lumipat sa AddtoCart Activity
            Intent intent = new Intent(context, AddtoCart.class);

            // "Ipapasa" natin ang data para parehas ang lalabas sa kabilang layout
            intent.putExtra("prodName", product.getName());
            intent.putExtra("prodCategory", product.getCategory());
            intent.putExtra("prodQuantity", String.valueOf(count[0]) + " pcs");

            // Kunin ang updated total price (basePrice * count)
            double total = basePrice * count[0];
            String finalPrice = String.format("₱%.2f", total);
            String finalQty = count[0] + " pcs";

            CartManager.addItem(new Product(product.getName(), product.getCategory(), finalQty, finalPrice));

            context.startActivity(intent);
        });

        btnBuyNow.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            // Kunin ang computed total price mula sa updatePrice logic mo kanina
            double total = basePrice * count[0];
            String finalPrice = String.format("₱%.2f", total);

            showPaymentMethodDialog(context, finalPrice, product); // Ipapasa ang presyo
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void showPaymentMethodDialog(Context context, String totalPriceString, Product currentProduct) {
        BottomSheetDialog paymentDialog = new BottomSheetDialog(context);
        View paymentView = LayoutInflater.from(context).inflate(R.layout.activity_payment_method, null);
        RadioGroup rgPayment = paymentView.findViewById(R.id.rgPaymentMethods);
        Button btnPayNow = paymentView.findViewById(R.id.btnBuyNow);

        // Gagamitin natin ang payment layout mo (halimbawa: layout_payment_method)
        TextView tvTotal = paymentView.findViewById(R.id.dialogProductPrice);
        tvTotal.setText(totalPriceString); // Dito lalabas yung "₱500.00" halimbawa

        // 2. Paganahin ang Return Button
        paymentView.findViewById(R.id.returnbtn).setOnClickListener(v -> {
            paymentDialog.dismiss(); // Isasara lang ang payment dialog
        });

        EditText etAddress = paymentView.findViewById(R.id.etShippingAddress);
        btnPayNow.setOnClickListener(v -> {
            String address = etAddress.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(context, "Please enter address", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Kunin ang master list mula sa ProductManager
            List<Product> masterList = ProductManager.getAllProducts();

            // 2. Hanapin ang index ng product sa master list base sa pangalan
            int indexInMaster = -1;
            for (int i = 0; i < masterList.size(); i++) {
                if (masterList.get(i).getName().equals(currentProduct.getName())) {
                    indexInMaster = i;
                    break;
                }
            }

            if (indexInMaster != -1) {
                try {
                    Product productToUpdate = masterList.get(indexInMaster);

                    // 3. I-parse ang quantity (Alisin ang " pcs")
                    int currentQty = Integer.parseInt(productToUpdate.getQuantity().replace(" pcs", "").trim());

                    // Bawasan ng 1 (o kung may input qty ka, gamitin iyon)
                    int newQty = currentQty - 1;

                    if (newQty <= 0) {
                        // 4. Tanggalin sa master list kung zero na
                        masterList.remove(indexInMaster);
                        Toast.makeText(context, "Product Sold Out!", Toast.LENGTH_SHORT).show();
                    } else {
                        // 5. I-update ang quantity string
                        productToUpdate.setQuantity(newQty + " pcs");
                    }

                    // 6. I-update ang UI (Refresh ang RecyclerView)
                    notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(context, "Error updating quantity", Toast.LENGTH_SHORT).show();
                }
            }

            // Isave sa History
            String paymentMethod = ((RadioButton) rgPayment.findViewById(rgPayment.getCheckedRadioButtonId())).getText().toString();
            Product soldProduct = new Product(currentProduct.getName(), address + " (" + paymentMethod + ")", "1 pcs", currentProduct.getPrice());
            OrderHistoryManager.addItem(soldProduct);

            paymentDialog.dismiss();
        });
        paymentDialog.setContentView(paymentView);
        paymentDialog.show();
    }
}
