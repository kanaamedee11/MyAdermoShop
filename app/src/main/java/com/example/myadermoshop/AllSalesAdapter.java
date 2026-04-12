package com.example.myadermoshop;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class AllSalesAdapter extends RecyclerView.Adapter<AllSalesAdapter.SalesViewHolder> {
    private final List<Cart> cartList;
    private final Context context;

    public static class SalesViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutProducts;
        TextView textViewCartDate, textViewCartID, textViewTotalAmount;

        public SalesViewHolder(View itemView) {
            super(itemView);
            textViewCartID       = itemView.findViewById(R.id.textViewCartID);
            textViewCartDate     = itemView.findViewById(R.id.textViewCartDate);
            textViewTotalAmount  = itemView.findViewById(R.id.textViewTotalAmount);
            linearLayoutProducts = itemView.findViewById(R.id.linearLayoutProducts);
        }
    }

    public AllSalesAdapter(Context context, List<Cart> cartList) {
        this.context  = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_sale_simple, parent, false);
        return new SalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        // Bind header info
        holder.textViewCartID.setText(cart.getCartID());
        holder.textViewCartDate.setText(cart.getTimestamp());
        holder.textViewTotalAmount.setText(
                String.format(Locale.getDefault(), "%.2f BIF", cart.getTotalAmount()));

        // Clear previous rows
        holder.linearLayoutProducts.removeAllViews();

        // Group products by name
        HashMap<String, CartItem> groupedItems = new HashMap<>();
        for (CartItem item : cart.getCartItems()) {
            if (groupedItems.containsKey(item.getProductName())) {
                CartItem existing = groupedItems.get(item.getProductName());
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
            } else {
                groupedItems.put(item.getProductName(), item);
            }
        }

        // Add product rows programmatically
        Iterator<CartItem> iterator = groupedItems.values().iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();

            LinearLayout row = new LinearLayout(context);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(16, 8, 16, 8);

            TextView tvName  = createTextView(item.getProductName(), 2f);
            TextView tvQty   = createTextView(String.valueOf(item.getQuantity()), 1f);
            TextView tvUnit  = createTextView(
                    String.format(Locale.getDefault(), "%.2f", item.getUnitPrice()), 1f);
            TextView tvTotal = createTextView(
                    String.format(Locale.getDefault(), "%.2f",
                            item.getQuantity() * item.getUnitPrice()), 1f);

            row.addView(tvName);
            row.addView(tvQty);
            row.addView(tvUnit);
            row.addView(tvTotal);

            holder.linearLayoutProducts.addView(row);
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    private TextView createTextView(String text, float weight) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(14);
        // ── FIXED: Color.parseColor() avoids R.color sync issues ──
        tv.setTextColor(Color.parseColor("#000000")); // ios_black
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, weight));
        return tv;
    }
}