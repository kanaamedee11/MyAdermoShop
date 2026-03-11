package com.example.myadermoshop;

/* loaded from: classes.dex */
public class TypeDispense {
    private int isActive;
    private String subAccountID;
    private int typeDispenseID;
    private String typeDispenseName;

    public TypeDispense(int i, String str, String str2, int i2) {
        this.typeDispenseID = i;
        this.typeDispenseName = str;
        this.subAccountID = str2;
        this.isActive = i2;
    }

    public int getTypeDispenseID() {
        return this.typeDispenseID;
    }

    public void setTypeDispenseID(int i) {
        this.typeDispenseID = i;
    }

    public String getTypeDispenseName() {
        return this.typeDispenseName;
    }

    public void setTypeDispenseName(String str) {
        this.typeDispenseName = str;
    }

    public String getSubAccountID() {
        return this.subAccountID;
    }

    public void setSubAccountID(String str) {
        this.subAccountID = str;
    }

    public int getIsActive() {
        return this.isActive;
    }

    public void setIsActive(int i) {
        this.isActive = i;
    }

    public boolean isActive() {
        return this.isActive == 1;
    }
}