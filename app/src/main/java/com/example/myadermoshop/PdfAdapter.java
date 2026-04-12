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
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {
    private final Context context;
    private final List<PdfFile> pdfFiles;

    public PdfAdapter(Context context, List<PdfFile> list) {
        this.context = context;
        this.pdfFiles = list;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PdfViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_pdf_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        PdfFile pdfFile = pdfFiles.get(position);
        holder.pdfName.setText(pdfFile.getName());
        holder.itemView.setOnClickListener(v ->
                suggestAppsToOpenPdf(pdfFile.getLocation()));
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }

    private void suggestAppsToOpenPdf(String path) {
        Uri uri = FileProvider.getUriForFile(
                context,
                "com.example.myadermoshop.fileprovider",
                new File(path));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context,
                    "Aucune application trouvée pour ouvrir le PDF",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static class PdfViewHolder extends RecyclerView.ViewHolder {
        ImageView pdfIcon;
        TextView pdfName;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfName = itemView.findViewById(R.id.pdf_name);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
        }
    }
}