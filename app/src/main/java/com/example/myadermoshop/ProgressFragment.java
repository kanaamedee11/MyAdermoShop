package com.example.myadermoshop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import com.example.myadermoshop.DatabaseHelper;
import com.google.gson.Gson;

/* loaded from: classes.dex */
public class ProgressFragment extends DialogFragment {
    private static final String ARG_CLOSING_SUMMARY = "closingSummary";
    private static final String ARG_DATE = "date";
    private static final String TAG = "ProgressFragment";
    private Button btnStartUpload;
    private ClosingSummary closingSummary;
    private String date;
    private DatabaseHelper dbHelper;
    private HttpService httpService;
    private ImageView ivStep1Status;
    private ImageView ivStep2Status;
    private ImageView ivStep3Status;
    private ImageView ivStep4Status;
    private ImageView ivStep5Status;
    private ImageView ivStep6Status;
    private ImageView ivStep7Status;

    public static ProgressFragment newInstance(String str, ClosingSummary closingSummary) {
        ProgressFragment progressFragment = new ProgressFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", str);
        bundle.putString(ARG_CLOSING_SUMMARY, new Gson().toJson(closingSummary));
        progressFragment.setArguments(bundle);
        return progressFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Log.d(TAG, "onCreateView: Fragment view is being created.");
        View viewInflate = layoutInflater.inflate(R.layout.fragment_progress, viewGroup, false);
        if (getArguments() != null) {
            this.date = getArguments().getString("date");
            this.closingSummary = new Gson().fromJson(getArguments().getString(ARG_CLOSING_SUMMARY), ClosingSummary.class);
            Log.d(TAG, "onCreateView: Retrieved date " + this.date + " and the closing summary.");
        }
        this.ivStep1Status = viewInflate.findViewById(R.id.ivStep1Status);
        this.ivStep2Status = viewInflate.findViewById(R.id.ivStep2Status);
        this.ivStep3Status = viewInflate.findViewById(R.id.ivStep3Status);
        this.ivStep4Status = viewInflate.findViewById(R.id.ivStep4Status);
        this.ivStep5Status = viewInflate.findViewById(R.id.ivStep5Status);
        this.ivStep6Status = viewInflate.findViewById(R.id.ivStep6Status);
        this.ivStep7Status = viewInflate.findViewById(R.id.ivStep7Status);
        this.btnStartUpload = viewInflate.findViewById(R.id.btnStartUpload);
        this.dbHelper = new DatabaseHelper(getContext());
        this.httpService = RetrofitInstance.getHttpService();
        setInitialRedStatus();
        startUpload();
        return viewInflate;
    }

    private void setInitialRedStatus() {
        this.ivStep1Status.setImageResource(R.drawable.ic_error_red);
        this.ivStep2Status.setImageResource(R.drawable.ic_error_red);
        this.ivStep3Status.setImageResource(R.drawable.ic_error_red);
        this.ivStep4Status.setImageResource(R.drawable.ic_error_red);
        this.ivStep5Status.setImageResource(R.drawable.ic_error_red);
        this.ivStep6Status.setImageResource(R.drawable.ic_error_red);
        this.ivStep7Status.setImageResource(R.drawable.ic_error_red);
    }

    private void startUpload() {
        Log.d(TAG, "startUpload: Beginning upload process for date: " + this.date);
        uploadDispenses();
    }

    private void uploadDispenses() {
        Log.d(TAG, "uploadDispenses: Uploading dispenses for date: " + this.date);
        this.dbHelper.uploadDispensesForDate(this.date, new DatabaseHelper.UploadCallback() { // from class: com.example.myadermoshop.ProgressFragment.1
            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onSuccess(String str) {
                Log.d(ProgressFragment.TAG, "uploadDispenses: onSuccess callback called.");
                ProgressFragment.this.ivStep1Status.setImageResource(R.drawable.ic_check_green);
                ProgressFragment.this.uploadVersements();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onFailure(String str) {
                ProgressFragment.this.ivStep1Status.setImageResource(R.drawable.ic_error_red);
                Toast.makeText(ProgressFragment.this.getContext(), "Échec du téléchargement des dispensations: " + str, 0).show();
                Log.e(ProgressFragment.TAG, "uploadDispenses: Error uploading dispensations: " + str);
                ProgressFragment.this.uploadVersements();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadVersements() {
        Log.d(TAG, "uploadVersements: Uploading versements for date: " + this.date);
        this.dbHelper.uploadVersementsForDate(this.date, new DatabaseHelper.UploadCallback() { // from class: com.example.myadermoshop.ProgressFragment.2
            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onSuccess(String str) {
                ProgressFragment.this.ivStep2Status.setImageResource(R.drawable.ic_check_green);
                Log.d(ProgressFragment.TAG, "uploadVersements: Versements uploaded successfully.");
                ProgressFragment.this.uploadDeterioratedProductsWithInstance();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onFailure(String str) {
                ProgressFragment.this.ivStep2Status.setImageResource(R.drawable.ic_error_red);
                Toast.makeText(ProgressFragment.this.getContext(), "Échec du téléchargement des versements: " + str, 0).show();
                Log.e(ProgressFragment.TAG, "uploadVersements: Error uploading versements: " + str);
                ProgressFragment.this.uploadDeterioratedProductsWithInstance();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadDeterioratedProductsWithInstance() {
        Log.d(TAG, "uploadDeterioratedProductsWithInstance: Uploading détériorés (avec instance) for date: " + this.date);
        this.dbHelper.uploadDeterioratedProductsWithInstanceForDate(this.date, new DatabaseHelper.UploadCallback() { // from class: com.example.myadermoshop.ProgressFragment.3
            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onSuccess(String str) {
                ProgressFragment.this.ivStep3Status.setImageResource(R.drawable.ic_check_green);
                Log.d(ProgressFragment.TAG, "uploadDeterioratedProductsWithInstance: Products uploaded successfully.");
                ProgressFragment.this.uploadDeterioratedProductsWithoutInstance();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onFailure(String str) {
                ProgressFragment.this.ivStep3Status.setImageResource(R.drawable.ic_error_red);
                Toast.makeText(ProgressFragment.this.getContext(), "Échec du téléchargement des produits détériorés (avec instance): " + str, 0).show();
                Log.e(ProgressFragment.TAG, "uploadDeterioratedProductsWithInstance: Error uploading products: " + str);
                ProgressFragment.this.uploadDeterioratedProductsWithoutInstance();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadDeterioratedProductsWithoutInstance() {
        Log.d(TAG, "uploadDeterioratedProductsWithoutInstance: Uploading détériorés (sans instance) for date: " + this.date);
        this.dbHelper.uploadDeterioratedProductsWithoutInstanceForDate(this.date, new DatabaseHelper.UploadCallback() { // from class: com.example.myadermoshop.ProgressFragment.4
            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onSuccess(String str) {
                ProgressFragment.this.ivStep4Status.setImageResource(R.drawable.ic_check_green);
                Log.d(ProgressFragment.TAG, "uploadDeterioratedProductsWithoutInstance: Products uploaded successfully.");
                ProgressFragment.this.uploadClosureData();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onFailure(String str) {
                ProgressFragment.this.ivStep4Status.setImageResource(R.drawable.ic_error_red);
                Toast.makeText(ProgressFragment.this.getContext(), "Échec du téléchargement des produits détériorés (sans instance): " + str, 0).show();
                Log.e(ProgressFragment.TAG, "uploadDeterioratedProductsWithoutInstance: Error uploading products: " + str);
                ProgressFragment.this.uploadClosureData();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadClosureData() {
        Log.d(TAG, "uploadClosureData: Uploading closure data for date: " + this.date);
        this.dbHelper.uploadClosureData(this.httpService, createClosureDataFromSummary(this.closingSummary), new DatabaseHelper.UploadCallback() { // from class: com.example.myadermoshop.ProgressFragment.5
            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onSuccess(String str) {
                ProgressFragment.this.ivStep5Status.setImageResource(R.drawable.ic_check_green);
                Log.d(ProgressFragment.TAG, "uploadClosureData: Closure data uploaded successfully.");
                ProgressFragment.this.uploadCarts();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onFailure(String str) {
                ProgressFragment.this.ivStep5Status.setImageResource(R.drawable.ic_error_red);
                Toast.makeText(ProgressFragment.this.getContext(), "Échec du téléchargement des données de clôture: " + str, 0).show();
                Log.e(ProgressFragment.TAG, "uploadClosureData: Error uploading closure data: " + str);
                ProgressFragment.this.uploadCarts();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadCarts() {
        Log.d(TAG, "uploadCarts: Uploading daily carts for date: " + this.date);
        this.dbHelper.uploadCartsForDate(this.date, this.httpService, new DatabaseHelper.UploadCallback() { // from class: com.example.myadermoshop.ProgressFragment.6
            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onSuccess(String str) {
                ProgressFragment.this.ivStep6Status.setImageResource(R.drawable.ic_check_green);
                Log.d(ProgressFragment.TAG, "uploadCarts: Daily carts uploaded successfully for date: " + ProgressFragment.this.date);
                ProgressFragment.this.uploadPayments();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onFailure(String str) {
                ProgressFragment.this.ivStep6Status.setImageResource(R.drawable.ic_error_red);
                Toast.makeText(ProgressFragment.this.getContext(), "Échec du téléchargement des chariots quotidiens: " + str, 0).show();
                Log.e(ProgressFragment.TAG, "uploadCarts: Error uploading daily carts: " + str);
                ProgressFragment.this.uploadPayments();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadPayments() {
        Log.d(TAG, "uploadPayments: Uploading payments for date: " + this.date);
        this.dbHelper.uploadPaymentsForDate(this.date, this.httpService, new DatabaseHelper.UploadCallback() { // from class: com.example.myadermoshop.ProgressFragment.7
            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onSuccess(String str) {
                ProgressFragment.this.ivStep7Status.setImageResource(R.drawable.ic_check_green);
                Toast.makeText(ProgressFragment.this.getContext(), "Tous les paiements ont été téléchargés avec succès!", 0).show();
                Log.d(ProgressFragment.TAG, "uploadPayments: Payments uploaded successfully for date: " + ProgressFragment.this.date);
            }

            @Override // com.example.myadermoshop.DatabaseHelper.UploadCallback
            public void onFailure(String str) {
                ProgressFragment.this.ivStep7Status.setImageResource(R.drawable.ic_error_red);
                Toast.makeText(ProgressFragment.this.getContext(), "Échec du téléchargement des paiements: " + str, 0).show();
                Log.e(ProgressFragment.TAG, "uploadPayments: Error uploading payments: " + str);
            }
        });
    }

    private ClosureData createClosureDataFromSummary(ClosingSummary closingSummary) {
        double totalSales = this.dbHelper.getTotalSales(this.date);
        double amountInStock = this.dbHelper.getAmountInStock(this.date);
        double amountInExpenses = this.dbHelper.getAmountInExpenses(this.date);
        double versementDeposit = this.dbHelper.getVersementDeposit(this.date);
        return new ClosureData(closingSummary.getDate(), this.date, totalSales, amountInStock, 1, getLoggedInEmployeeID(), this.dbHelper.getTotalStocksMade(this.date), amountInExpenses, versementDeposit);
    }

    private String getLoggedInEmployeeID() {
        return getActivity() != null ? getActivity().getSharedPreferences("MyApp", 0).getString("employeeID", "") : "";
    }
}