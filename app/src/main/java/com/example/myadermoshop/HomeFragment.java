package com.example.myadermoshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<Cart> cartList;
    private RecyclerView recyclerViewSales;
    private SaleCardAdapter saleCardAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        
        this.recyclerViewSales = viewInflate.findViewById(R.id.recyclerViewSales);
        this.swipeRefreshLayout = viewInflate.findViewById(R.id.swipeRefreshLayout);
        FloatingActionButton floatingActionButton = viewInflate.findViewById(R.id.fab_add_sale);
        
        this.recyclerViewSales.setLayoutManager(new LinearLayoutManager(getContext()));
        this.cartList = new ArrayList<>();
        this.saleCardAdapter = new SaleCardAdapter(getContext(), this.cartList);
        this.recyclerViewSales.setAdapter(this.saleCardAdapter);
        
        loadTodayCarts();
        
        this.swipeRefreshLayout.setOnRefreshListener(this::loadTodayCarts);
        
        floatingActionButton.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), AddSaleActivity.class));
        });
        
        return viewInflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTodayCarts();
    }

    private void loadTodayCarts() {
        if (this.swipeRefreshLayout != null) {
            this.swipeRefreshLayout.setRefreshing(true);
        }
        
        new Thread(() -> {
            final List<Cart> todayCarts = new DatabaseHelper(getContext()).getTodayCarts();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    this.cartList.clear();
                    this.cartList.addAll(todayCarts);
                    this.saleCardAdapter.notifyDataSetChanged();
                    if (this.swipeRefreshLayout != null) {
                        this.swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
