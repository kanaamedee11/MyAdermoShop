package com.example.myadermoshop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ChangePasswordFragment extends Fragment {
    private static final String TAG = "ChangePasswordFragment";
    private EditText existingPassword1;
    private EditText existingPassword2;
    private EditText existingPassword3;
    private EditText existingPassword4;
    private EditText existingPassword5;
    private EditText existingPassword6;
    private EditText existingPassword7;
    private EditText existingPassword8;
    private EditText newPassword1;
    private EditText newPassword2;
    private SharedPreferences sharedPreferences;
    private TextView statusMessage;
    private Button updateButton;
    private Button validateExistingPasswordButton;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_change_password, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        FragmentActivity activity = getActivity();
        getActivity();
        this.sharedPreferences = activity.getSharedPreferences("MyApp", 0);
        this.existingPassword1 = view.findViewById(R.id.existing_password_1);
        this.existingPassword2 = view.findViewById(R.id.existing_password_2);
        this.existingPassword3 = view.findViewById(R.id.existing_password_3);
        this.existingPassword4 = view.findViewById(R.id.existing_password_4);
        this.existingPassword5 = view.findViewById(R.id.existing_password_5);
        this.existingPassword6 = view.findViewById(R.id.existing_password_6);
        this.existingPassword7 = view.findViewById(R.id.existing_password_7);
        this.existingPassword8 = view.findViewById(R.id.existing_password_8);
        this.newPassword1 = view.findViewById(R.id.new_password_1);
        this.newPassword2 = view.findViewById(R.id.new_password_2);
        this.validateExistingPasswordButton = view.findViewById(R.id.validate_existing_password_button);
        this.updateButton = view.findViewById(R.id.update_button);
        this.statusMessage = view.findViewById(R.id.status_message);
        this.newPassword1.setEnabled(false);
        this.newPassword2.setEnabled(false);
        this.updateButton.setEnabled(false);
        setUpEditText(this.existingPassword1, this.existingPassword2);
        setUpEditText(this.existingPassword2, this.existingPassword3);
        setUpEditText(this.existingPassword3, this.existingPassword4);
        setUpEditText(this.existingPassword4, this.existingPassword5);
        setUpEditText(this.existingPassword5, this.existingPassword6);
        setUpEditText(this.existingPassword6, this.existingPassword7);
        setUpEditText(this.existingPassword7, this.existingPassword8);
        this.validateExistingPasswordButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.ChangePasswordFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) throws JSONException {
                this.f$0.m85x17692b94(view2);
            }
        });
        this.updateButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.ChangePasswordFragment$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) throws JSONException {
                this.f$0.m86x16f2c595(view2);
            }
        });
    }

    /* renamed from: lambda$onViewCreated$0$com-example-myadermoshop-ChangePasswordFragment, reason: not valid java name */
    /* synthetic */ void m85x17692b94(View view) throws JSONException {
        validatePassword(getExistingPassword());
    }

    /* renamed from: lambda$onViewCreated$1$com-example-myadermoshop-ChangePasswordFragment, reason: not valid java name */
    /* synthetic */ void m86x16f2c595(View view) throws JSONException {
        String string = this.newPassword1.getText().toString();
        String string2 = this.newPassword2.getText().toString();
        if (isPasswordValid(string) && string.equals(string2)) {
            changePassword(string);
        } else {
            showErrorMessage("New passwords must be 8 numeric characters and match.");
        }
    }

    private String getExistingPassword() {
        return this.existingPassword1.getText().toString() + this.existingPassword2.getText().toString() + this.existingPassword3.getText().toString() + this.existingPassword4.getText().toString() + this.existingPassword5.getText().toString() + this.existingPassword6.getText().toString() + this.existingPassword7.getText().toString() + this.existingPassword8.getText().toString();
    }

    private boolean isPasswordValid(String str) {
        return str.matches("\\d{8}");
    }

    private void setUpEditText(final EditText editText, final EditText editText2) {
        editText.addTextChangedListener(new TextWatcher() { // from class: com.example.myadermoshop.ChangePasswordFragment.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 1 || editText == ChangePasswordFragment.this.existingPassword8) {
                    return;
                }
                editText2.requestFocus();
            }
        });
    }

    private void validatePassword(String str) throws JSONException {
        Log.d(TAG, "Validating existing password: " + str);
        String string = this.sharedPreferences.getString(DatabaseHelper.COLUMN_API_KEY, null);
        String string2 = this.sharedPreferences.getString("employeeID", null);
        if (string == null || string2 == null) {
            showErrorMessage("API key or employee ID not found");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(DatabaseHelper.COLUMN_API_KEY, string);
            jSONObject.put("employeeID", string2);
            jSONObject.put("password", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Volley.newRequestQueue(getActivity()).add(new JsonObjectRequest(1, "https://adermoburundi.xyz/api/check_password_validity.php", jSONObject, new Response.Listener() { // from class: com.example.myadermoshop.ChangePasswordFragment$$ExternalSyntheticLambda0
            @Override // com.android.volley.Response.Listener
            public void onResponse(Object obj) throws JSONException {
                this.f$0.m87xffe865dd((JSONObject) obj);
            }
        }, new Response.ErrorListener() { // from class: com.example.myadermoshop.ChangePasswordFragment$$ExternalSyntheticLambda1
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                this.f$0.m88xff71ffde(volleyError);
            }
        }));
    }

    /* renamed from: lambda$validatePassword$2$com-example-myadermoshop-ChangePasswordFragment, reason: not valid java name */
    /* synthetic */ void m87xffe865dd(JSONObject jSONObject) throws JSONException {
        try {
            boolean z = jSONObject.getBoolean("success");
            String string = jSONObject.getString("message");
            Log.d(TAG, "Response: " + jSONObject);
            if (z) {
                showSuccessMessage(string);
                this.newPassword1.setEnabled(true);
                this.newPassword2.setEnabled(true);
                this.updateButton.setEnabled(true);
            } else {
                showErrorMessage(string);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showErrorMessage("An error occurred");
        }
    }

    /* renamed from: lambda$validatePassword$3$com-example-myadermoshop-ChangePasswordFragment, reason: not valid java name */
    /* synthetic */ void m88xff71ffde(VolleyError volleyError) {
        volleyError.printStackTrace();
        showErrorMessage("Error: " + volleyError.getMessage());
    }

    private void changePassword(String str) throws JSONException {
        Log.d(TAG, "Changing password to: " + str);
        String string = this.sharedPreferences.getString(DatabaseHelper.COLUMN_API_KEY, null);
        String string2 = this.sharedPreferences.getString("employeeID", null);
        String existingPassword = getExistingPassword();
        if (string == null || string2 == null) {
            showErrorMessage("API key or employee ID not found");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(DatabaseHelper.COLUMN_API_KEY, string);
            jSONObject.put("employeeID", string2);
            jSONObject.put("existingPassword", existingPassword);
            jSONObject.put("newPassword", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Volley.newRequestQueue(getActivity()).add(new JsonObjectRequest(1, "https://adermoburundi.xyz/api/change_password.php", jSONObject, new Response.Listener() { // from class: com.example.myadermoshop.ChangePasswordFragment$$ExternalSyntheticLambda4
            @Override // com.android.volley.Response.Listener
            public void onResponse(Object obj) throws JSONException {
                this.f$0.m83x5fd49b39((JSONObject) obj);
            }
        }, new Response.ErrorListener() { // from class: com.example.myadermoshop.ChangePasswordFragment$$ExternalSyntheticLambda5
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                this.f$0.m84x5f5e353a(volleyError);
            }
        }));
    }

    /* renamed from: lambda$changePassword$4$com-example-myadermoshop-ChangePasswordFragment, reason: not valid java name */
    /* synthetic */ void m83x5fd49b39(JSONObject jSONObject) throws JSONException {
        try {
            boolean z = jSONObject.getBoolean("success");
            String string = jSONObject.getString("message");
            Log.d(TAG, "Response: " + jSONObject);
            if (z) {
                showSuccessMessage(string);
                clearFields();
            } else {
                showErrorMessage(string);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showErrorMessage("An error occurred");
        }
    }

    /* renamed from: lambda$changePassword$5$com-example-myadermoshop-ChangePasswordFragment, reason: not valid java name */
    /* synthetic */ void m84x5f5e353a(VolleyError volleyError) {
        volleyError.printStackTrace();
        showErrorMessage("Error: " + volleyError.getMessage());
    }

    private void clearFields() {
        this.existingPassword1.setText("");
        this.existingPassword2.setText("");
        this.existingPassword3.setText("");
        this.existingPassword4.setText("");
        this.existingPassword5.setText("");
        this.existingPassword6.setText("");
        this.existingPassword7.setText("");
        this.existingPassword8.setText("");
        this.newPassword1.setText("");
        this.newPassword2.setText("");
        this.newPassword1.setEnabled(false);
        this.newPassword2.setEnabled(false);
        this.updateButton.setEnabled(false);
    }

    private void showSuccessMessage(String str) {
        this.statusMessage.setText(str);
        this.statusMessage.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark));
    }

    private void showErrorMessage(String str) {
        this.statusMessage.setText(str);
        this.statusMessage.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
    }
}