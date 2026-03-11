package com.example.myadermoshop;

/* loaded from: classes.dex */
public class Payment {
    private String cartID;
    private String employeeID;
    private String paymentID;
    private int paymentTypeID;

    public Payment(String str, String str2, int i, String str3) {
        this.paymentID = str;
        this.cartID = str2;
        this.paymentTypeID = i;
        this.employeeID = str3;
    }

    public String getPaymentID() {
        return this.paymentID;
    }

    public void setPaymentID(String str) {
        this.paymentID = str;
    }

    public String getCartID() {
        return this.cartID;
    }

    public void setCartID(String str) {
        this.cartID = str;
    }

    public int getPaymentTypeID() {
        return this.paymentTypeID;
    }

    public void setPaymentTypeID(int i) {
        this.paymentTypeID = i;
    }

    public String getEmployeeID() {
        return this.employeeID;
    }

    public void setEmployeeID(String str) {
        this.employeeID = str;
    }
}