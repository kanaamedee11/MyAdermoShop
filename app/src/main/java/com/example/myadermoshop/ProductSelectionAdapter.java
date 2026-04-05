package com.example.myadermoshop;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.List;

public class ProductSelectionAdapter
        extends RecyclerView.Adapter<ProductSelectionAdapter.ProductSelectViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    private final List<Product> productList;
    private final Context context;
    private final OnProductClickListener listener;

    public ProductSelectionAdapter(List<Product> productList,
                                   Context context,
                                   OnProductClickListener listener) {
        this.productList = productList;
        this.context     = context;
        this.listener    = listener;
    }

    @NonNull
    @Override
    public ProductSelectViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_selection, parent, false);
        return new ProductSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ProductSelectViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.textViewName.setText(product.getProductName());
        holder.textViewManufacture.setText(
                product.getProductManufacture() != null
                        ? product.getProductManufacture() : "—");

        loadImage(product.getProductPhotoName(), holder.imageView);

        holder.itemView.setOnClickListener(v -> listener.onProductClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void loadImage(String fileName, ImageView imageView) {
        if (fileName == null || imageView == null) return;
        File file = new File(context.getFilesDir(), "products/" + fileName);
        if (file.exists()) {
            imageView.setImageBitmap(
                    BitmapFactory.decodeFile(file.getAbsolutePath()));
        } else {
            imageView.setImageResource(R.drawable.ic_product_placeholder);
        }
    }

    static class ProductSelectViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewManufacture;

        public ProductSelectViewHolder(@NonNull View view) {
            super(view);
            imageView          = view.findViewById(R.id.imageViewProductSelect);
            textViewName       = view.findViewById(R.id.tvProductSelectName);
            textViewManufacture = view.findViewById(R.id.tvProductSelectManufacture);
        }
    }
}