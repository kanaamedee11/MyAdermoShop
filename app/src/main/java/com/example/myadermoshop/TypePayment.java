package com.example.myadermoshop;

/* loaded from: classes.dex */
public class TypePayment {
    private String paymentMethod;
    private int paymentTypeID;
    private String subSubAccountID;

    public TypePayment(int i, String str, String str2) {
        this.paymentTypeID = i;
        this.paymentMethod = str;
        this.subSubAccountID = str2;
    }

    public TypePayment() {
    }

    public int getPaymentTypeID() {
        return this.paymentTypeID;
    }

    public void setPaymentTypeID(int i) {
        this.paymentTypeID = i;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(String str) {
        this.paymentMethod = str;
    }

    public String getSubSubAccountID() {
        return this.subSubAccountID;
    }

    public void setSubSubAccountID(String str) {
        this.subSubAccountID = str;
    }

    public String toString() {
        return this.paymentMethod;
    }
}