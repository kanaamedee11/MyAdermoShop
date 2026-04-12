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

public class DeterioratedProductWithoutInstanceAdapter
        extends RecyclerView.Adapter<DeterioratedProductWithoutInstanceAdapter.ViewHolder> {

    private final Context context;
    private final List<DeterioratedProductWithoutInstance> deterioratedProductWithoutInstanceList;

    public DeterioratedProductWithoutInstanceAdapter(
            Context context, List<DeterioratedProductWithoutInstance> list) {
        this.context = context;
        this.deterioratedProductWithoutInstanceList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_deteriorated_product_without_instance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeterioratedProductWithoutInstance item =
                deterioratedProductWithoutInstanceList.get(position);

        // Header
        holder.tvDeteriorationDate.setText(item.getDeteriorationDate());

        // Action taken — colored like the "with instance" reference (blue/green/red)
        boolean actionTaken = item.isActionTaken();
        holder.tvActionTaken.setText(actionTaken ? "Accepté" : "Décliné");
        holder.tvActionTaken.setTextColor(
                context.getResources().getColor(
                        actionTaken ? R.color.ios_green : R.color.ios_red));

        // Meta row
        holder.tvSubmissionDate.setText(item.getSubmissionDate());

        // Upload status — text + color only, matching the reference card style
        if (item.getUploadStatus() == 1) {
            holder.tvUploadStatus.setText("Téléchargé");
            holder.tvUploadStatus.setTextColor(
                    context.getResources().getColor(R.color.ios_green));
        } else {
            holder.tvUploadStatus.setText("Pas encore téléchargé");
            holder.tvUploadStatus.setTextColor(
                    context.getResources().getColor(R.color.ios_orange));
        }

        // Detail fields
        holder.tvProductID.setText(item.getProductID());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvReason.setText(item.getReason());
        holder.tvDetectedBy.setText(item.getDetectedByEmployeeID());

        // Photo
        loadDeterioratedProductImage(item, holder.ivPicture);
    }

    @Override
    public int getItemCount() {
        return deterioratedProductWithoutInstanceList.size();
    }

    private void loadDeterioratedProductImage(
            DeterioratedProductWithoutInstance item, ImageView imageView) {
        String pictureUrl  = item.getPictureUrl();
        String pictureName = item.getPictureName();

        if (pictureUrl != null && !pictureUrl.isEmpty()) {
            Glide.with(context).load(pictureUrl).into(imageView);
            return;
        }
        if (pictureName != null && !pictureName.isEmpty()) {
            File file = new File(context.getFilesDir(), "deteriorated/" + pictureName);
            if (file.exists()) {
                Glide.with(context).load(file.getAbsolutePath()).into(imageView);
                return;
            }
        }
        imageView.setImageResource(R.drawable.ic_placeholder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  tvDeteriorationDate;
        TextView  tvActionTaken;
        TextView  tvSubmissionDate;
        TextView  tvUploadStatus;
        ImageView ivPicture;
        TextView  tvProductID;
        TextView  tvQuantity;
        TextView  tvReason;
        TextView  tvDetectedBy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeteriorationDate = itemView.findViewById(R.id.tvDeteriorationDate);
            tvActionTaken       = itemView.findViewById(R.id.tvActionTaken);
            tvSubmissionDate    = itemView.findViewById(R.id.tvSubmissionDate);
            tvUploadStatus      = itemView.findViewById(R.id.tvUploadStatus);
            ivPicture           = itemView.findViewById(R.id.ivPicture);
            tvProductID         = itemView.findViewById(R.id.tvProductID);
            tvQuantity          = itemView.findViewById(R.id.tvQuantity);
            tvReason            = itemView.findViewById(R.id.tvReason);
            tvDetectedBy        = itemView.findViewById(R.id.tvDetectedBy);
        }
    }
}