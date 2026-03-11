package com.example.myadermoshop;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class DeterioratedProductWithInstance {

    @SerializedName("actionDate")
    private String actionDate;

    @SerializedName(DatabaseHelper.COLUMN_ACTION_TAKEN)
    private boolean actionTaken;

    @SerializedName(DatabaseHelper.COLUMN_DETECTED_BY_EMPLOYEE_ID)
    private String detectedByEmployeeID;

    @SerializedName(DatabaseHelper.COLUMN_DETERIORATED_PRODUCT_WITH_INSTANCE_ID)
    private String deterioratedProductWithInstanceID;

    @SerializedName(DatabaseHelper.COLUMN_DETERIORATION_DATE)
    private String deteriorationDate;

    @SerializedName(DatabaseHelper.COLUMN_INSTANCE_ID)
    private String instanceID;

    @SerializedName("pictureName")
    private String pictureName;

    @SerializedName("pictureUrl")
    private String pictureUrl;

    @SerializedName(DatabaseHelper.COLUMN_QUANTITY)
    private int quantity;

    @SerializedName(DatabaseHelper.COLUMN_REASON)
    private String reason;

    @SerializedName(DatabaseHelper.COLUMN_SUBMISSION_DATE)
    private String submissionDate;

    @SerializedName(DatabaseHelper.COLUMN_UPLOAD_STATUS)
    private int uploadStatus;

    public DeterioratedProductWithInstance(String str, String str2, String str3, String str4, int i, String str5, String str6, boolean z, String str7, String str8, String str9, int i2) {
        this.deterioratedProductWithInstanceID = str;
        this.instanceID = str2;
        this.deteriorationDate = str3;
        this.reason = str4;
        this.quantity = i;
        this.detectedByEmployeeID = str5;
        this.pictureName = str6;
        this.actionTaken = z;
        this.actionDate = str7;
        this.submissionDate = str8;
        this.pictureUrl = str9;
        this.uploadStatus = i2;
    }

    public DeterioratedProductWithInstance(String str, String str2, String str3, String str4, int i, String str5, String str6, boolean z, String str7, String str8, int i2) {
        this.deterioratedProductWithInstanceID = str;
        this.instanceID = str2;
        this.deteriorationDate = str3;
        this.reason = str4;
        this.quantity = i;
        this.detectedByEmployeeID = str5;
        this.pictureName = str6;
        this.actionTaken = z;
        this.actionDate = str7;
        this.submissionDate = str8;
        this.uploadStatus = i2;
    }

    public DeterioratedProductWithInstance() {
    }

    public String getDeterioratedProductWithInstanceID() {
        return this.deterioratedProductWithInstanceID;
    }

    public void setDeterioratedProductWithInstanceID(String str) {
        this.deterioratedProductWithInstanceID = str;
    }

    public String getInstanceID() {
        return this.instanceID;
    }

    public void setInstanceID(String str) {
        this.instanceID = str;
    }

    public String getDeteriorationDate() {
        return this.deteriorationDate;
    }

    public void setDeteriorationDate(String str) {
        this.deteriorationDate = str;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String str) {
        this.reason = str;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int i) {
        this.quantity = i;
    }

    public String getDetectedByEmployeeID() {
        return this.detectedByEmployeeID;
    }

    public void setDetectedByEmployeeID(String str) {
        this.detectedByEmployeeID = str;
    }

    public String getPictureName() {
        return this.pictureName;
    }

    public void setPictureName(String str) {
        this.pictureName = str;
    }

    public boolean isActionTaken() {
        return this.actionTaken;
    }

    public void setActionTaken(boolean z) {
        this.actionTaken = z;
    }

    public String getActionDate() {
        return this.actionDate;
    }

    public void setActionDate(String str) {
        this.actionDate = str;
    }

    public String getSubmissionDate() {
        return this.submissionDate;
    }

    public void setSubmissionDate(String str) {
        this.submissionDate = str;
    }

    public String getPictureUrl() {
        return this.pictureUrl;
    }

    public void setPictureUrl(String str) {
        this.pictureUrl = str;
    }

    public int getUploadStatus() {
        return this.uploadStatus;
    }

    public void setUploadStatus(int i) {
        this.uploadStatus = i;
    }
}