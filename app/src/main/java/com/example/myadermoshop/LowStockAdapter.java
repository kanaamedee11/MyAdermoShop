package com.example.myadermoshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.List;

/* loaded from: classes.dex */
public class LowStockAdapter extends RecyclerView.Adapter<LowStockAdapter.ViewHolder> {
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final List<LowStockProduct> lowStockProducts;

    public LowStockAdapter(Context context, List<LowStockProduct> list) {
        this.context = context;
        this.lowStockProducts = list;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_low_stock, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        LowStockProduct lowStockProduct = this.lowStockProducts.get(i);
        viewHolder.productName.setText(lowStockProduct.getProductName());
        viewHolder.productQuantity.setText("Disponible: " + lowStockProduct.getAvailableStock());
        viewHolder.productSeuil.setText("Seuil: " + lowStockProduct.getSeuilStock());
        loadProductImage(lowStockProduct.getProductID(), viewHolder.productIcon);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.lowStockProducts.size();
    }

    private void loadProductImage(String str, ImageView imageView) {
        String productPhotoName = this.dbHelper.getProductPhotoName(str);
        if (productPhotoName != null && !productPhotoName.isEmpty()) {
            String absolutePath = new File(this.context.getFilesDir(), "products/" + productPhotoName).getAbsolutePath();
            if (new File(absolutePath).exists()) {
                Glide.with(this.context).load(absolutePath).into(imageView);
                return;
            } else {
                imageView.setImageResource(R.drawable.ic_placeholder);
                return;
            }
        }
        imageView.setImageResource(R.drawable.ic_placeholder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productIcon;
        TextView productName;
        TextView productQuantity;
        TextView productSeuil;

        public ViewHolder(View view) {
            super(view);
            this.productIcon = view.findViewById(R.id.imageViewProductIcon);
            this.productName = view.findViewById(R.id.textViewProductName);
            this.productQuantity = view.findViewById(R.id.textViewProductQuantity);
            this.productSeuil = view.findViewById(R.id.textViewProductSeuil);
        }
    }
}
