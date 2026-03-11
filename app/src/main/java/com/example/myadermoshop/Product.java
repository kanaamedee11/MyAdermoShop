package com.example.myadermoshop;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class Product {
    private String adminFullName;

    @SerializedName("adminID")
    private String adminID;

    @SerializedName(DatabaseHelper.COLUMN_IS_ACTIVE_TO_DECIMAL_QUANTITY)
    private int isActiveToDecimalQuantity;

    @SerializedName(DatabaseHelper.COLUMN_IS_ACTIVE_TO_INSTANCES)
    private int isActiveToInstances;

    @SerializedName(DatabaseHelper.COLUMN_MANUFACTURE_ADDRESS)
    private String manufactureAddress;

    @SerializedName(DatabaseHelper.COLUMN_PRICE_CASE_ID)
    private int pricecaseID;

    @SerializedName(DatabaseHelper.COLUMN_PRODUCT_ADD_DATE)
    private String productAddDate;

    @SerializedName("productID")
    private String productID;

    @SerializedName(DatabaseHelper.COLUMN_PRODUCT_MANUFACTURE)
    private String productManufacture;

    @SerializedName(DatabaseHelper.COLUMN_PRODUCT_NAME)
    private String productName;

    @SerializedName(DatabaseHelper.COLUMN_PRODUCT_PHOTO_NAME)
    private String productPhotoName;

    @SerializedName("product_photo_url")
    private String productPhotoUrl;

    @SerializedName("productPrice")
    private double productPrice;

    @SerializedName(DatabaseHelper.COLUMN_PRODUCT_SEUIL_STOCK)
    private int productSeuilStock;

    @SerializedName(DatabaseHelper.COLUMN_SUB_SUB_ACCOUNT_ID)
    private String subSubAccountID;
    private String typeName;

    @SerializedName(DatabaseHelper.COLUMN_TYPE_PRODUCT_ID)
    private int typeProductID;

    @SerializedName(DatabaseHelper.COLUMN_UNITE_ID)
    private String uniteID;

    public String getProductID() {
        return this.productID;
    }

    public void setProductID(String str) {
        this.productID = str;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String str) {
        this.productName = str;
    }

    public String getProductManufacture() {
        return this.productManufacture;
    }

    public void setProductManufacture(String str) {
        this.productManufacture = str;
    }

    public String getManufactureAddress() {
        return this.manufactureAddress;
    }

    public void setManufactureAddress(String str) {
        this.manufactureAddress = str;
    }

    public String getProductPhotoName() {
        return this.productPhotoName;
    }

    public void setProductPhotoName(String str) {
        this.productPhotoName = str;
    }

    public String getProductPhotoUrl() {
        return this.productPhotoUrl;
    }

    public void setProductPhotoUrl(String str) {
        this.productPhotoUrl = str;
    }

    public String getProductAddDate() {
        return this.productAddDate;
    }

    public void setProductAddDate(String str) {
        this.productAddDate = str;
    }

    public int getProductSeuilStock() {
        return this.productSeuilStock;
    }

    public void setProductSeuilStock(int i) {
        this.productSeuilStock = i;
    }

    public String getAdminID() {
        return this.adminID;
    }

    public void setAdminID(String str) {
        this.adminID = str;
    }

    public int getTypeProductID() {
        return this.typeProductID;
    }

    public void setTypeProductID(int i) {
        this.typeProductID = i;
    }

    public String getSubSubAccountID() {
        return this.subSubAccountID;
    }

    public void setSubSubAccountID(String str) {
        this.subSubAccountID = str;
    }

    public String getUniteID() {
        return this.uniteID;
    }

    public void setUniteID(String str) {
        this.uniteID = str;
    }

    public String getAdminFullName() {
        return this.adminFullName;
    }

    public void setAdminFullName(String str) {
        this.adminFullName = str;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String str) {
        this.typeName = str;
    }

    public double getProductPrice() {
        return this.productPrice;
    }

    public void setProductPrice(double d) {
        this.productPrice = d;
    }

    public int getIsActiveToInstances() {
        return this.isActiveToInstances;
    }

    public void setIsActiveToInstances(int i) {
        this.isActiveToInstances = i;
    }

    public int getIsActiveToDecimalQuantity() {
        return this.isActiveToDecimalQuantity;
    }

    public void setIsActiveToDecimalQuantity(int i) {
        this.isActiveToDecimalQuantity = i;
    }

    public int getPricecaseID() {
        return this.pricecaseID;
    }

    public void setPricecaseID(int i) {
        this.pricecaseID = i;
    }

    public boolean isUsingInstances() {
        return this.isActiveToInstances == 1;
    }
}