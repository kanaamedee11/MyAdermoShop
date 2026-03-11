package com.example.myadermoshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class SaleCardAdapter extends RecyclerView.Adapter<SaleCardAdapter.SaleViewHolder> {
    private final List<Cart> cartList;
    private final Context context;
    private final DatabaseHelper dbHelper;

    public SaleCardAdapter(Context context, List<Cart> list) {
        this.context = context;
        this.cartList = list;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public SaleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SaleViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_sale_card, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(SaleViewHolder saleViewHolder, final int i) {
        final Cart cart = this.cartList.get(i);
        saleViewHolder.textViewCartID.setText(cart.getCartID());
        saleViewHolder.textViewTime.setText(cart.getTimestamp());
        saleViewHolder.textViewCurrency.setText(cart.getCurrency());
        saleViewHolder.textViewAmount.setText(String.valueOf(cart.getTotalAmount()));
        saleViewHolder.linearLayoutItems.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setOrientation(0);
        TextView textView = new TextView(this.context);
        textView.setText("Produit");
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        textView.setTypeface(null, 1);
        linearLayout.addView(textView);
        TextView textView2 = new TextView(this.context);
        textView2.setText("Quantité");
        textView2.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        textView2.setTypeface(null, 1);
        linearLayout.addView(textView2);
        TextView textView3 = new TextView(this.context);
        textView3.setText("Prix Unitaire");
        textView3.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        textView3.setTypeface(null, 1);
        linearLayout.addView(textView3);
        TextView textView4 = new TextView(this.context);
        textView4.setText("Prix Total");
        textView4.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        textView4.setTypeface(null, 1);
        linearLayout.addView(textView4);
        saleViewHolder.linearLayoutItems.addView(linearLayout);
        HashMap<String, CartItem> map = new HashMap<>();
        for (CartItem cartItem : cart.getCartItems()) {
            if (map.containsKey(cartItem.getProductName())) {
                CartItem cartItem2 = map.get(cartItem.getProductName());
                cartItem2.setQuantity(cartItem2.getQuantity() + cartItem.getQuantity());
            } else {
                map.put(cartItem.getProductName(), cartItem);
            }
        }
        for (CartItem cartItem3 : map.values()) {
            LinearLayout linearLayout2 = new LinearLayout(this.context);
            linearLayout2.setOrientation(0);
            TextView textView5 = new TextView(this.context);
            textView5.setText(cartItem3.getProductName());
            textView5.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
            linearLayout2.addView(textView5);
            TextView textView6 = new TextView(this.context);
            textView6.setText(String.valueOf(cartItem3.getQuantity()));
            textView6.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
            linearLayout2.addView(textView6);
            TextView textView7 = new TextView(this.context);
            textView7.setText(String.valueOf(cartItem3.getUnitPrice()));
            textView7.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
            linearLayout2.addView(textView7);
            TextView textView8 = new TextView(this.context);
            textView8.setText(String.valueOf(cartItem3.getQuantity() * cartItem3.getUnitPrice()));
            textView8.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
            linearLayout2.addView(textView8);
            saleViewHolder.linearLayoutItems.addView(linearLayout2);
        }
        saleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m120x147a6f1c(cart, view);
            }
        });
        saleViewHolder.imageButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m122x2344d95a(cart, i, view);
            }
        });
    }

    private void m120x147a6f1c(Cart cart, View view) {
        Context context = this.context;
        if (context instanceof FragmentActivity) {
            ThermalReceiptFragment thermalReceiptFragment = new ThermalReceiptFragment();
            Bundle bundle = new Bundle();
            bundle.putString(DatabaseHelper.COLUMN_CART_ID, cart.getCartID());
            thermalReceiptFragment.setArguments(bundle);
            FragmentTransaction fragmentTransactionBeginTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransactionBeginTransaction.replace(R.id.container, thermalReceiptFragment);
            fragmentTransactionBeginTransaction.addToBackStack(null);
            fragmentTransactionBeginTransaction.commit();
        }
    }

    private void m122x2344d95a(final Cart cart, final int i, View view) {
        new AlertDialog.Builder(this.context).setTitle("Supprimer la vente").setMessage("Êtes-vous sûr de vouloir supprimer cette vente ?").setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i2) {
                m121x1bdfa43b(cart, i, dialogInterface, i2);
            }
        }).setNegativeButton("Non", null).show();
    }

    private void m121x1bdfa43b(Cart cart, int i, DialogInterface dialogInterface, int i2) {
        if (this.dbHelper.deleteSale(cart.getCartID())) {
            this.cartList.remove(i);
            notifyDataSetChanged();
            Toast.makeText(this.context, "La vente a été supprimée avec succès.", 0).show();
            return;
        }
        Toast.makeText(this.context, "Erreur lors de la suppression de la vente.", 0).show();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.cartList.size();
    }

    public static class SaleViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButtonCancel;
        LinearLayout linearLayoutItems;
        TextView textViewAmount;
        TextView textViewCartID;
        TextView textViewCurrency;
        TextView textViewTime;

        public SaleViewHolder(View view) {
            super(view);
            this.textViewCartID = view.findViewById(R.id.textViewCartID);
            this.textViewTime = view.findViewById(R.id.textViewTime);
            this.textViewCurrency = view.findViewById(R.id.textViewCurrency);
            this.textViewAmount = view.findViewById(R.id.textViewAmount);
            this.linearLayoutItems = view.findViewById(R.id.linearLayoutItems);
            this.imageButtonCancel = view.findViewById(R.id.imageButtonCancel);
        }
    }
}