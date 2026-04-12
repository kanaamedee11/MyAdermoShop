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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.client.android.Intents;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ItemsWithInstancesFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int SCAN_BARCODE_REQUEST = 2;

    private DatabaseHelper dbHelper;
    private DeterioratedProductWithInstanceAdapter deterioratedProductWithInstanceAdapter;
    private List<DeterioratedProductWithInstance> deterioratedProductWithInstanceList;

    private TextInputEditText etDeteriorationDate;
    private TextInputEditText etInstanceID;
    private TextInputEditText etQuantity;
    private TextInputEditText etReason;
    private RecyclerView recyclerViewItemsWithInstances;
    private Uri selectedImageUri;
    private TextView tvSelectedImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             @Nullable ViewGroup viewGroup,
                             @Nullable Bundle bundle) {
        View view = layoutInflater.inflate(
                R.layout.fragment_items_with_instances, viewGroup, false);

        recyclerViewItemsWithInstances =
                view.findViewById(R.id.recyclerViewItemsWithInstances);

        // FAB — now ExtendedFloatingActionButton with id fab_add_item_with_instance
        ExtendedFloatingActionButton fab =
                view.findViewById(R.id.fab_add_item_with_instance);

        recyclerViewItemsWithInstances
                .setLayoutManager(new LinearLayoutManager(getContext()));
        deterioratedProductWithInstanceList = new ArrayList<>();
        deterioratedProductWithInstanceAdapter =
                new DeterioratedProductWithInstanceAdapter(
                        getContext(), deterioratedProductWithInstanceList);
        recyclerViewItemsWithInstances
                .setAdapter(deterioratedProductWithInstanceAdapter);

        dbHelper = new DatabaseHelper(getContext());
        loadDeterioratedProductsWithInstances();

        fab.setOnClickListener(v -> {
            if (!Utils.checkAndDisplayClosure(getActivity(), dbHelper)) {
                showAddItemWithInstanceDialog();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDeterioratedProductsWithInstances();
    }

    private void loadDeterioratedProductsWithInstances() {
        deterioratedProductWithInstanceList.clear();
        deterioratedProductWithInstanceList.addAll(
                dbHelper.fetchAllDeterioratedProductsWithInstances());
        deterioratedProductWithInstanceAdapter.notifyDataSetChanged();
    }

    private void showAddItemWithInstanceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_add_item_with_instance, null);
        builder.setView(dialogView);

        etInstanceID       = dialogView.findViewById(R.id.etInstanceID);
        Button btnScanInstance = dialogView.findViewById(R.id.btnScanInstance);
        etQuantity         = dialogView.findViewById(R.id.etQuantity);
        etReason           = dialogView.findViewById(R.id.etReason);
        etDeteriorationDate = dialogView.findViewById(R.id.etDeteriorationDate);
        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);
        tvSelectedImage    = dialogView.findViewById(R.id.tvSelectedImage);

        etQuantity.setEnabled(false);

        btnScanInstance.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(getContext(),
                                BarcodeScannerDeterioretedInstanceActivity.class),
                        SCAN_BARCODE_REQUEST));

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        setDatePickerDialog(etDeteriorationDate);

        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            try {
                processAddItemWithInstance();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton("Annuler",
                (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void processAddItemWithInstance() throws IOException {
        String instanceID = etInstanceID.getText().toString().trim();
        String qtyStr     = etQuantity.getText().toString().trim();
        String reason     = etReason.getText().toString().trim();
        String dateStr    = etDeteriorationDate.getText().toString().trim();
        String photoName  = selectedImageUri != null ? generateUniqueFileName() : null;
        String now        = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String uniqueID   = generateUniqueID();

        if (instanceID.isEmpty() || qtyStr.isEmpty() || dateStr.isEmpty()) {
            Toast.makeText(getContext(),
                    "Veuillez remplir tous les champs requis",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!dbHelper.doesInstanceExist(instanceID)) {
            etInstanceID.setError("L'instance n'existe pas.");
            return;
        }
        if (dbHelper.isInstanceSold(instanceID)) {
            etInstanceID.setError("L'instance a déjà été vendue.");
            return;
        }
        if (dbHelper.isInstanceDeteriorated(instanceID)) {
            etInstanceID.setError("L'instance a déjà été marquée comme détériorée.");
            return;
        }

        if (selectedImageUri != null) {
            dbHelper.saveDeterioratedProductImageWithNewName(
                    selectedImageUri, photoName);
        }

        dbHelper.addDeterioratedProductWithInstance(
                new DeterioratedProductWithInstance(
                        uniqueID, instanceID, dateStr, reason,
                        Integer.parseInt(qtyStr),
                        getLoggedInEmployeeID(), photoName,
                        false, null, now, null, 0));

        dbHelper.updateInstanceState(instanceID, "deteriorated");
        loadDeterioratedProductsWithInstances();
    }

    private String getLoggedInEmployeeID() {
        return getActivity() != null
                ? getActivity()
                .getSharedPreferences("MyApp", 0)
                .getString("employeeID", "")
                : "";
    }

    private String generateUniqueFileName() {
        return "DTWI_" + getLoggedInEmployeeID() + "_"
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

    private void setDatePickerDialog(final EditText editText) {
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

        } else if (requestCode == SCAN_BARCODE_REQUEST
                && resultCode == -1
                && data != null) {
            String result = data.getStringExtra(Intents.Scan.RESULT);
            if (etInstanceID != null) {
                etInstanceID.setText(result);
                if (dbHelper.isInstanceSold(result)) {
                    etInstanceID.setError("L'instance a déjà été vendue.");
                } else if (dbHelper.isInstanceDeteriorated(result)) {
                    etInstanceID.setError(
                            "L'instance a déjà été marquée comme détériorée.");
                } else {
                    etInstanceID.setError(null);
                    etQuantity.setText("1");
                }
            }
        }
    }
}