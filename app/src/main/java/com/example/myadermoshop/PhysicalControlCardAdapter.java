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

public class PhysicalControlCardAdapter extends PagerAdapter {
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final List<Double> foundQuantities;
    private final List<Product> productList;

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    public PhysicalControlCardAdapter(Context context, List<Product> list, List<Double> list2, DatabaseHelper databaseHelper) {
        this.context = context;
        this.productList = list;
        this.foundQuantities = list2;
        this.dbHelper = databaseHelper;
    }

    @Override
    public int getCount() {
        return this.productList.size();
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup viewGroup, final int i) {
        View viewInflate = LayoutInflater.from(this.context).inflate(R.layout.item_physical_control_card, viewGroup, false);
        Product product = this.productList.get(i);
        
        ImageView imageView = viewInflate.findViewById(R.id.imageViewProduct);
        TextView textView = viewInflate.findViewById(R.id.textViewProductName);
        TextView textView2 = viewInflate.findViewById(R.id.textViewExpectedItems);
        EditText editText = viewInflate.findViewById(R.id.editTextFoundQuantity);
        
        textView.setText(product.getProductName());
        textView2.setText("Attendus: " + String.format(Locale.getDefault(), "%.2f", getTotalRemaining(product)));
        editText.setText(String.format(Locale.getDefault(), "%.2f", this.foundQuantities.get(i)));
        
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                try {
                    PhysicalControlCardAdapter.this.foundQuantities.set(i, Double.parseDouble(charSequence.toString()));
                } catch (NumberFormatException unused) {
                    PhysicalControlCardAdapter.this.foundQuantities.set(i, 0.0d);
                }
            }
        });
        
        loadProductImage(product.getProductID(), imageView);
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

    private void loadProductImage(String str, ImageView imageView) {
        String productPhotoName = this.dbHelper.getProductPhotoName(str);
        if (productPhotoName != null && !productPhotoName.isEmpty()) {
            File file = new File(this.context.getFilesDir(), "products/" + productPhotoName);
            if (file.exists()) {
                Glide.with(this.context).load(file).into(imageView);
                return;
            }
        }
        imageView.setImageResource(R.mipmap.ic_adermologo_foreground);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
        viewGroup.removeView((View) obj);
    }
}
