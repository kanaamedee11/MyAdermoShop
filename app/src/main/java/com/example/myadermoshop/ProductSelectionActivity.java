package com.example.myadermoshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ProductSelectionActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;
    private ListView listViewProducts;
    private List<Product> productList;
    private List<String> productNames;
    private EditText searchBar;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_product_selection);
        this.listViewProducts = findViewById(R.id.listViewProducts);
        this.searchBar = findViewById(R.id.searchBar);
        this.dbHelper = new DatabaseHelper(this);
        loadProducts("");
        this.searchBar.addTextChangedListener(new TextWatcher() { // from class: com.example.myadermoshop.ProductSelectionActivity.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                ProductSelectionActivity.this.filterProducts(editable.toString());
            }
        });
        this.listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.example.myadermoshop.ProductSelectionActivity$$ExternalSyntheticLambda2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                this.f$0.m116x63d61176(adapterView, view, i, j);
            }
        });
    }

    /* renamed from: lambda$onCreate$0$com-example-myadermoshop-ProductSelectionActivity, reason: not valid java name */
    /* synthetic */ void m116x63d61176(AdapterView adapterView, View view, int i, long j) {
        showQuantityDialog(this.productList.get(i));
    }

    private void loadProducts(String str) {
        this.productList = this.dbHelper.searchProductsWithoutInstances(str);
        this.productNames = new ArrayList();
        Iterator<Product> it = this.productList.iterator();
        while (it.hasNext()) {
            this.productNames.add(it.next().getProductName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.productNames);
        this.adapter = arrayAdapter;
        this.listViewProducts.setAdapter(arrayAdapter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void filterProducts(String str) {
        loadProducts(str);
    }

    private void showQuantityDialog(final Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Entrez la quantité");
        final EditText editText = new EditText(this);
        editText.setInputType(product.getIsActiveToDecimalQuantity() == 1 ? 8194 : 2);
        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.ProductSelectionActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) throws NumberFormatException {
                this.f$0.m117x929880cc(editText, product, dialogInterface, i);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.ProductSelectionActivity$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    /* renamed from: lambda$showQuantityDialog$1$com-example-myadermoshop-ProductSelectionActivity, reason: not valid java name */
    /* synthetic */ void m117x929880cc(EditText editText, Product product, DialogInterface dialogInterface, int i) throws NumberFormatException {
        double d = Double.parseDouble(editText.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("selected_product_id", product.getProductID());
        intent.putExtra("selected_product_quantity", d);
        setResult(-1, intent);
        finish();
    }
}