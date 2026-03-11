package com.example.myadermoshop;

/* loaded from: classes.dex */
public class ControleCase {
    private int actualQuantity;
    private int controleCaseID;
    private int controleID;
    private int expectedQuantity;
    private String productID;

    public ControleCase() {
    }

    public ControleCase(int i, int i2, String str, int i3, int i4) {
        this.controleCaseID = i;
        this.controleID = i2;
        this.productID = str;
        this.expectedQuantity = i3;
        this.actualQuantity = i4;
    }

    public int getControleCaseID() {
        return this.controleCaseID;
    }

    public void setControleCaseID(int i) {
        this.controleCaseID = i;
    }

    public int getControleID() {
        return this.controleID;
    }

    public void setControleID(int i) {
        this.controleID = i;
    }

    public String getProductID() {
        return this.productID;
    }

    public void setProductID(String str) {
        this.productID = str;
    }

    public int getExpectedQuantity() {
        return this.expectedQuantity;
    }

    public void setExpectedQuantity(int i) {
        this.expectedQuantity = i;
    }

    public int getActualQuantity() {
        return this.actualQuantity;
    }

    public void setActualQuantity(int i) {
        this.actualQuantity = i;
    }
}
