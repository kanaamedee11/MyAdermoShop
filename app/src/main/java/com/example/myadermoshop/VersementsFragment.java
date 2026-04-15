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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class VersementsFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerViewVersements;
    private VersementAdapter versementAdapter;
    private List<Versement> versementList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             @Nullable ViewGroup viewGroup,
                             @Nullable Bundle bundle) {
        View view = layoutInflater.inflate(
                R.layout.fragment_versements, viewGroup, false);

        recyclerViewVersements =
                view.findViewById(R.id.recyclerViewVersements);

        ExtendedFloatingActionButton fab =
                view.findViewById(R.id.fab_add_versement);

        recyclerViewVersements.setLayoutManager(
                new LinearLayoutManager(getContext()));

        versementList = new ArrayList<>();
        dbHelper = new DatabaseHelper(getContext());
        versementAdapter = new VersementAdapter(
                getContext(), versementList, dbHelper);
        recyclerViewVersements.setAdapter(versementAdapter);

        loadVersements();

        fab.setOnClickListener(v ->
                startActivity(new Intent(getContext(),
                        AddVersementActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadVersements();
    }

    private void loadVersements() {
        versementList.clear();
        versementList.addAll(dbHelper.fetchAllVersementsAsList());
        versementAdapter.notifyDataSetChanged();
    }
}