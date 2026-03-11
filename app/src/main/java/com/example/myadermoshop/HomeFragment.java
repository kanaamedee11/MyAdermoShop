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

/* loaded from: classes.dex */
public class HomeFragment extends Fragment {
    private List<Cart> cartList;
    private RecyclerView recyclerViewSales;
    private SaleCardAdapter saleCardAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        this.recyclerViewSales = viewInflate.findViewById(R.id.recyclerViewSales);
        this.swipeRefreshLayout = viewInflate.findViewById(R.id.swipeRefreshLayout);
        FloatingActionButton floatingActionButton = viewInflate.findViewById(R.id.fab_add_sale);
        this.recyclerViewSales.setLayoutManager(new LinearLayoutManager(getContext()));
        this.cartList = new ArrayList();
        SaleCardAdapter saleCardAdapter = new SaleCardAdapter(getContext(), this.cartList);
        this.saleCardAdapter = saleCardAdapter;
        this.recyclerViewSales.setAdapter(saleCardAdapter);
        loadTodayCarts();
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.example.myadermoshop.HomeFragment$$ExternalSyntheticLambda2
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                this.f$0.loadTodayCarts();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.HomeFragment$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m98lambda$onCreateView$0$comexamplemyadermoshopHomeFragment(view);
            }
        });
        return viewInflate;
    }

    /* renamed from: lambda$onCreateView$0$com-example-myadermoshop-HomeFragment, reason: not valid java name */
    /* synthetic */ void m98lambda$onCreateView$0$comexamplemyadermoshopHomeFragment(View view) {
        startActivity(new Intent(getContext(), AddSaleActivity.class));
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        loadTodayCarts();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadTodayCarts() {
        this.swipeRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() { // from class: com.example.myadermoshop.HomeFragment$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public void run() {
                this.f$0.m97lambda$loadTodayCarts$2$comexamplemyadermoshopHomeFragment();
            }
        }).start();
    }

    /* renamed from: lambda$loadTodayCarts$2$com-example-myadermoshop-HomeFragment, reason: not valid java name */
    /* synthetic */ void m97lambda$loadTodayCarts$2$comexamplemyadermoshopHomeFragment() {
        final List<Cart> todayCarts = new DatabaseHelper(getContext()).getTodayCarts();
        getActivity().runOnUiThread(new Runnable() { // from class: com.example.myadermoshop.HomeFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public void run() {
                this.f$0.m96lambda$loadTodayCarts$1$comexamplemyadermoshopHomeFragment(todayCarts);
            }
        });
    }

    /* renamed from: lambda$loadTodayCarts$1$com-example-myadermoshop-HomeFragment, reason: not valid java name */
    /* synthetic */ void m96lambda$loadTodayCarts$1$comexamplemyadermoshopHomeFragment(List list) {
        this.cartList.clear();
        this.cartList.addAll(list);
        this.saleCardAdapter.notifyDataSetChanged();
        this.swipeRefreshLayout.setRefreshing(false);
    }
}