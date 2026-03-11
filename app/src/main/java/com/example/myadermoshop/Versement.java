package com.example.myadermoshop;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class Versement {
    private String actionDate;
    private String adminID;
    private String employeeID;
    private double expectedAmount;
    private int paymentTypeID;
    private int statusID;
    private int uploadStatus;
    private double versedAmount;
    private String versementDateTime;
    private String versementID;

    @SerializedName("versementPictureName")
    private String versementPictureName;

    @SerializedName("versementPictureUrl")
    private String versementPictureUrl;

    public Versement(String str, String str2, String str3, int i, double d, double d2, String str4, String str5, String str6, String str7, int i2, int i3) {
        this.versementID = str;
        this.employeeID = str2;
        this.adminID = str3;
        this.statusID = i;
        this.expectedAmount = d;
        this.versedAmount = d2;
        this.versementPictureName = str4;
        this.versementPictureUrl = str5;
        this.versementDateTime = str6;
        this.actionDate = str7;
        this.paymentTypeID = i2;
        this.uploadStatus = i3;
    }

    public Versement() {
    }

    public Versement(String str, String str2, String str3, int i, double d, double d2, int i2, String str4, String str5, String str6) {
        this.versementID = str;
        this.employeeID = str2;
        this.adminID = str3;
        this.statusID = i;
        this.expectedAmount = d;
        this.versedAmount = d2;
        this.paymentTypeID = i2;
        this.versementPictureName = str4;
        this.versementDateTime = str5;
        this.actionDate = str6;
    }

    public String getVersementID() {
        return this.versementID;
    }

    public void setVersementID(String str) {
        this.versementID = str;
    }

    public String getEmployeeID() {
        return this.employeeID;
    }

    public void setEmployeeID(String str) {
        this.employeeID = str;
    }

    public String getAdminID() {
        return this.adminID;
    }

    public void setAdminID(String str) {
        this.adminID = str;
    }

    public int getStatusID() {
        return this.statusID;
    }

    public void setStatusID(int i) {
        this.statusID = i;
    }

    public double getExpectedAmount() {
        return this.expectedAmount;
    }

    public void setExpectedAmount(double d) {
        this.expectedAmount = d;
    }

    public double getVersedAmount() {
        return this.versedAmount;
    }

    public void setVersedAmount(double d) {
        this.versedAmount = d;
    }

    public int getPaymentTypeID() {
        return this.paymentTypeID;
    }

    public void setPaymentTypeID(int i) {
        this.paymentTypeID = i;
    }

    public String getVersementPictureName() {
        return this.versementPictureName;
    }

    public void setVersementPictureName(String str) {
        this.versementPictureName = str;
    }

    public String getVersementPictureUrl() {
        return this.versementPictureUrl;
    }

    public void setVersementPictureUrl(String str) {
        this.versementPictureUrl = str;
    }

    public String getVersementDateTime() {
        return this.versementDateTime;
    }

    public void setVersementDateTime(String str) {
        this.versementDateTime = str;
    }

    public String getActionDate() {
        return this.actionDate;
    }

    public void setActionDate(String str) {
        this.actionDate = str;
    }

    public int getUploadStatus() {
        return this.uploadStatus;
    }

    public void setUploadStatus(int i) {
        this.uploadStatus = i;
    }
}