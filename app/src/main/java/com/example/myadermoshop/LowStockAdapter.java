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
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_low_stock, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LowStockProduct item = lowStockProducts.get(position);

        holder.productName.setText(item.getProductName());
        holder.productQuantity.setText(item.getAvailableStock() + " pcs");
        holder.productSeuil.setText("Seuil: " + item.getSeuilStock());

        loadProductImage(item.getProductID(), holder.productIcon);
    }

    @Override
    public int getItemCount() {
        return lowStockProducts.size();
    }

    private void loadProductImage(String productID, ImageView imageView) {
        String photoName = dbHelper.getProductPhotoName(productID);
        if (photoName != null && !photoName.isEmpty()) {
            File file = new File(context.getFilesDir(), "products/" + photoName);
            if (file.exists()) {
                Glide.with(context).load(file.getAbsolutePath()).into(imageView);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productIcon     = itemView.findViewById(R.id.imageViewProductIcon);
            productName     = itemView.findViewById(R.id.textViewProductName);
            productQuantity = itemView.findViewById(R.id.textViewProductQuantity);
            productSeuil    = itemView.findViewById(R.id.textViewProductSeuil);
        }
    }
}