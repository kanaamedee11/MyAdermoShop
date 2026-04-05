package com.example.myadermoshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductSelectionActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;
    private ListView listViewProducts;
    private List<Product> productList;
    private List<String> productNames;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);

        listViewProducts = findViewById(R.id.listViewProducts);
        searchBar = findViewById(R.id.searchBar);
        dbHelper = new DatabaseHelper(this);

        // Load all products initially
        loadProducts("");

        // Search listener
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        // Item click listener
        listViewProducts.setOnItemClickListener((parent, view, position, id) -> {
            showQuantityDialog(productList.get(position));
        });
    }

    private void loadProducts(String query) {
        productList = dbHelper.searchProductsWithoutInstances(query);
        productNames = new ArrayList<>();
        Iterator<Product> iterator = productList.iterator();
        while (iterator.hasNext()) {
            Product p = iterator.next();
            productNames.add(p.getProductName());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productNames);
        listViewProducts.setAdapter(adapter);
    }

    private void filterProducts(String query) {
        loadProducts(query);
    }

    private void showQuantityDialog(final Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Entrez la quantité");

        final EditText input = new EditText(this);
        int inputType = (product.getIsActiveToDecimalQuantity() == 1) ? 8194 : 2; // decimal or integer
        input.setInputType(inputType);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            double quantity = Double.parseDouble(input.getText().toString());
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_product_id", product.getProductID());
            resultIntent.putExtra("selected_product_quantity", quantity);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
