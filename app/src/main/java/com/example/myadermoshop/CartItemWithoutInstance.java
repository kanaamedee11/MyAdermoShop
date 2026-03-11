package com.example.myadermoshop;

/* loaded from: classes.dex */
public class CartItemWithoutInstance {
    private String cartID;
    private String cartItemwithoutinstanceID;
    private int pricecaseID;
    private String productID;
    private double quantityCart;

    public CartItemWithoutInstance(String str, double d, String str2, String str3, int i) {
        this.cartItemwithoutinstanceID = str;
        this.quantityCart = d;
        this.cartID = str2;
        this.productID = str3;
        this.pricecaseID = i;
    }

    public String getCartItemwithoutinstanceID() {
        return this.cartItemwithoutinstanceID;
    }

    public void setCartItemwithoutinstanceID(String str) {
        this.cartItemwithoutinstanceID = str;
    }

    public double getQuantityCart() {
        return this.quantityCart;
    }

    public void setQuantityCart(double d) {
        this.quantityCart = d;
    }

    public String getCartID() {
        return this.cartID;
    }

    public void setCartID(String str) {
        this.cartID = str;
    }

    public String getProductID() {
        return this.productID;
    }

    public void setProductID(String str) {
        this.productID = str;
    }

    public int getPricecaseID() {
        return this.pricecaseID;
    }

    public void setPricecaseID(int i) {
        this.pricecaseID = i;
    }
}