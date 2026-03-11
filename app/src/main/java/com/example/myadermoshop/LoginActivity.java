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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private DatabaseHelper databaseHelper;
    private EditText emailEditText;
    private Button loginButton;
    private EditText passwordEditText;
    private SharedPreferences sharedPreferences;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        this.emailEditText = findViewById(R.id.emailEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);
        this.loginButton = findViewById(R.id.loginButton);
        this.databaseHelper = new DatabaseHelper(this);
        this.sharedPreferences = getSharedPreferences("MyApp", 0);
        this.loginButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.LoginActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws JSONException {
                this.f$0.m107lambda$onCreate$0$comexamplemyadermoshopLoginActivity(view);
            }
        });
    }

    /* renamed from: lambda$onCreate$0$com-example-myadermoshop-LoginActivity, reason: not valid java name */
    /* synthetic */ void m107lambda$onCreate$0$comexamplemyadermoshopLoginActivity(View view) throws JSONException {
        String string = this.emailEditText.getText().toString();
        String string2 = this.passwordEditText.getText().toString();
        if (string.isEmpty() || string2.isEmpty()) {
            showAlert("Please enter email and password");
        } else {
            this.loginButton.setEnabled(false);
            loginUser(string, string2);
        }
    }

    private void loginUser(String str, String str2) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(NotificationCompat.CATEGORY_EMAIL, str);
            jSONObject.put("password", str2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Volley.newRequestQueue(this).add(new JsonObjectRequest(1, "https://adermoburundi.xyz/api/employee_login.php", jSONObject, new Response.Listener<JSONObject>() { // from class: com.example.myadermoshop.LoginActivity.1
            @Override // com.android.volley.Response.Listener
            public void onResponse(JSONObject jSONObject2) throws JSONException {
                try {
                    if (!jSONObject2.getBoolean("success")) {
                        LoginActivity.this.showAlert(jSONObject2.getString("message"));
                        LoginActivity.this.loginButton.setEnabled(true);
                        return;
                    }
                    JSONObject jSONObject3 = jSONObject2.getJSONObject("employee");
                    SharedPreferences.Editor editorEdit = LoginActivity.this.sharedPreferences.edit();
                    editorEdit.putString("employeeID", jSONObject3.getString("employeeID"));
                    editorEdit.putString(DatabaseHelper.COLUMN_API_KEY, jSONObject3.getString(DatabaseHelper.COLUMN_API_KEY));
                    editorEdit.apply();
                    Log.d(LoginActivity.TAG, "Login successful. Employee ID: " + jSONObject3.getString("employeeID"));
                    LoginActivity.this.databaseHelper.addEmployee(new Employee(jSONObject3.getString("employeeID"), jSONObject3.getString(DatabaseHelper.COLUMN_EMPLOYEE_FIRST_NAME), jSONObject3.getString(DatabaseHelper.COLUMN_EMPLOYEE_LAST_NAME), jSONObject3.getString(DatabaseHelper.COLUMN_EMPLOYEE_TEL), jSONObject3.getString(DatabaseHelper.COLUMN_EMPLOYEE_EMAIL), jSONObject3.getString(DatabaseHelper.COLUMN_API_KEY), LoginActivity.this.safeGetString(jSONObject3, DatabaseHelper.COLUMN_FATHER_FULL_NAME, ""), LoginActivity.this.safeGetString(jSONObject3, "motherFullname", ""), LoginActivity.this.safeGetString(jSONObject3, DatabaseHelper.COLUMN_EMPLOYEE_BIRTHDAY, ""), LoginActivity.this.safeGetString(jSONObject3, DatabaseHelper.COLUMN_EMPLOYEE_ACCOUNT_ACTIVATION, ""), LoginActivity.this.safeGetString(jSONObject3, DatabaseHelper.COLUMN_EMPLOYEE_CNI, ""), LoginActivity.this.safeGetString(jSONObject3, DatabaseHelper.COLUMN_PICTURE_NAME, ""), LoginActivity.this.safeGetString(jSONObject3, DatabaseHelper.COLUMN_PICTURE_URL, "")));
                    String strSafeGetString = LoginActivity.this.safeGetString(jSONObject3, DatabaseHelper.COLUMN_PICTURE_URL, "");
                    if (!strSafeGetString.isEmpty()) {
                        ImageDownloadUtil.downloadImageWithCustomPath(LoginActivity.this, strSafeGetString, "employee_pictures");
                    }
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, SplashActivity.class));
                    LoginActivity.this.finish();
                } catch (JSONException e2) {
                    e2.printStackTrace();
                    LoginActivity.this.showAlert("An error occurred");
                    LoginActivity.this.loginButton.setEnabled(true);
                }
            }
        }, new Response.ErrorListener() { // from class: com.example.myadermoshop.LoginActivity.2
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                LoginActivity.this.showAlert("An error occurred");
                LoginActivity.this.loginButton.setEnabled(true);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String safeGetString(JSONObject jSONObject, String str, String str2) {
        try {
            return jSONObject.getString(str);
        } catch (JSONException unused) {
            return str2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAlert(String str) {
        Toast.makeText(this, str, 0).show();
    }
}