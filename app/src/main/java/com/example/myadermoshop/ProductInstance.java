package com.example.myadermoshop;

/* loaded from: classes.dex */
public class ProductInstance {
    private String instanceID;
    private String instanceState;
    private String stockID;

    public ProductInstance(String str, String str2, String str3) {
        this.instanceID = str;
        this.instanceState = str2;
        this.stockID = str3;
    }

    public ProductInstance() {
    }

    public String getInstanceID() {
        return this.instanceID;
    }

    public void setInstanceID(String str) {
        this.instanceID = str;
    }

    public String getInstanceState() {
        return this.instanceState;
    }

    public void setInstanceState(String str) {
        this.instanceState = str;
    }

    public String getStockID() {
        return this.stockID;
    }

    public void setStockID(String str) {
        this.stockID = str;
    }
}