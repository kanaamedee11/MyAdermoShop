package com.example.myadermoshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private InitializationHelper initializationHelper;
    private TextView             loadingStepsTextView;
    private SharedPreferences    sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // ── FIXED: new XML uses tvLoadingStep, old used loadingStepsTextView ──
        loadingStepsTextView = findViewById(R.id.tvLoadingStep);
        sharedPreferences    = getSharedPreferences("MyApp", 0);

        String employeeID = sharedPreferences.getString("employeeID", null);
        Log.d(TAG, "Employee ID retrieved: " + employeeID);

        if (employeeID == null) {
            redirectToLogin();
        } else {
            startInitialization();
        }
    }

    private void redirectToLogin() {
        Log.d(TAG, "Redirecting to LoginActivity...");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void startInitialization() {
        Log.d(TAG, "Starting initialization...");
        initializationHelper = new InitializationHelper(this, loadingStepsTextView);
        initializationHelper.initialize();
    }
}