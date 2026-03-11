package com.example.myadermoshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;
import com.example.myadermoshop.DatabaseHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class InitializationHelper {
    private static final String TAG = "InitializationHelper";
    private static final String[] steps = {"Initialisation...", "Vérification du réseau...", "Connexion au serveur...", "Mise à jour des produits...", "Mise à jour des stocks...", "Mise à jour des prix des produits...", "Mise à jour des types de paiement...", "Mise à jour des statuts d'opération...", "Mise à jour des unités de mesure...", "Mise à jour des contrôles physiques...", "Récupération des instances de produits...", "Mise à jour des types de dépenses...", "Mise à jour des dépenses...", "Mise à jour des versements...", "Mise à jour des DTWI...", "Mise à jour des DTWTI...", "Mise à jour des clôtures...", "Mise à jour des paniers...", "Mise à jour des paniers ITWI...", "Mise à jour des paniers ITWTI...", "Mise à jour des paiements...", "Presque terminé..."};
    private final Context context;
    private final DatabaseHelper databaseHelper;
    private final TextView loadingStepsTextView;
    private final SharedPreferences sharedPreferences;
    private int stepIndex = 0;
    private boolean isServerConnected = false;

    static /* synthetic */ int access$108(InitializationHelper initializationHelper) {
        int i = initializationHelper.stepIndex;
        initializationHelper.stepIndex = i + 1;
        return i;
    }

    public InitializationHelper(Context context, TextView textView) {
        this.context = context;
        this.loadingStepsTextView = textView;
        this.databaseHelper = new DatabaseHelper(context);
        this.sharedPreferences = context.getSharedPreferences("MyApp", 0);
    }

    public void initialize() {
        this.stepIndex = 0;
        executeCurrentStep();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeCurrentStep() {
        int i = this.stepIndex;
        String[] strArr = steps;
        if (i >= strArr.length) {
            proceedToMainActivity();
            return;
        }
        String str = strArr[i];
        Log.d(TAG, "Executing step: " + str + " (Step Index: " + this.stepIndex + ")");
        this.loadingStepsTextView.setText(str);
        int i2 = this.stepIndex;
        if (i2 == 0) {
            checkNetwork();
        } else if (i2 == 1) {
            checkApiKeyAndConnect();
        } else {
            performServerDependentUpdate();
        }
    }

    private void performServerDependentUpdate() {
        if (this.isServerConnected) {
            switch (this.stepIndex) {
                case 2:
                    updateProducts();
                    break;
                case 3:
                    updateStocks();
                    break;
                case 4:
                    updateProductPrices();
                    break;
                case 5:
                    updatePaymentTypes();
                    break;
                case 6:
                    updateOperationStatuses();
                    break;
                case 7:
                    updateMeasurementUnits();
                    break;
                case 8:
                    updatePhysicalControls();
                    break;
                case 9:
                    fetchProductInstances();
                    break;
                case 10:
                    updateTypeDispenses();
                    break;
                case 11:
                    updateDispenses();
                    break;
                case 12:
                    getFromServerVersements();
                    break;
                case 13:
                    getFromServerDeterioratedProductsWithInstance();
                    break;
                case 14:
                    getFromServerDeterioratedProductsWithoutInstance();
                    break;
                case 15:
                    updateFromServerCarts();
                    break;
                case 16:
                    updateFromServerCITWI();
                    break;
                case 17:
                    updateFromServerCITWTI();
                    break;
                case 18:
                    getFromServerClosures();
                    break;
                case 19:
                    getFromServerPayments();
                    break;
                default:
                    proceedToMainActivity();
                    break;
            }
        }
        Log.d(TAG, "Server not connected. Skipping step " + this.stepIndex);
        this.stepIndex++;
        executeCurrentStep();
    }

    private void checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            Log.d(TAG, "No network connection.");
            attemptOfflineMode();
        } else {
            Log.d(TAG, "Network connected.");
            this.stepIndex++;
            executeCurrentStep();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attemptOfflineMode() {
        if (this.sharedPreferences.getString(DatabaseHelper.COLUMN_API_KEY, null) == null) {
            Log.d(TAG, "No API key found. Redirecting to Login.");
            redirectToLogin();
        } else {
            Log.d(TAG, "Proceeding with offline mode.");
            proceedWithLocalData();
        }
    }

    private void checkApiKeyAndConnect() {
        if (this.sharedPreferences.getString(DatabaseHelper.COLUMN_API_KEY, null) != null) {
            Log.d(TAG, "API Key found. Attempting to connect to server...");
            connectToServer();
        } else {
            Log.d(TAG, "API Key not found. Redirecting to Login.");
            redirectToLogin();
        }
    }

    private void connectToServer() {
        Log.d(TAG, "Attempting to connect to server...");
        this.databaseHelper.getFromServerStatus(new DatabaseHelper.ServerStatusCallback() { // from class: com.example.myadermoshop.InitializationHelper.1
            @Override // com.example.myadermoshop.DatabaseHelper.ServerStatusCallback
            public void onSuccess() {
                Log.d(InitializationHelper.TAG, "Successfully connected to server.");
                InitializationHelper.this.isServerConnected = true;
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.ServerStatusCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Failed to connect to server: " + str);
                InitializationHelper.this.attemptOfflineMode();
            }
        });
    }

    private void proceedWithLocalData() {
        Log.d(TAG, "Proceeding with local data.");
        this.isServerConnected = false;
        this.stepIndex = 2;
        executeCurrentStep();
    }

    private void updateProducts() {
        Log.d(TAG, "Updating products...");
        this.databaseHelper.getFromServerProducts(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.2
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Products updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating products: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void updateStocks() {
        Log.d(TAG, "Updating stocks...");
        this.databaseHelper.getFromServerStocks(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.3
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Stocks updated successfully.");
                InitializationHelper.this.fetchAllInstancesForStocks();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating stocks: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchAllInstancesForStocks() {
        this.databaseHelper.fetchAllInstancesForStocks(this.databaseHelper.getAllStockIDs(), new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.4
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "All product instances fetched and saved successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error fetching product instances: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void updateProductPrices() {
        Log.d(TAG, "Updating product prices...");
        this.databaseHelper.getFromServerProductPrices(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.5
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Product prices updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating product prices: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void updatePaymentTypes() {
        Log.d(TAG, "Updating payment types...");
        this.databaseHelper.getFromServerPaymentTypes(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.6
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Payment types updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating payment types: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void updateOperationStatuses() {
        Log.d(TAG, "Updating operation statuses...");
        this.databaseHelper.getFromServerOperationStatuses(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.7
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Operation statuses updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating operation statuses: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void updateMeasurementUnits() {
        Log.d(TAG, "Updating measurement units...");
        this.databaseHelper.getFromServerMeasurementUnits(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.8
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Measurement units updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating measurement units: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void updatePhysicalControls() {
        Log.d(TAG, "Updating physical controls...");
        this.databaseHelper.fetchAndStorePhysicalControls(new DatabaseHelper.PhysicalControlCallback() { // from class: com.example.myadermoshop.InitializationHelper.9
            @Override // com.example.myadermoshop.DatabaseHelper.PhysicalControlCallback
            public void onComplete(List<PhysicalControle> list) {
                Log.d(InitializationHelper.TAG, "Physical controls updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.PhysicalControlCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating physical controls: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void fetchProductInstances() {
        Log.d(TAG, "Fetching product instances...");
        this.databaseHelper.fetchAllInstancesForStocks(this.databaseHelper.getAllStockIDs(), new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.10
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Product instances fetched and saved successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error fetching product instances: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void updateTypeDispenses() {
        Log.d(TAG, "Updating type dispenses...");
        this.databaseHelper.getFromServerTypeDispenses(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.11
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Type dispenses updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating type dispenses: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void updateDispenses() {
        Log.d(TAG, "Updating dispenses...");
        this.databaseHelper.getFromServerDispenses(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.12
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Dispenses updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating dispenses: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void getFromServerVersements() {
        Log.d(TAG, "Updating versements...");
        this.databaseHelper.getFromServerVersements(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.13
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Versements updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating versements: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void getFromServerDeterioratedProductsWithInstance() {
        Log.d(TAG, "Updating deteriorated products (with instance)...");
        this.databaseHelper.getFromServerDeterioratedProductsWithInstance(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.14
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Deteriorated products (with instance) updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating deteriorated products (with instance): " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void getFromServerDeterioratedProductsWithoutInstance() {
        Log.d(TAG, "Updating deteriorated products (without instance)...");
        this.databaseHelper.getFromServerDeterioratedProductsWithoutInstance(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.15
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Deteriorated products (without instance) updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating deteriorated products (without instance): " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void getFromServerClosures() {
        Log.d(TAG, "Updating closures...");
        this.databaseHelper.getFromServerClosures(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.16
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Closures updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating closures: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void updateFromServerCITWI() {
        Log.d(TAG, "Updating CITWI...");
        this.databaseHelper.getFromServerCartItemsWithInstance(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.17
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "CITWI updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating CITWI: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void updateFromServerCITWTI() {
        Log.d(TAG, "Updating CITWTI...");
        this.databaseHelper.getFromServerCartItemsWithoutInstance(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.18
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "CITWTI updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating CITWTI: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void updateFromServerCarts() {
        String string = this.sharedPreferences.getString("lastCartsUpdateDate", null);
        final String str = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        if (str.equals(string)) {
            Log.d(TAG, "Carts already updated for today.");
            this.stepIndex++;
            executeCurrentStep();
        } else {
            Log.d(TAG, "Updating Carts...");
            this.databaseHelper.getFromServerCarts(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.19
                @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
                public void onComplete() {
                    Log.d(InitializationHelper.TAG, "Carts updated successfully.");
                    SharedPreferences.Editor editorEdit = InitializationHelper.this.sharedPreferences.edit();
                    editorEdit.putString("lastCartsUpdateDate", str);
                    editorEdit.apply();
                    InitializationHelper.access$108(InitializationHelper.this);
                    InitializationHelper.this.executeCurrentStep();
                }

                @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
                public void onFailure(String str2) {
                    Log.e(InitializationHelper.TAG, "Error updating Carts: " + str2);
                    InitializationHelper.access$108(InitializationHelper.this);
                    InitializationHelper.this.executeCurrentStep();
                }
            });
        }
    }

    private void getFromServerPayments() {
        Log.d(TAG, "Updating payments...");
        this.databaseHelper.getFromServerPayments(new DatabaseHelper.DataUpdateCallback() { // from class: com.example.myadermoshop.InitializationHelper.20
            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onComplete() {
                Log.d(InitializationHelper.TAG, "Payments updated successfully.");
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }

            @Override // com.example.myadermoshop.DatabaseHelper.DataUpdateCallback
            public void onFailure(String str) {
                Log.e(InitializationHelper.TAG, "Error updating payments: " + str);
                InitializationHelper.access$108(InitializationHelper.this);
                InitializationHelper.this.executeCurrentStep();
            }
        });
    }

    private void redirectToLogin() {
        this.context.startActivity(new Intent(this.context, LoginActivity.class));
        ((Activity) this.context).finish();
    }

    private void proceedToMainActivity() {
        Log.d(TAG, "Initialization complete. Proceeding to MainActivity.");
        this.context.startActivity(new Intent(this.context, MainActivity.class));
        ((Activity) this.context).finish();
    }
}