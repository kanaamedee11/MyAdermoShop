package com.example.myadermoshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;
import java.util.List;

public class AddPhysicalControlActivity extends AppCompatActivity {

    private String adminBarcode = "";
    private Button buttonNext;
    private Button buttonPrevious;
    private Button buttonScanAdminCode;
    private Button buttonSendData;
    private DatabaseHelper databaseHelper;
    private List<Double> foundQuantities;
    private PhysicalControlCardAdapter productCardAdapter;
    private List<Product> productList;
    private ProgressDialog progressDialog;
    private ViewPager viewPagerProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_physical_control);
        setSupportActionBar(findViewById(R.id.toolbar));

        viewPagerProducts    = findViewById(R.id.viewPagerProducts);
        buttonPrevious       = findViewById(R.id.buttonPrevious);
        buttonNext           = findViewById(R.id.buttonNext);
        buttonScanAdminCode  = findViewById(R.id.buttonScanAdminCode);
        buttonSendData       = findViewById(R.id.buttonSendData);

        databaseHelper = new DatabaseHelper(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending data...");
        progressDialog.setCancelable(false);

        loadProductsFromDatabase();

        buttonPrevious.setOnClickListener(v -> {
            int current = viewPagerProducts.getCurrentItem();
            if (current > 0) viewPagerProducts.setCurrentItem(current - 1);
        });

        buttonNext.setOnClickListener(v -> {
            int current = viewPagerProducts.getCurrentItem();
            if (current < productList.size() - 1) viewPagerProducts.setCurrentItem(current + 1);
        });

        buttonScanAdminCode.setOnClickListener(v -> {
            com.google.zxing.integration.android.IntentIntegrator integrator =
                    new com.google.zxing.integration.android.IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(
                    com.google.zxing.integration.android.IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan Admin Barcode");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });

        buttonSendData.setOnClickListener(v -> {
            if (adminBarcode.isEmpty()) {
                Toast.makeText(this, "Please scan admin barcode first", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.show();
            databaseHelper.sendPhysicalControlData(
                    adminBarcode, productList, foundQuantities,
                    new DatabaseHelper.DataUpdateCallback() {
                        @Override
                        public void onComplete() {
                            progressDialog.dismiss();
                            showAlert("Data sent successfully", AlertFragment.AlertType.SUCCESS);
                        }
                        @Override
                        public void onFailure(String msg) {
                            progressDialog.dismiss();
                            showAlert("Failed to send data: " + msg, AlertFragment.AlertType.ERROR);
                        }
                    });
        });
    }

    private void loadProductsFromDatabase() {
        Cursor cursor = databaseHelper.getAllProductsCursor();
        productList      = new ArrayList<>();
        foundQuantities  = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product p = new Product();
                p.setProductID(cursor.getString(
                        cursor.getColumnIndexOrThrow("productID")));
                p.setProductName(cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME)));
                p.setProductManufacture(cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_MANUFACTURE)));
                p.setManufactureAddress(cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MANUFACTURE_ADDRESS)));
                p.setProductPhotoName(cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PHOTO_NAME)));
                p.setProductAddDate(cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ADD_DATE)));
                p.setProductSeuilStock(cursor.getInt(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_SEUIL_STOCK)));
                p.setAdminID(cursor.getString(
                        cursor.getColumnIndexOrThrow("adminID")));
                p.setTypeProductID(cursor.getInt(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TYPE_PRODUCT_ID)));
                p.setSubSubAccountID(cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUB_SUB_ACCOUNT_ID)));
                p.setUniteID(cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UNITE_ID)));
                productList.add(p);
                foundQuantities.add(0.0d);
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (productList != null && !productList.isEmpty()) {
            productCardAdapter = new PhysicalControlCardAdapter(
                    this, productList, foundQuantities, databaseHelper);
            viewPagerProducts.setAdapter(productCardAdapter);
        } else {
            Log.e("AddPhysicalControlActivity", "Product list is empty or null");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        com.google.zxing.integration.android.IntentResult result =
                com.google.zxing.integration.android.IntentIntegrator
                        .parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                adminBarcode = result.getContents();
                Toast.makeText(this, "Scanned: " + adminBarcode, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showAlert(String message, AlertFragment.AlertType type) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment existing = fm.findFragmentByTag("alert");
        if (existing != null) ft.remove(existing);
        ft.add(R.id.fragment_container, AlertFragment.newInstance(message, type), "alert");
        ft.commit();
    }
}
