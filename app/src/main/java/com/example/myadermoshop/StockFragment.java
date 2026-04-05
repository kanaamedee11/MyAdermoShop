package com.example.myadermoshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class StockFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private ProductInStockAdapter productInStockAdapter;
    private RecyclerView recyclerViewStock;
    private SearchView searchViewStock;
    private List<Product> allProducts = new ArrayList<>();
    private final List<Product> filteredProducts = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_stock, viewGroup, false);
        
        this.recyclerViewStock = viewInflate.findViewById(R.id.recyclerViewStock);
        this.searchViewStock = viewInflate.findViewById(R.id.searchViewStock);
        this.recyclerViewStock.setLayoutManager(new LinearLayoutManager(getContext()));
        
        this.databaseHelper = new DatabaseHelper(getContext());
        
        List<Product> distinctProductsFromStock = databaseHelper.getDistinctProductsFromStock();
        this.allProducts = distinctProductsFromStock;
        this.filteredProducts.clear();
        this.filteredProducts.addAll(distinctProductsFromStock);
        
        this.productInStockAdapter = new ProductInStockAdapter(this.filteredProducts, this.databaseHelper, getContext());
        this.recyclerViewStock.setAdapter(this.productInStockAdapter);
        
        this.searchViewStock.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });
        
        return viewInflate;
    }

    private void filterProducts(String query) {
        this.filteredProducts.clear();
        if (query == null || query.trim().isEmpty()) {
            this.filteredProducts.addAll(this.allProducts);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (Product product : this.allProducts) {
                String productName = this.databaseHelper.getProductName(product.getProductID());
                if (productName != null && productName.toLowerCase().contains(lowerCaseQuery)) {
                    this.filteredProducts.add(product);
                }
            }
        }
        this.productInStockAdapter.notifyDataSetChanged();
    }
}
