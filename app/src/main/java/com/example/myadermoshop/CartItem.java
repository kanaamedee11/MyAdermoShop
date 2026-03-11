package com.example.myadermoshop;

/* loaded from: classes.dex */
public class CartItem {
    private String instanceID;
    private String itemID;
    private String pricecaseID;
    private String productID;
    private String productName;
    private double quantity;
    private double unitPrice;

    public CartItem(String str, double d, double d2, String str2) {
        this.productName = str;
        this.quantity = d;
        this.unitPrice = d2;
        this.itemID = str2;
    }

    public CartItem(String str, double d, double d2, String str2, String str3, String str4, String str5) {
        this.productName = str;
        this.quantity = d;
        this.unitPrice = d2;
        this.productID = str2;
        this.instanceID = str3;
        this.pricecaseID = str4;
        this.itemID = str5;
    }

    public String getItemID() {
        return this.itemID;
    }

    public void setItemID(String str) {
        this.itemID = str;
    }

    public String getProductName() {
        return this.productName;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public double getUnitPrice() {
        return this.unitPrice;
    }

    public String getProductID() {
        return this.productID;
    }

    public String getInstanceID() {
        return this.instanceID;
    }

    public String getPricecaseID() {
        return this.pricecaseID;
    }

    public void setProductName(String str) {
        this.productName = str;
    }

    public void setQuantity(double d) {
        this.quantity = d;
    }

    public void setUnitPrice(double d) {
        this.unitPrice = d;
    }

    public void setProductID(String str) {
        this.productID = str;
    }

    public void setInstanceID(String str) {
        this.instanceID = str;
    }

    public void setPricecaseID(String str) {
        this.pricecaseID = str;
    }
}