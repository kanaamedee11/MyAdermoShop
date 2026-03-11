package com.example.myadermoshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class ClosingSummaryAdapter extends RecyclerView.Adapter<ClosingSummaryAdapter.ViewHolder> {
    private final Context context;
    private final List<ClosingSummary> closingSummaryList;
    private final DatabaseHelper dbHelper;

    public ClosingSummaryAdapter(Context context, List<ClosingSummary> closingSummaryList) {
        this.context = context;
        this.closingSummaryList = closingSummaryList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_closing_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClosingSummary summary = closingSummaryList.get(position);
        holder.tvDate.setText(summary.getDate());
        holder.tvClosingAmount.setText(String.format(Locale.getDefault(), "%.0f BIF", summary.getTotalSalePrice()));
        holder.tvTotalPurchasePrice.setText(String.format(Locale.getDefault(), "%.0f BIF", summary.getTotalPurchasePrice()));
        holder.tvTotalSalePrice.setText(String.format(Locale.getDefault(), "%.0f BIF", summary.getTotalSalePrice()));

        // Fill sales summary
        holder.linearLayoutSales.removeAllViews();
        if (summary.getSalesSummary() != null) {
            for (CartItem item : summary.getSalesSummary()) {
                View row = LayoutInflater.from(context).inflate(R.layout.item_sale_simple, holder.linearLayoutSales, false);
                TextView tvName = row.findViewById(R.id.tvProductName);
                TextView tvQty = row.findViewById(R.id.tvQty);
                TextView tvPrice = row.findViewById(R.id.tvUnitPrice);
                TextView tvTotal = row.findViewById(R.id.tvTotal);

                tvName.setText(item.getProductName());
                tvQty.setText(String.format(Locale.getDefault(), "%.1f", item.getQuantity()));
                tvPrice.setText(String.format(Locale.getDefault(), "%.0f", item.getUnitPrice()));
                tvTotal.setText(String.format(Locale.getDefault(), "%.0f BIF", item.getQuantity() * item.getUnitPrice()));
                holder.linearLayoutSales.addView(row);
            }
        }

        // Fill stock summary
        holder.linearLayoutStock.removeAllViews();
        if (summary.getStockSummary() != null) {
            for (Stock stock : summary.getStockSummary()) {
                View row = LayoutInflater.from(context).inflate(R.layout.item_sale_simple, holder.linearLayoutStock, false);
                TextView tvName = row.findViewById(R.id.tvProductName);
                TextView tvQty = row.findViewById(R.id.tvQty);
                TextView tvPrice = row.findViewById(R.id.tvUnitPrice);
                TextView tvTotal = row.findViewById(R.id.tvTotal);

                String productName = dbHelper.getProductName(stock.getProductID());
                tvName.setText(productName);
                tvQty.setText(String.valueOf(stock.getStockQuantity()));
                double pu = stock.getStockQuantity() > 0 ? stock.getTotalAmountUsed() / stock.getStockQuantity() : 0;
                tvPrice.setText(String.format(Locale.getDefault(), "%.0f", pu));
                tvTotal.setText(String.format(Locale.getDefault(), "%.0f BIF", stock.getTotalAmountUsed()));
                holder.linearLayoutStock.addView(row);
            }
        }
    }

    @Override
    public int getItemCount() {
        return closingSummaryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvClosingAmount, tvTotalPurchasePrice, tvTotalSalePrice;
        LinearLayout linearLayoutSales, linearLayoutStock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvClosingAmount = itemView.findViewById(R.id.tvClosingAmount);
            tvTotalPurchasePrice = itemView.findViewById(R.id.tvTotalPurchasePrice);
            tvTotalSalePrice = itemView.findViewById(R.id.tvTotalSalePrice);
            linearLayoutSales = itemView.findViewById(R.id.linearLayoutSales);
            linearLayoutStock = itemView.findViewById(R.id.linearLayoutStock);
        }
    }
}
