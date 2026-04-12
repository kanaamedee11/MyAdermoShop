package com.example.myadermoshop;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class PhysicalControlsFragment extends Fragment {
    private CompletedPhysicalControlAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<PhysicalControle> physicalControleList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_physical_controls, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fabAddPhysicalControl);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPhysicalControls);
        this.swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        this.databaseHelper = new DatabaseHelper(getContext());
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.physicalControleList = new ArrayList<>();
        this.adapter = new CompletedPhysicalControlAdapter(getContext(), this.physicalControleList, this.databaseHelper);
        recyclerView.setAdapter(this.adapter);
        
        loadPhysicalControls();
        
        this.swipeRefreshLayout.setOnRefreshListener(this::loadPhysicalControls);
        
        floatingActionButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddPhysicalControlActivity.class));
        });
    }

    private void loadPhysicalControls() {
        if (this.swipeRefreshLayout != null) {
            this.swipeRefreshLayout.setRefreshing(true);
        }
        if (isNetworkAvailable()) {
            this.databaseHelper.fetchAndStorePhysicalControls(new DatabaseHelper.PhysicalControlCallback() {
                @Override
                public void onComplete(List<PhysicalControle> list) {
                    physicalControleList.clear();
                    physicalControleList.addAll(list);
                    adapter.notifyDataSetChanged();
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(String str) {
                    loadPhysicalControlsFromLocal();
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            loadPhysicalControlsFromLocal();
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadPhysicalControlsFromLocal() {
        this.physicalControleList.clear();
        this.physicalControleList.addAll(this.databaseHelper.getAllPhysicalControls());
        this.adapter.notifyDataSetChanged();
    }

    private boolean isNetworkAvailable() {
        if (getContext() == null) return false;
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(ConnectivityManager.class);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}