package com.example.myadermoshop;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class PurchaseFragment extends Fragment {

    private static final String TAG = "PurchaseFragment";

    private StockAdapter adapter;
    private DatabaseHelper dbHelper;
    private HttpService httpService;
    private RecyclerView recyclerViewPurchases;
    private BroadcastReceiver refreshReceiver;
    private List<Stock> stockList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        dbHelper    = new DatabaseHelper(getActivity());
        httpService = RetrofitInstance.getHttpService();

        recyclerViewPurchases = view.findViewById(R.id.recyclerViewPurchases);
        recyclerViewPurchases.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        FloatingActionButton fabAddPurchase = view.findViewById(R.id.fab_add_purchase);

        stockList = new ArrayList<>();
        adapter   = new StockAdapter(stockList, dbHelper, getActivity(), httpService);
        recyclerViewPurchases.setAdapter(adapter);

        loadPurchaseData();

        fabAddPurchase.setOnClickListener(v -> {
            if (Utils.checkAndDisplayClosure(getActivity(), dbHelper)) return;
            startActivity(new Intent(getActivity(), AddPurchaseActivity.class));
        });

        // ── Listen for external refresh requests ──
        refreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadPurchaseData();
            }
        };
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(refreshReceiver,
                        new IntentFilter("REFRESH_PURCHASE_LIST"));

        return view;
    }

    private void onRefresh() {
        if (dbHelper.isNetworkConnected()) {
            dbHelper.getFromServerStocks(new DatabaseHelper.DataUpdateCallback() {
                @Override
                public void onComplete() {
                    loadPurchaseData();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(String message) {
                    Log.e(TAG, message);
                    Toast.makeText(getActivity(),
                            "Échec du rafraîchissement: " + message,
                            Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            loadPurchaseData();
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(),
                    "Pas de connexion. Données hors ligne affichées.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dbHelper.isNetworkConnected()) {
            dbHelper.getFromServerStocks(new DatabaseHelper.DataUpdateCallback() {
                @Override
                public void onComplete() {
                    loadPurchaseData();
                }

                @Override
                public void onFailure(String message) {
                    Log.e(TAG, message);
                    Toast.makeText(getActivity(),
                            "Échec du rafraîchissement: " + message,
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            loadPurchaseData();
            Toast.makeText(getActivity(),
                    "Pas de connexion. Données hors ligne affichées.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(refreshReceiver);
    }

    @SuppressLint("Range")
    private void loadPurchaseData() {
        stockList.clear();
        Cursor cursor = dbHelper.getAllStocksCursor();

        if (cursor == null) {
            Log.d(TAG, "Cursor is null.");
            Toast.makeText(getActivity(),
                    "Échec de la récupération des données",
                    Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            return;
        }

        Log.d(TAG, "Cursor count: " + cursor.getCount());

        if (!cursor.moveToFirst()) {
            Log.d(TAG, "Aucun stock trouvé.");
            cursor.close();
            adapter.notifyDataSetChanged();
            return;
        }

        do {
            try {
                Stock stock = new Stock();
                stock.setStockID(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_ID)));
                stock.setStockDateTime(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_DATE_TIME)));
                stock.setStockQuantity(cursor.getInt(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_QUANTITY)));
                stock.setTotalAmountUsed(cursor.getDouble(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL_AMOUNT_USED)));
                stock.setProductID(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_ID)));
                stock.setStockManDate(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_MAN_DATE)));
                stock.setStockExpDate(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_EXP_DATE)));
                stock.setSupplierName(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_SUPPLIER_NAME)));
                stock.setSupplierContact(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_SUPPLIER_CONTACT)));
                stock.setFactureNumber(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_FACTURE_NUMBER)));
                stock.setFactureImageName(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_FACTURE_IMAGE_NAME)));
                stock.setPaymentTypeID(cursor.getInt(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_TYPE_ID)));
                stock.setStatusID(cursor.getInt(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS_ID)));
                stock.setEmployeeID(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_EMPLOYEE_ID)));
                stock.setUploadStatus(cursor.getInt(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_UPLOAD_STATUS)));
                stockList.add(stock);
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors du chargement du stock", e);
            }
        } while (cursor.moveToNext());

        cursor.close();
        adapter.notifyDataSetChanged();
    }
}