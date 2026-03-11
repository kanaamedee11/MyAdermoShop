package com.example.myadermoshop;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/* loaded from: classes.dex */
public class Cart implements Serializable {

    @SerializedName(DatabaseHelper.COLUMN_CART_ID)
    private String cartID;

    @SerializedName("cartItems")
    private List<CartItem> cartItems;

    @SerializedName("currency")
    private String currency;

    @SerializedName("employeeID")
    private String employeeID;

    @SerializedName(DatabaseHelper.COLUMN_CART_DATE)
    private String timestamp;

    @SerializedName("totalAmount")
    private double totalAmount;

    public Cart(String str, String str2, String str3, double d, List<CartItem> list, String str4) {
        this.cartID = str;
        this.timestamp = str2;
        this.currency = str3;
        this.totalAmount = d;
        this.cartItems = list;
        this.employeeID = str4;
    }

    public Cart() {
    }

    public String getCartID() {
        return this.cartID;
    }

    public void setCartID(String str) {
        this.cartID = str;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String str) {
        this.timestamp = str;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String str) {
        this.currency = str;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(double d) {
        this.totalAmount = d;
    }

    public List<CartItem> getCartItems() {
        return this.cartItems;
    }

    public void setCartItems(List<CartItem> list) {
        this.cartItems = list;
    }

    public String getEmployeeID() {
        return this.employeeID;
    }

    public void setEmployeeID(String str) {
        this.employeeID = str;
    }
}