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

public class DeterioratedProductWithInstanceAdapter
        extends RecyclerView.Adapter<DeterioratedProductWithInstanceAdapter.ViewHolder> {

    private final Context context;
    private final List<DeterioratedProductWithInstance> itemList;

    public DeterioratedProductWithInstanceAdapter(
            Context context,
            List<DeterioratedProductWithInstance> list) {
        this.context  = context;
        this.itemList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_deteriorated_product_with_instance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeterioratedProductWithInstance item = itemList.get(position);

        // ── Header row ──
        holder.tvDeteriorationDate.setText(
                item.getDeteriorationDate() != null
                        ? item.getDeteriorationDate() : "—");
        holder.tvActionTaken.setText(
                item.isActionTaken() ? "✓ Action prise" : "En attente");
        holder.tvSubmissionDate.setText(
                item.getSubmissionDate() != null
                        ? item.getSubmissionDate() : "—");
        holder.tvUploadStatus.setText(
                item.getUploadStatus() == 1 ? "✓ Envoyé" : "Non envoyé");

        // ── Detail fields ──
        holder.tvInstanceID.setText(
                item.getInstanceID() != null ? item.getInstanceID() : "—");
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvReason.setText(
                item.getReason() != null && !item.getReason().isEmpty()
                        ? item.getReason() : "—");
        holder.tvDetectedBy.setText(
                item.getDetectedByEmployeeID() != null
                        ? item.getDetectedByEmployeeID() : "—");

        // ── Photo ──
        loadImage(item.getPictureName(), holder.ivPicture);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void loadImage(String fileName, ImageView imageView) {
        if (imageView == null) return;
        if (fileName == null || fileName.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_placeholder);
            return;
        }
        File file = new File(context.getFilesDir(), "deteriorated/" + fileName);
        if (file.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        } else {
            imageView.setImageResource(R.drawable.ic_placeholder);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Header
        TextView tvDeteriorationDate;
        TextView tvActionTaken;
        TextView tvSubmissionDate;
        TextView tvUploadStatus;
        // Photo
        ImageView ivPicture;
        // Details
        TextView tvInstanceID;
        TextView tvQuantity;
        TextView tvReason;
        TextView tvDetectedBy;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvDeteriorationDate = view.findViewById(R.id.tvDeteriorationDate);
            tvActionTaken       = view.findViewById(R.id.tvActionTaken);
            tvSubmissionDate    = view.findViewById(R.id.tvSubmissionDate);
            tvUploadStatus      = view.findViewById(R.id.tvUploadStatus);
            ivPicture           = view.findViewById(R.id.ivPicture);
            tvInstanceID        = view.findViewById(R.id.tvInstanceID);
            tvQuantity          = view.findViewById(R.id.tvQuantity);
            tvReason            = view.findViewById(R.id.tvReason);
            tvDetectedBy        = view.findViewById(R.id.tvDetectedBy);
        }
    }
}