package com.example.myadermoshop;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class PhysicalControlCardAdapter extends PagerAdapter {
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final List<Double> foundQuantities;
    private final List<Product> productList;

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    public PhysicalControlCardAdapter(Context context, List<Product> list, List<Double> list2, DatabaseHelper databaseHelper) {
        this.context = context;
        this.productList = list;
        this.foundQuantities = list2;
        this.dbHelper = databaseHelper;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.productList.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup viewGroup, final int i) {
        View viewInflate = LayoutInflater.from(this.context).inflate(R.layout.item_physical_control_card, viewGroup, false);
        Product product = this.productList.get(i);
        
        TextView textView = viewInflate.findViewById(R.id.tvProductName);
        TextView textView2 = viewInflate.findViewById(R.id.tvExpectedQty);
        TextView textViewActual = viewInflate.findViewById(R.id.tvActualQty);
        
        textView.setText(product.getProductName());
        textView2.setText(String.format(Locale.getDefault(), "Prévu: %.2f", getTotalRemaining(product)));
        textViewActual.setText(String.format(Locale.getDefault(), "Réel: %.2f", this.foundQuantities.get(i)));

        // Note: The layout item_physical_control_card.xml does not have an EditText or an ImageView with an ID.
        // If editing is required, the layout might need to be updated.
        
        viewGroup.addView(viewInflate);
        return viewInflate;
    }

    private double getTotalRemaining(Product product) {
        double remainingInstancesForStock = 0.0d;
        if (product.isUsingInstances()) {
            for (Stock stock : this.dbHelper.getStocksForProduct(product.getProductID())) {
                remainingInstancesForStock += this.dbHelper.getRemainingInstancesForStock(stock.getStockID());
            }
            return remainingInstancesForStock;
        }
        double totalSoldWithoutInstances = this.dbHelper.getTotalSoldWithoutInstances(product.getProductID()) + this.dbHelper.getTotalDeterioratedWithoutInstances(product.getProductID());
        Iterator<Stock> it = this.dbHelper.getStocksForProductFIFO(product.getProductID()).iterator();
        double d = 0.0d;
        while (it.hasNext()) {
            double stockQuantity = it.next().getStockQuantity();
            double dMin = Math.min(totalSoldWithoutInstances, stockQuantity);
            d += stockQuantity - dMin;
            totalSoldWithoutInstances -= dMin;
            if (totalSoldWithoutInstances <= 0.0d) {
                break;
            }
        }
        return d;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
        viewGroup.removeView((View) obj);
    }
}
