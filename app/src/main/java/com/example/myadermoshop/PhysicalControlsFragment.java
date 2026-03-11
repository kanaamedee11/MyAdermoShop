package com.example.myadermoshop;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.myadermoshop.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PhysicalControlsFragment extends Fragment {
    private CompletedPhysicalControlAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<PhysicalControle> physicalControleList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_physical_controls, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fabAddPhysicalControl);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPhysicalControls);
        this.swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        this.databaseHelper = new DatabaseHelper(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.physicalControleList = new ArrayList();
        CompletedPhysicalControlAdapter completedPhysicalControlAdapter = new CompletedPhysicalControlAdapter(getContext(), this.physicalControleList, this.databaseHelper);
        this.adapter = completedPhysicalControlAdapter;
        recyclerView.setAdapter(completedPhysicalControlAdapter);
        loadPhysicalControls();
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.example.myadermoshop.PhysicalControlsFragment$$ExternalSyntheticLambda0
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                this.f$0.loadPhysicalControls();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.PhysicalControlsFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                this.f$0.m109xc1138196(view2);
            }
        });
    }

    /* renamed from: lambda$onViewCreated$0$com-example-myadermoshop-PhysicalControlsFragment, reason: not valid java name */
    /* synthetic */ void m109xc1138196(View view) {
        startActivity(new Intent(getActivity(), AddPhysicalControlActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadPhysicalControls() {
        this.swipeRefreshLayout.setRefreshing(true);
        if (isNetworkAvailable()) {
            this.databaseHelper.fetchAndStorePhysicalControls(new DatabaseHelper.PhysicalControlCallback() { // from class: com.example.myadermoshop.PhysicalControlsFragment.1
                @Override // com.example.myadermoshop.DatabaseHelper.PhysicalControlCallback
                public void onComplete(List<PhysicalControle> list) {
                    PhysicalControlsFragment.this.physicalControleList.clear();
                    PhysicalControlsFragment.this.physicalControleList.addAll(list);
                    PhysicalControlsFragment.this.adapter.notifyDataSetChanged();
                    PhysicalControlsFragment.this.swipeRefreshLayout.setRefreshing(false);
                }

                @Override // com.example.myadermoshop.DatabaseHelper.PhysicalControlCallback
                public void onFailure(String str) {
                    PhysicalControlsFragment.this.loadPhysicalControlsFromLocal();
                    PhysicalControlsFragment.this.swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            loadPhysicalControlsFromLocal();
            this.swipeRefreshLayout.setRefreshing(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadPhysicalControlsFromLocal() {
        this.physicalControleList.clear();
        this.physicalControleList.addAll(this.databaseHelper.getAllPhysicalControls());
        this.adapter.notifyDataSetChanged();
    }

    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getContext().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}