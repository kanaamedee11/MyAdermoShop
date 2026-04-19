package com.example.myadermoshop;

/* loaded from: classes.dex */
public class Employee {
    private String apiKey;
    private String employeeAccountActivation;
    private String employeeBirthday;
    private String employeeCNI;
    private String employeeEmail;
    private String employeeFirstName;
    private String employeeID;
    private String employeeLastName;
    private String employeeTel;
    private String fatherFullName;
    private String motherFullName;
    private String pictureName;
    private String pictureUrl;

    public Employee(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13) {
        this.employeeID = str;
        this.employeeFirstName = str2;
        this.employeeLastName = str3;
        this.employeeTel = str4;
        this.employeeEmail = str5;
        this.fatherFullName = str7;
        this.motherFullName = str8;
        this.employeeBirthday = str9;
        this.employeeAccountActivation = str10;
        this.employeeCNI = str11;
        this.apiKey = str6;
        this.pictureName = str12;
        this.pictureUrl = str13;
    }

    public Employee() {
    }

    public String getEmployeeID() {
        return this.employeeID;
    }

    public void setEmployeeID(String str) {
        this.employeeID = str;
    }

    public String getEmployeeFirstName() {
        return this.employeeFirstName;
    }

    public void setEmployeeFirstName(String str) {
        this.employeeFirstName = str;
    }

    public String getEmployeeLastName() {
        return this.employeeLastName;
    }

    public void setEmployeeLastName(String str) {
        this.employeeLastName = str;
    }

    public String getEmployeeTel() {
        return this.employeeTel;
    }

    public void setEmployeeTel(String str) {
        this.employeeTel = str;
    }

    public String getEmployeeEmail() {
        return this.employeeEmail;
    }

    public void setEmployeeEmail(String str) {
        this.employeeEmail = str;
    }

    public String getFatherFullName() {
        return this.fatherFullName;
    }

    public void setFatherFullName(String str) {
        this.fatherFullName = str;
    }

    public String getMotherFullName() {
        return this.motherFullName;
    }

    public void setMotherFullName(String str) {
        this.motherFullName = str;
    }

    public String getEmployeeBirthday() {
        return this.employeeBirthday;
    }

    public void setEmployeeBirthday(String str) {
        this.employeeBirthday = str;
    }

    public String getEmployeeAccountActivation() {
        return this.employeeAccountActivation;
    }

    public void setEmployeeAccountActivation(String str) {
        this.employeeAccountActivation = str;
    }

    public String getEmployeeCNI() {
        return this.employeeCNI;
    }

    public void setEmployeeCNI(String str) {
        this.employeeCNI = str;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String str) {
        this.apiKey = str;
    }

    public String getPictureName() {
        return this.pictureName;
    }

    public void setPictureName(String str) {
        this.pictureName = str;
    }

    public String getPictureUrl() {
        return this.pictureUrl;
    }

    public void setPictureUrl(String str) {
        this.pictureUrl = str;
    }

    public void setEmployeePassword(String password) {
    }
}