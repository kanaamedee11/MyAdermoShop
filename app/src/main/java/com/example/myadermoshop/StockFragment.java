package com.example.myadermoshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class StockFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private ProductInStockAdapter productInStockAdapter;
    private RecyclerView recyclerViewStock;
    private SearchView searchViewStock;
    private List<Product> allProducts = new ArrayList();
    private final List<Product> filteredProducts = new ArrayList();

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_stock, viewGroup, false);
        this.recyclerViewStock = viewInflate.findViewById(R.id.recyclerViewStock);
        this.searchViewStock = viewInflate.findViewById(R.id.searchViewStock);
        this.recyclerViewStock.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        this.databaseHelper = databaseHelper;
        List<Product> distinctProductsFromStock = databaseHelper.getDistinctProductsFromStock();
        this.allProducts = distinctProductsFromStock;
        this.filteredProducts.addAll(distinctProductsFromStock);
        ProductInStockAdapter productInStockAdapter = new ProductInStockAdapter(this.filteredProducts, this.databaseHelper, getContext());
        this.productInStockAdapter = productInStockAdapter;
        this.recyclerViewStock.setAdapter(productInStockAdapter);
        this.searchViewStock.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // from class: com.example.myadermoshop.StockFragment.1
            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextChange(String str) {
                StockFragment.this.filterProducts(str);
                return true;
            }
        });
        return viewInflate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void filterProducts(String str) {
        this.filteredProducts.clear();
        if (str == null || str.trim().isEmpty()) {
            this.filteredProducts.addAll(this.allProducts);
        } else {
            String lowerCase = str.toLowerCase();
            for (Product product : this.allProducts) {
                String productName = this.databaseHelper.getProductName(product.getProductID());
                if (productName != null && productName.toLowerCase().contains(lowerCase)) {
                    this.filteredProducts.add(product);
                }
            }
        }
        this.productInStockAdapter.notifyDataSetChanged();
    }
}