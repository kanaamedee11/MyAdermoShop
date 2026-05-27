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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class PhysicalControlsFragment extends Fragment {

    private CompletedPhysicalControlAdapter adapter;
    private DatabaseHelper                  databaseHelper;
    private List<PhysicalControle>          physicalControleList;
    private SwipeRefreshLayout              swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_physical_controls, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ExtendedFloatingActionButton fabAddPhysicalControl =
                view.findViewById(R.id.fabAddPhysicalControl);
        RecyclerView recyclerView =
                view.findViewById(R.id.recyclerViewPhysicalControls);
        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);

        databaseHelper = new DatabaseHelper(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        physicalControleList = new ArrayList<>();
        adapter = new CompletedPhysicalControlAdapter(
                getContext(), physicalControleList, databaseHelper);
        recyclerView.setAdapter(adapter);

        loadPhysicalControls();

        swipeRefreshLayout.setOnRefreshListener(this::loadPhysicalControls);

        fabAddPhysicalControl.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), AddPhysicalControlActivity.class)));
    }

    private void loadPhysicalControls() {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable()) {
            databaseHelper.fetchAndStorePhysicalControls(
                    new DatabaseHelper.PhysicalControlCallback() {
                        @Override
                        public void onComplete(List<PhysicalControle> list) {
                            physicalControleList.clear();
                            physicalControleList.addAll(list);
                            adapter.notifyDataSetChanged();
                            if (swipeRefreshLayout != null)
                                swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onFailure(String error) {
                            loadPhysicalControlsFromLocal();
                            if (swipeRefreshLayout != null)
                                    swipeRefreshLayout.setRefreshing(false);
                        }
                    });
        } else {
            loadPhysicalControlsFromLocal();
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadPhysicalControlsFromLocal() {
        physicalControleList.clear();
        physicalControleList.addAll(databaseHelper.getAllPhysicalControls());
        adapter.notifyDataSetChanged();
    }

    private boolean isNetworkAvailable() {
        if (getContext() == null) return false;
        ConnectivityManager cm = (ConnectivityManager)
                getContext().getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}