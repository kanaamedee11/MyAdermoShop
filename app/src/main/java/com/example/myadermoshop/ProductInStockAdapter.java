package com.example.myadermoshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class ProductInStockAdapter extends RecyclerView.Adapter<ProductInStockAdapter.ProductInStockViewHolder> {
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final List<Product> products;

    public ProductInStockAdapter(List<Product> list, DatabaseHelper databaseHelper, Context context) {
        this.products = list;
        this.dbHelper = databaseHelper;
        this.context = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ProductInStockViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ProductInStockViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_product_in_stock, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ProductInStockViewHolder productInStockViewHolder, int i) {
        double remainingQuantityForProduct;
        List<Stock> stocksForProductFIFO;
        String quantity;
        String quantity2;
        Product product = this.products.get(i);
        String productID = product.getProductID();
        productInStockViewHolder.textViewId.setText("PRODUCT ID : " + productID);
        productInStockViewHolder.textViewProductName.setText(this.dbHelper.getProductName(productID));
        Double dValueOf = Double.valueOf(this.dbHelper.getExpectedSellingPrice(productID));
        productInStockViewHolder.textViewProductPrice.setText(dValueOf != null ? String.format(Locale.getDefault(), "%.2f BIF", dValueOf) : "N/A");
        if (product.isUsingInstances()) {
            remainingQuantityForProduct = this.dbHelper.getRemainingInstancesForProduct(productID);
        } else {
            remainingQuantityForProduct = this.dbHelper.getRemainingQuantityForProduct(productID);
        }
        productInStockViewHolder.textViewTotalProducts.setText(formatQuantity(remainingQuantityForProduct) + " pièces");
        PhysicalControle mostRecentPhysicalControl = this.dbHelper.getMostRecentPhysicalControl(productID);
        if (mostRecentPhysicalControl != null) {
            productInStockViewHolder.textViewLastControlDate.setText(mostRecentPhysicalControl.getControleDateTime());
            double expectedQuantityFromControl = this.dbHelper.getExpectedQuantityFromControl(productID);
            double actualQuantityFromControl = this.dbHelper.getActualQuantityFromControl(productID);
            productInStockViewHolder.textViewExpectedQuantity.setText("Attendu : " + formatQuantity(expectedQuantityFromControl));
            productInStockViewHolder.textViewFoundQuantity.setText("Trouvé : " + formatQuantity(actualQuantityFromControl));
        } else {
            productInStockViewHolder.textViewLastControlDate.setText("N/A");
            productInStockViewHolder.textViewExpectedQuantity.setText("Attendu : N/A");
            productInStockViewHolder.textViewFoundQuantity.setText("Trouvé : N/A");
        }
        TableLayout tableLayout = productInStockViewHolder.stockTable;
        tableLayout.removeAllViews();
        tableLayout.addView(createHeaderRow());
        if (product.isUsingInstances()) {
            stocksForProductFIFO = this.dbHelper.getStocksForProduct(productID);
        } else {
            stocksForProductFIFO = this.dbHelper.getStocksForProductFIFO(productID);
        }
        for (Stock stock : stocksForProductFIFO) {
            if (product.isUsingInstances()) {
                int totalInstancesForStock = this.dbHelper.getTotalInstancesForStock(stock.getStockID());
                int remainingInstancesForStock = this.dbHelper.getRemainingInstancesForStock(stock.getStockID());
                quantity = formatQuantity(totalInstancesForStock);
                quantity2 = formatQuantity(remainingInstancesForStock);
            } else {
                double stockQuantity = stock.getStockQuantity();
                double remainingQuantityForStock = this.dbHelper.getRemainingQuantityForStock(productID, stock.getStockID());
                quantity = formatQuantity(stockQuantity);
                quantity2 = formatQuantity(remainingQuantityForStock);
            }
            TableRow tableRow = new TableRow(this.context);
            tableRow.addView(createBodyTextView(stock.getStockDateTime(), 11));
            tableRow.addView(createBodyTextView(quantity, 14));
            tableRow.addView(createBodyTextView(quantity2, 14));
            tableRow.addView(createBodyTextView(stock.getStockManDate(), 11));
            tableRow.addView(createBodyTextView(stock.getStockExpDate(), 11));
            tableLayout.addView(tableRow);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.products.size();
    }

    private TableRow createHeaderRow() {
        TableRow tableRow = new TableRow(this.context);
        tableRow.setBackgroundColor(this.context.getResources().getColor(R.color.colorPrimary));
        tableRow.addView(createHeaderTextView("Date du stock"));
        tableRow.addView(createHeaderTextView("Qté"));
        tableRow.addView(createHeaderTextView("Restant"));
        tableRow.addView(createHeaderTextView("Date fab."));
        tableRow.addView(createHeaderTextView("Date exp."));
        return tableRow;
    }

    private TextView createHeaderTextView(String str) {
        TextView textView = new TextView(this.context);
        textView.setLayoutParams(new TableRow.LayoutParams(0, -1, 1.0f));
        textView.setText(str);
        textView.setTextSize(12.0f);
        textView.setTypeface(null, 1);
        textView.setTextColor(this.context.getResources().getColor(android.R.color.white));
        textView.setPadding(8, 4, 8, 4);
        textView.setBackgroundResource(R.drawable.cell_border);
        return textView;
    }

    private TextView createBodyTextView(String str, int i) {
        TextView textView = new TextView(this.context);
        textView.setLayoutParams(new TableRow.LayoutParams(0, -1, 1.0f));
        textView.setText(str);
        textView.setTextSize(i);
        textView.setPadding(8, 4, 8, 4);
        textView.setBackgroundResource(R.drawable.cell_border);
        return textView;
    }

    private String formatQuantity(double d) {
        return d == Math.floor(d) ? String.format(Locale.getDefault(), "%d", (int) d) : String.format(Locale.getDefault(), "%.2f", d);
    }

    public static class ProductInStockViewHolder extends RecyclerView.ViewHolder {
        TableLayout stockTable;
        TextView textViewExpectedQuantity;
        TextView textViewFoundQuantity;
        TextView textViewId;
        TextView textViewLastControlDate;
        TextView textViewProductName;
        TextView textViewProductPrice;
        TextView textViewTotalProducts;

        ProductInStockViewHolder(View view) {
            super(view);
            this.textViewId = view.findViewById(R.id.textViewId);
            this.textViewProductName = view.findViewById(R.id.textViewProductName);
            this.textViewTotalProducts = view.findViewById(R.id.textViewTotalProducts);
            this.textViewProductPrice = view.findViewById(R.id.textViewProductPrice);
            this.textViewLastControlDate = view.findViewById(R.id.textViewLastControlDate);
            this.textViewExpectedQuantity = view.findViewById(R.id.textViewExpectedQuantity);
            this.textViewFoundQuantity = view.findViewById(R.id.textViewFoundQuantity);
            this.stockTable = view.findViewById(R.id.stockTable);
        }
    }
}