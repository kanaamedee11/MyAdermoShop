package com.example.myadermoshop;

/* loaded from: classes.dex */
public class ProductPrice {
    private double price;
    private String priceStateDate;
    private int pricecaseID;
    private String pricecaseState;
    private String productID;

    public int getPricecaseID() {
        return this.pricecaseID;
    }

    public void setPricecaseID(int i) {
        this.pricecaseID = i;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double d) {
        this.price = d;
    }

    public String getPricecaseState() {
        return this.pricecaseState;
    }

    public void setPricecaseState(String str) {
        this.pricecaseState = str;
    }

    public String getPriceStateDate() {
        return this.priceStateDate;
    }

    public void setPriceStateDate(String str) {
        this.priceStateDate = str;
    }

    public String getProductID() {
        return this.productID;
    }

    public void setProductID(String str) {
        this.productID = str;
    }
}