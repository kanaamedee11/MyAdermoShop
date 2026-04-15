package com.example.myadermoshop;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class LowStockFragment extends Fragment {
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             @Nullable ViewGroup viewGroup,
                             @Nullable Bundle bundle) {
        return layoutInflater.inflate(
                R.layout.fragment_low_stock, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        databaseHelper = new DatabaseHelper(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLowStock);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new LowStockAdapter(
                requireContext(), getLowStockProducts()));
    }

    @SuppressLint("Range")
    private List<LowStockProduct> getLowStockProducts() {
        List<LowStockProduct> list = new ArrayList<>();
        Cursor cursor = databaseHelper.getLowStockProducts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new LowStockProduct(
                        cursor.getString(cursor.getColumnIndex("productID")),
                        cursor.getString(cursor.getColumnIndex(
                                DatabaseHelper.COLUMN_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndex(
                                DatabaseHelper.COLUMN_PRODUCT_SEUIL_STOCK)),
                        cursor.getInt(cursor.getColumnIndex("availableStock"))));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}