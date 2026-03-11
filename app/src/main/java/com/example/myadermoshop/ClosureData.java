package com.example.myadermoshop;

/* loaded from: classes.dex */
public class ClosureData {
    private double amountInExpenses;
    private double amountInStock;
    private String closureDate;
    private String closureID;
    private int closureStatus;
    private String employeeID;
    private double totalSales;
    private int totalStocksMade;
    private double versementDeposit;

    public ClosureData(String str, String str2, double d, double d2, int i, String str3, int i2, double d3, double d4) {
        this.closureID = str;
        this.closureDate = str2;
        this.totalSales = d;
        this.amountInStock = d2;
        this.closureStatus = i;
        this.employeeID = str3;
        this.totalStocksMade = i2;
        this.amountInExpenses = d3;
        this.versementDeposit = d4;
    }

    public String getClosureID() {
        return this.closureID;
    }

    public void setClosureID(String str) {
        this.closureID = str;
    }

    public String getClosureDate() {
        return this.closureDate;
    }

    public void setClosureDate(String str) {
        this.closureDate = str;
    }

    public double getTotalSales() {
        return this.totalSales;
    }

    public void setTotalSales(double d) {
        this.totalSales = d;
    }

    public double getAmountInStock() {
        return this.amountInStock;
    }

    public void setAmountInStock(double d) {
        this.amountInStock = d;
    }

    public int getClosureStatus() {
        return this.closureStatus;
    }

    public void setClosureStatus(int i) {
        this.closureStatus = i;
    }

    public String getEmployeeID() {
        return this.employeeID;
    }

    public void setEmployeeID(String str) {
        this.employeeID = str;
    }

    public int getTotalStocksMade() {
        return this.totalStocksMade;
    }

    public void setTotalStocksMade(int i) {
        this.totalStocksMade = i;
    }

    public double getAmountInExpenses() {
        return this.amountInExpenses;
    }

    public void setAmountInExpenses(double d) {
        this.amountInExpenses = d;
    }

    public double getVersementDeposit() {
        return this.versementDeposit;
    }

    public void setVersementDeposit(double d) {
        this.versementDeposit = d;
    }
}