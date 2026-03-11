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

/* loaded from: classes.dex */
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

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_items_with_instances, viewGroup, false);
        this.recyclerViewItemsWithInstances = viewInflate.findViewById(R.id.recyclerViewItemsWithInstances);
        FloatingActionButton floatingActionButton = viewInflate.findViewById(R.id.fab_add_item_with_instance);
        this.recyclerViewItemsWithInstances.setLayoutManager(new LinearLayoutManager(getContext()));
        this.deterioratedProductWithInstanceList = new ArrayList();
        DeterioratedProductWithInstanceAdapter deterioratedProductWithInstanceAdapter = new DeterioratedProductWithInstanceAdapter(getContext(), this.deterioratedProductWithInstanceList);
        this.deterioratedProductWithInstanceAdapter = deterioratedProductWithInstanceAdapter;
        this.recyclerViewItemsWithInstances.setAdapter(deterioratedProductWithInstanceAdapter);
        this.dbHelper = new DatabaseHelper(getContext());
        loadDeterioratedProductsWithInstances();
        floatingActionButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.ItemsWithInstancesFragment$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m99x4f6adb57(view);
            }
        });
        return viewInflate;
    }

    /* renamed from: lambda$onCreateView$0$com-example-myadermoshop-ItemsWithInstancesFragment, reason: not valid java name */
    /* synthetic */ void m99x4f6adb57(View view) {
        if (Utils.checkAndDisplayClosure(getActivity(), this.dbHelper)) {
            return;
        }
        showAddItemWithInstanceDialog();
    }

    @Override // androidx.fragment.app.Fragment
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
        Button button = viewInflate.findViewById(R.id.btnScanInstance);
        this.etQuantity = viewInflate.findViewById(R.id.etQuantity);
        this.etReason = viewInflate.findViewById(R.id.etReason);
        this.etDeteriorationDate = viewInflate.findViewById(R.id.etDeteriorationDate);
        Button button2 = viewInflate.findViewById(R.id.btnSelectImage);
        this.tvSelectedImage = viewInflate.findViewById(R.id.tvSelectedImage);
        this.etQuantity.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.ItemsWithInstancesFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m101x5e05e898(view);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.ItemsWithInstancesFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m102xeb409a19(view);
            }
        });
        setDatePickerDialog(this.etDeteriorationDate);
        builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.ItemsWithInstancesFragment$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) throws IOException {
                this.f$0.m103x787b4b9a(dialogInterface, i);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.ItemsWithInstancesFragment$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /* renamed from: lambda$showAddItemWithInstanceDialog$1$com-example-myadermoshop-ItemsWithInstancesFragment, reason: not valid java name */
    /* synthetic */ void m101x5e05e898(View view) {
        startActivityForResult(new Intent(getContext(), BarcodeScannerDeterioretedInstanceActivity.class), 2);
    }

    /* renamed from: lambda$showAddItemWithInstanceDialog$2$com-example-myadermoshop-ItemsWithInstancesFragment, reason: not valid java name */
    /* synthetic */ void m102xeb409a19(View view) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType(FileUtils.MIME_TYPE_IMAGE);
        startActivityForResult(intent, 1);
    }

    /* renamed from: lambda$showAddItemWithInstanceDialog$3$com-example-myadermoshop-ItemsWithInstancesFragment, reason: not valid java name */
    /* synthetic */ void m103x787b4b9a(DialogInterface dialogInterface, int i) throws IOException {
        String strTrim = this.etInstanceID.getText().toString().trim();
        String strTrim2 = this.etQuantity.getText().toString().trim();
        String strTrim3 = this.etReason.getText().toString().trim();
        String strTrim4 = this.etDeteriorationDate.getText().toString().trim();
        String strGenerateUniqueFileName = this.selectedImageUri != null ? generateUniqueFileName() : null;
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String strGenerateUniqueID = generateUniqueID();
        if (strTrim.isEmpty() || strTrim2.isEmpty() || strTrim4.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs requis", 0).show();
            return;
        }
        if (!this.dbHelper.doesInstanceExist(strTrim)) {
            this.etInstanceID.setError("L'instance n'existe pas.");
            this.etInstanceID.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            return;
        }
        Uri uri = this.selectedImageUri;
        if (uri != null) {
            saveDeterioratedProductImage(uri, strGenerateUniqueFileName);
        }
        if (this.dbHelper.isInstanceSold(strTrim)) {
            this.etInstanceID.setError("L'instance a déjà été vendue.");
            this.etInstanceID.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            return;
        }
        if (this.dbHelper.isInstanceDeteriorated(strTrim)) {
            this.etInstanceID.setError("L'instance a déjà été marquée comme détériorée.");
            this.etInstanceID.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            return;
        }
        this.etInstanceID.setError(null);
        this.etInstanceID.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        this.etQuantity.setText("1");
        this.etQuantity.setEnabled(false);
        this.dbHelper.addDeterioratedProductWithInstance(new DeterioratedProductWithInstance(strGenerateUniqueID, strTrim, strTrim4, strTrim3, Integer.parseInt(strTrim2), getLoggedInEmployeeID(), strGenerateUniqueFileName, false, null, str, null, 0));
        this.dbHelper.updateInstanceState(strTrim, "deteriorated");
        loadDeterioratedProductsWithInstances();
    }

    private String getLoggedInEmployeeID() {
        return getActivity() != null ? getActivity().getSharedPreferences("MyApp", 0).getString("employeeID", "") : "";
    }

    private void saveDeterioratedProductImage(Uri uri, String str) throws IOException {
        this.dbHelper.saveDeterioratedProductImageWithNewName(uri, str);
    }

    private String generateUniqueFileName() {
        return "DTWI_" + getLoggedInEmployeeID() + "_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".jpeg";
    }

    private String generateUniqueID() {
        return "prod_" + getLoggedInEmployeeID() + "_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private void setDatePickerDialog(final EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.ItemsWithInstancesFragment$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m100xe1a8d309(editText, view);
            }
        });
    }

    /* renamed from: lambda$setDatePickerDialog$6$com-example-myadermoshop-ItemsWithInstancesFragment, reason: not valid java name */
    /* synthetic */ void m100xe1a8d309(final EditText editText, View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() { // from class: com.example.myadermoshop.ItemsWithInstancesFragment$$ExternalSyntheticLambda5
            @Override // android.app.DatePickerDialog.OnDateSetListener
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                editText.setText(i + "-" + (i2 + 1) + "-" + i3);
            }
        }, calendar.get(1), calendar.get(2), calendar.get(5)).show();
    }

    private String getFileNameFromUri(Uri uri) {
        Cursor cursorQuery = getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursorQuery != null && cursorQuery.moveToFirst()) {
            int columnIndex = cursorQuery.getColumnIndex("_display_name");
            string = columnIndex != -1 ? cursorQuery.getString(columnIndex) : null;
            cursorQuery.close();
        }
        return string;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            getActivity();
            if (i2 == -1 && intent != null && intent.getData() != null) {
                Uri data = intent.getData();
                this.selectedImageUri = data;
                String fileNameFromUri = getFileNameFromUri(data);
                TextView textView = this.tvSelectedImage;
                if (textView != null) {
                    if (fileNameFromUri == null) {
                        fileNameFromUri = "Aucune image sélectionnée";
                    }
                    textView.setText(fileNameFromUri);
                    return;
                }
                return;
            }
        }
        if (i == 2) {
            getActivity();
            if (i2 != -1 || intent == null) {
                return;
            }
            String stringExtra = intent.getStringExtra(Intents.Scan.RESULT);
            TextInputEditText textInputEditText = this.etInstanceID;
            if (textInputEditText != null) {
                textInputEditText.setText(stringExtra);
            }
            if (this.dbHelper.isInstanceSold(stringExtra)) {
                this.etInstanceID.setError("L'instance a déjà été vendue.");
                this.etInstanceID.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else if (this.dbHelper.isInstanceDeteriorated(stringExtra)) {
                this.etInstanceID.setError("L'instance a déjà été marquée comme détériorée.");
                this.etInstanceID.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                this.etInstanceID.setError(null);
                this.etInstanceID.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                this.etQuantity.setText("1");
                this.etQuantity.setEnabled(false);
            }
        }
    }
}