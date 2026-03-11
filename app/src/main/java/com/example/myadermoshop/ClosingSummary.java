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

/**
 * ClosingSummaryAdapter
 * Outer card : card_closing_item.xml
 * Inner rows : item_sale_row.xml  (tvProductName, tvQty, tvUnitPrice, tvTotal)
 *   — used for both sales rows (linearLayoutSales)
 *   — and stock rows (linearLayoutStock)
 */
public class ClosingSummaryAdapter
        extends RecyclerView.Adapter<ClosingSummaryAdapter.ViewHolder> {

    private final Context context;
    private final List<ClosingSummary> closingSummaryList;
    private final DatabaseHelper dbHelper;

    public ClosingSummaryAdapter(Context context, List<ClosingSummary> list) {
        this.context           = context;
        this.closingSummaryList = list;
        this.dbHelper          = new DatabaseHelper(context);
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_closing_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClosingSummary summary = closingSummaryList.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        holder.tvDate.setText(summary.getDate());
        holder.tvClosingAmount.setText(
                fmt0(summary.getTotalSalePrice()));
        holder.tvTotalPurchasePrice.setText(
                fmt0(summary.getTotalPurchasePrice()));
        holder.tvTotalSalePrice.setText(
                fmt0(summary.getTotalSalePrice()));

        // ── Sales rows ──────────────────────────────────────────────────────
        holder.linearLayoutSales.removeAllViews();
        if (summary.getSalesSummary() != null) {
            for (CartItem item : summary.getSalesSummary()) {
                View row = inflater.inflate(
                        R.layout.item_sale_row, holder.linearLayoutSales, false);
                ((TextView) row.findViewById(R.id.tvProductName))
                        .setText(item.getProductName());
                ((TextView) row.findViewById(R.id.tvQty))
                        .setText(fmt1(item.getQuantity()));
                ((TextView) row.findViewById(R.id.tvUnitPrice))
                        .setText(fmt0(item.getUnitPrice()));
                ((TextView) row.findViewById(R.id.tvTotal))
                        .setText(fmt0bif(item.getQuantity() * item.getUnitPrice()));
                holder.linearLayoutSales.addView(row);
            }
        }

        // ── Stock rows ──────────────────────────────────────────────────────
        holder.linearLayoutStock.removeAllViews();
        if (summary.getStockSummary() != null) {
            for (Stock stock : summary.getStockSummary()) {
                View row = inflater.inflate(
                        R.layout.item_sale_row, holder.linearLayoutStock, false);
                String productName = dbHelper.getProductName(stock.getProductID());
                double pu = stock.getStockQuantity() > 0
                        ? stock.getTotalAmountUsed() / stock.getStockQuantity() : 0;
                ((TextView) row.findViewById(R.id.tvProductName))
                        .setText(productName != null ? productName : stock.getProductID());
                ((TextView) row.findViewById(R.id.tvQty))
                        .setText(String.valueOf(stock.getStockQuantity()));
                ((TextView) row.findViewById(R.id.tvUnitPrice))
                        .setText(fmt0(pu));
                ((TextView) row.findViewById(R.id.tvTotal))
                        .setText(fmt0bif(stock.getTotalAmountUsed()));
                holder.linearLayoutStock.addView(row);
            }
        }
    }

    @Override
    public int getItemCount() {
        return closingSummaryList != null ? closingSummaryList.size() : 0;
    }

    // ── ViewHolder ────────────────────────────────────────────────────────────

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView     tvDate;
        final TextView     tvClosingAmount;
        final TextView     tvTotalPurchasePrice;
        final TextView     tvTotalSalePrice;
        final LinearLayout linearLayoutSales;
        final LinearLayout linearLayoutStock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate               = itemView.findViewById(R.id.tvDate);
            tvClosingAmount      = itemView.findViewById(R.id.tvClosingAmount);
            tvTotalPurchasePrice = itemView.findViewById(R.id.tvTotalPurchasePrice);
            tvTotalSalePrice     = itemView.findViewById(R.id.tvTotalSalePrice);
            linearLayoutSales    = itemView.findViewById(R.id.linearLayoutSales);
            linearLayoutStock    = itemView.findViewById(R.id.linearLayoutStock);
        }
    }

    // ── Formatters ────────────────────────────────────────────────────────────

    private static String fmt0(double v) {
        return String.format(Locale.getDefault(), "%.0f", v);
    }

    private static String fmt0bif(double v) {
        return String.format(Locale.getDefault(), "%.0f BIF", v);
    }

    private static String fmt1(double v) {
        return String.format(Locale.getDefault(), "%.1f", v);
    }
}