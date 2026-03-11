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
import android.widget.SpinnerAdapter;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/* loaded from: classes.dex */
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

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_items_without_instances, viewGroup, false);
        this.recyclerViewItemsWithoutInstances = viewInflate.findViewById(R.id.recyclerViewItemsWithoutInstances);
        FloatingActionButton floatingActionButton = viewInflate.findViewById(R.id.fab_add_item_without_instance);
        this.recyclerViewItemsWithoutInstances.setLayoutManager(new LinearLayoutManager(getContext()));
        this.deterioratedProductWithoutInstanceList = new ArrayList();
        DeterioratedProductWithoutInstanceAdapter deterioratedProductWithoutInstanceAdapter = new DeterioratedProductWithoutInstanceAdapter(getContext(), this.deterioratedProductWithoutInstanceList);
        this.deterioratedProductWithoutInstanceAdapter = deterioratedProductWithoutInstanceAdapter;
        this.recyclerViewItemsWithoutInstances.setAdapter(deterioratedProductWithoutInstanceAdapter);
        this.dbHelper = new DatabaseHelper(getContext());
        loadDeterioratedProductsWithoutInstances();
        floatingActionButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.ItemsWithoutInstancesFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (Utils.checkAndDisplayClosure(ItemsWithoutInstancesFragment.this.getActivity(), ItemsWithoutInstancesFragment.this.dbHelper)) {
                    return;
                }
                ItemsWithoutInstancesFragment.this.showAddItemWithoutInstanceDialog();
            }
        });
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        loadDeterioratedProductsWithoutInstances();
    }

    private void loadDeterioratedProductsWithoutInstances() {
        this.deterioratedProductWithoutInstanceList.clear();
        this.deterioratedProductWithoutInstanceList.addAll(this.dbHelper.fetchAllDeterioratedProductsWithoutInstances());
        this.deterioratedProductWithoutInstanceAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAddItemWithoutInstanceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View viewInflate = getLayoutInflater().inflate(R.layout.dialog_add_item_without_instance, null);
        builder.setView(viewInflate);
        this.spinnerProductID = viewInflate.findViewById(R.id.spinnerProductID);
        this.etQuantity = viewInflate.findViewById(R.id.etQuantity);
        this.etReason = viewInflate.findViewById(R.id.etReason);
        this.etDeteriorationDate = viewInflate.findViewById(R.id.etDeteriorationDate);
        Button button = viewInflate.findViewById(R.id.btnSelectImage);
        this.tvSelectedImage = viewInflate.findViewById(R.id.tvSelectedImage);
        this.productList = this.dbHelper.getProductsWithoutInstances();
        ArrayList arrayList = new ArrayList();
        Iterator<Product> it = this.productList.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getProductName());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerProductID.setAdapter(arrayAdapter);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.ItemsWithoutInstancesFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m105xca07e589(view);
            }
        });
        setDatePickerDialog(this.etDeteriorationDate);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.ItemsWithoutInstancesFragment$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) throws IOException {
                this.f$0.m106xcb3e3868(dialogInterface, i);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.ItemsWithoutInstancesFragment$$ExternalSyntheticLambda4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /* renamed from: lambda$showAddItemWithoutInstanceDialog$0$com-example-myadermoshop-ItemsWithoutInstancesFragment, reason: not valid java name */
    /* synthetic */ void m105xca07e589(View view) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType(FileUtils.MIME_TYPE_IMAGE);
        startActivityForResult(intent, 1);
    }

    /* renamed from: lambda$showAddItemWithoutInstanceDialog$1$com-example-myadermoshop-ItemsWithoutInstancesFragment, reason: not valid java name */
    /* synthetic */ void m106xcb3e3868(DialogInterface dialogInterface, int i) throws IOException {
        String productID = this.productList.get(this.spinnerProductID.getSelectedItemPosition()).getProductID();
        String strTrim = this.etQuantity.getText().toString().trim();
        String strTrim2 = this.etReason.getText().toString().trim();
        String strTrim3 = this.etDeteriorationDate.getText().toString().trim();
        String strGenerateUniqueFileName = this.selectedImageUri != null ? generateUniqueFileName() : null;
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String strGenerateUniqueID = generateUniqueID();
        if (!productID.isEmpty() && !strTrim.isEmpty() && !strTrim3.isEmpty()) {
            Uri uri = this.selectedImageUri;
            if (uri != null) {
                saveDeterioratedProductImage(uri, strGenerateUniqueFileName);
            }
            this.dbHelper.addDeterioratedProductWithoutInstance(new DeterioratedProductWithoutInstance(strGenerateUniqueID, productID, strTrim3, strTrim2, Integer.parseInt(strTrim), getLoggedInEmployeeID(), strGenerateUniqueFileName, false, null, str, 0));
            loadDeterioratedProductsWithoutInstances();
            return;
        }
        Toast.makeText(getContext(), "Please fill in all required fields", 0).show();
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
        textInputEditText.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.ItemsWithoutInstancesFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m104x49b73d67(textInputEditText, view);
            }
        });
    }

    /* renamed from: lambda$setDatePickerDialog$4$com-example-myadermoshop-ItemsWithoutInstancesFragment, reason: not valid java name */
    /* synthetic */ void m104x49b73d67(final TextInputEditText textInputEditText, View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() { // from class: com.example.myadermoshop.ItemsWithoutInstancesFragment$$ExternalSyntheticLambda1
            @Override // android.app.DatePickerDialog.OnDateSetListener
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                textInputEditText.setText(i + "-" + (i2 + 1) + "-" + i3);
            }
        }, calendar.get(1), calendar.get(2), calendar.get(5)).show();
    }

    private String getFileNameFromUri(Uri uri) {
        Cursor cursorQuery = getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursorQuery == null || !cursorQuery.moveToFirst()) {
            return null;
        }
        String string = cursorQuery.getString(cursorQuery.getColumnIndex("_display_name"));
        cursorQuery.close();
        return string;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            getActivity();
            if (i2 != -1 || intent == null || intent.getData() == null) {
                return;
            }
            Uri data = intent.getData();
            this.selectedImageUri = data;
            String fileNameFromUri = getFileNameFromUri(data);
            TextView textView = this.tvSelectedImage;
            if (textView != null) {
                if (fileNameFromUri == null) {
                    fileNameFromUri = "No image selected";
                }
                textView.setText(fileNameFromUri);
            }
        }
    }
}