package com.example.myadermoshop;

import java.util.Date;

/* loaded from: classes.dex */
public class Sale {
    private double amount;
    private String cartID;
    private String currency;
    private String itemDetails;
    private Date time;

    public Sale(Date date, String str, String str2, String str3, double d) {
        this.time = date;
        this.cartID = str;
        this.itemDetails = str2;
        this.currency = str3;
        this.amount = d;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date date) {
        this.time = date;
    }

    public String getCartID() {
        return this.cartID;
    }

    public void setCartID(String str) {
        this.cartID = str;
    }

    public String getItemDetails() {
        return this.itemDetails;
    }

    public void setItemDetails(String str) {
        this.itemDetails = str;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String str) {
        this.currency = str;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double d) {
        this.amount = d;
    }

    public void addItem(String str, int i, double d) {
        String str2 = this.itemDetails;
        if (str2 == null || str2.isEmpty()) {
            this.itemDetails = str + " x " + i + " x " + d;
        } else {
            this.itemDetails += "\n" + str + " x " + i + " x " + d;
        }
    }
}