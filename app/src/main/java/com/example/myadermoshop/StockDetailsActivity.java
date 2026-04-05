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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import java.io.File;
import java.util.List;

public class StockDetailsActivity extends AppCompatActivity {

    private static final int    REQUEST_CODE_CONFIRM = 1;
    private static final String TAG                  = "StockDetailsActivity";

    // ── Views ─────────────────────────────────────────────────────────────────
    private ImageView imageViewProduct;
    private ImageView imageViewFacture;
    private TextView  productTitle;
    private TextView  totalAmount;
    private TextView  pieces;
    private TextView  manufactureDate;
    private TextView  expirationDate;
    private TextView  supplierName;
    private TextView  supplierContact;
    private TextView  pricePerUnit;
    private TextView  totalAmountUsed;
    private TextView  expectedSellingAmount;
    private TextView  benefice;
    private TextView  decision;
    private TextView  stockDate;
    private TextView  uploadStatus;
    private Button    buttonCreateInstances;

    // ── State ─────────────────────────────────────────────────────────────────
    private String         stockID;
    private DatabaseHelper dbHelper;
    private PdfGenerator   pdfGenerator;
    private ProgressDialog progressDialog;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        dbHelper     = new DatabaseHelper(this);
        pdfGenerator = new PdfGenerator(dbHelper);

        setupToolbar();
        initializeViews();

        stockID = getIntent().getStringExtra(DatabaseHelper.COLUMN_STOCK_ID);
        Log.d(TAG, "Received stock ID: " + stockID);
        loadStockDetails(stockID);

        buttonCreateInstances.setOnClickListener(v -> {
            if (isNetworkConnected()) {
                authenticateUser();
            } else {
                Toast.makeText(this,
                        "Pas de connexion réseau. Veuillez vous connecter et réessayer.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ── Setup ─────────────────────────────────────────────────────────────────

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initializeViews() {
        imageViewProduct      = findViewById(R.id.imageViewProduct);
        imageViewFacture      = findViewById(R.id.imageViewFacture);
        productTitle          = findViewById(R.id.textViewProductTitle);
        totalAmount           = findViewById(R.id.textViewTotalAmount);
        pieces                = findViewById(R.id.textViewPieces);
        manufactureDate       = findViewById(R.id.textViewManufactureDate);
        expirationDate        = findViewById(R.id.textViewExpirationDate);
        supplierName          = findViewById(R.id.textViewSupplierName);
        supplierContact       = findViewById(R.id.textViewSupplierContact);
        pricePerUnit          = findViewById(R.id.textViewPU);
        totalAmountUsed       = findViewById(R.id.tvTotalAmountUsed);
        expectedSellingAmount = findViewById(R.id.tvExpectedSellingAmount);
        benefice              = findViewById(R.id.tvBenefice);
        decision              = findViewById(R.id.tvDecision);
        stockDate             = findViewById(R.id.tvDate);
        uploadStatus          = findViewById(R.id.tvUploadStatus);
        buttonCreateInstances = findViewById(R.id.buttonCreateInstances);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Génération des instances...");
        progressDialog.setCancelable(false);
    }

    // ── Data loading ──────────────────────────────────────────────────────────

    private void loadStockDetails(String id) {
        Cursor cursor = dbHelper.getStockById(id);
        if (cursor == null || !cursor.moveToFirst()) {
            Toast.makeText(this, "Détails du stock introuvables.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            displayStockDetails(cursor);
            loadProductImage(cursor);
            loadFactureImage(id);
        } catch (Exception e) {
            Log.e(TAG, "Error loading stock details", e);
        } finally {
            cursor.close();
        }
    }

    private void displayStockDetails(Cursor cursor) {
        String productID    = cursor.getString(cursor.getColumnIndexOrThrow("productID"));
        int    qty          = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOCK_QUANTITY));
        double amountUsed   = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TOTAL_AMOUNT_USED));
        int    statusID     = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS_ID));
        double expectedPrice = dbHelper.getExpectedSellingPrice(productID);

        productTitle.setText(dbHelper.getProductName(productID));
        totalAmount.setText(amountUsed + " BIF");
        pieces.setText(qty + " pièces");
        manufactureDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOCK_MAN_DATE)));
        expirationDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOCK_EXP_DATE)));
        supplierName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_NAME)));
        supplierContact.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_CONTACT)));
        pricePerUnit.setText((qty > 0 ? amountUsed / qty : 0) + " BIF");
        totalAmountUsed.setText(amountUsed + " BIF");
        expectedSellingAmount.setText((expectedPrice * qty) + " BIF");
        benefice.setText(((qty * expectedPrice) - amountUsed) + " BIF");
        decision.setText(dbHelper.getStatusLabel(statusID));
        stockDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOCK_DATE_TIME)));
        uploadStatus.setText(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UPLOAD_STATUS)) == 1
                        ? "Téléchargé" : "Non Téléchargé");

        boolean activeToInstances = dbHelper.isProductActiveToInstances(productID);
        buttonCreateInstances.setVisibility(
                (statusID == 3 && activeToInstances) ? Button.VISIBLE : Button.GONE);
    }

    private void loadProductImage(Cursor cursor) {
        String name = dbHelper.getProductPhotoName(
                cursor.getString(cursor.getColumnIndexOrThrow("productID")));
        if (name != null && !name.isEmpty()) {
            File file = new File(getFilesDir(), "products/" + name);
            if (file.exists()) {
                Glide.with(this).load(file).into(imageViewProduct);
                return;
            }
        }
        imageViewProduct.setImageResource(R.drawable.ic_placeholder);
    }

    private void loadFactureImage(String id) {
        String name = dbHelper.getFactureImageName(id);
        if (name != null && !name.isEmpty()) {
            File file = new File(getFilesDir(), "factures/" + name);
            if (file.exists()) {
                Glide.with(this).load(file).into(imageViewFacture);
                return;
            }
            // File missing locally — try downloading
            String url = "http://192.168.5.1/adermomanagement/uploads/factures/" + name;
            ImageDownloadUtil.downloadImageWithCustomPath(this, url, "factures");
        }
        imageViewFacture.setImageResource(R.drawable.ic_placeholder);
    }

    // ── Instance creation ─────────────────────────────────────────────────────

    private void createInstances(final String id) {
        dbHelper.generateInstancesOnServer(id, new DatabaseHelper.DataUpdateCallback() {
            @Override
            public void onComplete() {
                generatePdf(id);
                progressDialog.dismiss();
                Toast.makeText(StockDetailsActivity.this,
                        "Instances créées avec succès.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(String msg) {
                Log.e(TAG, "Error: " + msg);
                progressDialog.dismiss();
                Toast.makeText(StockDetailsActivity.this,
                        "Erreur : " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generatePdf(String id) {
        List<String> instances = dbHelper.getProductInstances(id);
        String path = getExternalFilesDir(null) + "/StockInstances_" + id + ".pdf";
        pdfGenerator.createPdfWithBarcodes(instances, path);
        Toast.makeText(this, "PDF généré : " + path, Toast.LENGTH_LONG).show();
        Log.d(TAG, "PDF generated at: " + path);
        openPdf(path);
    }

    private void openPdf(String path) {
        Uri uri = FileProvider.getUriForFile(
                this, getPackageName() + ".fileprovider", new File(path));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(Intent.createChooser(intent, "Ouvrir le PDF"));
    }

    // ── Auth ──────────────────────────────────────────────────────────────────

    private void authenticateUser() {
        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        Intent intent = km.createConfirmDeviceCredentialIntent(
                "Authentification requise",
                "Confirmez votre schéma, PIN ou mot de passe pour continuer.");
        if (intent != null) {
            startActivityForResult(intent, REQUEST_CODE_CONFIRM);
        } else {
            Toast.makeText(this,
                    "Aucun verrouillage d'écran configuré.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONFIRM) {
            if (resultCode == RESULT_OK) {
                progressDialog.show();
                createInstances(stockID);
            } else {
                Toast.makeText(this,
                        "Authentification échouée. Veuillez réessayer.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm != null ? cm.getActiveNetworkInfo() : null;
        return ni != null && ni.isConnectedOrConnecting();
    }
}