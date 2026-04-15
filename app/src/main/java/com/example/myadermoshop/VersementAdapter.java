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

public class VersementAdapter extends RecyclerView.Adapter<VersementAdapter.ViewHolder> {
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final List<Versement> versementList;

    public VersementAdapter(Context context, List<Versement> list,
                            DatabaseHelper databaseHelper) {
        this.context = context;
        this.versementList = list;
        this.dbHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_versement, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Versement versement = versementList.get(position);

        holder.tvDate.setText(versement.getVersementDateTime());
        holder.tvExpectedAmount.setText(String.valueOf(versement.getExpectedAmount()));
        holder.tvVersedAmount.setText(String.valueOf(versement.getVersedAmount()));
        holder.tvStatus.setText(dbHelper.getStatusLabel(versement.getStatusID()));

        double rest = versement.getVersedAmount() - versement.getExpectedAmount();
        holder.tvRest.setText(String.valueOf(rest));
        holder.tvRest.setTextColor(context.getResources().getColor(
                rest < 0.0d ? R.color.ios_red : R.color.ios_green));

        if (versement.getUploadStatus() == 1) {
            holder.tvUploadStatus.setText("Téléchargé");
            holder.tvUploadStatus.setTextColor(
                    context.getResources().getColor(R.color.ios_green));
        } else {
            holder.tvUploadStatus.setText("Pas encore téléchargé");
            holder.tvUploadStatus.setTextColor(
                    context.getResources().getColor(R.color.ios_orange));
        }

        loadVersementImage(versement, holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return versementList.size();
    }

    private void loadVersementImage(Versement versement, ImageView imageView) {
        String pictureName = versement.getVersementPictureName();
        if (pictureName != null && !pictureName.isEmpty()) {
            File file = new File(context.getFilesDir(),
                    "versements/" + pictureName);
            if (file.exists()) {
                Glide.with(context).load(file.getAbsolutePath()).into(imageView);
                return;
            }
        }
        imageView.setImageResource(R.drawable.ic_placeholder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvDate;
        TextView tvExpectedAmount;
        TextView tvVersedAmount;
        TextView tvRest;
        TextView tvStatus;
        TextView tvUploadStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage          = itemView.findViewById(R.id.ivImage);
            tvDate           = itemView.findViewById(R.id.tvDate);
            tvExpectedAmount = itemView.findViewById(R.id.tvExpectedAmount);
            tvVersedAmount   = itemView.findViewById(R.id.tvVersedAmount);
            tvRest           = itemView.findViewById(R.id.tvRest);
            tvStatus         = itemView.findViewById(R.id.tvStatus);
            tvUploadStatus   = itemView.findViewById(R.id.tvUploadStatus);
        }
    }
}