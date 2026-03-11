package com.example.myadermoshop;

import java.util.List;

/* loaded from: classes.dex */
public class PhysicalControle {
    private String adminID;
    private List<ControleCase> controleCases;
    private String controleDateTime;
    private int controleID;
    private String employeeID;

    public PhysicalControle(int i, String str, String str2, String str3, List<ControleCase> list) {
        this.controleID = i;
        this.controleDateTime = str;
        this.adminID = str2;
        this.employeeID = str3;
        this.controleCases = list;
    }

    public int getControleID() {
        return this.controleID;
    }

    public void setControleID(int i) {
        this.controleID = i;
    }

    public String getControleDateTime() {
        return this.controleDateTime;
    }

    public void setControleDateTime(String str) {
        this.controleDateTime = str;
    }

    public String getAdminID() {
        return this.adminID;
    }

    public void setAdminID(String str) {
        this.adminID = str;
    }

    public String getEmployeeID() {
        return this.employeeID;
    }

    public void setEmployeeID(String str) {
        this.employeeID = str;
    }

    public List<ControleCase> getControleCases() {
        return this.controleCases;
    }

    public void setControleCases(List<ControleCase> list) {
        this.controleCases = list;
    }
}