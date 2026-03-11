package com.example.myadermoshop;

/* loaded from: classes.dex */
public class ProductInfo {
    private int pricecaseID;
    private String productID;
    private String productManufacture;
    private String productName;
    private String productPhotoName;
    private double productPrice;
    private int scannedDocumentNumber;
    private String stockExpDate;
    private String stockId;
    private String stockManDate;
    private int stockQuantity;

    public ProductInfo(String str, String str2, String str3, int i, String str4, String str5, String str6, String str7, int i2, double d, int i3) {
        this.productID = str;
        this.productName = str2;
        this.stockId = str3;
        this.stockQuantity = i;
        this.stockManDate = str4;
        this.stockExpDate = str5;
        this.productManufacture = str6;
        this.productPhotoName = str7;
        this.scannedDocumentNumber = i2;
        this.productPrice = d;
        this.pricecaseID = i3;
    }

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

    public String getStockId() {
        return this.stockId;
    }

    public void setStockId(String str) {
        this.stockId = str;
    }

    public int getStockQuantity() {
        return this.stockQuantity;
    }

    public void setStockQuantity(int i) {
        this.stockQuantity = i;
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

    public String getProductManufacture() {
        return this.productManufacture;
    }

    public void setProductManufacture(String str) {
        this.productManufacture = str;
    }

    public String getProductPhotoName() {
        return this.productPhotoName;
    }

    public void setProductPhotoName(String str) {
        this.productPhotoName = str;
    }

    public int getScannedDocumentNumber() {
        return this.scannedDocumentNumber;
    }

    public void setScannedDocumentNumber(int i) {
        this.scannedDocumentNumber = i;
    }

    public double getProductPrice() {
        return this.productPrice;
    }

    public void setProductPrice(double d) {
        this.productPrice = d;
    }

    public int getPricecaseID() {
        return this.pricecaseID;
    }

    public void setPricecaseID(int i) {
        this.pricecaseID = i;
    }
}