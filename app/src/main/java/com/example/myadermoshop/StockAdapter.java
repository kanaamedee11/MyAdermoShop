package com.example.myadermoshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import java.util.Locale;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private final Context context;
    private final DatabaseHelper dbHelper;
    private final HttpService httpService;
    private final List<Stock> stocks;

    public StockAdapter(List<Stock> stocks,
                        DatabaseHelper dbHelper,
                        Context context,
                        HttpService httpService) {
        this.stocks      = stocks;
        this.dbHelper    = dbHelper;
        this.context     = context;
        this.httpService = httpService;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_product_purchase, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, final int position) {
        final Stock stock = stocks.get(position);

        // ── Header ──
        holder.productTitle.setText(dbHelper.getProductName(stock.getProductID()));
        holder.totalAmount.setText(String.format(Locale.getDefault(),
                "%.2f BIF", stock.getTotalAmountUsed()));

        // ── Basic info ──
        holder.pieces.setText(stock.getStockQuantity() + " pièces");
        holder.manufactureDate.setText(stock.getStockManDate());
        holder.expirationDate.setText(stock.getStockExpDate());
        holder.supplierName.setText(stock.getSupplierName());
        holder.supplierContact.setText(stock.getSupplierContact());

        // ── Financial calculations ──
        double totalUsed      = stock.getTotalAmountUsed();
        int    qty            = stock.getStockQuantity();
        double sellingPrice   = dbHelper.getExpectedSellingPrice(stock.getProductID());
        double pricePerUnit   = qty > 0 ? totalUsed / qty : 0;
        double expectedTotal  = sellingPrice * qty;
        double benefice       = expectedTotal - totalUsed;

        holder.pricePerUnit.setText(String.format(Locale.getDefault(),
                "%.2f BIF", pricePerUnit));
        holder.benefice.setText(String.format(Locale.getDefault(),
                "%.2f BIF", benefice));
        holder.totalAmountUsed.setText(String.format(Locale.getDefault(),
                "%.2f BIF", totalUsed));
        holder.expectedSellingAmount.setText(String.format(Locale.getDefault(),
                "%.2f BIF", expectedTotal));

        // ── Decision / status ──
        OperationStatus status = dbHelper.getOperationStatus(stock.getStatusID());
        if (status != null) {
            holder.decision.setText(status.getStatusLabel());
        }
        holder.stockDate.setText(stock.getStockDateTime());

        // ── Upload status ──
        if (stock.getUploadStatus() == 1) {
            holder.uploadStatus.setText("Téléchargé");
            holder.buttonResend.setVisibility(View.GONE);
            holder.buttonCancel.setVisibility(View.GONE);
        } else {
            holder.uploadStatus.setText("Non Téléchargé");
            holder.buttonResend.setVisibility(View.VISIBLE);
            holder.buttonCancel.setVisibility(View.VISIBLE);

            holder.buttonCancel.setOnClickListener(v -> deleteStock(stock, position));
            holder.buttonResend.setOnClickListener(v -> resendStockData(stock));
        }

        // ── View stock details ──
        holder.buttonViewStock.setOnClickListener(v -> {
            Intent intent = new Intent(context, StockDetailsActivity.class);
            intent.putExtra(DatabaseHelper.COLUMN_STOCK_ID, stock.getStockID());
            context.startActivity(intent);
        });
    }

    private void deleteStock(Stock stock, int position) {
        if (dbHelper.deleteStock(stock.getStockID())) {
            stocks.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context,
                    "Stock supprimé avec succès",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,
                    "Échec de la suppression du stock",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void resendStockData(Stock stock) {
        dbHelper.uploadStockDataToServer(httpService, stock,
                new DatabaseHelper.UploadCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    // ── ViewHolder — IDs match card_product_purchase.xml ──
    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle;
        TextView totalAmount;
        TextView pieces;
        TextView manufactureDate;
        TextView expirationDate;
        TextView supplierName;
        TextView supplierContact;
        TextView pricePerUnit;
        TextView benefice;
        TextView totalAmountUsed;
        TextView expectedSellingAmount;
        TextView decision;
        TextView stockDate;
        TextView uploadStatus;
        MaterialButton buttonResend;
        MaterialButton buttonViewStock;
        MaterialButton buttonCancel;

        public StockViewHolder(View view) {
            super(view);
            productTitle          = view.findViewById(R.id.textViewProductTitle);
            totalAmount           = view.findViewById(R.id.textViewTotalAmount);
            pieces                = view.findViewById(R.id.textViewPieces);
            manufactureDate       = view.findViewById(R.id.textViewManufactureDate);
            expirationDate        = view.findViewById(R.id.textViewExpirationDate);
            supplierName          = view.findViewById(R.id.textViewSupplierName);
            supplierContact       = view.findViewById(R.id.textViewSupplierContact);
            pricePerUnit          = view.findViewById(R.id.textViewPU);
            benefice              = view.findViewById(R.id.tvBenefice);
            totalAmountUsed       = view.findViewById(R.id.tvTotalAmountUsed);
            expectedSellingAmount = view.findViewById(R.id.tvExpectedSellingAmount);
            decision              = view.findViewById(R.id.tvDecision);
            stockDate             = view.findViewById(R.id.tvDate);
            uploadStatus          = view.findViewById(R.id.tvUploadStatus);
            buttonResend          = view.findViewById(R.id.buttonResend);
            buttonViewStock       = view.findViewById(R.id.buttonViewStock);
            buttonCancel          = view.findViewById(R.id.buttonCancel);
        }
    }
}