package com.example.myadermoshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InitializationHelper {

    private static final String TAG = "InitializationHelper";

    private static final int STEP_CHECK_NETWORK          = 0;
    private static final int STEP_CHECK_API_KEY          = 1;
    private static final int STEP_UPDATE_PRODUCTS        = 2;
    private static final int STEP_UPDATE_STOCKS          = 3;
    private static final int STEP_UPDATE_PRICES          = 4;
    private static final int STEP_UPDATE_PAYMENT_TYPES   = 5;
    private static final int STEP_UPDATE_OP_STATUSES     = 6;
    private static final int STEP_UPDATE_MEASURE_UNITS   = 7;
    private static final int STEP_UPDATE_PHYSICAL        = 8;
    private static final int STEP_FETCH_INSTANCES        = 9;
    private static final int STEP_UPDATE_TYPE_DISPENSES  = 10;
    private static final int STEP_UPDATE_DISPENSES       = 11;
    private static final int STEP_UPDATE_VERSEMENTS      = 12;
    private static final int STEP_UPDATE_DTWI            = 13;
    private static final int STEP_UPDATE_DTWTI           = 14;
    private static final int STEP_UPDATE_CLOSURES        = 15;
    private static final int STEP_UPDATE_CARTS           = 16;
    private static final int STEP_UPDATE_CITWI           = 17;
    private static final int STEP_UPDATE_CITWTI          = 18;
    private static final int STEP_UPDATE_PAYMENTS        = 19;

    private static final String[] STEP_LABELS = {
            "Vérification du réseau...",
            "Connexion au serveur...",
            "Mise à jour des produits...",
            "Mise à jour des stocks...",
            "Mise à jour des prix...",
            "Mise à jour des types de paiement...",
            "Mise à jour des statuts d'opération...",
            "Mise à jour des unités de mesure...",
            "Mise à jour des contrôles physiques...",
            "Récupération des instances de produits...",
            "Mise à jour des types de dépenses...",
            "Mise à jour des dépenses...",
            "Mise à jour des versements...",
            "Mise à jour des DTWI...",
            "Mise à jour des DTWTI...",
            "Mise à jour des clôtures...",
            "Mise à jour des paniers...",
            "Mise à jour des paniers ITWI...",
            "Mise à jour des paniers ITWTI...",
            "Mise à jour des paiements..."
    };

    private final Context           context;
    private final DatabaseHelper    databaseHelper;
    private final TextView          loadingStepsTextView;
    private final SharedPreferences sharedPreferences;

    private int     stepIndex         = 0;
    private boolean isServerConnected = false;

    public InitializationHelper(Context context, TextView loadingStepsTextView) {
        this.context             = context;
        this.loadingStepsTextView = loadingStepsTextView;
        this.databaseHelper       = new DatabaseHelper(context);
        this.sharedPreferences    = context.getSharedPreferences("MyApp", 0);
    }

    public void initialize() {
        stepIndex = 0;
        executeCurrentStep();
    }

    // ── Core runner ───────────────────────────────────────────────────────────

    private void executeCurrentStep() {
        if (stepIndex >= STEP_LABELS.length) {
            proceedToMainActivity();
            return;
        }
        String label = STEP_LABELS[stepIndex];
        Log.d(TAG, "Step " + stepIndex + ": " + label);
        loadingStepsTextView.setText(label);

        switch (stepIndex) {
            case STEP_CHECK_NETWORK:  checkNetwork();          break;
            case STEP_CHECK_API_KEY:  checkApiKeyAndConnect(); break;
            default:                  performServerDependentUpdate(); break;
        }
    }

    private void nextStep() {
        stepIndex++;
        executeCurrentStep();
    }

    // ── Network ───────────────────────────────────────────────────────────────

    private void checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
        if (info != null && info.isConnectedOrConnecting()) {
            Log.d(TAG, "Network connected.");
            nextStep();
        } else {
            Log.d(TAG, "No network connection.");
            attemptOfflineMode();
        }
    }

    // ── API key / server ──────────────────────────────────────────────────────

    private void checkApiKeyAndConnect() {
        String apiKey = sharedPreferences.getString(DatabaseHelper.COLUMN_API_KEY, null);
        if (apiKey == null) {
            Log.d(TAG, "No API key. Redirecting to Login.");
            redirectToLogin();
        } else {
            Log.d(TAG, "API key found. Connecting...");
            connectToServer();
        }
    }

    private void connectToServer() {
        // ── FIXED: uses ServerStatusCallback (not SimpleCallback) ──
        databaseHelper.getFromServerStatus(new DatabaseHelper.ServerStatusCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Server connected.");
                isServerConnected = true;
                nextStep();
            }
            @Override
            public void onFailure(String error) {
                Log.d(TAG, "Server connection failed: " + error);
                isServerConnected = false;
                attemptOfflineMode();
            }
        });
    }

    // ── Offline ───────────────────────────────────────────────────────────────

    private void attemptOfflineMode() {
        String apiKey = sharedPreferences.getString(DatabaseHelper.COLUMN_API_KEY, null);
        if (apiKey == null) {
            redirectToLogin();
        } else {
            Log.d(TAG, "Offline mode — using local data.");
            proceedToMainActivity();
        }
    }

    // ── Server-dependent steps ────────────────────────────────────────────────

    private void performServerDependentUpdate() {
        if (!isServerConnected) {
            Log.d(TAG, "Server not connected. Skipping step " + stepIndex);
            nextStep();
            return;
        }
        switch (stepIndex) {
            case STEP_UPDATE_PRODUCTS:       updateProducts();       break;
            case STEP_UPDATE_STOCKS:         updateStocks();         break;
            case STEP_UPDATE_PRICES:         updateProductPrices();  break;
            case STEP_UPDATE_PAYMENT_TYPES:  updatePaymentTypes();   break;
            case STEP_UPDATE_OP_STATUSES:    updateOperationStatuses(); break;
            case STEP_UPDATE_MEASURE_UNITS:  updateMeasurementUnits(); break;
            case STEP_UPDATE_PHYSICAL:       updatePhysicalControls(); break;
            case STEP_FETCH_INSTANCES:       fetchProductInstances(); break;
            case STEP_UPDATE_TYPE_DISPENSES: updateTypeDispenses();  break;
            case STEP_UPDATE_DISPENSES:      updateDispenses();      break;
            case STEP_UPDATE_VERSEMENTS:     getFromServerVersements(); break;
            case STEP_UPDATE_DTWI:           getFromServerDeterioratedProductsWithInstance(); break;
            case STEP_UPDATE_DTWTI:          getFromServerDeterioratedProductsWithoutInstance(); break;
            case STEP_UPDATE_CLOSURES:       getFromServerClosures(); break;
            case STEP_UPDATE_CARTS:          updateFromServerCarts(); break;
            case STEP_UPDATE_CITWI:          updateFromServerCITWI(); break;
            case STEP_UPDATE_CITWTI:         updateFromServerCITWTI(); break;
            case STEP_UPDATE_PAYMENTS:       getFromServerPayments(); break;
            default: proceedToMainActivity(); break;
        }
    }

    // ── Individual steps — all use DataUpdateCallback ─────────────────────────

    private void updateProducts() {
        Log.d(TAG, "Updating products...");
        databaseHelper.getFromServerProducts(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "updateProducts: " + e); nextStep(); }
        });
    }

    private void updateStocks() {
        Log.d(TAG, "Updating stocks...");
        databaseHelper.getFromServerStocks(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { fetchAllInstancesForStocks(); }
            @Override public void onFailure(String e) { Log.e(TAG, "updateStocks: " + e); nextStep(); }
        });
    }

    private void fetchAllInstancesForStocks() {
        List<String> stockIDs = databaseHelper.getAllStockIDs();
        databaseHelper.fetchAllInstancesForStocks(stockIDs, new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "fetchAllInstancesForStocks: " + e); nextStep(); }
        });
    }

    private void updateProductPrices() {
        Log.d(TAG, "Updating product prices...");
        databaseHelper.getFromServerProductPrices(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "updateProductPrices: " + e); nextStep(); }
        });
    }

    private void updatePaymentTypes() {
        Log.d(TAG, "Updating payment types...");
        databaseHelper.getFromServerPaymentTypes(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "updatePaymentTypes: " + e); nextStep(); }
        });
    }

    private void updateOperationStatuses() {
        Log.d(TAG, "Updating operation statuses...");
        databaseHelper.getFromServerOperationStatuses(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "updateOperationStatuses: " + e); nextStep(); }
        });
    }

    private void updateMeasurementUnits() {
        Log.d(TAG, "Updating measurement units...");
        databaseHelper.getFromServerMeasurementUnits(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "updateMeasurementUnits: " + e); nextStep(); }
        });
    }

    private void updatePhysicalControls() {
        Log.d(TAG, "Updating physical controls...");
        // ── FIXED: uses PhysicalControlCallback (not SimpleCallback) ──
        databaseHelper.fetchAndStorePhysicalControls(new DatabaseHelper.PhysicalControlCallback() {
            @Override public void onComplete(List<PhysicalControle> list) { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "updatePhysicalControls: " + e); nextStep(); }
        });
    }

    private void fetchProductInstances() {
        Log.d(TAG, "Fetching product instances...");
        List<String> stockIDs = databaseHelper.getAllStockIDs();
        databaseHelper.fetchAllInstancesForStocks(stockIDs, new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "fetchProductInstances: " + e); nextStep(); }
        });
    }

    private void updateTypeDispenses() {
        Log.d(TAG, "Updating type dispenses...");
        databaseHelper.getFromServerTypeDispenses(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "updateTypeDispenses: " + e); nextStep(); }
        });
    }

    private void updateDispenses() {
        Log.d(TAG, "Updating dispenses...");
        databaseHelper.getFromServerDispenses(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "updateDispenses: " + e); nextStep(); }
        });
    }

    private void getFromServerVersements() {
        Log.d(TAG, "Updating versements...");
        databaseHelper.getFromServerVersements(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "getFromServerVersements: " + e); nextStep(); }
        });
    }

    private void getFromServerDeterioratedProductsWithInstance() {
        Log.d(TAG, "Updating DTWI...");
        databaseHelper.getFromServerDeterioratedProductsWithInstance(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "DTWI: " + e); nextStep(); }
        });
    }

    private void getFromServerDeterioratedProductsWithoutInstance() {
        Log.d(TAG, "Updating DTWTI...");
        databaseHelper.getFromServerDeterioratedProductsWithoutInstance(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "DTWTI: " + e); nextStep(); }
        });
    }

    private void getFromServerClosures() {
        Log.d(TAG, "Updating closures...");
        databaseHelper.getFromServerClosures(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "getFromServerClosures: " + e); nextStep(); }
        });
    }

    private void updateFromServerCarts() {
        String lastUpdate = sharedPreferences.getString("lastCartsUpdateDate", null);
        final String today = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());
        if (today.equals(lastUpdate)) {
            Log.d(TAG, "Carts already updated today. Skipping.");
            nextStep();
            return;
        }
        Log.d(TAG, "Updating carts...");
        databaseHelper.getFromServerCarts(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() {
                sharedPreferences.edit().putString("lastCartsUpdateDate", today).apply();
                nextStep();
            }
            @Override public void onFailure(String e) { Log.e(TAG, "updateFromServerCarts: " + e); nextStep(); }
        });
    }

    private void updateFromServerCITWI() {
        Log.d(TAG, "Updating CITWI...");
        databaseHelper.getFromServerCartItemsWithInstance(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "CITWI: " + e); nextStep(); }
        });
    }

    private void updateFromServerCITWTI() {
        Log.d(TAG, "Updating CITWTI...");
        databaseHelper.getFromServerCartItemsWithoutInstance(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "CITWTI: " + e); nextStep(); }
        });
    }

    private void getFromServerPayments() {
        Log.d(TAG, "Updating payments...");
        databaseHelper.getFromServerPayments(new DatabaseHelper.DataUpdateCallback() {
            @Override public void onComplete() { nextStep(); }
            @Override public void onFailure(String e) { Log.e(TAG, "getFromServerPayments: " + e); nextStep(); }
        });
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    private void redirectToLogin() {
        context.startActivity(new Intent(context, LoginActivity.class));
        ((Activity) context).finish();
    }

    private void proceedToMainActivity() {
        Log.d(TAG, "Initialization complete. Proceeding to MainActivity.");
        context.startActivity(new Intent(context, MainActivity.class));
        ((Activity) context).finish();
    }
}