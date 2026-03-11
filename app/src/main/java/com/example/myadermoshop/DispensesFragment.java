package com.example.myadermoshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class DispensesFragment extends Fragment {

    private DatabaseHelper  dbHelper;
    private DispenseAdapter dispenseAdapter;
    private List<Dispense>  dispenseList;
    private RecyclerView    recyclerViewDispenses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dispenses, container, false);

        recyclerViewDispenses = root.findViewById(R.id.recyclerViewDispenses);
        recyclerViewDispenses.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper      = new DatabaseHelper(getContext());
        dispenseList  = new ArrayList<>();
        dispenseAdapter = new DispenseAdapter(getContext(), dispenseList, dbHelper);
        recyclerViewDispenses.setAdapter(dispenseAdapter);

        FloatingActionButton fab = root.findViewById(R.id.fab_add_dispense);
        fab.setOnClickListener(v -> {
            if (Utils.checkAndDisplayClosure(getActivity(), dbHelper)) return;
            startActivity(new Intent(getContext(), AddDispenseActivity.class));
        });

        loadDispenses();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDispenses();
    }

    private void loadDispenses() {
        dispenseList.clear();
        dispenseList.addAll(dbHelper.fetchAllDispensesAsList());
        dispenseAdapter.notifyDataSetChanged();
    }
}