package com.example.myadermoshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;

/**
 * InitializationHelper — refactored to use syncAll().
 *
 * BEFORE: 20 sequential HTTP calls on every startup → triggers IP block.
 * AFTER:  1 HTTP call (sync_all.php) → returns everything in one response.
 *
 * Steps:
 *   1. Check network
 *   2. Check API key (redirect to login if missing)
 *   3. Check server reachable
 *   4. syncAll() — one call, replaces all 18 individual fetch steps
 *   5. Go to MainActivity
 */
public class InitializationHelper {

    private static final String TAG = "InitializationHelper";

    private final Context           context;
    private final DatabaseHelper    databaseHelper;
    private final TextView          statusTextView;
    private final SharedPreferences prefs;

    public InitializationHelper(Context context, TextView statusTextView) {
        this.context        = context;
        this.statusTextView = statusTextView;
        this.databaseHelper = new DatabaseHelper(context);
        this.prefs          = context.getSharedPreferences("MyApp", 0);
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public void initialize() {
        step1_checkNetwork();
    }

    // ── Step 1: Network ───────────────────────────────────────────────────────

    private void step1_checkNetwork() {
        updateStatus("Vérification du réseau...");
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;

        if (info != null && info.isConnectedOrConnecting()) {
            Log.d(TAG, "Network OK.");
            step2_checkApiKey();
        } else {
            Log.d(TAG, "No network — attempting offline mode.");
            attemptOfflineMode();
        }
    }

    // ── Step 2: API key ───────────────────────────────────────────────────────

    private void step2_checkApiKey() {
        updateStatus("Connexion au serveur...");
        String apiKey = prefs.getString(DatabaseHelper.COLUMN_API_KEY, null);
        if (apiKey == null || apiKey.isEmpty()) {
            Log.d(TAG, "No API key — redirecting to login.");
            redirectToLogin();
        } else {
            step3_checkServer();
        }
    }

    // ── Step 3: Server reachable? ─────────────────────────────────────────────

    private void step3_checkServer() {
        databaseHelper.getFromServerStatus(new DatabaseHelper.ServerStatusCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Server reachable — starting sync.");
                step4_syncAll();
            }
            @Override
            public void onFailure(String error) {
                Log.w(TAG, "Server unreachable: " + error + " — offline mode.");
                attemptOfflineMode();
            }
        });
    }

    // ── Step 4: Single sync call ──────────────────────────────────────────────

    private void step4_syncAll() {
        updateStatus("Synchronisation des données...");

        databaseHelper.syncAll(new DatabaseHelper.DataUpdateCallback() {
            @Override
            public void onComplete() {
                Log.d(TAG, "Sync complete.");
                // Save today as the last sync date so other parts of the app
                // can check if data is fresh without re-syncing.
                prefs.edit()
                        .putString("lastSyncDate",
                                new java.text.SimpleDateFormat("yyyy-MM-dd",
                                        java.util.Locale.getDefault())
                                        .format(new java.util.Date()))
                        .apply();
                proceedToMainActivity();
            }
            @Override
            public void onFailure(String error) {
                // Sync failed but we have local data — go offline rather than
                // blocking the user on the splash screen.
                Log.e(TAG, "Sync failed: " + error + " — using cached data.");
                updateStatus("Données locales utilisées.");
                proceedToMainActivity();
            }
        });
    }

    // ── Offline fallback ──────────────────────────────────────────────────────

    private void attemptOfflineMode() {
        String apiKey = prefs.getString(DatabaseHelper.COLUMN_API_KEY, null);
        if (apiKey == null || apiKey.isEmpty()) {
            redirectToLogin();
        } else {
            Log.d(TAG, "Offline mode — using local cache.");
            proceedToMainActivity();
        }
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    private void redirectToLogin() {
        context.startActivity(new Intent(context, LoginActivity.class));
        ((Activity) context).finish();
    }

    private void proceedToMainActivity() {
        Log.d(TAG, "Going to MainActivity.");
        context.startActivity(new Intent(context, MainActivity.class));
        ((Activity) context).finish();
    }

    // ── UI helper — safe to call from any thread ──────────────────────────────

    private void updateStatus(String message) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> statusTextView.setText(message));
        } else {
            statusTextView.setText(message);
        }
    }
}