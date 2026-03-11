package com.example.myadermoshop;

import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.example.myadermoshop.DatabaseHelper;
import java.io.File;
import java.util.List;

/* loaded from: classes.dex */
public class StockDetailsActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1;
    private static final String TAG = "StockDetailsActivity";
    private TextView benefice;
    private DatabaseHelper dbHelper;
    private TextView decision;
    private TextView expectedSellingAmount;
    private TextView expirationDate;
    private ImageView imageViewFacture;
    private ImageView imageViewProduct;
    private TextView manufactureDate;
    private PdfGenerator pdfGenerator;
    private TextView pieces;
    private TextView pricePerUnit;
    private TextView productTitle;
    private ProgressDialog progressDialog;
    private TextView stockDate;
    private String stockID;
    private TextView supplierContact;
    private TextView supplierName;
    private TextView totalAmount;
    private TextView totalAmountUsed;
    private TextView uploadStatus;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_stock_details);
        this.dbHelper = new DatabaseHelper(this);
        this.pdfGenerator = new PdfGenerator(this.dbHelper);
        setupToolbar();
        initializeViews();
        this.stockID = getIntent().getStringExtra(DatabaseHelper.COLUMN_STOCK_ID);
        Log.d(TAG, "Received stock ID: " + this.stockID);
        loadStockDetails(this.stockID);
        findViewById(R.id.buttonCreateInstances).setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.StockDetailsActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m129lambda$onCreate$0$comexamplemyadermoshopStockDetailsActivity(view);
            }
        });
    }

    /* renamed from: lambda$onCreate$0$com-example-myadermoshop-StockDetailsActivity, reason: not valid java name */
    /* synthetic */ void m129lambda$onCreate$0$comexamplemyadermoshopStockDetailsActivity(View view) {
        if (isNetworkConnected()) {
            authenticateUser();
        } else {
            Toast.makeText(this, "No network connection. Please connect to the internet and try again.", 0).show();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.StockDetailsActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m130x80ac8ca9(view);
            }
        });
    }

    /* renamed from: lambda$setupToolbar$1$com-example-myadermoshop-StockDetailsActivity, reason: not valid java name */
    /* synthetic */ void m130x80ac8ca9(View view) {
        onBackPressed();
    }

    private void initializeViews() {
        this.imageViewProduct = findViewById(R.id.imageViewProduct);
        this.imageViewFacture = findViewById(R.id.imageViewFacture);
        this.productTitle = findViewById(R.id.textViewProductTitle);
        this.totalAmount = findViewById(R.id.textViewTotalAmount);
        this.pieces = findViewById(R.id.textViewPieces);
        this.manufactureDate = findViewById(R.id.textViewManufactureDate);
        this.expirationDate = findViewById(R.id.textViewExpirationDate);
        this.supplierName = findViewById(R.id.textViewSupplierName);
        this.supplierContact = findViewById(R.id.textViewSupplierContact);
        this.pricePerUnit = findViewById(R.id.textViewPU);
        this.benefice = findViewById(R.id.tvBenefice);
        this.totalAmountUsed = findViewById(R.id.tvTotalAmountUsed);
        this.expectedSellingAmount = findViewById(R.id.tvExpectedSellingAmount);
        this.decision = findViewById(R.id.tvDecision);
        this.stockDate = findViewById(R.id.tvDate);
        this.uploadStatus = findViewById(R.id.tvUploadStatus);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.progressDialog = progressDialog;
        progressDialog.setMessage("Generating instances...");
        this.progressDialog.setCancelable(false);
    }

    private void loadStockDetails(String str) {
        Cursor stockById = this.dbHelper.getStockById(str);
        if (stockById != null && stockById.moveToFirst()) {
            try {
                try {
                    logColumnNames(stockById);
                    logColumnValues(stockById);
                    displayStockDetails(stockById);
                    loadProductImage(stockById);
                    loadFactureImage(str);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading stock details", e);
                }
                return;
            } finally {
                stockById.close();
            }
        }
        Toast.makeText(this, "Stock details not found", 0).show();
    }

    private void logColumnNames(Cursor cursor) {
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.d(TAG, "Column " + i + ": " + cursor.getColumnName(i));
        }
    }

    private void logColumnValues(Cursor cursor) {
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.d(TAG, "Value for " + cursor.getColumnName(i) + ": " + cursor.getString(i));
        }
    }

    private void displayStockDetails(Cursor cursor) {
        String string = cursor.getString(cursor.getColumnIndex("productID"));
        this.productTitle.setText(this.dbHelper.getProductName(string));
        this.totalAmount.setText(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL_AMOUNT_USED)) + " BIF");
        this.pieces.setText(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_QUANTITY)) + " pièces");
        this.manufactureDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_MAN_DATE)));
        this.expirationDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_EXP_DATE)));
        this.supplierName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SUPPLIER_NAME)));
        this.supplierContact.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SUPPLIER_CONTACT)));
        this.pricePerUnit.setText((cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL_AMOUNT_USED)) / cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_QUANTITY))) + " BIF");
        double expectedSellingPrice = this.dbHelper.getExpectedSellingPrice(string);
        this.benefice.setText(((cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_QUANTITY)) * expectedSellingPrice) - cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL_AMOUNT_USED))) + " BIF");
        this.totalAmountUsed.setText(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL_AMOUNT_USED)) + " BIF");
        this.expectedSellingAmount.setText((expectedSellingPrice * cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_QUANTITY))) + " BIF");
        int i = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS_ID));
        this.decision.setText(this.dbHelper.getStatusLabel(i));
        this.stockDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_DATE_TIME)));
        this.uploadStatus.setText(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_UPLOAD_STATUS)) == 1 ? "Téléchargé" : "Non Téléchargé");
        boolean zIsProductActiveToInstances = this.dbHelper.isProductActiveToInstances(string);
        Button button = findViewById(R.id.buttonCreateInstances);
        if (i == 3 && zIsProductActiveToInstances) {
            button.setVisibility(0);
        } else {
            button.setVisibility(8);
        }
    }

    private void loadProductImage(Cursor cursor) {
        String productPhotoName = this.dbHelper.getProductPhotoName(cursor.getString(cursor.getColumnIndex("productID")));
        if (productPhotoName != null && !productPhotoName.isEmpty()) {
            String absolutePath = new File(getFilesDir(), "products/" + productPhotoName).getAbsolutePath();
            Log.d(TAG, "Product Image Path: " + absolutePath);
            if (new File(absolutePath).exists()) {
                Log.d(TAG, "Product image file exists");
                Glide.with(this).load(absolutePath).into(this.imageViewProduct);
                return;
            } else {
                Log.e(TAG, "Product image file does not exist");
                this.imageViewProduct.setImageResource(R.drawable.ic_placeholder);
                return;
            }
        }
        this.imageViewProduct.setImageResource(R.drawable.ic_placeholder);
    }

    private void loadFactureImage(String str) {
        String factureImageName = this.dbHelper.getFactureImageName(str);
        Log.d(TAG, "Facture Image Name: " + factureImageName);
        if (factureImageName != null && !factureImageName.isEmpty()) {
            String absolutePath = new File(getFilesDir(), "factures/" + factureImageName).getAbsolutePath();
            Log.d(TAG, "Facture Image Path: " + absolutePath);
            if (new File(absolutePath).exists()) {
                Log.d(TAG, "Facture image file exists");
                Glide.with(this).load(absolutePath).into(this.imageViewFacture);
                return;
            }
            Log.e(TAG, "Facture image file does not exist locally");
            Log.e(TAG, "Facture image name: " + factureImageName);
            String str2 = "http://192.168.5.1/adermomanagement/uploads/factures/" + factureImageName;
            Log.d(TAG, "Attempting to download facture image: " + str2);
            ImageDownloadUtil.downloadImageWithCustomPath(this, str2, "factures");
            this.imageViewFacture.setImageResource(R.drawable.ic_placeholder);
            return;
        }
        Log.e(TAG, "Facture image file name is empty or null");
        this.imageViewFacture.setImageResource(R.drawable.ic_placeholder);
    }

    private void createInstances(final String str) {
        this.dbHelper.generateInstancesOnServer(str, new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.StockDetailsActivity.1
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                StockDetailsActivity.this.generatePdf(str);
                StockDetailsActivity.this.progressDialog.dismiss();
                Toast.makeText(StockDetailsActivity.this, "Instances created successfully", 0).show();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str2) {
                Log.e(StockDetailsActivity.TAG, "Error: " + str2);
                StockDetailsActivity.this.progressDialog.dismiss();
                Toast.makeText(StockDetailsActivity.this, "Error: " + str2, 0).show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void generatePdf(String str) {
        List<String> productInstances = this.dbHelper.getProductInstances(str);
        String str2 = getExternalFilesDir(null) + "/StockInstances_" + str + ".pdf";
        this.pdfGenerator.createPdfWithBarcodes(productInstances, str2);
        Toast.makeText(this, "PDF generated: " + str2, 1).show();
        Log.d(TAG, "PDF generated at: " + str2);
        openPdf(str2);
    }

    private void openPdf(String str) {
        Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(str));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uriForFile, "application/pdf");
        intent.setFlags(1073741824);
        intent.addFlags(1);
        startActivity(Intent.createChooser(intent, "Open PDF"));
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void showLoading(boolean z) {
        if (z) {
            this.progressDialog.show();
        } else {
            this.progressDialog.dismiss();
        }
    }

    private void authenticateUser() {
        Intent intentCreateConfirmDeviceCredentialIntent = ((KeyguardManager) getSystemService("keyguard")).createConfirmDeviceCredentialIntent("Authentication Required", "Please confirm your screen lock pattern, PIN, or password to continue.");
        if (intentCreateConfirmDeviceCredentialIntent != null) {
            startActivityForResult(intentCreateConfirmDeviceCredentialIntent, 1);
        } else {
            Toast.makeText(this, "No lock screen security setup found.", 0).show();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            if (i2 == -1) {
                showLoading(true);
                createInstances(this.stockID);
            } else {
                Toast.makeText(this, "Authentication failed. Please try again.", 0).show();
            }
        }
    }
}