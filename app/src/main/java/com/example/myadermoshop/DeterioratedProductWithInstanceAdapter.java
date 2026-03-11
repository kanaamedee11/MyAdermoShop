package com.example.myadermoshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DeterioratedProductWithInstanceAdapter extends RecyclerView.Adapter<DeterioratedProductWithInstanceAdapter.ViewHolder> {
    private final Context context;
    private final List<DeterioratedProductWithInstance> deterioratedProductWithInstanceList;

    public DeterioratedProductWithInstanceAdapter(Context context, List<DeterioratedProductWithInstance> list) {
        this.context = context;
        this.deterioratedProductWithInstanceList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_deteriorated_product_with_instance, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DeterioratedProductWithInstance item = this.deterioratedProductWithInstanceList.get(i);
        
        // Using fields available in the provided item_deteriorated_product_with_instance.xml
        viewHolder.tvProductName.setText("Instance: " + item.getInstanceID());
        viewHolder.tvBarcode.setText("Date: " + item.getDeteriorationDate());
        viewHolder.tvQuantity.setText(item.getQuantity() + " pc");
    }

    @Override
    public int getItemCount() {
        return this.deterioratedProductWithInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvBarcode;
        TextView tvQuantity;

        public ViewHolder(View view) {
            super(view);
            this.tvProductName = view.findViewById(R.id.tvProductName);
            this.tvBarcode = view.findViewById(R.id.tvBarcode);
            this.tvQuantity = view.findViewById(R.id.tvQuantity);
        }
    }
}
