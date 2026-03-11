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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.completed_physical_control_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        PhysicalControle physicalControle = this.physicalControlList.get(i);
        viewHolder.tvDate.setText(physicalControle.getControleDateTime());
        // For product count and discrepancies, you might want to calculate them from physicalControle.getControleCases()
        if (physicalControle.getControleCases() != null) {
            viewHolder.tvProductCount.setText(String.valueOf(physicalControle.getControleCases().size()));
            int discrepancies = 0;
            for (ControleCase c : physicalControle.getControleCases()) {
                if (c.getExpectedQuantity() != c.getActualQuantity()) {
                    discrepancies++;
                }
            }
            viewHolder.tvDiscrepancies.setText(String.valueOf(discrepancies));
        }
    }

    @Override
    public int getItemCount() {
        return this.physicalControlList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvProductCount;
        TextView tvDiscrepancies;

        public ViewHolder(View view) {
            super(view);
            this.tvDate = view.findViewById(R.id.tvDate);
            this.tvProductCount = view.findViewById(R.id.tvProductCount);
            this.tvDiscrepancies = view.findViewById(R.id.tvDiscrepancies);
        }
    }
}
