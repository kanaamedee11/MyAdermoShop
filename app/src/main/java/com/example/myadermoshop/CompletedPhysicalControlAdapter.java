package com.example.myadermoshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompletedPhysicalControlAdapter extends RecyclerView.Adapter<CompletedPhysicalControlAdapter.ViewHolder> {
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final List<PhysicalControle> physicalControlList;

    public CompletedPhysicalControlAdapter(Context context, List<PhysicalControle> list, DatabaseHelper databaseHelper) {
        this.context = context;
        this.physicalControlList = list;
        this.dbHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.completed_physical_control_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        PhysicalControle physicalControle = physicalControlList.get(position);

        // Bind date
        holder.tvDate.setText(physicalControle.getControleDateTime());

        // Product count & discrepancies
        if (physicalControle.getControleCases() != null) {
            holder.tvProductCount.setText(String.valueOf(physicalControle.getControleCases().size()));

            int discrepancies = 0;
            for (ControleCase c : physicalControle.getControleCases()) {
                if (c.getExpectedQuantity() != c.getActualQuantity()) {
                    discrepancies++;
                }
            }
            holder.tvDiscrepancies.setText(String.valueOf(discrepancies));
        } else {
            holder.tvProductCount.setText("0");
            holder.tvDiscrepancies.setText("0");
        }
    }

    @Override
    public int getItemCount() {
        return physicalControlList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvProductCount, tvDiscrepancies;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvProductCount = itemView.findViewById(R.id.tvProductCount);
            tvDiscrepancies = itemView.findViewById(R.id.tvDiscrepancies);
        }
    }
}
