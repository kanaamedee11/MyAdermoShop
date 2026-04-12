package com.example.myadermoshop;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PdfListFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private PdfAdapter pdfAdapter;
    private List<PdfFile> pdfFiles;
    private RecyclerView pdfRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             @Nullable ViewGroup viewGroup,
                             @Nullable Bundle bundle) {
        return layoutInflater.inflate(
                R.layout.fragment_pdf_list, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        dbHelper = new DatabaseHelper(getActivity());

        pdfRecyclerView = view.findViewById(R.id.pdf_recycler_view);
        pdfRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        pdfFiles = new ArrayList<>();
        pdfAdapter = new PdfAdapter(getActivity(), pdfFiles);
        pdfRecyclerView.setAdapter(pdfAdapter);

        loadPdfFiles();
    }

    @SuppressLint("Range")
    private void loadPdfFiles() {
        Cursor cursor = dbHelper.getAllPdfFiles();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                pdfFiles.add(new PdfFile(
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex(
                                DatabaseHelper.COLUMN_LOCATION))));
            }
            cursor.close();
            pdfAdapter.notifyDataSetChanged();
        }
    }
}