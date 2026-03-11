package com.example.myadermoshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.internal.view.SupportMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myadermoshop.DatabaseHelper;
import java.util.List;

/* loaded from: classes.dex */
public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final HttpService httpService;
    private final List<Stock> stocks;

    public StockAdapter(List<Stock> list, DatabaseHelper databaseHelper, Context context, HttpService httpService) {
        this.stocks = list;
        this.dbHelper = databaseHelper;
        this.context = context;
        this.httpService = httpService;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public StockViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new StockViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_product_purchase, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(StockViewHolder stockViewHolder, final int i) {
        final Stock stock = this.stocks.get(i);
        stockViewHolder.productTitle.setText(this.dbHelper.getProductName(stock.getProductID()));
        stockViewHolder.totalAmount.setText("Montant Total: " + stock.getTotalAmountUsed() + " BIF");
        stockViewHolder.pieces.setText(stock.getStockQuantity() + " pièces");
        stockViewHolder.manufactureDate.setText(stock.getStockManDate());
        stockViewHolder.expirationDate.setText(stock.getStockExpDate());
        stockViewHolder.supplierName.setText("Fournisseur: " + stock.getSupplierName());
        stockViewHolder.supplierContact.setText("Contact: " + stock.getSupplierContact());
        stockViewHolder.pricePerUnit.setText("Prix par Unité (PU): " + (stock.getTotalAmountUsed() / stock.getStockQuantity()) + " BIF");
        double expectedSellingPrice = this.dbHelper.getExpectedSellingPrice(stock.getProductID());
        stockViewHolder.benefice.setText("Bénéfice: " + ((stock.getStockQuantity() * expectedSellingPrice) - stock.getTotalAmountUsed()) + " BIF");
        stockViewHolder.totalAmountUsed.setText(stock.getTotalAmountUsed() + " BIF");
        stockViewHolder.expectedSellingAmount.setText((expectedSellingPrice * stock.getStockQuantity()) + " BIF");
        OperationStatus operationStatus = this.dbHelper.getOperationStatus(stock.getStatusID());
        if (operationStatus != null) {
            stockViewHolder.decision.setText(operationStatus.getStatusLabel());
        }
        stockViewHolder.stockDate.setText(stock.getStockDateTime());
        if (stock.getUploadStatus() == 1) {
            stockViewHolder.uploadStatus.setText("Téléchargé");
            stockViewHolder.uploadStatus.setTextColor(-16711936);
            stockViewHolder.buttonResend.setVisibility(8);
            stockViewHolder.buttonCancel.setVisibility(8);
        } else {
            stockViewHolder.uploadStatus.setText("Non Téléchargé");
            stockViewHolder.uploadStatus.setTextColor(SupportMenu.CATEGORY_MASK);
            stockViewHolder.buttonResend.setVisibility(0);
            stockViewHolder.buttonCancel.setVisibility(0);
            stockViewHolder.buttonCancel.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.StockAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    StockAdapter.this.m126lambda$onBindViewHolder$0$comexamplemyadermoshopStockAdapter(stock, i, view);
                }
            });
            stockViewHolder.buttonResend.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.StockAdapter$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    StockAdapter.this.m127lambda$onBindViewHolder$1$comexamplemyadermoshopStockAdapter(stock, view);
                }
            });
        }
        stockViewHolder.buttonViewStock.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.StockAdapter$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                StockAdapter.this.m128lambda$onBindViewHolder$2$comexamplemyadermoshopStockAdapter(stock, view);
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$0$com-example-myadermoshop-StockAdapter, reason: not valid java name */
    /* synthetic */ void m126lambda$onBindViewHolder$0$comexamplemyadermoshopStockAdapter(Stock stock, int i, View view) {
        deleteStock(stock, i);
    }

    /* renamed from: lambda$onBindViewHolder$1$com-example-myadermoshop-StockAdapter, reason: not valid java name */
    /* synthetic */ void m127lambda$onBindViewHolder$1$comexamplemyadermoshopStockAdapter(Stock stock, View view) {
        resendStockData(stock);
    }

    /* renamed from: lambda$onBindViewHolder$2$com-example-myadermoshop-StockAdapter, reason: not valid java name */
    /* synthetic */ void m128lambda$onBindViewHolder$2$comexamplemyadermoshopStockAdapter(Stock stock, View view) {
        Intent intent = new Intent(this.context, StockDetailsActivity.class);
        intent.putExtra(DatabaseHelper.COLUMN_STOCK_ID, stock.getStockID());
        this.context.startActivity(intent);
    }

    private void deleteStock(Stock stock, int i) {
        if (this.dbHelper.deleteStock(stock.getStockID())) {
            this.stocks.remove(i);
            notifyItemRemoved(i);
            Toast.makeText(this.context, "Stock supprimé avec succès", 0).show();
            return;
        }
        Toast.makeText(this.context, "Échec de la suppression du stock", 0).show();
    }

    private void resendStockData(Stock stock) {
        this.dbHelper.uploadStockDataToServer(this.httpService, stock, new DatabaseHelper.UploadCallback() { // from class: com.example.myadermoshop.StockAdapter.1
            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onSuccess(String str) {
                Toast.makeText(StockAdapter.this.context, str, 0).show();
                StockAdapter.this.notifyDataSetChanged();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onFailure(String str) {
                Toast.makeText(StockAdapter.this.context, str, 0).show();
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.stocks.size();
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView benefice;
        Button buttonCancel;
        Button buttonResend;
        Button buttonViewStock;
        TextView decision;
        TextView expectedSellingAmount;
        TextView expirationDate;
        TextView manufactureDate;
        TextView pieces;
        TextView pricePerUnit;
        TextView productTitle;
        TextView stockDate;
        TextView supplierContact;
        TextView supplierName;
        TextView totalAmount;
        TextView totalAmountUsed;
        TextView uploadStatus;

        public StockViewHolder(View view) {
            super(view);
            this.productTitle = view.findViewById(R.id.textViewProductTitle);
            this.totalAmount = view.findViewById(R.id.textViewTotalAmount);
            this.pieces = view.findViewById(R.id.textViewPieces);
            this.manufactureDate = view.findViewById(R.id.textViewManufactureDate);
            this.expirationDate = view.findViewById(R.id.textViewExpirationDate);
            this.supplierName = view.findViewById(R.id.textViewSupplierName);
            this.supplierContact = view.findViewById(R.id.textViewSupplierContact);
            this.pricePerUnit = view.findViewById(R.id.textViewPU);
            this.benefice = view.findViewById(R.id.tvBenefice);
            this.totalAmountUsed = view.findViewById(R.id.tvTotalAmountUsed);
            this.expectedSellingAmount = view.findViewById(R.id.tvExpectedSellingAmount);
            this.decision = view.findViewById(R.id.tvDecision);
            this.stockDate = view.findViewById(R.id.tvDate);
            this.uploadStatus = view.findViewById(R.id.tvUploadStatus);
            this.buttonResend = view.findViewById(R.id.buttonResend);
            this.buttonViewStock = view.findViewById(R.id.buttonViewStock);
            this.buttonCancel = view.findViewById(R.id.buttonCancel);
        }
    }
}
