package com.example.myadermoshop;

import com.google.gson.annotations.SerializedName;

/**
 * Minimal request body for POST employee_login.php.
 * Only sends the two fields the server actually reads:
 *   $data['email'] and $data['password']
 *
 * Using the full Employee object would send ~13 extra fields,
 * which some servers reject with HTTP 400.
 */
public class LoginRequest {

    @SerializedName("email")
    private final String email;

    @SerializedName("password")
    private final String password;

    public LoginRequest(String email, String password) {
        this.email    = email;
        this.password = password;
    }

    public String getEmail()    { return email; }
    public String getPassword() { return password; }
}
