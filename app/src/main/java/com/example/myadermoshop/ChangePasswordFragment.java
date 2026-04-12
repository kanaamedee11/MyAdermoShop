package com.example.myadermoshop;

import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordFragment extends Fragment {

    private static final String TAG = "ChangePasswordFragment";

    // ── Existing password — 8 single-digit boxes ──────────────────────────────
    private EditText existingPassword1, existingPassword2, existingPassword3,
            existingPassword4, existingPassword5, existingPassword6,
            existingPassword7, existingPassword8;

    // ── New password — two full 8-char fields ─────────────────────────────────
    private EditText newPassword1, newPassword2;

    private TextView           statusMessage;
    private Button             validateExistingPasswordButton;
    private Button             updateButton;
    private SharedPreferences  sharedPreferences;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences("MyApp", 0);

        existingPassword1 = view.findViewById(R.id.existing_password_1);
        existingPassword2 = view.findViewById(R.id.existing_password_2);
        existingPassword3 = view.findViewById(R.id.existing_password_3);
        existingPassword4 = view.findViewById(R.id.existing_password_4);
        existingPassword5 = view.findViewById(R.id.existing_password_5);
        existingPassword6 = view.findViewById(R.id.existing_password_6);
        existingPassword7 = view.findViewById(R.id.existing_password_7);
        existingPassword8 = view.findViewById(R.id.existing_password_8);
        newPassword1                   = view.findViewById(R.id.new_password_1);
        newPassword2                   = view.findViewById(R.id.new_password_2);
        validateExistingPasswordButton = view.findViewById(R.id.validate_existing_password_button);
        updateButton                   = view.findViewById(R.id.update_button);
        statusMessage                  = view.findViewById(R.id.status_message);

        // New password fields and update button locked until existing password validated
        newPassword1.setEnabled(false);
        newPassword2.setEnabled(false);
        updateButton.setEnabled(false);

        // Auto-advance focus between single-digit boxes
        setUpEditText(existingPassword1, existingPassword2);
        setUpEditText(existingPassword2, existingPassword3);
        setUpEditText(existingPassword3, existingPassword4);
        setUpEditText(existingPassword4, existingPassword5);
        setUpEditText(existingPassword5, existingPassword6);
        setUpEditText(existingPassword6, existingPassword7);
        setUpEditText(existingPassword7, existingPassword8);

        validateExistingPasswordButton.setOnClickListener(v -> {
            try { validatePassword(getExistingPassword()); }
            catch (JSONException e) { e.printStackTrace(); }
        });

        updateButton.setOnClickListener(v -> {
            String p1 = newPassword1.getText().toString();
            String p2 = newPassword2.getText().toString();
            if (isPasswordValid(p1) && p1.equals(p2)) {
                try { changePassword(p1); }
                catch (JSONException e) { e.printStackTrace(); }
            } else {
                showErrorMessage("Les mots de passe doivent contenir 8 chiffres et être identiques.");
            }
        });
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String getExistingPassword() {
        return existingPassword1.getText().toString()
                + existingPassword2.getText().toString()
                + existingPassword3.getText().toString()
                + existingPassword4.getText().toString()
                + existingPassword5.getText().toString()
                + existingPassword6.getText().toString()
                + existingPassword7.getText().toString()
                + existingPassword8.getText().toString();
    }

    private boolean isPasswordValid(String password) {
        return password.matches("\\d{8}");
    }

    /** Auto-advances focus from `current` to `next` after 1 character is typed. */
    private void setUpEditText(final EditText current, final EditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1 && current != existingPassword8) {
                    next.requestFocus();
                }
            }
        });
    }

    // ── Network: validate existing password ───────────────────────────────────

    private void validatePassword(String password) throws JSONException {
        Log.d(TAG, "Validating existing password");
        String apiKey     = sharedPreferences.getString(DatabaseHelper.COLUMN_API_KEY, null);
        String employeeID = sharedPreferences.getString("employeeID", null);
        if (apiKey == null || employeeID == null) {
            showErrorMessage("Clé API ou ID employé introuvable.");
            return;
        }
        JSONObject body = new JSONObject();
        body.put(DatabaseHelper.COLUMN_API_KEY, apiKey);
        body.put("employeeID", employeeID);
        body.put("password", password);

        Volley.newRequestQueue(requireActivity()).add(
                new JsonObjectRequest(JsonObjectRequest.Method.POST,
                        "https://adermoburundi.xyz/api/check_password_validity.php",
                        body,
                        response -> {
                            try {
                                boolean success = response.getBoolean("success");
                                String  message = response.getString("message");
                                Log.d(TAG, "Validate response: " + response);
                                if (success) {
                                    showSuccessMessage(message);
                                    newPassword1.setEnabled(true);
                                    newPassword2.setEnabled(true);
                                    updateButton.setEnabled(true);
                                } else {
                                    showErrorMessage(message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showErrorMessage("Une erreur s'est produite.");
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            showErrorMessage("Erreur : " + error.getMessage());
                        }));
    }

    // ── Network: change password ──────────────────────────────────────────────

    private void changePassword(String newPassword) throws JSONException {
        Log.d(TAG, "Changing password");
        String apiKey     = sharedPreferences.getString(DatabaseHelper.COLUMN_API_KEY, null);
        String employeeID = sharedPreferences.getString("employeeID", null);
        if (apiKey == null || employeeID == null) {
            showErrorMessage("Clé API ou ID employé introuvable.");
            return;
        }
        JSONObject body = new JSONObject();
        body.put(DatabaseHelper.COLUMN_API_KEY, apiKey);
        body.put("employeeID", employeeID);
        body.put("existingPassword", getExistingPassword());
        body.put("newPassword", newPassword);

        Volley.newRequestQueue(requireActivity()).add(
                new JsonObjectRequest(JsonObjectRequest.Method.POST,
                        "https://adermoburundi.xyz/api/change_password.php",
                        body,
                        response -> {
                            try {
                                boolean success = response.getBoolean("success");
                                String  message = response.getString("message");
                                Log.d(TAG, "Change response: " + response);
                                if (success) {
                                    showSuccessMessage(message);
                                    clearFields();
                                } else {
                                    showErrorMessage(message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showErrorMessage("Une erreur s'est produite.");
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            showErrorMessage("Erreur : " + error.getMessage());
                        }));
    }

    // ── UI helpers ────────────────────────────────────────────────────────────

    private void clearFields() {
        existingPassword1.setText("");
        existingPassword2.setText("");
        existingPassword3.setText("");
        existingPassword4.setText("");
        existingPassword5.setText("");
        existingPassword6.setText("");
        existingPassword7.setText("");
        existingPassword8.setText("");
        newPassword1.setText("");
        newPassword2.setText("");
        newPassword1.setEnabled(false);
        newPassword2.setEnabled(false);
        updateButton.setEnabled(false);
    }

    private void showSuccessMessage(String message) {
        statusMessage.setText(message);
        // ── FIXED: use Color.parseColor() to avoid R.color sync issues ──
        statusMessage.setTextColor(Color.parseColor("#34C759")); // ios_green
    }

    private void showErrorMessage(String message) {
        statusMessage.setText(message);
        // ── FIXED: use Color.parseColor() to avoid R.color sync issues ──
        statusMessage.setTextColor(Color.parseColor("#FF3B30")); // ios_red
    }
}