package com.example.myadermoshop;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddPurchaseActivity extends AppCompatActivity {

    private static final String TAG = "AddPurchaseActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    // Views
    private Spinner spinnerProduct;
    private Spinner spinnerPaymentType;
    private EditText editTextPieces;
    private EditText editTextTotalAmount;
    private EditText editTextPricePerUnit;
    private EditText editTextMfDate;
    private EditText editTextExpDate;
    private EditText editTextSupplierName;
    private EditText editTextSupplierContact;
    private EditText editTextFactureNumber;
    private TextView textViewSelectedImage;
    private Button buttonAddPurchase;
    private Button buttonSelectImage;

    // State
    private DatabaseHelper dbHelper;
    private HttpService httpService;
    private String apiKey;
    private Uri selectedImageUri;
    private String selectedProductId;
    private String selectedPaymentTypeId;
    private String uniqueStockID;

    // Spinner backing data
    private List<String> productIds = new ArrayList<>();
    private List<Integer> paymentTypeIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();

        dbHelper     = new DatabaseHelper(this);
        httpService  = RetrofitInstance.getHttpService();
        apiKey       = getSharedPreferences("MyApp", MODE_PRIVATE)
                .getString(DatabaseHelper.COLUMN_API_KEY, "");

        loadProductsIntoSpinner();
        loadPaymentTypesIntoSpinner();
        setDatePickerDialog(editTextMfDate);
        setDatePickerDialog(editTextExpDate);

        editTextPieces.addTextChangedListener(new PriceRecalcWatcher());
        editTextTotalAmount.addTextChangedListener(new PriceRecalcWatcher());

        buttonSelectImage.setOnClickListener(v -> openFileChooser());
        buttonAddPurchase.setOnClickListener(v -> addPurchase());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // View init
    // ──────────────────────────────────────────────────────────────────────────

    private void initViews() {
        spinnerProduct        = findViewById(R.id.spinnerProduct);
        spinnerPaymentType    = findViewById(R.id.spinnerPaymentType);
        editTextPieces        = findViewById(R.id.editTextPieces);
        editTextTotalAmount   = findViewById(R.id.editTextTotalAmount);
        editTextPricePerUnit  = findViewById(R.id.editTextPricePerUnit);
        editTextMfDate        = findViewById(R.id.editTextMfDate);
        editTextExpDate       = findViewById(R.id.editTextExpDate);
        editTextSupplierName  = findViewById(R.id.editTextSupplierName);
        editTextSupplierContact = findViewById(R.id.editTextSupplierContact);
        editTextFactureNumber = findViewById(R.id.editTextFactureNumber);
        textViewSelectedImage = findViewById(R.id.textViewSelectedImage);
        buttonAddPurchase     = findViewById(R.id.buttonAddPurchase);
        buttonSelectImage     = findViewById(R.id.buttonSelectImage);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Spinner loaders
    // ──────────────────────────────────────────────────────────────────────────

    private void loadProductsIntoSpinner() {
        List<String> productNames = new ArrayList<>();
        productIds.clear();

        Cursor cursor = dbHelper.getAllProductsCursor();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        productIds.add(cursor.getString(
                                cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID)));
                        productNames.add(cursor.getString(
                                cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME)));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, productNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduct.setAdapter(adapter);
        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onNothingSelected(AdapterView<?> parent) { }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedProductId = productIds.get(pos);
            }
        });
    }

    private void loadPaymentTypesIntoSpinner() {
        List<String> paymentNames = new ArrayList<>();
        paymentTypeIds.clear();

        Cursor cursor = dbHelper.getAllPaymentTypes();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        paymentTypeIds.add(cursor.getInt(
                                cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PAYMENT_TYPE_ID)));
                        paymentNames.add(cursor.getString(
                                cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PAYMENT_METHOD)));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, paymentNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentType.setAdapter(adapter);
        spinnerPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onNothingSelected(AdapterView<?> parent) { }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedPaymentTypeId = String.valueOf(paymentTypeIds.get(pos));
            }
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Date picker
    // ──────────────────────────────────────────────────────────────────────────

    private void setDatePickerDialog(final EditText editText) {
        editText.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this,
                    (picker, year, month, day) ->
                            editText.setText(year + "-" + (month + 1) + "-" + day),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Image picker
    // ──────────────────────────────────────────────────────────────────────────

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                String name = getFileNameFromUri(selectedImageUri);
                textViewSelectedImage.setText(name != null ? name : selectedImageUri.getLastPathSegment());
            }
        }
    }

    private String getFileNameFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) return null;
        try {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex("_display_name");
                return idx >= 0 ? cursor.getString(idx) : null;
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Add purchase
    // ──────────────────────────────────────────────────────────────────────────

    private void addPurchase() {
        String piecesStr        = editTextPieces.getText().toString().trim();
        String totalAmountStr   = editTextTotalAmount.getText().toString().trim();
        String mfDate           = editTextMfDate.getText().toString().trim();
        String expDate          = editTextExpDate.getText().toString().trim();
        String supplierName     = editTextSupplierName.getText().toString().trim();
        String supplierContact  = editTextSupplierContact.getText().toString().trim();
        String factureNumber    = editTextFactureNumber.getText().toString().trim();

        if (selectedProductId == null
                || selectedPaymentTypeId == null
                || selectedImageUri == null
                || piecesStr.isEmpty()
                || totalAmountStr.isEmpty()
                || mfDate.isEmpty()
                || expDate.isEmpty()
                || supplierName.isEmpty()
                || supplierContact.isEmpty()
                || factureNumber.isEmpty()) {
            Toast.makeText(this, "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        int pieces;
        double totalAmount;
        try {
            pieces      = Integer.parseInt(piecesStr);
            totalAmount = Double.parseDouble(totalAmountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pieces <= 0) {
            Toast.makeText(this, "Number of pieces must be greater than 0.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update price-per-unit display
        editTextPricePerUnit.setText(String.valueOf(totalAmount / pieces));

        uniqueStockID = generateUniqueStockID(getLoggedInEmployeeID());
        String stockDateTime    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String employeeID       = getLoggedInEmployeeID();
        String factureImageName = generateUniqueFileName();

        try {
            String saved = dbHelper.saveImageWithNewName(selectedImageUri, factureImageName);
            if (saved == null) {
                Toast.makeText(this, "Error saving image.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error saving image", e);
            Toast.makeText(this, "Error saving image.", Toast.LENGTH_SHORT).show();
            return;
        }

        Stock stock = new Stock(
                uniqueStockID,
                stockDateTime,
                pieces,
                totalAmount,
                selectedProductId,
                mfDate,
                expDate,
                supplierName,
                supplierContact,
                factureNumber,
                factureImageName,
                Integer.parseInt(selectedPaymentTypeId),
                1,   // statusID
                employeeID
        );

        if (dbHelper.addStock(stock)) {
            Toast.makeText(this, "Purchase added to local database", Toast.LENGTH_SHORT).show();
            checkServerStatusAndUpload(stock);
        } else {
            Toast.makeText(this, "Error saving to local database", Toast.LENGTH_SHORT).show();
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Server upload
    // ──────────────────────────────────────────────────────────────────────────

    private void checkServerStatusAndUpload(final Stock stock) {
        dbHelper.getFromServerStatus(new DatabaseHelper.ServerStatusCallback() {
            @Override
            public void onSuccess() {
                dbHelper.uploadStockDataToServer(httpService, stock,
                        new DatabaseHelper.UploadCallback() {
                            @Override
                            public void onSuccess(String message) {
                                runOnUiThread(() -> {
                                    Toast.makeText(AddPurchaseActivity.this,
                                            message, Toast.LENGTH_SHORT).show();
                                    dbHelper.markStockAsUploaded(stock.getStockID());
                                });
                            }
                            @Override
                            public void onFailure(String message) {
                                runOnUiThread(() ->
                                        Toast.makeText(AddPurchaseActivity.this,
                                                message, Toast.LENGTH_SHORT).show());
                            }
                        });
            }
            @Override
            public void onFailure(String message) {
                runOnUiThread(() ->
                        Toast.makeText(AddPurchaseActivity.this,
                                "Server not reachable. Data saved locally.",
                                Toast.LENGTH_LONG).show());
            }
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────────────────

    private String generateUniqueStockID(String employeeID) {
        return employeeID + "_"
                + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date())
                + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateUniqueFileName() {
        return "facture_" + getLoggedInEmployeeID()
                + "_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date())
                + ".jpeg";
    }

    private String getLoggedInEmployeeID() {
        return getSharedPreferences("MyApp", MODE_PRIVATE).getString("employeeID", "");
    }

    private void calculatePricePerUnit() {
        String piecesStr      = editTextPieces.getText().toString().trim();
        String totalAmountStr = editTextTotalAmount.getText().toString().trim();
        if (piecesStr.isEmpty() || totalAmountStr.isEmpty()) return;
        try {
            int pieces       = Integer.parseInt(piecesStr);
            double total     = Double.parseDouble(totalAmountStr);
            if (pieces > 0) {
                editTextPricePerUnit.setText(String.valueOf(total / pieces));
            }
        } catch (NumberFormatException e) {
            // Ignore — user is mid-typing
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TextWatcher
    // ──────────────────────────────────────────────────────────────────────────

    private class PriceRecalcWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
        @Override
        public void afterTextChanged(Editable s) {
            calculatePricePerUnit();
        }
    }
}