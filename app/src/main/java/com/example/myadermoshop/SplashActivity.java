package com.example.myadermoshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes.dex */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private InitializationHelper initializationHelper;
    private TextView loadingStepsTextView;
    private SharedPreferences sharedPreferences;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        this.loadingStepsTextView = findViewById(R.id.loadingStepsTextView);
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", 0);
        this.sharedPreferences = sharedPreferences;
        String string = sharedPreferences.getString("employeeID", null);
        Log.d(TAG, "Employee ID retrieved: " + string);
        if (string == null) {
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
        InitializationHelper initializationHelper = new InitializationHelper(this, this.loadingStepsTextView);
        this.initializationHelper = initializationHelper;
        initializationHelper.initialize();
    }
}