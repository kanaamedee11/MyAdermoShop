package com.example.myadermoshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SaleCardAdapter extends RecyclerView.Adapter<SaleCardAdapter.SaleViewHolder> {

    private final List<Cart> cartList;
    private final Context context;
    private final DatabaseHelper dbHelper;

    public SaleCardAdapter(Context context, List<Cart> list) {
        this.context = context;
        this.cartList = list;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public SaleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_sale_card, parent, false);
        return new SaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SaleViewHolder holder, final int position) {
        final Cart cart = cartList.get(position);

        // ── Header ──
        holder.textViewCartID.setText(cart.getCartID());
        holder.textViewTime.setText(cart.getTimestamp());
        holder.textViewCurrency.setText(cart.getCurrency());
        holder.textViewAmount.setText(
                String.format(Locale.getDefault(), "%.2f", cart.getTotalAmount()));

        // ── Build product rows ──
        holder.linearLayoutItems.removeAllViews();

        // Aggregate items by product name
        HashMap<String, CartItem> map = new HashMap<>();
        for (CartItem item : cart.getCartItems()) {
            if (map.containsKey(item.getProductName())) {
                CartItem existing = map.get(item.getProductName());
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
            } else {
                map.put(item.getProductName(), item);
            }
        }

        // Add one row per aggregated item
        for (CartItem item : map.values()) {
            holder.linearLayoutItems.addView(buildProductRow(item));
        }

        // ── Click: open thermal receipt ──
        holder.itemView.setOnClickListener(v -> openThermalReceipt(cart));

        // ── Cancel: confirm then delete ──
        holder.imageButtonCancel.setOnClickListener(v ->
                confirmDelete(cart, position));
    }

    // ── Build a single product row with iOS-style text ──
    private View buildProductRow(CartItem item) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);

        int horizontalPadding = dp(16);
        int verticalPadding   = dp(8);
        row.setPadding(horizontalPadding, verticalPadding,
                horizontalPadding, verticalPadding);

        double lineTotal = item.getQuantity() * item.getUnitPrice();

        // Product name  (weight 2)
        row.addView(makeCell(item.getProductName(),   2, false));
        // Quantity      (weight 1, end-aligned)
        row.addView(makeCell(
                formatQty(item.getQuantity()),         1, false));
        // Unit price    (weight 1, end-aligned)
        row.addView(makeCell(
                formatPrice(item.getUnitPrice()),      1, false));
        // Line total    (weight 1, end-aligned)
        row.addView(makeCell(
                formatPrice(lineTotal),                1, false));

        return row;
    }

    // ── Helper: make a single cell TextView ──
    private TextView makeCell(String text, float weight, boolean bold) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(13f);
        tv.setTextColor(ContextCompat.getColor(context, R.color.ios_label));
        tv.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, weight));
        if (bold) {
            tv.setTypeface(null, Typeface.BOLD);
        }
        // Right-align all columns except the first
        if (weight < 2f) {
            tv.setGravity(android.view.Gravity.END);
        }
        return tv;
    }

    // ── Format helpers ──
    private String formatQty(double qty) {
        if (qty == Math.floor(qty)) {
            return String.valueOf((int) qty);
        }
        return String.format(Locale.getDefault(), "%.2f", qty);
    }

    private String formatPrice(double price) {
        return String.format(Locale.getDefault(), "%.2f", price);
    }

    private int dp(int value) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }

    // ── Open thermal receipt fragment ──
    private void openThermalReceipt(Cart cart) {
        if (!(context instanceof FragmentActivity)) return;
        ThermalReceiptFragment fragment = new ThermalReceiptFragment();
        Bundle args = new Bundle();
        args.putString(DatabaseHelper.COLUMN_CART_ID, cart.getCartID());
        fragment.setArguments(args);
        FragmentTransaction tx = ((FragmentActivity) context)
                .getSupportFragmentManager()
                .beginTransaction();
        tx.replace(R.id.container, fragment);
        tx.addToBackStack(null);
        tx.commit();
    }

    // ── Confirm then delete ──
    private void confirmDelete(final Cart cart, final int position) {
        new AlertDialog.Builder(context)
                .setTitle("Supprimer la vente")
                .setMessage("Êtes-vous sûr de vouloir supprimer cette vente ?")
                .setPositiveButton("Oui", (dialog, which) -> deleteSale(cart, position))
                .setNegativeButton("Non", null)
                .show();
    }

    private void deleteSale(Cart cart, int position) {
        if (dbHelper.deleteSale(cart.getCartID())) {
            cartList.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context,
                    "La vente a été supprimée avec succès.",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,
                    "Erreur lors de la suppression de la vente.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // ── ViewHolder ──
    public static class SaleViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButtonCancel;
        LinearLayout linearLayoutItems;
        TextView textViewAmount;
        TextView textViewCartID;
        TextView textViewCurrency;
        TextView textViewTime;

        public SaleViewHolder(View view) {
            super(view);
            textViewCartID    = view.findViewById(R.id.textViewCartID);
            textViewTime      = view.findViewById(R.id.textViewTime);
            textViewCurrency  = view.findViewById(R.id.textViewCurrency);
            textViewAmount    = view.findViewById(R.id.textViewAmount);
            linearLayoutItems = view.findViewById(R.id.linearLayoutItems);
            imageButtonCancel = view.findViewById(R.id.imageButtonCancel);
        }
    }
}