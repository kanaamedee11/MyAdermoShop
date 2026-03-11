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
public class DeterioratedProductWithoutInstanceAdapter extends RecyclerView.Adapter<DeterioratedProductWithoutInstanceAdapter.ViewHolder> {
    private final Context context;
    private final List<DeterioratedProductWithoutInstance> deterioratedProductWithoutInstanceList;

    public DeterioratedProductWithoutInstanceAdapter(Context context, List<DeterioratedProductWithoutInstance> list) {
        this.context = context;
        this.deterioratedProductWithoutInstanceList = list;
    }

    @NonNull
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_deteriorated_product_without_instance, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DeterioratedProductWithoutInstance deterioratedProductWithoutInstance = this.deterioratedProductWithoutInstanceList.get(i);
        viewHolder.tvDeteriorationDate.setText(deterioratedProductWithoutInstance.getDeteriorationDate());
        viewHolder.tvProductID.setText(deterioratedProductWithoutInstance.getProductID());
        viewHolder.tvQuantity.setText(String.valueOf(deterioratedProductWithoutInstance.getQuantity()));
        viewHolder.tvReason.setText(deterioratedProductWithoutInstance.getReason());
        viewHolder.tvDetectedBy.setText(deterioratedProductWithoutInstance.getDetectedByEmployeeID());
        viewHolder.tvActionTaken.setText(deterioratedProductWithoutInstance.isActionTaken() ? "Accepté" : "Décliné");
        viewHolder.tvSubmissionDate.setText(deterioratedProductWithoutInstance.getSubmissionDate());
        if (deterioratedProductWithoutInstance.getUploadStatus() == 1) {
            viewHolder.tvUploadStatus.setText("Téléchargé");
            viewHolder.tvUploadStatus.setTextColor(this.context.getResources().getColor(android.R.color.holo_green_light));
        } else {
            viewHolder.tvUploadStatus.setText("Pas encore téléchargé");
            viewHolder.tvUploadStatus.setTextColor(this.context.getResources().getColor(android.R.color.holo_orange_light));
        }
        loadDeterioratedProductImage(deterioratedProductWithoutInstance, viewHolder.ivPicture);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.deterioratedProductWithoutInstanceList.size();
    }

    private void loadDeterioratedProductImage(DeterioratedProductWithoutInstance deterioratedProductWithoutInstance, ImageView imageView) {
        String pictureName = deterioratedProductWithoutInstance.getPictureName();
        String pictureUrl = deterioratedProductWithoutInstance.getPictureUrl();
        if (pictureUrl != null && !pictureUrl.isEmpty()) {
            Glide.with(this.context).load(pictureUrl).into(imageView);
            return;
        }
        if (pictureName != null && !pictureName.isEmpty()) {
            String absolutePath = new File(this.context.getFilesDir(), "deteriorated/" + pictureName).getAbsolutePath();
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
        ImageView ivPicture;
        TextView tvActionTaken;
        TextView tvDetectedBy;
        TextView tvDeteriorationDate;
        TextView tvProductID;
        TextView tvQuantity;
        TextView tvReason;
        TextView tvSubmissionDate;
        TextView tvUploadStatus;

        public ViewHolder(View view) {
            super(view);
            this.tvDeteriorationDate = view.findViewById(R.id.tvDeteriorationDate);
            this.tvProductID = view.findViewById(R.id.tvProductID);
            this.tvQuantity = view.findViewById(R.id.tvQuantity);
            this.tvReason = view.findViewById(R.id.tvReason);
            this.tvDetectedBy = view.findViewById(R.id.tvDetectedBy);
            this.tvActionTaken = view.findViewById(R.id.tvActionTaken);
            this.tvSubmissionDate = view.findViewById(R.id.tvSubmissionDate);
            this.tvUploadStatus = view.findViewById(R.id.tvUploadStatus);
            this.ivPicture = view.findViewById(R.id.ivPicture);
        }
    }
}
