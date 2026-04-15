package com.example.myadermoshop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ClosingSummaryAdapter extends RecyclerView.Adapter<ClosingSummaryAdapter.ViewHolder> {
    private final List<ClosingSummary> closingSummaries;
    private final Context context;
    private final DatabaseHelper dbHelper;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button btnAccept, btnResend;
        LinearLayout linearLayoutSales, linearLayoutStock;
        TextView tvClosingAmount, tvDate, tvTotalPurchasePrice, tvTotalSalePrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotalPurchasePrice = itemView.findViewById(R.id.tvTotalPurchasePrice);
            tvTotalSalePrice = itemView.findViewById(R.id.tvTotalSalePrice);
            tvClosingAmount = itemView.findViewById(R.id.tvClosingAmount);
            linearLayoutSales = itemView.findViewById(R.id.linearLayoutSales);
            linearLayoutStock = itemView.findViewById(R.id.linearLayoutStock);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnResend = itemView.findViewById(R.id.btnResend);
        }
    }

    public ClosingSummaryAdapter(Context context, List<ClosingSummary> closingSummaries) {
        this.context = context;
        this.closingSummaries = closingSummaries;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_closing_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ClosingSummary summary = closingSummaries.get(position);

        // Date + Closing amount
        holder.tvDate.setText(formatDate(summary.getDate()));
        holder.tvTotalPurchasePrice.setText(formatDouble(summary.getTotalPurchasePrice()) + " BIF");
        holder.tvTotalSalePrice.setText(formatDouble(summary.getTotalSalePrice()) + " BIF");
        holder.tvClosingAmount.setText(formatDouble(summary.getTotalSalePrice() - summary.getTotalPurchasePrice()) + " BIF");

        // Populate sales by payment type
        populateSalesByPaymentType(holder.linearLayoutSales, summary.getSalesByPaymentType());

        // Populate stock summary
        populateStockSummary(holder.linearLayoutStock, summary.getStockSummary());

        // Button logic
        if (!dbHelper.isDateInClosureTable(summary.getDate())) {
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnResend.setVisibility(View.GONE);
            holder.btnAccept.setOnClickListener(v -> authenticateUser(summary));
        } else {
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnResend.setVisibility(View.VISIBLE);
            holder.btnResend.setOnClickListener(v -> resendClosing(summary));
        }
    }

    @Override
    public int getItemCount() {
        return closingSummaries.size();
    }

    private void populateSalesByPaymentType(LinearLayout layout, Map<String, Double> salesMap) {
        layout.removeAllViews();
        if (salesMap == null || salesMap.isEmpty()) {
            LinearLayout row = createRow();
            row.addView(createTextView("Aucune vente", 12));
            layout.addView(row);
            return;
        }
        for (Map.Entry<String, Double> entry : salesMap.entrySet()) {
            LinearLayout row = createRow();
            row.addView(createTextView(entry.getKey(), 12));
            row.addView(createTextView(formatDouble(entry.getValue()) + " BIF", 12));
            layout.addView(row);
        }
    }

    private void populateStockSummary(LinearLayout layout, List<Stock> stockList) {
        layout.removeAllViews();
        if (stockList == null || stockList.isEmpty()) {
            LinearLayout row = createRow();
            row.addView(createTextView("Aucune entrée de stock", 12));
            layout.addView(row);
            return;
        }
        for (Stock stock : stockList) {
            LinearLayout row = createRow();
            row.addView(createTextView(dbHelper.getProductName(stock.getProductID()), 12));
            row.addView(createTextView(formatDate(stock.getStockDateTime()), 11));
            row.addView(createTextView(String.valueOf(stock.getStockQuantity()), 12));
            row.addView(createTextView(stock.getStockManDate(), 11));
            row.addView(createTextView(stock.getStockExpDate(), 11));
            row.addView(createTextView(formatDouble(stock.getTotalAmountUsed()) + " BIF", 12));
            layout.addView(row);
        }
    }

    private LinearLayout createRow() {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        return row;
    }

    private TextView createTextView(String text, int size) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(size);
        tv.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        tv.setPadding(12, 6, 8, 12);
        tv.setBackgroundResource(R.drawable.cell_border);
        return tv;
    }

    private String formatDouble(double value) {
        return String.format(Locale.getDefault(), "%,.2f", value);
    }

    private String formatDate(String dateStr) {
        if (dateStr == null) return "";
        try {
            // Determine format based on whether it contains time info
            String pattern = (dateStr.length() > 10) ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd";
            Date date = new SimpleDateFormat(pattern, Locale.getDefault()).parse(dateStr);
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    private void authenticateUser(ClosingSummary summary) {
        if (context instanceof MainActivity) {
            ((MainActivity) context).authenticateUserForClosing(summary);
        } else {
            Log.e("ClosingSummaryAdapter", "Context is not an instance of MainActivity.");
        }
    }

    private void resendClosing(ClosingSummary summary) {
        if (context instanceof MainActivity) {
            ((MainActivity) context).resendClosing(summary);
        } else {
            Log.e("ClosingSummaryAdapter", "Context is not an instance of MainActivity.");
        }
    }
}
