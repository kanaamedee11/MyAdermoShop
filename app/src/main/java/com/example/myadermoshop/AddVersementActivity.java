package com.example.myadermoshop;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddVersementActivity extends AppCompatActivity {

    private static final String TAG = "AddVersementActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputEditText etExpectedAmount;
    private TextInputEditText etVersedAmount;
    private TextInputEditText etResteAmount;
    private Spinner spinnerPaymentType;
    private ImageView ivSelectedImage;

    private Uri imageUri;
    private DatabaseHelper dbHelper;

    private final List<TypePayment> paymentTypeList = new ArrayList<>();
    private ArrayAdapter<TypePayment> paymentTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_versement);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etExpectedAmount = findViewById(R.id.etExpectedAmount);
        etVersedAmount   = findViewById(R.id.etVersedAmount);
        etResteAmount    = findViewById(R.id.etResteAmount);
        spinnerPaymentType = findViewById(R.id.spinnerPaymentType);
        ivSelectedImage  = findViewById(R.id.ivSelectedImage);

        dbHelper = new DatabaseHelper(this);

        // Spinner setup
        paymentTypeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, paymentTypeList);
        paymentTypeAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentType.setAdapter(paymentTypeAdapter);

        loadPaymentTypesFromDatabase();

        // Expected amount is read-only — auto-filled from DB
        etExpectedAmount.setText(
                String.format(Locale.getDefault(), "%.2f", calculateAmountAvailableNow()));
        etExpectedAmount.setEnabled(false);
        etResteAmount.setEnabled(false);

        // Recalculate "reste" whenever versed amount changes
        etVersedAmount.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateResteAmount();
            }
        });

        findViewById(R.id.btnSelectImage).setOnClickListener(v -> openImageChooser());

        findViewById(R.id.btnAddVersement).setOnClickListener(v -> {
            String versedStr = etVersedAmount.getText().toString().trim();
            TypePayment selectedType = (TypePayment) spinnerPaymentType.getSelectedItem();

            if (versedStr.isEmpty() || selectedType == null) {
                Toast.makeText(this,
                        "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            double expectedAmount = Double.parseDouble(
                    etExpectedAmount.getText().toString());
            double versedAmount;
            try {
                versedAmount = Double.parseDouble(versedStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Montant invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            if (versedAmount > expectedAmount) {
                Toast.makeText(this,
                        "Le montant versé ne peut pas dépasser le montant attendu",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                addVersement(expectedAmount, versedAmount, selectedType);
            } catch (IOException e) {
                Log.e(TAG, "Error saving versement image", e);
                Toast.makeText(this,
                        "Erreur lors de la sauvegarde de l'image",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ── Data loading ──────────────────────────────────────────────────────────

    private void loadPaymentTypesFromDatabase() {
        Cursor cursor = dbHelper.getAllPaymentTypes();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        paymentTypeList.add(new TypePayment(
                                cursor.getInt(cursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_PAYMENT_TYPE_ID)),
                                cursor.getString(cursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_PAYMENT_METHOD)),
                                cursor.getString(cursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_SUB_SUB_ACCOUNT_ID))));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        paymentTypeAdapter.notifyDataSetChanged();
    }

    private double calculateAmountAvailableNow() {
        return dbHelper.calculateAmountAvailableSinceLastControl();
    }

    // ── Image chooser ─────────────────────────────────────────────────────────

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(FileUtils.MIME_TYPE_IMAGE);
        startActivityForResult(
                Intent.createChooser(intent, "Sélectionner une image"),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != PICK_IMAGE_REQUEST
                || resultCode != RESULT_OK
                || data == null
                || data.getData() == null) return;

        imageUri = data.getData();
        try {
            ivSelectedImage.setImageBitmap(
                    MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "Échec du chargement de l'image.", Toast.LENGTH_SHORT).show();
        }
    }

    // ── Add versement ─────────────────────────────────────────────────────────

    private void addVersement(double expectedAmount, double versedAmount,
                              TypePayment typePayment) throws IOException {
        String pictureName  = generateUniqueFileName();
        String versementID  = generateUniqueVersementID();
        String now = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Log.d(TAG, "Generated picture name: " + pictureName);
        Log.d(TAG, "Generated versement ID: " + versementID);

        dbHelper.addVersement(new Versement(
                versementID,
                getLoggedInEmployeeID(),
                null,           // adminID — filled server-side
                1,              // statusID
                expectedAmount,
                versedAmount,
                pictureName,
                "",             // pictureUrl — filled after upload
                now,
                now,
                typePayment.getPaymentTypeID(),
                0               // uploadStatus — not yet uploaded
        ));

        if (imageUri != null) {
            dbHelper.saveVersementImageWithNewName(imageUri, pictureName);
        } else {
            Log.w(TAG, "No image URI selected — versement saved without image.");
        }

        Toast.makeText(this, "Versement ajouté avec succès", Toast.LENGTH_SHORT).show();

        // Reset form
        etExpectedAmount.setText(
                String.format(Locale.getDefault(), "%.2f", calculateAmountAvailableNow()));
        etVersedAmount.setText("");
        ivSelectedImage.setImageResource(R.drawable.ic_product_placeholder);
        imageUri = null;
        updateResteAmount();
    }

    // ── UI helpers ────────────────────────────────────────────────────────────

    private void updateResteAmount() {
        String versedStr   = etVersedAmount.getText().toString().trim();
        String expectedStr = etExpectedAmount.getText().toString().trim();

        if (versedStr.isEmpty() || expectedStr.isEmpty()) {
            etResteAmount.setText(expectedStr);
            return;
        }
        try {
            double reste = Double.parseDouble(expectedStr) - Double.parseDouble(versedStr);
            if (reste < 0) reste = 0;
            etResteAmount.setText(
                    String.format(Locale.getDefault(), "%.2f", reste));
        } catch (NumberFormatException e) {
            etResteAmount.setText(expectedStr);
        }
    }

    // ── Generators ───────────────────────────────────────────────────────────

    private String generateUniqueFileName() {
        return "versement_" + getLoggedInEmployeeID()
                + "_" + new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(new Date())
                + ".jpeg";
    }

    private String generateUniqueVersementID() {
        return "vers" + getLoggedInEmployeeID()
                + "_" + new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(new Date())
                + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String getLoggedInEmployeeID() {
        return getSharedPreferences("MyApp", MODE_PRIVATE)
                .getString("employeeID", "");
    }
}