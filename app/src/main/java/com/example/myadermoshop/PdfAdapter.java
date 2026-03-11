package com.example.myadermoshop;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.List;

/* loaded from: classes.dex */
public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {
    private final Context context;
    private final List<PdfFile> pdfFiles;

    public PdfAdapter(Context context, List<PdfFile> list) {
        this.context = context;
        this.pdfFiles = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public PdfViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PdfViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_pdf_card, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(PdfViewHolder pdfViewHolder, int i) {
        final PdfFile pdfFile = this.pdfFiles.get(i);
        pdfViewHolder.pdfName.setText(pdfFile.getName());
        pdfViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m108lambda$onBindViewHolder$0$comexamplemyadermoshopPdfAdapter(pdfFile, view);
            }
        });
    }

    private void m108lambda$onBindViewHolder$0$comexamplemyadermoshopPdfAdapter(PdfFile pdfFile, View view) {
        suggestAppsToOpenPdf(pdfFile.getLocation());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.pdfFiles.size();
    }

    public static class PdfViewHolder extends RecyclerView.ViewHolder {
        ImageView pdfIcon;
        TextView pdfName;

        public PdfViewHolder(View view) {
            super(view);
            this.pdfName = view.findViewById(R.id.pdf_name);
            this.pdfIcon = view.findViewById(R.id.pdf_icon);
        }
    }

    private void suggestAppsToOpenPdf(String str) {
        Uri uriForFile = FileProvider.getUriForFile(this.context, "com.example.myadermoshop.fileprovider", new File(str));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uriForFile, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(this.context.getPackageManager()) != null) {
            this.context.startActivity(intent);
        } else {
            Toast.makeText(this.context, "Aucune application trouvée pour ouvrir le PDF", 0).show();
        }
    }
}