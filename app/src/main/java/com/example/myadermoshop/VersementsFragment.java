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

/* loaded from: classes.dex */
public class VersementsFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerViewVersements;
    private VersementAdapter versementAdapter;
    private List<Versement> versementList;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_versements, viewGroup, false);
        this.recyclerViewVersements = viewInflate.findViewById(R.id.recyclerViewVersements);
        FloatingActionButton floatingActionButton = viewInflate.findViewById(R.id.fab_add_versement);
        this.recyclerViewVersements.setLayoutManager(new LinearLayoutManager(getContext()));
        this.versementList = new ArrayList();
        this.dbHelper = new DatabaseHelper(getContext());
        VersementAdapter versementAdapter = new VersementAdapter(getContext(), this.versementList, this.dbHelper);
        this.versementAdapter = versementAdapter;
        this.recyclerViewVersements.setAdapter(versementAdapter);
        loadVersements();
        floatingActionButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.VersementsFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VersementsFragment.this.startActivity(new Intent(VersementsFragment.this.getContext(), AddVersementActivity.class));
            }
        });
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        loadVersements();
    }

    private void loadVersements() {
        this.versementList.clear();
        this.versementList.addAll(this.dbHelper.fetchAllVersementsAsList());
        this.versementAdapter.notifyDataSetChanged();
    }
}