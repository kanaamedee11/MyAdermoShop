package com.example.myadermoshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompletedPhysicalControlAdapter
        extends RecyclerView.Adapter<CompletedPhysicalControlAdapter.ViewHolder> {

    private final Context context;
    private final DatabaseHelper dbHelper;
    private final List<PhysicalControle> physicalControlList;

    public CompletedPhysicalControlAdapter(Context context,
                                           List<PhysicalControle> list,
                                           DatabaseHelper dbHelper) {
        this.context             = context;
        this.physicalControlList = list;
        this.dbHelper            = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.completed_physical_control_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        PhysicalControle physicalControle = physicalControlList.get(position);

        // ── Bind header ──
        holder.textViewEmployeeName.setText(physicalControle.getEmployeeID());
        holder.textViewDate.setText(physicalControle.getControleDateTime());

        // ── Expand / collapse toggle ──
        holder.imageViewExpandCollapse.setOnClickListener(v -> {
            if (holder.linearLayoutCollapsible.getVisibility() == View.VISIBLE) {
                holder.linearLayoutCollapsible.setVisibility(View.GONE);
                holder.imageViewExpandCollapse.setImageResource(R.drawable.ic_expand_more);
            } else {
                holder.linearLayoutCollapsible.setVisibility(View.VISIBLE);
                holder.imageViewExpandCollapse.setImageResource(R.drawable.ic_expand_less);
            }
        });

        // ── Populate table rows ──
        holder.tableLayoutProducts.removeAllViews();

        if (physicalControle.getControleCases() != null) {
            for (ControleCase c : physicalControle.getControleCases()) {
                TableRow row = (TableRow) LayoutInflater.from(context)
                        .inflate(R.layout.product_table_row,
                                holder.tableLayoutProducts, false);

                TextView tvName     = row.findViewById(R.id.textViewProductName);
                TextView tvExpected = row.findViewById(R.id.textViewExpectedItems);
                TextView tvFound    = row.findViewById(R.id.textViewFoundQuantity);

                tvName.setText(dbHelper.getProductName(c.getProductID()));
                tvExpected.setText(String.valueOf(c.getExpectedQuantity()));
                tvFound.setText(String.valueOf(c.getActualQuantity()));

                holder.tableLayoutProducts.addView(row);
            }
        }
    }

    @Override
    public int getItemCount() {
        return physicalControlList.size();
    }

    // ── ViewHolder ────────────────────────────────────────────────────────────

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView     textViewEmployeeName;
        TextView     textViewDate;
        ImageView    imageViewExpandCollapse;
        LinearLayout linearLayoutCollapsible;
        TableLayout  tableLayoutProducts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEmployeeName    = itemView.findViewById(R.id.textViewEmployeeName);
            textViewDate            = itemView.findViewById(R.id.textViewDate);
            imageViewExpandCollapse = itemView.findViewById(R.id.imageViewExpandCollapse);
            linearLayoutCollapsible = itemView.findViewById(R.id.linearLayoutCollapsible);
            tableLayoutProducts     = itemView.findViewById(R.id.tableLayoutProducts);
        }
    }
}