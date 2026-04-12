package com.example.myadermoshop;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddDispenseActivity extends AppCompatActivity {

    private static final String TAG             = "AddDispenseActivity";
    private static final int    PICK_IMAGE_REQUEST = 1;

    private DatabaseHelper       dbHelper;
    private Uri                  imageUri;
    private ImageView            ivSelectedImage;
    private Spinner              spinnerTypeDispense;
    private Spinner              spinnerPaymentType;

    private final ArrayList<String>  typeDispenseNames = new ArrayList<>();
    private final ArrayList<Integer> typeDispenseIds   = new ArrayList<>();
    private final ArrayList<String>  paymentTypeNames  = new ArrayList<>();
    private final ArrayList<Integer> paymentTypeIds    = new ArrayList<>();

    private ArrayAdapter<String> typeDispenseAdapter;
    private ArrayAdapter<String> paymentTypeAdapter;

    private int selectedTypeDispenseId;
    private int selectedPaymentTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dispense);

        dbHelper = new DatabaseHelper(this);

        EditText etDispenseAmount = findViewById(R.id.etDispenseAmount);
        spinnerTypeDispense       = findViewById(R.id.spinnerTypeDispense);
        spinnerPaymentType        = findViewById(R.id.spinnerPaymentType);
        Button btnSelectImage     = findViewById(R.id.btnSelectImage);
        ivSelectedImage           = findViewById(R.id.ivSelectedImage);
        Button btnAddDispense     = findViewById(R.id.btnAddDispense);

        // ── Spinners ──
        typeDispenseAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, typeDispenseNames);
        typeDispenseAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeDispense.setAdapter(typeDispenseAdapter);

        paymentTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, paymentTypeNames);
        paymentTypeAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentType.setAdapter(paymentTypeAdapter);

        loadTypeDispensesIntoSpinner();
        loadPaymentTypesIntoSpinner();

        // ── Listeners ──
        btnSelectImage.setOnClickListener(v -> openImageChooser());

        btnAddDispense.setOnClickListener(v -> {
            String amountStr = etDispenseAmount.getText().toString().trim();
            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer un montant", Toast.LENGTH_SHORT).show();
                return;
            }
            double amount = Double.parseDouble(amountStr);
            addDispense(amount, selectedTypeDispenseId, selectedPaymentTypeId);
        });
    }

    // ── Data loading ──────────────────────────────────────────────────────────

    private void loadTypeDispensesIntoSpinner() {
        Cursor cursor = dbHelper.getAllTypeDispenses();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                typeDispenseIds.add(cursor.getInt(
                        cursor.getColumnIndex("typeDispenseID")));
                typeDispenseNames.add(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE_DISPENSE_NAME)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        typeDispenseAdapter.notifyDataSetChanged();
        spinnerTypeDispense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view,
                                       int position, long id) {
                selectedTypeDispenseId = typeDispenseIds.get(position);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void loadPaymentTypesIntoSpinner() {
        Cursor cursor = new DatabaseHelper(this).getAllPaymentTypes();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                paymentTypeIds.add(cursor.getInt(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_TYPE_ID)));
                paymentTypeNames.add(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_METHOD)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        paymentTypeAdapter.notifyDataSetChanged();
        spinnerPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view,
                                       int position, long id) {
                selectedPaymentTypeId = paymentTypeIds.get(position);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    // ── Image chooser ─────────────────────────────────────────────────────────

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(FileUtils.MIME_TYPE_IMAGE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    getContentResolver(), imageUri);
            ivSelectedImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── Add dispense ──────────────────────────────────────────────────────────

    private void addDispense(double amount, int typeDispenseId, int paymentTypeId) {
        String pictureName  = generateUniqueFileName();
        String dispenseID   = generateUniqueDispenseID();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()).format(new Date());

        Log.d(TAG, "Generated picture name: " + pictureName);
        Log.d(TAG, "Generated dispense ID: "  + dispenseID);

        new DatabaseHelper(this).addDispense(new Dispense(
                dispenseID,
                now,
                typeDispenseId,
                getLoggedInEmployeeID(),
                1,
                pictureName,
                amount,
                paymentTypeId,
                0
        ));

        saveDispenseImage(imageUri, pictureName);
        Toast.makeText(this, "Dispense added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveDispenseImage(Uri uri, String fileName) {
        new DatabaseHelper(this).saveDispenseImageWithNewName(uri, fileName);
    }

    // ── Generators ───────────────────────────────────────────────────────────

    private String generateUniqueFileName() {
        return "dispense_" + getLoggedInEmployeeID()
                + "_" + new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(new Date())
                + ".jpeg";
    }

    private String generateUniqueDispenseID() {
        return "disp" + getLoggedInEmployeeID()
                + "_" + new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(new Date())
                + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String getLoggedInEmployeeID() {
        return getSharedPreferences("MyApp", MODE_PRIVATE)
                .getString("employeeID", "");
    }
}