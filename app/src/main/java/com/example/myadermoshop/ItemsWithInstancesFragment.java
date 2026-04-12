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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_items_with_instances, viewGroup, false);
        this.recyclerViewItemsWithInstances = viewInflate.findViewById(R.id.recyclerViewItemsWithInstances);
        FloatingActionButton floatingActionButton = viewInflate.findViewById(R.id.fab_add_item_with_instance);
        this.recyclerViewItemsWithInstances.setLayoutManager(new LinearLayoutManager(getContext()));
        this.deterioratedProductWithInstanceList = new ArrayList<>();
        this.deterioratedProductWithInstanceAdapter = new DeterioratedProductWithInstanceAdapter(getContext(), this.deterioratedProductWithInstanceList);
        this.recyclerViewItemsWithInstances.setAdapter(this.deterioratedProductWithInstanceAdapter);
        this.dbHelper = new DatabaseHelper(getContext());
        loadDeterioratedProductsWithInstances();
        
        floatingActionButton.setOnClickListener(view -> {
            if (!Utils.checkAndDisplayClosure(getActivity(), dbHelper)) {
                showAddItemWithInstanceDialog();
            }
        });
        
        return viewInflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDeterioratedProductsWithInstances();
    }

    private void loadDeterioratedProductsWithInstances() {
        this.deterioratedProductWithInstanceList.clear();
        this.deterioratedProductWithInstanceList.addAll(this.dbHelper.fetchAllDeterioratedProductsWithInstances());
        this.deterioratedProductWithInstanceAdapter.notifyDataSetChanged();
    }

    private void showAddItemWithInstanceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View viewInflate = getLayoutInflater().inflate(R.layout.dialog_add_item_with_instance, null);
        builder.setView(viewInflate);
        this.etInstanceID = viewInflate.findViewById(R.id.etInstanceID);
        Button btnScanInstance = viewInflate.findViewById(R.id.btnScanInstance);
        this.etQuantity = viewInflate.findViewById(R.id.etQuantity);
        this.etReason = viewInflate.findViewById(R.id.etReason);
        this.etDeteriorationDate = viewInflate.findViewById(R.id.etDeteriorationDate);
        Button btnSelectImage = viewInflate.findViewById(R.id.btnSelectImage);
        this.tvSelectedImage = viewInflate.findViewById(R.id.tvSelectedImage);
        this.etQuantity.setEnabled(false);

        btnScanInstance.setOnClickListener(view -> {
            startActivityForResult(new Intent(getContext(), BarcodeScannerDeterioretedInstanceActivity.class), SCAN_BARCODE_REQUEST);
        });

        btnSelectImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        setDatePickerDialog(this.etDeteriorationDate);

        builder.setPositiveButton("Ajouter", (dialogInterface, i) -> {
            try {
                processAddItemWithInstance();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton("Annuler", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private void processAddItemWithInstance() throws IOException {
        String instanceID = this.etInstanceID.getText().toString().trim();
        String qtyStr = this.etQuantity.getText().toString().trim();
        String reason = this.etReason.getText().toString().trim();
        String dateStr = this.etDeteriorationDate.getText().toString().trim();
        String photoName = this.selectedImageUri != null ? generateUniqueFileName() : null;
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String uniqueID = generateUniqueID();

        if (instanceID.isEmpty() || qtyStr.isEmpty() || dateStr.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs requis", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!this.dbHelper.doesInstanceExist(instanceID)) {
            this.etInstanceID.setError("L'instance n'existe pas.");
            return;
        }
        if (this.dbHelper.isInstanceSold(instanceID)) {
            this.etInstanceID.setError("L'instance a déjà été vendue.");
            return;
        }
        if (this.dbHelper.isInstanceDeteriorated(instanceID)) {
            this.etInstanceID.setError("L'instance a déjà été marquée comme détériorée.");
            return;
        }

        if (this.selectedImageUri != null) {
            this.dbHelper.saveDeterioratedProductImageWithNewName(this.selectedImageUri, photoName);
        }

        this.dbHelper.addDeterioratedProductWithInstance(new DeterioratedProductWithInstance(
                uniqueID, instanceID, dateStr, reason, Integer.parseInt(qtyStr),
                getLoggedInEmployeeID(), photoName, false, null, now, null, 0));
        this.dbHelper.updateInstanceState(instanceID, "deteriorated");
        loadDeterioratedProductsWithInstances();
    }

    private String getLoggedInEmployeeID() {
        return getActivity() != null ? getActivity().getSharedPreferences("MyApp", 0).getString("employeeID", "") : "";
    }

    private String generateUniqueFileName() {
        return "DTWI_" + getLoggedInEmployeeID() + "_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".jpeg";
    }

    private String generateUniqueID() {
        return "prod_" + getLoggedInEmployeeID() + "_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private void setDatePickerDialog(final EditText editText) {
        editText.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(getContext(), (datePicker, i, i2, i3) -> {
                editText.setText(i + "-" + (i2 + 1) + "-" + i3);
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
                this.tvSelectedImage.setText(fileName != null ? fileName : "Image sélectionnée");
            }
        } else if (i == SCAN_BARCODE_REQUEST && i2 == -1 && intent != null) {
            String result = intent.getStringExtra(Intents.Scan.RESULT);
            if (this.etInstanceID != null) {
                this.etInstanceID.setText(result);
                if (this.dbHelper.isInstanceSold(result)) {
                    this.etInstanceID.setError("L'instance a déjà été vendue.");
                } else if (this.dbHelper.isInstanceDeteriorated(result)) {
                    this.etInstanceID.setError("L'instance a déjà été marquée comme détériorée.");
                } else {
                    this.etInstanceID.setError(null);
                    this.etQuantity.setText("1");
                }
            }
        }
    }
}