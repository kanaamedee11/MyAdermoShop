package com.example.myadermoshop;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ItemsWithoutInstancesFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseHelper dbHelper;
    private DeterioratedProductWithoutInstanceAdapter deterioratedProductWithoutInstanceAdapter;
    private List<DeterioratedProductWithoutInstance> deterioratedProductWithoutInstanceList;
    private TextInputEditText etDeteriorationDate;
    private TextInputEditText etQuantity;
    private TextInputEditText etReason;
    private List<Product> productList;
    private RecyclerView recyclerViewItemsWithoutInstances;
    private Uri selectedImageUri;
    private Spinner spinnerProductID;
    private TextView tvSelectedImage;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_items_without_instances, viewGroup, false);
        this.recyclerViewItemsWithoutInstances = viewInflate.findViewById(R.id.recyclerViewItemsWithoutInstances);
        FloatingActionButton floatingActionButton = viewInflate.findViewById(R.id.fab_add_item_without_instance);
        this.recyclerViewItemsWithoutInstances.setLayoutManager(new LinearLayoutManager(getContext()));
        this.deterioratedProductWithoutInstanceList = new ArrayList<>();
        this.deterioratedProductWithoutInstanceAdapter = new DeterioratedProductWithoutInstanceAdapter(getContext(), this.deterioratedProductWithoutInstanceList);
        this.recyclerViewItemsWithoutInstances.setAdapter(this.deterioratedProductWithoutInstanceAdapter);
        this.dbHelper = new DatabaseHelper(getContext());
        loadDeterioratedProductsWithoutInstances();
        floatingActionButton.setOnClickListener(view -> {
            if (!Utils.checkAndDisplayClosure(getActivity(), dbHelper)) {
                showAddItemWithoutInstanceDialog();
            }
        });
        return viewInflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDeterioratedProductsWithoutInstances();
    }

    private void loadDeterioratedProductsWithoutInstances() {
        this.deterioratedProductWithoutInstanceList.clear();
        this.deterioratedProductWithoutInstanceList.addAll(this.dbHelper.fetchAllDeterioratedProductsWithoutInstances());
        this.deterioratedProductWithoutInstanceAdapter.notifyDataSetChanged();
    }

    private void showAddItemWithoutInstanceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View viewInflate = getLayoutInflater().inflate(R.layout.dialog_add_item_without_instance, null);
        builder.setView(viewInflate);
        this.spinnerProductID = viewInflate.findViewById(R.id.spinnerProductID);
        this.etQuantity = viewInflate.findViewById(R.id.etQuantity);
        this.etReason = viewInflate.findViewById(R.id.etReason);
        this.etDeteriorationDate = viewInflate.findViewById(R.id.etDeteriorationDate);
        Button btnSelectImage = viewInflate.findViewById(R.id.btnSelectImage);
        this.tvSelectedImage = viewInflate.findViewById(R.id.tvSelectedImage);
        
        this.productList = this.dbHelper.getProductsWithoutInstances();
        ArrayList<String> productNames = new ArrayList<>();
        for (Product p : this.productList) {
            productNames.add(p.getProductName());
        }
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, productNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerProductID.setAdapter(arrayAdapter);
        
        btnSelectImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
        
        setDatePickerDialog(this.etDeteriorationDate);
        
        builder.setPositiveButton("Add", (dialogInterface, i) -> {
            try {
                processAddItemWithoutInstance();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private void processAddItemWithoutInstance() throws IOException {
        if (this.productList.isEmpty()) return;
        
        String productID = this.productList.get(this.spinnerProductID.getSelectedItemPosition()).getProductID();
        String qtyStr = this.etQuantity.getText().toString().trim();
        String reason = this.etReason.getText().toString().trim();
        String dateStr = this.etDeteriorationDate.getText().toString().trim();
        String photoName = this.selectedImageUri != null ? generateUniqueFileName() : null;
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String uniqueID = generateUniqueID();
        
        if (!productID.isEmpty() && !qtyStr.isEmpty() && !dateStr.isEmpty()) {
            if (this.selectedImageUri != null) {
                this.dbHelper.saveDeterioratedProductImageWithNewName(this.selectedImageUri, photoName);
            }
            this.dbHelper.addDeterioratedProductWithoutInstance(new DeterioratedProductWithoutInstance(
                uniqueID, productID, dateStr, reason, Integer.parseInt(qtyStr), 
                getLoggedInEmployeeID(), photoName, false, null, now, 0));
            loadDeterioratedProductsWithoutInstances();
        } else {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDeterioratedProductImage(Uri uri, String str) throws IOException {
        this.dbHelper.saveDeterioratedProductImageWithNewName(uri, str);
    }

    private String generateUniqueFileName() {
        return "DTWTI_" + getLoggedInEmployeeID() + "_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".jpeg";
    }

    private String generateUniqueID() {
        return "prod_" + getLoggedInEmployeeID() + "_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String getLoggedInEmployeeID() {
        return getActivity() != null ? getActivity().getSharedPreferences("MyApp", 0).getString("employeeID", "") : "";
    }

    private void setDatePickerDialog(final TextInputEditText textInputEditText) {
        textInputEditText.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(getContext(), (datePicker, i, i2, i3) -> {
                textInputEditText.setText(i + "-" + (i2 + 1) + "-" + i3);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("_display_name");
                if (columnIndex != -1) {
                    result = cursor.getString(columnIndex);
                }
            }
            cursor.close();
        }
        return result;
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == PICK_IMAGE_REQUEST && i2 == -1 && intent != null && intent.getData() != null) {
            this.selectedImageUri = intent.getData();
            String fileName = getFileNameFromUri(this.selectedImageUri);
            if (this.tvSelectedImage != null) {
                this.tvSelectedImage.setText(fileName != null ? fileName : "No image selected");
            }
        }
    }
}