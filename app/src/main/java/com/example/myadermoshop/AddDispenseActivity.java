package com.example.myadermoshop;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "AddDispenseActivity";
    private DatabaseHelper dbHelper;
    private Uri imageUri;
    private ImageView ivSelectedImage;
    private ArrayAdapter<String> paymentTypeAdapter;
    private int selectedPaymentTypeId;
    private int selectedTypeDispenseId;
    private Spinner spinnerPaymentType;
    private Spinner spinnerTypeDispense;
    private ArrayAdapter<String> typeDispenseAdapter;
    private final ArrayList<String> typeDispenseNames = new ArrayList<>();
    private final ArrayList<Integer> typeDispenseIds = new ArrayList<>();
    private final ArrayList<String> paymentTypeNames = new ArrayList<>();
    private final ArrayList<Integer> paymentTypeIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_dispense);
        this.dbHelper = new DatabaseHelper(this);

        final EditText editText = findViewById(R.id.etDispenseAmount);
        this.spinnerTypeDispense = findViewById(R.id.spinnerTypeDispense);
        this.spinnerPaymentType = findViewById(R.id.spinnerPaymentType);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        this.ivSelectedImage = findViewById(R.id.ivSelectedImage);
        Button btnAddDispense = findViewById(R.id.btnAddDispense);

        this.typeDispenseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, this.typeDispenseNames);
        this.typeDispenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (this.spinnerTypeDispense != null) {
            this.spinnerTypeDispense.setAdapter(this.typeDispenseAdapter);
        }

        this.paymentTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, this.paymentTypeNames);
        this.paymentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (this.spinnerPaymentType != null) {
            this.spinnerPaymentType.setAdapter(this.paymentTypeAdapter);
        }

        loadTypeDispensesIntoSpinner();
        loadPaymentTypesIntoSpinner();

        if (btnSelectImage != null) {
            btnSelectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddDispenseActivity.this.openImageChooser();
                }
            });
        }

        if (btnAddDispense != null) {
            btnAddDispense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editText == null) return;
                    String string = editText.getText().toString();
                    if (!string.isEmpty()) {
                        try {
                            AddDispenseActivity.this.addDispense(Double.parseDouble(string), AddDispenseActivity.this.selectedTypeDispenseId, AddDispenseActivity.this.selectedPaymentTypeId);
                        } catch (IOException e) {
                            Log.e(TAG, "Error adding dispense", e);
                            Toast.makeText(AddDispenseActivity.this, "Error adding dispense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddDispenseActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void loadTypeDispensesIntoSpinner() {
        Cursor allTypeDispenses = this.dbHelper.getAllTypeDispenses();
        if (allTypeDispenses != null && allTypeDispenses.moveToFirst()) {
            do {
                int idIdx = allTypeDispenses.getColumnIndex("typeDispenseID");
                int nameIdx = allTypeDispenses.getColumnIndex(DatabaseHelper.COLUMN_TYPE_DISPENSE_NAME);
                if (idIdx != -1 && nameIdx != -1) {
                    int i = allTypeDispenses.getInt(idIdx);
                    String string = allTypeDispenses.getString(nameIdx);
                    this.typeDispenseIds.add(i);
                    this.typeDispenseNames.add(string);
                }
            } while (allTypeDispenses.moveToNext());
            allTypeDispenses.close();
        }
        this.typeDispenseAdapter.notifyDataSetChanged();
        if (this.spinnerTypeDispense != null) {
            this.spinnerTypeDispense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
                    AddDispenseActivity.this.selectedTypeDispenseId = AddDispenseActivity.this.typeDispenseIds.get(i2);
                }
            });
        }
    }

    private void loadPaymentTypesIntoSpinner() {
        Cursor allPaymentTypes = new DatabaseHelper(this).getAllPaymentTypes();
        if (allPaymentTypes != null && allPaymentTypes.moveToFirst()) {
            do {
                int idIdx = allPaymentTypes.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_TYPE_ID);
                int methodIdx = allPaymentTypes.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_METHOD);
                if (idIdx != -1 && methodIdx != -1) {
                    int i = allPaymentTypes.getInt(idIdx);
                    String string = allPaymentTypes.getString(methodIdx);
                    this.paymentTypeIds.add(i);
                    this.paymentTypeNames.add(string);
                }
            } while (allPaymentTypes.moveToNext());
            allPaymentTypes.close();
        }
        this.paymentTypeAdapter.notifyDataSetChanged();
        if (this.spinnerPaymentType != null) {
            this.spinnerPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
                    AddDispenseActivity.this.selectedPaymentTypeId = AddDispenseActivity.this.paymentTypeIds.get(i2);
                }
            });
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(FileUtils.MIME_TYPE_IMAGE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1 && i2 == RESULT_OK && intent != null && intent.getData() != null) {
            this.imageUri = intent.getData();
            try {
                if (this.ivSelectedImage != null) {
                    this.ivSelectedImage.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), this.imageUri));
                }
            } catch (IOException e) {
                Log.e(TAG, "Error loading image", e);
            }
        }
    }

    private void addDispense(double d, int i, int i2) throws IOException {
        String strGenerateUniqueFileName = generateUniqueFileName();
        String strGenerateUniqueDispenseID = generateUniqueDispenseID();
        new DatabaseHelper(this).addDispense(new Dispense(strGenerateUniqueDispenseID, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()), i, getLoggedInEmployeeID(), 1, strGenerateUniqueFileName, d, i2, 0));
        saveDispenseImage(this.imageUri, strGenerateUniqueFileName);
        Toast.makeText(this, "Dispense added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveDispenseImage(Uri uri, String str) throws IOException {
        if (uri != null) {
            new DatabaseHelper(this).saveDispenseImageWithNewName(uri, str);
        }
    }

    private String generateUniqueFileName() {
        return "dispense_" + getLoggedInEmployeeID() + "_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".jpeg";
    }

    private String generateUniqueDispenseID() {
        return "disp" + getLoggedInEmployeeID() + "_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String getLoggedInEmployeeID() {
        return getSharedPreferences("MyApp", 0).getString("employeeID", "");
    }
}
