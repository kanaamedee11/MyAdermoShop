package com.example.myadermoshop;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class Stock {
    private String employeeID;

    @SerializedName(DatabaseHelper.COLUMN_FACTURE_IMAGE_NAME)
    private String factureImageName;

    @SerializedName("factureImage_url")
    private String factureImageUrl;
    private String factureNumber;
    private int paymentTypeID;
    private String productID;
    private int statusID;
    private String stockDateTime;
    private String stockExpDate;
    private String stockID;
    private String stockManDate;
    private int stockQuantity;
    private String supplierContact;
    private String supplierName;
    private double totalAmountUsed;
    private int uploadStatus;

    public Stock(String str, String str2, int i, double d, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, int i2, int i3, int i4, String str11) {
        this.stockID = str;
        this.stockDateTime = str2;
        this.stockQuantity = i;
        this.totalAmountUsed = d;
        this.productID = str3;
        this.stockManDate = str4;
        this.stockExpDate = str5;
        this.supplierName = str6;
        this.supplierContact = str7;
        this.factureNumber = str8;
        this.factureImageName = str9;
        this.factureImageUrl = str10;
        this.paymentTypeID = i2;
        this.statusID = i3;
        this.uploadStatus = i4;
        this.employeeID = str11;
    }

    public Stock(String str, String str2, int i, double d, String str3, String str4, String str5, String str6, String str7, String str8, String str9, int i2, int i3, String str10) {
        this(str, str2, i, d, str3, str4, str5, str6, str7, str8, str9, null, i2, i3, 0, str10);
    }

    public Stock(String str, String str2, int i, double d, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        this(str, str2, i, d, str3, str4, str5, str6, str7, str8, str9, null, 0, 0, 0, null);
    }

    public Stock() {
    }

    public String getStockID() {
        return this.stockID;
    }

    public void setStockID(String str) {
        this.stockID = str;
    }

    public String getStockDateTime() {
        return this.stockDateTime;
    }

    public void setStockDateTime(String str) {
        this.stockDateTime = str;
    }

    public int getStockQuantity() {
        return this.stockQuantity;
    }

    public void setStockQuantity(int i) {
        this.stockQuantity = i;
    }

    public double getTotalAmountUsed() {
        return this.totalAmountUsed;
    }

    public void setTotalAmountUsed(double d) {
        this.totalAmountUsed = d;
    }

    public String getProductID() {
        return this.productID;
    }

    public void setProductID(String str) {
        this.productID = str;
    }

    public String getStockManDate() {
        return this.stockManDate;
    }

    public void setStockManDate(String str) {
        this.stockManDate = str;
    }

    public String getStockExpDate() {
        return this.stockExpDate;
    }

    public void setStockExpDate(String str) {
        this.stockExpDate = str;
    }

    public String getSupplierName() {
        return this.supplierName;
    }

    public void setSupplierName(String str) {
        this.supplierName = str;
    }

    public String getSupplierContact() {
        return this.supplierContact;
    }

    public void setSupplierContact(String str) {
        this.supplierContact = str;
    }

    public String getFactureNumber() {
        return this.factureNumber;
    }

    public void setFactureNumber(String str) {
        this.factureNumber = str;
    }

    public String getFactureImageName() {
        return this.factureImageName;
    }

    public void setFactureImageName(String str) {
        this.factureImageName = str;
    }

    public String getFactureImageUrl() {
        return this.factureImageUrl;
    }

    public void setFactureImageUrl(String str) {
        this.factureImageUrl = str;
    }

    public int getPaymentTypeID() {
        return this.paymentTypeID;
    }

    public void setPaymentTypeID(int i) {
        this.paymentTypeID = i;
    }

    public int getStatusID() {
        return this.statusID;
    }

    public void setStatusID(int i) {
        this.statusID = i;
    }

    public int getUploadStatus() {
        return this.uploadStatus;
    }

    public void setUploadStatus(int i) {
        this.uploadStatus = i;
    }

    public String getEmployeeID() {
        return this.employeeID;
    }

    public void setEmployeeID(String str) {
        this.employeeID = str;
    }
}