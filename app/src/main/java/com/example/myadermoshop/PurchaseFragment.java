package com.example.myadermoshop;

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
import com.example.myadermoshop.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PurchaseFragment extends Fragment {
    private static final String TAG = "PurchaseFragment";
    private StockAdapter adapter;
    private DatabaseHelper dbHelper;
    private HttpService httpService;
    private RecyclerView recyclerViewPurchases;
    private BroadcastReceiver refreshReceiver;
    private List<Stock> stockList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_purchase, viewGroup, false);
        this.dbHelper = new DatabaseHelper(getActivity());
        this.httpService = RetrofitInstance.getHttpService();
        RecyclerView recyclerView = viewInflate.findViewById(R.id.recyclerViewPurchases);
        this.recyclerViewPurchases = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SwipeRefreshLayout swipeRefreshLayout = viewInflate.findViewById(R.id.swipeRefreshLayout);
        this.swipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.example.myadermoshop.PurchaseFragment$$ExternalSyntheticLambda0
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                this.f$0.m118lambda$onCreateView$0$comexamplemyadermoshopPurchaseFragment();
            }
        });
        FloatingActionButton floatingActionButton = viewInflate.findViewById(R.id.fab_add_purchase);
        this.stockList = new ArrayList();
        StockAdapter stockAdapter = new StockAdapter(this.stockList, this.dbHelper, getActivity(), this.httpService);
        this.adapter = stockAdapter;
        this.recyclerViewPurchases.setAdapter(stockAdapter);
        loadPurchaseData();
        floatingActionButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.PurchaseFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m119lambda$onCreateView$1$comexamplemyadermoshopPurchaseFragment(view);
            }
        });
        this.refreshReceiver = new BroadcastReceiver() { // from class: com.example.myadermoshop.PurchaseFragment.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                PurchaseFragment.this.loadPurchaseData();
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.refreshReceiver, new IntentFilter("REFRESH_PURCHASE_LIST"));
        return viewInflate;
    }

    /* renamed from: lambda$onCreateView$0$com-example-myadermoshop-PurchaseFragment, reason: not valid java name */
    /* synthetic */ void m118lambda$onCreateView$0$comexamplemyadermoshopPurchaseFragment() {
        if (this.dbHelper.isNetworkConnected()) {
            this.dbHelper.getFromServerStocks(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.PurchaseFragment.1
                @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
                public void onComplete() {
                    PurchaseFragment.this.loadPurchaseData();
                    PurchaseFragment.this.swipeRefreshLayout.setRefreshing(false);
                }

                @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
                public void onFailure(String str) {
                    Log.e(PurchaseFragment.TAG, str);
                    Toast.makeText(PurchaseFragment.this.getActivity(), "Failed to refresh data: " + str, 0).show();
                    PurchaseFragment.this.swipeRefreshLayout.setRefreshing(false);
                }
            });
            return;
        }
        loadPurchaseData();
        this.swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), "No network connection. Showing offline data.", 0).show();
    }

    /* renamed from: lambda$onCreateView$1$com-example-myadermoshop-PurchaseFragment, reason: not valid java name */
    /* synthetic */ void m119lambda$onCreateView$1$comexamplemyadermoshopPurchaseFragment(View view) {
        if (Utils.checkAndDisplayClosure(getActivity(), this.dbHelper)) {
            return;
        }
        startActivity(new Intent(getActivity(), AddPurchaseActivity.class));
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.dbHelper.isNetworkConnected()) {
            this.dbHelper.getFromServerStocks(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.PurchaseFragment.3
                @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
                public void onComplete() {
                    PurchaseFragment.this.loadPurchaseData();
                }

                @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
                public void onFailure(String str) {
                    Log.e(PurchaseFragment.TAG, str);
                    Toast.makeText(PurchaseFragment.this.getActivity(), "Failed to refresh data: " + str, 0).show();
                }
            });
        } else {
            loadPurchaseData();
            Toast.makeText(getActivity(), "No network connection. Showing offline data.", 0).show();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.refreshReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadPurchaseData() {
        this.stockList.clear();
        Cursor allStocksCursor = this.dbHelper.getAllStocksCursor();
        if (allStocksCursor != null) {
            Log.d(TAG, "Cursor count: " + allStocksCursor.getCount());
            logColumnNames(allStocksCursor);
            if (!allStocksCursor.moveToFirst()) {
                Log.d(TAG, "No stocks found.");
                Toast.makeText(getActivity(), "No stocks found", 0).show();
            } else {
                do {
                    try {
                        Stock stock = new Stock();
                        stock.setStockID(allStocksCursor.getString(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_ID)));
                        stock.setStockDateTime(allStocksCursor.getString(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_DATE_TIME)));
                        stock.setStockQuantity(allStocksCursor.getInt(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_QUANTITY)));
                        stock.setTotalAmountUsed(allStocksCursor.getDouble(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL_AMOUNT_USED)));
                        stock.setProductID(allStocksCursor.getString(allStocksCursor.getColumnIndex("productID")));
                        stock.setStockManDate(allStocksCursor.getString(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_MAN_DATE)));
                        stock.setStockExpDate(allStocksCursor.getString(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_STOCK_EXP_DATE)));
                        stock.setSupplierName(allStocksCursor.getString(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_SUPPLIER_NAME)));
                        stock.setSupplierContact(allStocksCursor.getString(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_SUPPLIER_CONTACT)));
                        stock.setFactureNumber(allStocksCursor.getString(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_FACTURE_NUMBER)));
                        stock.setFactureImageName(allStocksCursor.getString(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_FACTURE_IMAGE_NAME)));
                        stock.setPaymentTypeID(allStocksCursor.getInt(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_TYPE_ID)));
                        stock.setStatusID(allStocksCursor.getInt(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS_ID)));
                        stock.setEmployeeID(allStocksCursor.getString(allStocksCursor.getColumnIndex("employeeID")));
                        stock.setUploadStatus(allStocksCursor.getInt(allStocksCursor.getColumnIndex(DatabaseHelper.COLUMN_UPLOAD_STATUS)));
                        this.stockList.add(stock);
                    } catch (Exception e) {
                        Log.e(TAG, "Error loading stock data", e);
                    }
                } while (allStocksCursor.moveToNext());
            }
            allStocksCursor.close();
        } else {
            Log.d(TAG, "Cursor is null.");
            Toast.makeText(getActivity(), "Failed to fetch data", 0).show();
        }
        this.adapter.notifyDataSetChanged();
    }

    private void logColumnNames(Cursor cursor) {
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.d(TAG, "Column " + i + ": " + cursor.getColumnName(i));
        }
    }
}