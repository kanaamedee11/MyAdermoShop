package com.example.myadermoshop;

/* loaded from: classes.dex */
public class OperationStatus {
    private String statusDescription;
    private int statusID;
    private String statusLabel;

    public int getStatusID() {
        return this.statusID;
    }

    public void setStatusID(int i) {
        this.statusID = i;
    }

    public String getStatusLabel() {
        return this.statusLabel;
    }

    public void setStatusLabel(String str) {
        this.statusLabel = str;
    }

    public String getStatusDescription() {
        return this.statusDescription;
    }

    public void setStatusDescription(String str) {
        this.statusDescription = str;
    }
}