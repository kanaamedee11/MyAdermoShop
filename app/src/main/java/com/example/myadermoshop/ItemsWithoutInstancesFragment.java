package com.example.myadermoshop;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             @Nullable ViewGroup viewGroup,
                             @Nullable Bundle bundle) {
        View view = layoutInflater.inflate(
                R.layout.fragment_items_without_instances, viewGroup, false);

        recyclerViewItemsWithoutInstances =
                view.findViewById(R.id.recyclerViewItemsWithoutInstances);

        ExtendedFloatingActionButton fab =
                view.findViewById(R.id.fab_add_item_without_instance);

        recyclerViewItemsWithoutInstances
                .setLayoutManager(new LinearLayoutManager(getContext()));
        deterioratedProductWithoutInstanceList = new ArrayList<>();
        deterioratedProductWithoutInstanceAdapter =
                new DeterioratedProductWithoutInstanceAdapter(
                        getContext(), deterioratedProductWithoutInstanceList);
        recyclerViewItemsWithoutInstances
                .setAdapter(deterioratedProductWithoutInstanceAdapter);

        dbHelper = new DatabaseHelper(getContext());
        loadDeterioratedProductsWithoutInstances();

        fab.setOnClickListener(v -> {
            if (!Utils.checkAndDisplayClosure(getActivity(), dbHelper)) {
                showAddItemWithoutInstanceDialog();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDeterioratedProductsWithoutInstances();
    }

    private void loadDeterioratedProductsWithoutInstances() {
        deterioratedProductWithoutInstanceList.clear();
        deterioratedProductWithoutInstanceList.addAll(
                dbHelper.fetchAllDeterioratedProductsWithoutInstances());
        deterioratedProductWithoutInstanceAdapter.notifyDataSetChanged();
    }

    private void showAddItemWithoutInstanceDialog() {
        View dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_add_item_without_instance, null);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
        }

        spinnerProductID    = dialogView.findViewById(R.id.spinnerProductID);
        etQuantity          = dialogView.findViewById(R.id.etQuantity);
        etReason            = dialogView.findViewById(R.id.etReason);
        etDeteriorationDate = dialogView.findViewById(R.id.etDeteriorationDate);
        tvSelectedImage     = dialogView.findViewById(R.id.tvSelectedImage);

        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);
        Button btnCancel      = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm     = dialogView.findViewById(R.id.btnConfirm);

        productList = dbHelper.getProductsWithoutInstances();
        ArrayList<String> productNames = new ArrayList<>();
        for (Product p : productList) {
            productNames.add(p.getProductName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                productNames);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerProductID.setAdapter(adapter);

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        setDatePickerDialog(etDeteriorationDate);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            try {
                processAddItemWithoutInstance();
                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        dialog.show();
    }

    private void processAddItemWithoutInstance() throws IOException {
        if (productList == null || productList.isEmpty()) return;

        String productID = productList
                .get(spinnerProductID.getSelectedItemPosition())
                .getProductID();
        String qtyStr  = etQuantity.getText().toString().trim();
        String reason  = etReason.getText().toString().trim();
        String dateStr = etDeteriorationDate.getText().toString().trim();
        String photoName = selectedImageUri != null ? generateUniqueFileName() : null;
        String now = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String uniqueID = generateUniqueID();

        if (!productID.isEmpty() && !qtyStr.isEmpty() && !dateStr.isEmpty()) {
            if (selectedImageUri != null) {
                dbHelper.saveDeterioratedProductImageWithNewName(
                        selectedImageUri, photoName);
            }
            dbHelper.addDeterioratedProductWithoutInstance(
                    new DeterioratedProductWithoutInstance(
                            uniqueID, productID, dateStr, reason,
                            Integer.parseInt(qtyStr),
                            getLoggedInEmployeeID(), photoName,
                            false, null, now, 0));
            loadDeterioratedProductsWithoutInstances();
        } else {
            Toast.makeText(getContext(),
                    "Veuillez remplir tous les champs requis",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String generateUniqueFileName() {
        return "DTWTI_" + getLoggedInEmployeeID() + "_"
                + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .format(new Date())
                + ".jpeg";
    }

    private String generateUniqueID() {
        return "prod_" + getLoggedInEmployeeID() + "_"
                + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .format(new Date())
                + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String getLoggedInEmployeeID() {
        return getActivity() != null
                ? getActivity()
                .getSharedPreferences("MyApp", 0)
                .getString("employeeID", "")
                : "";
    }

    private void setDatePickerDialog(final TextInputEditText editText) {
        editText.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(getContext(),
                    (datePicker, year, month, day) ->
                            editText.setText(year + "-" + (month + 1) + "-" + day),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        Cursor cursor = getContext().getContentResolver()
                .query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int col = cursor.getColumnIndex("_display_name");
                if (col != -1) result = cursor.getString(col);
            }
            cursor.close();
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == -1
                && data != null
                && data.getData() != null) {
            selectedImageUri = data.getData();
            String fileName = getFileNameFromUri(selectedImageUri);
            if (tvSelectedImage != null) {
                tvSelectedImage.setText(
                        fileName != null ? fileName : "Image sélectionnée");
            }
        }
    }
}