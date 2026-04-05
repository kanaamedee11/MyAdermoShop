package com.example.myadermoshop;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ProductOnCartAdapter
        extends RecyclerView.Adapter<ProductOnCartAdapter.ProductViewHolder> {

    private static final String TAG = "ProductOnCartAdapter";

    private final Context context;
    private final DatabaseHelper dbHelper;
    private final Set<String> instanceIDs;
    private final List<Product> productList;
    private final HashMap<String, Double> productQuantities;

    public ProductOnCartAdapter(List<Product> productList,
                                Context context,
                                HashMap<String, Double> productQuantities,
                                Set<String> instanceIDs) {
        this.productList       = productList;
        this.context           = context;
        this.productQuantities = productQuantities;
        this.instanceIDs       = instanceIDs;
        this.dbHelper          = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_on_cart, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final Product product = productList.get(position);
        boolean isInstanceBased = instanceIDs.contains(product.getProductID());

        Log.d(TAG, "Product ID: " + product.getProductID()
                + ", isInstanceBased: " + isInstanceBased);

        // ── Product name ──
        holder.textViewProductName.setText(product.getProductName());

        if (isInstanceBased) {
            // ── Scanned instance — qty is always 1 ──
            ProductInfo info = dbHelper.getProductInfoByInstance(product.getProductID());
            if (info != null) {
                holder.textViewProductPrice.setText(
                        String.format(Locale.getDefault(),
                                "BIF %.2f", info.getProductPrice()));
                holder.textViewProductManufacture.setText(
                        info.getProductManufacture() != null
                                ? info.getProductManufacture() : "—");
                holder.textViewStockManDate.setText(
                        "MAN: " + (info.getStockManDate() != null
                                ? info.getStockManDate() : "—"));
                holder.textViewStockExpDate.setText(
                        "EXP: " + (info.getStockExpDate() != null
                                ? info.getStockExpDate() : "—"));
                loadImage(info.getProductPhotoName(), holder.imageViewProduct);
            }
            holder.textViewProductQuantity.setText("1");

        } else {
            // ── Selected product — qty from productQuantities map ──
            Product full = dbHelper.getProductByID(product.getProductID());
            if (full != null) {
                double price = dbHelper.getExpectedSellingPrice(full.getProductID());
                holder.textViewProductPrice.setText(
                        String.format(Locale.getDefault(), "BIF %.2f", price));
                holder.textViewProductManufacture.setText(
                        full.getProductManufacture() != null
                                ? full.getProductManufacture() : "—");
                loadImage(full.getProductPhotoName(), holder.imageViewProduct);
            }
            // No stock dates for quantity-based products
            holder.textViewStockManDate.setText("MAN: —");
            holder.textViewStockExpDate.setText("EXP: —");

            double qty = productQuantities.getOrDefault(product.getProductID(), 0.0d);
            holder.textViewProductQuantity.setText(formatQty(qty));
        }

        // ── Delete / decrement button ──
        holder.buttonDelete.setOnClickListener(v -> {
            if (instanceIDs.contains(product.getProductID())) {
                // Instance-based: remove entirely
                instanceIDs.remove(product.getProductID());
                productList.remove(product);
            } else {
                // Quantity-based: decrement or remove
                Double current = productQuantities.get(product.getProductID());
                double qty = current != null ? current : 0.0d;
                if (qty <= 1.0d) {
                    productQuantities.remove(product.getProductID());
                    productList.remove(product);
                } else {
                    productQuantities.put(product.getProductID(), qty - 1.0d);
                }
            }
            notifyDataSetChanged();
            // Notify parent activity to recalculate total
            if (context instanceof AddSaleActivity) {
                ((AddSaleActivity) context).calculateTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ── Format quantity: integer if whole number ──
    private String formatQty(double qty) {
        if (qty == Math.floor(qty)) {
            return String.valueOf((int) qty);
        }
        return String.format(Locale.getDefault(), "%.2f", qty);
    }

    // ── Load product image from local storage ──
    private void loadImage(String fileName, ImageView imageView) {
        if (imageView == null || fileName == null) return;
        File file = new File(context.getFilesDir(), "products/" + fileName);
        if (file.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        } else {
            imageView.setImageResource(R.drawable.ic_product_placeholder);
        }
    }

    // ── ViewHolder — uses IDs from item_product_on_cart.xml ──
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        MaterialButton buttonDelete;
        TextView textViewProductName;
        TextView textViewProductPrice;
        TextView textViewProductManufacture;
        TextView textViewStockManDate;
        TextView textViewStockExpDate;
        TextView textViewProductQuantity;

        public ProductViewHolder(@NonNull View view) {
            super(view);
            imageViewProduct        = view.findViewById(R.id.imageViewProduct);
            textViewProductName     = view.findViewById(R.id.textViewProductName);
            textViewProductPrice    = view.findViewById(R.id.textViewProductPrice);
            textViewProductManufacture = view.findViewById(R.id.textViewProductManufacture);
            textViewStockManDate    = view.findViewById(R.id.textViewStockManDate);
            textViewStockExpDate    = view.findViewById(R.id.textViewStockExpDate);
            textViewProductQuantity = view.findViewById(R.id.textViewProductQuantity);
            buttonDelete            = view.findViewById(R.id.buttonDelete);
        }
    }
}