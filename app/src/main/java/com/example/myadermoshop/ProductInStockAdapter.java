package com.example.myadermoshop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class ProductInStockAdapter
        extends RecyclerView.Adapter<ProductInStockAdapter.ProductInStockViewHolder> {

    private final Context        context;
    private final DatabaseHelper dbHelper;
    private final List<Product>  products;

    public ProductInStockAdapter(List<Product> products,
                                 DatabaseHelper dbHelper,
                                 Context context) {
        this.products  = products;
        this.dbHelper  = dbHelper;
        this.context   = context;
    }

    @NonNull
    @Override
    public ProductInStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_product_in_stock, parent, false);
        return new ProductInStockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductInStockViewHolder holder, int position) {
        Product product   = products.get(position);
        String  productID = product.getProductID();

        holder.textViewId.setText("PRODUCT ID : " + productID);
        holder.textViewProductName.setText(dbHelper.getProductName(productID));

        double price = dbHelper.getExpectedSellingPrice(productID);
        holder.textViewProductPrice.setText(
                String.format(Locale.getDefault(), "%.2f BIF", price));

        // ── Stock quantity ──
        double remaining = product.isUsingInstances()
                ? dbHelper.getRemainingInstancesForProduct(productID)
                : dbHelper.getRemainingQuantityForProduct(productID);
        holder.textViewTotalProducts.setText(formatQuantity(remaining) + " pièces");

        // ── Last physical control ──
        PhysicalControle control = dbHelper.getMostRecentPhysicalControl(productID);
        if (control != null) {
            holder.textViewLastControlDate.setText(control.getControleDateTime());
            holder.textViewExpectedQuantity.setText(
                    "Attendu : " + formatQuantity(
                            dbHelper.getExpectedQuantityFromControl(productID)));
            holder.textViewFoundQuantity.setText(
                    "Trouvé : " + formatQuantity(
                            dbHelper.getActualQuantityFromControl(productID)));
        } else {
            holder.textViewLastControlDate.setText("N/A");
            holder.textViewExpectedQuantity.setText("Attendu : N/A");
            holder.textViewFoundQuantity.setText("Trouvé : N/A");
        }

        // ── Stock table ──
        holder.stockTable.removeAllViews();
        holder.stockTable.addView(createHeaderRow());

        List<Stock> stocks = product.isUsingInstances()
                ? dbHelper.getStocksForProduct(productID)
                : dbHelper.getStocksForProductFIFO(productID);

        for (Stock stock : stocks) {
            String qty, remaining2;
            if (product.isUsingInstances()) {
                qty        = formatQuantity(dbHelper.getTotalInstancesForStock(stock.getStockID()));
                remaining2 = formatQuantity(dbHelper.getRemainingInstancesForStock(stock.getStockID()));
            } else {
                qty        = formatQuantity(stock.getStockQuantity());
                remaining2 = formatQuantity(dbHelper.getRemainingQuantityForStock(
                        productID, stock.getStockID()));
            }
            TableRow row = new TableRow(context);
            row.addView(createBodyTextView(stock.getStockDateTime(), 11));
            row.addView(createBodyTextView(qty,                       14));
            row.addView(createBodyTextView(remaining2,                14));
            row.addView(createBodyTextView(stock.getStockManDate(),   11));
            row.addView(createBodyTextView(stock.getStockExpDate(),   11));
            holder.stockTable.addView(row);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    // ── Table helpers ─────────────────────────────────────────────────────────

    private TableRow createHeaderRow() {
        TableRow row = new TableRow(context);
        // ── FIXED: Color.parseColor() instead of deprecated getColor() ──
        row.setBackgroundColor(Color.parseColor("#007AFF")); // ios_blue
        row.addView(createHeaderTextView("Date du stock"));
        row.addView(createHeaderTextView("Qté"));
        row.addView(createHeaderTextView("Restant"));
        row.addView(createHeaderTextView("Date fab."));
        row.addView(createHeaderTextView("Date exp."));
        return row;
    }

    private TextView createHeaderTextView(String text) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1.0f));
        tv.setText(text);
        tv.setTextSize(12f);
        tv.setTypeface(null, Typeface.BOLD);
        // ── FIXED: Color.parseColor() instead of deprecated getColor() ──
        tv.setTextColor(Color.WHITE);
        tv.setPadding(8, 4, 8, 4);
        tv.setBackgroundResource(R.drawable.cell_border);
        return tv;
    }

    private TextView createBodyTextView(String text, int textSize) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new TableRow.LayoutParams(0, -1, 1.0f));
        tv.setText(text);
        tv.setTextSize(textSize);
        tv.setPadding(8, 4, 8, 4);
        tv.setBackgroundResource(R.drawable.cell_border);
        return tv;
    }

    private String formatQuantity(double qty) {
        if (qty == Math.floor(qty)) {
            return String.format(Locale.getDefault(), "%d", (int) qty);
        }
        return String.format(Locale.getDefault(), "%.2f", qty);
    }

    // ── ViewHolder ────────────────────────────────────────────────────────────

    public static class ProductInStockViewHolder extends RecyclerView.ViewHolder {
        TextView    textViewId;
        TextView    textViewProductName;
        TextView    textViewTotalProducts;
        TextView    textViewProductPrice;
        TextView    textViewLastControlDate;
        TextView    textViewExpectedQuantity;
        TextView    textViewFoundQuantity;
        TableLayout stockTable;

        ProductInStockViewHolder(@NonNull View view) {
            super(view);
            textViewId               = view.findViewById(R.id.textViewId);
            textViewProductName      = view.findViewById(R.id.textViewProductName);
            textViewTotalProducts    = view.findViewById(R.id.textViewTotalProducts);
            textViewProductPrice     = view.findViewById(R.id.textViewProductPrice);
            textViewLastControlDate  = view.findViewById(R.id.textViewLastControlDate);
            textViewExpectedQuantity = view.findViewById(R.id.textViewExpectedQuantity);
            textViewFoundQuantity    = view.findViewById(R.id.textViewFoundQuantity);
            stockTable               = view.findViewById(R.id.stockTable);
        }
    }
}