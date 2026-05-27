package com.example.myadermoshop;

import com.google.gson.annotations.SerializedName;

/**
 * Maps the exact JSON shape returned by employee_login.php:
 *
 * {
 *   "success": true,
 *   "employee": { ... },   <-- key is "employee", NOT "data"
 *   "message": "Login successful."
 * }
 *
 * The generic ServerResponse<T> uses "data" so it cannot be used here.
 */
public class LoginResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    // PHP returns the employee object under the key "employee"
    @SerializedName("employee")
    private Employee employee;

    public boolean isSuccess() { return success; }
    public String  getMessage() { return message; }
    public Employee getEmployee() { return employee; }
}
