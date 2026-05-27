package com.example.myadermoshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private DatabaseHelper    databaseHelper;
    private SharedPreferences sharedPreferences;
    private EditText          emailEditText;
    private EditText          passwordEditText;
    private Button            loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText    = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton      = findViewById(R.id.loginButton);

        databaseHelper    = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("MyApp", 0);

        loginButton.setOnClickListener(v -> {
            String email    = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Please enter email and password");
                return;
            }

            // Disable immediately to prevent double-tap
            loginButton.setEnabled(false);
            loginUser(email, password);
        });
    }

    private void loginUser(String email, String password) {
        // Uses Retrofit (single shared OkHttp connection pool) — no Volley,
        // no per-call queue creation, no burst of parallel TLS handshakes.
        databaseHelper.loginEmployee(email, password, new DatabaseHelper.LoginCallback() {

            @Override
            public void onSuccess(Employee employee) {
                // Persist credentials for the rest of the app
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("employeeID",               employee.getEmployeeID());
                editor.putString(DatabaseHelper.COLUMN_API_KEY, employee.getApiKey());
                editor.apply();

                Log.d(TAG, "Login successful. Employee ID: " + employee.getEmployeeID());

                // Download profile picture in background (non-blocking)
                String pictureUrl = employee.getPictureUrl();
                if (pictureUrl != null && !pictureUrl.isEmpty()) {
                    ImageDownloadUtil.downloadImageWithCustomPath(
                            LoginActivity.this, pictureUrl, "employee_pictures");
                }

                // Go to splash / main screen
                startActivity(new Intent(LoginActivity.this, SplashActivity.class));
                finish();
            }

            @Override
            public void onFailure(String msg) {
                Log.e(TAG, "Login failed: " + msg);
                runOnUiThread(() -> {
                    showToast(msg != null ? msg : "Login failed. Please try again.");
                    loginButton.setEnabled(true);
                });
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}