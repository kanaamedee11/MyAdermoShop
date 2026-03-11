package com.example.myadermoshop;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LowStockFragment extends Fragment {
    private DatabaseHelper databaseHelper;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_low_stock, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.databaseHelper = new DatabaseHelper(requireContext());
        List<LowStockProduct> lowStockProducts = getLowStockProducts();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLowStock);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new LowStockAdapter(requireContext(), lowStockProducts));
    }

    private List<LowStockProduct> getLowStockProducts() {
        ArrayList arrayList = new ArrayList();
        Cursor lowStockProducts = this.databaseHelper.getLowStockProducts();
        if (lowStockProducts != null && lowStockProducts.moveToFirst()) {
            do {
                arrayList.add(new LowStockProduct(lowStockProducts.getString(lowStockProducts.getColumnIndex("productID")), lowStockProducts.getString(lowStockProducts.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_NAME)), lowStockProducts.getInt(lowStockProducts.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_SEUIL_STOCK)), lowStockProducts.getInt(lowStockProducts.getColumnIndex("availableStock"))));
            } while (lowStockProducts.moveToNext());
            lowStockProducts.close();
        }
        return arrayList;
    }
}