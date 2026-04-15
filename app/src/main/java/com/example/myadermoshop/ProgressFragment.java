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
import com.google.gson.Gson;

public class ProgressFragment extends DialogFragment {

    private static final String TAG                = "ProgressFragment";
    private static final String ARG_DATE           = "date";
    private static final String ARG_CLOSING_SUMMARY = "closingSummary";

    private String         date;
    private ClosingSummary closingSummary;
    private DatabaseHelper dbHelper;
    private HttpService    httpService;

    private Button    btnStartUpload;
    private ImageView ivStep1Status;
    private ImageView ivStep2Status;
    private ImageView ivStep3Status;
    private ImageView ivStep4Status;
    private ImageView ivStep5Status;
    private ImageView ivStep6Status;
    private ImageView ivStep7Status;

    public static ProgressFragment newInstance(String date, ClosingSummary closingSummary) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        args.putString(ARG_CLOSING_SUMMARY, new Gson().toJson(closingSummary));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Fragment view is being created.");
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        if (getArguments() != null) {
            date           = getArguments().getString(ARG_DATE);
            closingSummary = new Gson().fromJson(
                    getArguments().getString(ARG_CLOSING_SUMMARY), ClosingSummary.class);
            Log.d(TAG, "onCreateView: Retrieved date " + date + " and the closing summary.");
        }

        ivStep1Status = view.findViewById(R.id.ivStep1Status);
        ivStep2Status = view.findViewById(R.id.ivStep2Status);
        ivStep3Status = view.findViewById(R.id.ivStep3Status);
        ivStep4Status = view.findViewById(R.id.ivStep4Status);
        ivStep5Status = view.findViewById(R.id.ivStep5Status);
        ivStep6Status = view.findViewById(R.id.ivStep6Status);
        ivStep7Status = view.findViewById(R.id.ivStep7Status);
        btnStartUpload = view.findViewById(R.id.btnStartUpload);

        dbHelper    = new DatabaseHelper(getContext());
        httpService = RetrofitInstance.getHttpService();

        setInitialRedStatus();
        startUpload();

        return view;
    }

    private void setInitialRedStatus() {
        ivStep1Status.setImageResource(R.drawable.ic_error_red);
        ivStep2Status.setImageResource(R.drawable.ic_error_red);
        ivStep3Status.setImageResource(R.drawable.ic_error_red);
        ivStep4Status.setImageResource(R.drawable.ic_error_red);
        ivStep5Status.setImageResource(R.drawable.ic_error_red);
        ivStep6Status.setImageResource(R.drawable.ic_error_red);
        ivStep7Status.setImageResource(R.drawable.ic_error_red);
    }

    private void startUpload() {
        Log.d(TAG, "startUpload: Beginning upload process for date: " + date);
        uploadDispenses();
    }

    private void uploadDispenses() {
        Log.d(TAG, "uploadDispenses: Uploading dispenses for date: " + date);
        dbHelper.uploadDispensesForDate(date, new DatabaseHelper.UploadCallback() {
            @Override
            public void onSuccess(String msg) {
                Log.d(TAG, "uploadDispenses: onSuccess callback called.");
                ivStep1Status.setImageResource(R.drawable.ic_check_green);
                uploadVersements();
            }
            @Override
            public void onFailure(String msg) {
                ivStep1Status.setImageResource(R.drawable.ic_error_red);
                Toast.makeText(getContext(),
                        "Échec du téléchargement des dispensations: " + msg,
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "uploadDispenses: Error: " + msg);
                uploadVersements();
            }
        });
    }

    private void uploadVersements() {
        Log.d(TAG, "uploadVersements: Uploading versements for date: " + date);
        dbHelper.uploadVersementsForDate(date, new DatabaseHelper.UploadCallback() {
            @Override
            public void onSuccess(String msg) {
                ivStep2Status.setImageResource(R.drawable.ic_check_green);
                Log.d(TAG, "uploadVersements: Success.");
                uploadDeterioratedProductsWithInstance();
            }
            @Override
            public void onFailure(String msg) {
                ivStep2Status.setImageResource(R.drawable.ic_error_red);
                Toast.makeText(getContext(),
                        "Échec du téléchargement des versements: " + msg,
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "uploadVersements: Error: " + msg);
                uploadDeterioratedProductsWithInstance();
            }
        });
    }

    private void uploadDeterioratedProductsWithInstance() {
        Log.d(TAG, "uploadDeterioratedProductsWithInstance: Uploading for date: " + date);
        dbHelper.uploadDeterioratedProductsWithInstanceForDate(date,
                new DatabaseHelper.UploadCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        ivStep3Status.setImageResource(R.drawable.ic_check_green);
                        Log.d(TAG, "uploadDeterioratedProductsWithInstance: Success.");
                        uploadDeterioratedProductsWithoutInstance();
                    }
                    @Override
                    public void onFailure(String msg) {
                        ivStep3Status.setImageResource(R.drawable.ic_error_red);
                        Toast.makeText(getContext(),
                                "Échec des produits détériorés (avec instance): " + msg,
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "uploadDeterioratedProductsWithInstance: Error: " + msg);
                        uploadDeterioratedProductsWithoutInstance();
                    }
                });
    }

    private void uploadDeterioratedProductsWithoutInstance() {
        Log.d(TAG, "uploadDeterioratedProductsWithoutInstance: Uploading for date: " + date);
        dbHelper.uploadDeterioratedProductsWithoutInstanceForDate(date,
                new DatabaseHelper.UploadCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        ivStep4Status.setImageResource(R.drawable.ic_check_green);
                        Log.d(TAG, "uploadDeterioratedProductsWithoutInstance: Success.");
                        uploadClosureData();
                    }
                    @Override
                    public void onFailure(String msg) {
                        ivStep4Status.setImageResource(R.drawable.ic_error_red);
                        Toast.makeText(getContext(),
                                "Échec des produits détériorés (sans instance): " + msg,
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "uploadDeterioratedProductsWithoutInstance: Error: " + msg);
                        uploadClosureData();
                    }
                });
    }

    private void uploadClosureData() {
        Log.d(TAG, "uploadClosureData: Uploading closure data for date: " + date);
        dbHelper.uploadClosureData(httpService,
                createClosureDataFromSummary(closingSummary),
                new DatabaseHelper.UploadCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        ivStep5Status.setImageResource(R.drawable.ic_check_green);
                        Log.d(TAG, "uploadClosureData: Success.");
                        uploadCarts();
                    }
                    @Override
                    public void onFailure(String msg) {
                        ivStep5Status.setImageResource(R.drawable.ic_error_red);
                        Toast.makeText(getContext(),
                                "Échec des données de clôture: " + msg,
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "uploadClosureData: Error: " + msg);
                        uploadCarts();
                    }
                });
    }

    private void uploadCarts() {
        Log.d(TAG, "uploadCarts: Uploading daily carts for date: " + date);
        dbHelper.uploadCartsForDate(date, httpService, new DatabaseHelper.UploadCallback() {
            @Override
            public void onSuccess(String msg) {
                ivStep6Status.setImageResource(R.drawable.ic_check_green);
                Log.d(TAG, "uploadCarts: Success for date: " + date);
                uploadPayments();
            }
            @Override
            public void onFailure(String msg) {
                ivStep6Status.setImageResource(R.drawable.ic_error_red);
                Toast.makeText(getContext(),
                        "Échec des chariots quotidiens: " + msg,
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "uploadCarts: Error: " + msg);
                uploadPayments();
            }
        });
    }

    private void uploadPayments() {
        Log.d(TAG, "uploadPayments: Uploading payments for date: " + date);
        dbHelper.uploadPaymentsForDate(date, httpService, new DatabaseHelper.UploadCallback() {
            @Override
            public void onSuccess(String msg) {
                ivStep7Status.setImageResource(R.drawable.ic_check_green);
                Toast.makeText(getContext(),
                        "Tous les paiements ont été téléchargés avec succès!",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "uploadPayments: Success for date: " + date);
            }
            @Override
            public void onFailure(String msg) {
                ivStep7Status.setImageResource(R.drawable.ic_error_red);
                Toast.makeText(getContext(),
                        "Échec du téléchargement des paiements: " + msg,
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "uploadPayments: Error: " + msg);
            }
        });
    }

    private ClosureData createClosureDataFromSummary(ClosingSummary summary) {
        double totalSales       = dbHelper.getTotalSales(date);
        double amountInStock    = dbHelper.getAmountInStock(date);
        double amountInExpenses = dbHelper.getAmountInExpenses(date);
        double versementDeposit = dbHelper.getVersementDeposit(date);
        int    totalStocks      = dbHelper.getTotalStocksMade(date);
        return new ClosureData(summary.getDate(), date, totalSales, amountInStock,
                1, getLoggedInEmployeeID(), totalStocks, amountInExpenses, versementDeposit);
    }

    private String getLoggedInEmployeeID() {
        return getActivity() != null
                ? getActivity().getSharedPreferences("MyApp", 0)
                .getString("employeeID", "")
                : "";
    }
}