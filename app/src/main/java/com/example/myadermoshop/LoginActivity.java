package com.example.myadermoshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private DatabaseHelper databaseHelper;
    private EditText emailEditText;
    private Button loginButton;
    private EditText passwordEditText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        
        this.emailEditText = findViewById(R.id.emailEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);
        this.loginButton = findViewById(R.id.loginButton);
        
        this.databaseHelper = new DatabaseHelper(this);
        this.sharedPreferences = getSharedPreferences("MyApp", 0);
        
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                
                if (email.isEmpty() || password.isEmpty()) {
                    showAlert("Please enter email and password");
                } else {
                    loginButton.setEnabled(false);
                    try {
                        loginUser(email, password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loginButton.setEnabled(true);
                    }
                }
            }
        });
    }

    private void loginUser(String email, String password) throws JSONException {
        JSONObject loginParams = new JSONObject();
        loginParams.put("email", email);
        loginParams.put("password", password);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, 
                "https://adermoburundi.xyz/api/employee_login.php", 
                loginParams, 
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.getBoolean("success")) {
                        showAlert(response.getString("message"));
                        loginButton.setEnabled(true);
                        return;
                    }
                    
                    JSONObject employeeJson = response.getJSONObject("employee");
                    String employeeID = employeeJson.getString("employeeID");
                    String apiKey = employeeJson.getString(DatabaseHelper.COLUMN_API_KEY);
                    
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("employeeID", employeeID);
                    editor.putString(DatabaseHelper.COLUMN_API_KEY, apiKey);
                    editor.apply();
                    
                    Log.d(TAG, "Login successful. Employee ID: " + employeeID);
                    
                    Employee employee = new Employee(
                        employeeID,
                        employeeJson.getString(DatabaseHelper.COLUMN_EMPLOYEE_FIRST_NAME),
                        employeeJson.getString(DatabaseHelper.COLUMN_EMPLOYEE_LAST_NAME),
                        employeeJson.getString(DatabaseHelper.COLUMN_EMPLOYEE_TEL),
                        employeeJson.getString(DatabaseHelper.COLUMN_EMPLOYEE_EMAIL),
                        apiKey,
                        safeGetString(employeeJson, DatabaseHelper.COLUMN_FATHER_FULL_NAME, ""),
                        safeGetString(employeeJson, "motherFullname", ""),
                        safeGetString(employeeJson, DatabaseHelper.COLUMN_EMPLOYEE_BIRTHDAY, ""),
                        safeGetString(employeeJson, DatabaseHelper.COLUMN_EMPLOYEE_ACCOUNT_ACTIVATION, ""),
                        safeGetString(employeeJson, DatabaseHelper.COLUMN_EMPLOYEE_CNI, ""),
                        safeGetString(employeeJson, DatabaseHelper.COLUMN_PICTURE_NAME, ""),
                        safeGetString(employeeJson, DatabaseHelper.COLUMN_PICTURE_URL, "")
                    );
                    
                    databaseHelper.addEmployee(employee);
                    
                    String pictureUrl = safeGetString(employeeJson, DatabaseHelper.COLUMN_PICTURE_URL, "");
                    if (!pictureUrl.isEmpty()) {
                        ImageDownloadUtil.downloadImageWithCustomPath(LoginActivity.this, pictureUrl, "employee_pictures");
                    }
                    
                    startActivity(new Intent(LoginActivity.this, SplashActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showAlert("An error occurred during response processing");
                    loginButton.setEnabled(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                showAlert("Server error: " + error.getMessage());
                loginButton.setEnabled(true);
            }
        });
        
        Volley.newRequestQueue(this).add(request);
    }

    private String safeGetString(JSONObject json, String key, String defaultValue) {
        try {
            return json.isNull(key) ? defaultValue : json.getString(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    private void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
