package com.example.myadermoshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * AllSalesAdapter
 * Used in: all sales fragment — getAllCarts() / getCartsBetweenDates()
 *
 * Outer card : item_sale_simple.xml  (full Cart card — identical structure
 *              to item_sale_card, just used in the all-sales context)
 *   Binds: textViewCartID, textViewCartDate,
 *          linearLayoutProducts, textViewTotalAmount
 *
 * Inner rows : item_sale_row.xml  (one per CartItem, plain row with separator)
 *   Binds: tvProductName, tvQty, tvUnitPrice, tvTotal
 */
public class AllSalesAdapter extends RecyclerView.Adapter<AllSalesAdapter.ViewHolder> {

    private final Context context;
    private final List<Cart> cartList;

    public AllSalesAdapter(Context context, List<Cart> cartList) {
        this.context  = context;
        this.cartList = cartList;
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_sale_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        // Header
        holder.textViewCartID.setText(cart.getCartID());
        holder.textViewCartDate.setText(cart.getTimestamp());

        // Clear stale rows from recycled ViewHolder
        holder.linearLayoutProducts.removeAllViews();

        // Inflate one item_sale_row per CartItem
        List<CartItem> items = cart.getCartItems();
        if (items != null && !items.isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(context);
            for (CartItem item : items) {
                View row = inflater.inflate(
                        R.layout.item_sale_row,
                        holder.linearLayoutProducts,
                        false);
                ((TextView) row.findViewById(R.id.tvProductName))
                        .setText(item.getProductName());
                ((TextView) row.findViewById(R.id.tvQty))
                        .setText(formatQty(item.getQuantity()));
                ((TextView) row.findViewById(R.id.tvUnitPrice))
                        .setText(formatAmount(item.getUnitPrice()));
                ((TextView) row.findViewById(R.id.tvTotal))
                        .setText(formatAmount(item.getQuantity() * item.getUnitPrice()));
                holder.linearLayoutProducts.addView(row);
            }
        }

        // Footer
        holder.textViewTotalAmount.setText(
                formatAmount(cart.getTotalAmount()) + " " + cart.getCurrency());
    }

    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0;
    }

    // ── ViewHolder ────────────────────────────────────────────────────────────

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView     textViewCartID;
        final TextView     textViewCartDate;
        final LinearLayout linearLayoutProducts;
        final TextView     textViewTotalAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCartID       = itemView.findViewById(R.id.textViewCartID);
            textViewCartDate     = itemView.findViewById(R.id.textViewCartDate);
            linearLayoutProducts = itemView.findViewById(R.id.linearLayoutProducts);
            textViewTotalAmount  = itemView.findViewById(R.id.textViewTotalAmount);
        }
    }

    // ── Formatters ────────────────────────────────────────────────────────────

    private static String formatQty(double qty) {
        if (qty == Math.floor(qty)) return String.valueOf((int) qty);
        return String.format("%.1f", qty);
    }

    private static String formatAmount(double amount) {
        return String.format("%.2f", amount);
    }
}