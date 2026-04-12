package com.example.myadermoshop;

import android.content.Context;
import android.graphics.Color;
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

public class DispenseAdapter extends RecyclerView.Adapter<DispenseAdapter.ViewHolder> {

    private final Context        context;
    private final List<Dispense> dispenseList;
    private final DatabaseHelper dbHelper;

    public DispenseAdapter(Context context, List<Dispense> list, DatabaseHelper dbHelper) {
        this.context      = context;
        this.dispenseList = list;
        this.dbHelper     = dbHelper;
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_dispense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dispense d = dispenseList.get(position);

        holder.tvAmount.setText(String.format("%.2f BIF", d.getAmount()));
        holder.tvDate.setText(d.getDispenseDate());
        holder.tvStatus.setText(dbHelper.getStatusLabel(d.getStatusID()));
        holder.tvPaymentType.setText(getPaymentTypeLabel(d.getPaymentTypeID()));

        // ── FIXED: Color.parseColor() instead of R.color to avoid sync issues ──
        if (d.getUploadStatus() == 1) {
            holder.tvUploadStatus.setText("Téléchargé");
            holder.tvUploadStatus.setTextColor(Color.parseColor("#34C759")); // ios_green
        } else {
            holder.tvUploadStatus.setText("Non téléchargé");
            holder.tvUploadStatus.setTextColor(Color.parseColor("#FF3B30")); // ios_red
        }

        loadDispenseImage(d, holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return dispenseList != null ? dispenseList.size() : 0;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void loadDispenseImage(Dispense dispense, ImageView imageView) {
        String name = dispense.getPictureName();
        if (name != null && !name.isEmpty()) {
            File file = new File(context.getFilesDir(), "dispenses/" + name);
            if (file.exists()) {
                Glide.with(context).load(file).into(imageView);
                return;
            }
        }
        // ── FIXED: use built-in Android placeholder instead of missing drawable ──
        imageView.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    private String getPaymentTypeLabel(int id) {
        String label = dbHelper.getStatusLabel(id);
        if (label != null && !label.isEmpty()) return label;
        switch (id) {
            case 1:  return "Cash";
            case 2:  return "Crédit";
            default: return "Autre";
        }
    }

    // ── ViewHolder ────────────────────────────────────────────────────────────

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivImage;
        final TextView  tvAmount;
        final TextView  tvDate;
        final TextView  tvStatus;
        final TextView  tvUploadStatus;
        final TextView  tvPaymentType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage        = itemView.findViewById(R.id.ivImage);
            tvAmount       = itemView.findViewById(R.id.tvAmount);
            tvDate         = itemView.findViewById(R.id.tvDate);
            tvStatus       = itemView.findViewById(R.id.tvStatus);
            tvUploadStatus = itemView.findViewById(R.id.tvUploadStatus);
            tvPaymentType  = itemView.findViewById(R.id.tvPaymentType);
        }
    }
}