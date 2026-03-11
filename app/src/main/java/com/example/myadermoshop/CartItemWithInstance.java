package com.example.myadermoshop;

/* loaded from: classes.dex */
public class CartItemWithInstance {
    private String cartID;
    private String cartItemID;
    private String instanceID;
    private int pricecaseID;

    public CartItemWithInstance(String str, String str2, String str3, int i) {
        this.cartItemID = str;
        this.cartID = str2;
        this.instanceID = str3;
        this.pricecaseID = i;
    }

    public String getCartItemID() {
        return this.cartItemID;
    }

    public void setCartItemID(String str) {
        this.cartItemID = str;
    }

    public String getCartID() {
        return this.cartID;
    }

    public void setCartID(String str) {
        this.cartID = str;
    }

    public String getInstanceID() {
        return this.instanceID;
    }

    public void setInstanceID(String str) {
        this.instanceID = str;
    }

    public int getPricecaseID() {
        return this.pricecaseID;
    }

    public void setPricecaseID(int i) {
        this.pricecaseID = i;
    }
}