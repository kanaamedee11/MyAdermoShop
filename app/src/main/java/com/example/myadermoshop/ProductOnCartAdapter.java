package com.example.myadermoshop;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ProductOnCartAdapter extends RecyclerView.Adapter<ProductOnCartAdapter.ProductViewHolder> {
    private static final String TAG = "ProductOnCartAdapter";
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final Set<String> instanceIDs;
    private final List<Product> productList;
    private final HashMap<String, Double> productQuantities;

    public ProductOnCartAdapter(List<Product> list, Context context, HashMap<String, Double> map, Set<String> set) {
        this.productList = list;
        this.context = context;
        this.productQuantities = map;
        this.instanceIDs = set;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProductViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_on_cart, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        double dDoubleValue;
        final Product product = this.productList.get(i);
        productViewHolder.textViewProductName.setText(product.getProductName());
        boolean zContains = this.instanceIDs.contains(product.getProductID());
        Log.d(TAG, "Product ID: " + product.getProductID() + ", isInstanceBased: " + zContains);
        
        if (zContains) {
            String productID = product.getProductID();
            ProductInfo productInfoByInstance = this.dbHelper.getProductInfoByInstance(productID);
            if (productInfoByInstance != null) {
                productViewHolder.textViewProductPrice.setText(String.format("BIF %.2f", productInfoByInstance.getProductPrice()));
                loadImage(productInfoByInstance.getProductPhotoName(), productViewHolder.imageViewProduct);
            }
            dDoubleValue = 1.0d;
        } else {
            Product productByID = this.dbHelper.getProductByID(product.getProductID());
            if (productByID != null) {
                productViewHolder.textViewProductPrice.setText(String.format("BIF %.2f", this.dbHelper.getExpectedSellingPrice(productByID.getProductID())));
                loadImage(productByID.getProductPhotoName(), productViewHolder.imageViewProduct);
            }
            dDoubleValue = this.productQuantities.getOrDefault(product.getProductID(), 0.0d);
        }
        
        productViewHolder.textViewProductQuantity.setText(String.format("%.2f", dDoubleValue));
        
        productViewHolder.buttonDelete.setOnClickListener(view -> {
            if (instanceIDs.contains(product.getProductID())) {
                instanceIDs.remove(product.getProductID());
                productList.remove(product);
            } else {
                Double qty = productQuantities.get(product.getProductID());
                double dDoubleValue2 = qty != null ? qty : 0.0d;
                if (dDoubleValue2 <= 1.0d) {
                    productQuantities.remove(product.getProductID());
                    productList.remove(product);
                } else {
                    productQuantities.put(product.getProductID(), dDoubleValue2 - 1.0d);
                }
            }
            notifyDataSetChanged();
            if (context instanceof AddSaleActivity) {
                ((AddSaleActivity) context).calculateTotalPrice();
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        MaterialButton buttonDelete;
        ImageView imageViewProduct;
        TextView textViewProductName;
        TextView textViewProductPrice;
        TextView textViewProductQuantity;

        public ProductViewHolder(View view) {
            super(view);
            this.textViewProductName = view.findViewById(R.id.tvProductName);
            this.textViewProductPrice = view.findViewById(R.id.tvProductPrice);
            this.textViewProductQuantity = view.findViewById(R.id.tvQty);
            this.imageViewProduct = view.findViewById(R.id.imageViewProduct);
            if (this.imageViewProduct == null) {
                // Fallback to searching for any ImageView if the specific ID is missing
                this.imageViewProduct = findImageView(view);
            }
            this.buttonDelete = view.findViewById(R.id.btnMinus);
        }

        private ImageView findImageView(View view) {
            if (view instanceof ImageView) return (ImageView) view;
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    ImageView iv = findImageView(vg.getChildAt(i));
                    if (iv != null) return iv;
                }
            }
            return null;
        }
    }

    private void loadImage(String str, ImageView imageView) {
        if (imageView == null) return;
        File file = new File(this.context.getFilesDir(), "products/" + str);
        if (file.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        } else {
            imageView.setImageResource(R.drawable.ic_product_placeholder);
        }
    }
}
