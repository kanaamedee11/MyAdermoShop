package com.example.myadermoshop;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PdfListFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private PdfAdapter pdfAdapter;
    private List<PdfFile> pdfFiles;
    private RecyclerView pdfRecyclerView;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_pdf_list, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.dbHelper = new DatabaseHelper(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.pdf_recycler_view);
        this.pdfRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.pdfFiles = new ArrayList();
        PdfAdapter pdfAdapter = new PdfAdapter(getActivity(), this.pdfFiles);
        this.pdfAdapter = pdfAdapter;
        this.pdfRecyclerView.setAdapter(pdfAdapter);
        loadPdfFiles();
    }

    private void loadPdfFiles() {
        Cursor allPdfFiles = this.dbHelper.getAllPdfFiles();
        if (allPdfFiles != null) {
            while (allPdfFiles.moveToNext()) {
                this.pdfFiles.add(new PdfFile(allPdfFiles.getString(allPdfFiles.getColumnIndex("name")), allPdfFiles.getString(allPdfFiles.getColumnIndex(DatabaseHelper.COLUMN_LOCATION))));
            }
            allPdfFiles.close();
            this.pdfAdapter.notifyDataSetChanged();
        }
    }
}