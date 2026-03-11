package com.example.myadermoshop;

/* loaded from: classes.dex */
public class LowStockProduct {
    private final int availableStock;
    private final String productID;
    private final String productName;
    private final int seuilStock;

    public LowStockProduct(String str, String str2, int i, int i2) {
        this.productID = str;
        this.productName = str2;
        this.seuilStock = i;
        this.availableStock = i2;
    }

    public String getProductID() {
        return this.productID;
    }

    public String getProductName() {
        return this.productName;
    }

    public int getSeuilStock() {
        return this.seuilStock;
    }

    public int getAvailableStock() {
        return this.availableStock;
    }
}