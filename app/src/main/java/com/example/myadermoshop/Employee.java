package com.example.myadermoshop;

import com.google.gson.annotations.SerializedName;

public class Employee {

    @SerializedName("employeeID")
    private String employeeID;

    @SerializedName("employeeFirstName")
    private String employeeFirstName;

    @SerializedName("employeeLastName")
    private String employeeLastName;

    @SerializedName("employeeTel")
    private String employeeTel;

    @SerializedName("employeeEmail")
    private String employeeEmail;

    // Sent to server on login; never stored locally or returned to UI
    @SerializedName("password")
    private String employeePassword;

    @SerializedName("apiKey")
    private String apiKey;

    @SerializedName("fatherFullName")
    private String fatherFullName;

    @SerializedName("motherFullname")   // matches PHP column name exactly
    private String motherFullName;

    @SerializedName("employeeBirthday")
    private String employeeBirthday;

    @SerializedName("employeeAccountActivation")
    private String employeeAccountActivation;

    @SerializedName("employeeCNI")
    private String employeeCNI;

    @SerializedName("picture_name")
    private String pictureName;

    @SerializedName("picture_url")
    private String pictureUrl;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Employee() {}

    /**
     * Full constructor — argument order matches the original decompiled class.
     * str6 = apiKey (original had it in position 6, not password)
     */
    public Employee(String employeeID, String employeeFirstName, String employeeLastName,
                    String employeeTel, String employeeEmail, String apiKey,
                    String fatherFullName, String motherFullName, String employeeBirthday,
                    String employeeAccountActivation, String employeeCNI,
                    String pictureName, String pictureUrl) {
        this.employeeID = employeeID;
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.employeeTel = employeeTel;
        this.employeeEmail = employeeEmail;
        this.apiKey = apiKey;
        this.fatherFullName = fatherFullName;
        this.motherFullName = motherFullName;
        this.employeeBirthday = employeeBirthday;
        this.employeeAccountActivation = employeeAccountActivation;
        this.employeeCNI = employeeCNI;
        this.pictureName = pictureName;
        this.pictureUrl = pictureUrl;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public String getEmployeeID() { return employeeID; }
    public void setEmployeeID(String employeeID) { this.employeeID = employeeID; }

    public String getEmployeeFirstName() { return employeeFirstName; }
    public void setEmployeeFirstName(String employeeFirstName) { this.employeeFirstName = employeeFirstName; }

    public String getEmployeeLastName() { return employeeLastName; }
    public void setEmployeeLastName(String employeeLastName) { this.employeeLastName = employeeLastName; }

    public String getEmployeeTel() { return employeeTel; }
    public void setEmployeeTel(String employeeTel) { this.employeeTel = employeeTel; }

    public String getEmployeeEmail() { return employeeEmail; }
    public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }

    /**
     * Used only when building the login request body.
     * The server reads this as "password" (see employee_login.php).
     * Never read back from a server response — the login response omits it.
     */
    public String getEmployeePassword() { return employeePassword; }
    public void setEmployeePassword(String employeePassword) { this.employeePassword = employeePassword; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getFatherFullName() { return fatherFullName; }
    public void setFatherFullName(String fatherFullName) { this.fatherFullName = fatherFullName; }

    public String getMotherFullName() { return motherFullName; }
    public void setMotherFullName(String motherFullName) { this.motherFullName = motherFullName; }

    public String getEmployeeBirthday() { return employeeBirthday; }
    public void setEmployeeBirthday(String employeeBirthday) { this.employeeBirthday = employeeBirthday; }

    public String getEmployeeAccountActivation() { return employeeAccountActivation; }
    public void setEmployeeAccountActivation(String employeeAccountActivation) {
        this.employeeAccountActivation = employeeAccountActivation;
    }

    public String getEmployeeCNI() { return employeeCNI; }
    public void setEmployeeCNI(String employeeCNI) { this.employeeCNI = employeeCNI; }

    public String getPictureName() { return pictureName; }
    public void setPictureName(String pictureName) { this.pictureName = pictureName; }

    public String getPictureUrl() { return pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }
}