package com.example.myadermoshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<Cart> cartList;
    private RecyclerView recyclerViewSales;
    private SaleCardAdapter saleCardAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewSales  = view.findViewById(R.id.recyclerViewSales);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        ExtendedFloatingActionButton fab =
                view.findViewById(R.id.fab_add_sale);

        recyclerViewSales.setLayoutManager(
                new LinearLayoutManager(getContext()));

        cartList = new ArrayList<>();
        saleCardAdapter = new SaleCardAdapter(getContext(), cartList);
        recyclerViewSales.setAdapter(saleCardAdapter);

        loadTodayCarts();

        swipeRefreshLayout.setOnRefreshListener(this::loadTodayCarts);

        fab.setOnClickListener(v ->
                startActivity(new Intent(getContext(),
                        AddSaleActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTodayCarts();
    }

    private void loadTodayCarts() {
        swipeRefreshLayout.setRefreshing(true);
        new Thread(() -> {
            final List<Cart> carts =
                    new DatabaseHelper(getContext()).getTodayCarts();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    cartList.clear();
                    cartList.addAll(carts);
                    saleCardAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        }).start();
    }
}