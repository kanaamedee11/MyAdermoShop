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
public class VersementAdapter extends RecyclerView.Adapter<VersementAdapter.ViewHolder> {
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final List<Versement> versementList;

    public VersementAdapter(Context context, List<Versement> list, DatabaseHelper databaseHelper) {
        this.context = context;
        this.versementList = list;
        this.dbHelper = databaseHelper;
    }

    @NonNull
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_versement, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Versement versement = this.versementList.get(i);
        viewHolder.tvExpectedAmount.setText(String.valueOf(versement.getExpectedAmount()));
        viewHolder.tvVersedAmount.setText(String.valueOf(versement.getVersedAmount()));
        viewHolder.tvDate.setText(versement.getVersementDateTime());
        viewHolder.tvStatus.setText(this.dbHelper.getStatusLabel(versement.getStatusID()));
        double versedAmount = versement.getVersedAmount() - versement.getExpectedAmount();
        viewHolder.tvRest.setText(String.valueOf(versedAmount));
        if (versedAmount < 0.0d) {
            viewHolder.tvRest.setTextColor(this.context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            viewHolder.tvRest.setTextColor(this.context.getResources().getColor(android.R.color.holo_green_dark));
        }
        if (versement.getUploadStatus() == 1) {
            viewHolder.tvUploadStatus.setText("Téléchargé");
            viewHolder.tvUploadStatus.setTextColor(this.context.getResources().getColor(android.R.color.holo_green_light));
        } else {
            viewHolder.tvUploadStatus.setText("Pas encore téléchargé");
            viewHolder.tvUploadStatus.setTextColor(this.context.getResources().getColor(android.R.color.holo_orange_light));
        }
        loadVersementImage(versement, viewHolder.ivImage);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.versementList.size();
    }

    private void loadVersementImage(Versement versement, ImageView imageView) {
        String versementPictureName = versement.getVersementPictureName();
        if (versementPictureName != null && !versementPictureName.isEmpty()) {
            String absolutePath = new File(this.context.getFilesDir(), "versements/" + versementPictureName).getAbsolutePath();
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
        ImageView ivImage;
        TextView tvDate;
        TextView tvExpectedAmount;
        TextView tvRest;
        TextView tvStatus;
        TextView tvUploadStatus;
        TextView tvVersedAmount;

        public ViewHolder(View view) {
            super(view);
            this.ivImage = view.findViewById(R.id.ivImage);
            this.tvExpectedAmount = view.findViewById(R.id.tvExpectedAmount);
            this.tvVersedAmount = view.findViewById(R.id.tvVersedAmount);
            this.tvRest = view.findViewById(R.id.tvRest);
            this.tvUploadStatus = view.findViewById(R.id.tvUploadStatus);
            this.tvDate = view.findViewById(R.id.tvDate);
            this.tvStatus = view.findViewById(R.id.tvStatus);
        }
    }
}
