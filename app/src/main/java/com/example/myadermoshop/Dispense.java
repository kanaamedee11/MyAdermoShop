package com.example.myadermoshop;

import com.google.gson.annotations.SerializedName;

public class Dispense {

    private String dispenseID;
    private String dispenseDate;
    private int    typeDispenseID;
    private String employeeID;
    private int    statusID;

    @SerializedName("pictureName")
    private String pictureName;

    @SerializedName("pictureUrl")
    private String pictureUrl;

    private double amount;
    private int    paymentTypeID;
    private int    uploadStatus;

    // ── Full constructor (from server — includes pictureUrl) ──────────────────
    public Dispense(String dispenseID, String dispenseDate, int typeDispenseID,
                    String employeeID, int statusID,
                    String pictureName, String pictureUrl,
                    double amount, int paymentTypeID, int uploadStatus) {
        this.dispenseID     = dispenseID;
        this.dispenseDate   = dispenseDate;
        this.typeDispenseID = typeDispenseID;
        this.employeeID     = employeeID;
        this.statusID       = statusID;
        this.pictureName    = pictureName;
        this.pictureUrl     = pictureUrl;
        this.amount         = amount;
        this.paymentTypeID  = paymentTypeID;
        this.uploadStatus   = uploadStatus;
    }

    // ── Local constructor (no pictureUrl yet) ─────────────────────────────────
    public Dispense(String dispenseID, String dispenseDate, int typeDispenseID,
                    String employeeID, int statusID,
                    String pictureName,
                    double amount, int paymentTypeID, int uploadStatus) {
        this(dispenseID, dispenseDate, typeDispenseID, employeeID, statusID,
                pictureName, "", amount, paymentTypeID, uploadStatus);
    }

    // ── DB read constructor (no pictureUrl column in old rows) ────────────────
    public Dispense(String dispenseID, String dispenseDate, int typeDispenseID,
                    String employeeID, int statusID,
                    double amount, int paymentTypeID, String pictureName) {
        this.dispenseID     = dispenseID;
        this.dispenseDate   = dispenseDate;
        this.typeDispenseID = typeDispenseID;
        this.employeeID     = employeeID;
        this.statusID       = statusID;
        this.amount         = amount;
        this.paymentTypeID  = paymentTypeID;
        this.pictureName    = pictureName;
    }

    // ── No-arg constructor (for Gson) ─────────────────────────────────────────
    public Dispense() { }

    // ── Getters / setters ─────────────────────────────────────────────────────

    public String getDispenseID()              { return dispenseID; }
    public void   setDispenseID(String v)      { this.dispenseID = v; }

    public String getDispenseDate()            { return dispenseDate; }
    public void   setDispenseDate(String v)    { this.dispenseDate = v; }

    public int    getTypeDispenseID()          { return typeDispenseID; }
    public void   setTypeDispenseID(int v)     { this.typeDispenseID = v; }

    public String getEmployeeID()              { return employeeID; }
    public void   setEmployeeID(String v)      { this.employeeID = v; }

    public int    getStatusID()                { return statusID; }
    public void   setStatusID(int v)           { this.statusID = v; }

    public double getAmount()                  { return amount; }
    public void   setAmount(double v)          { this.amount = v; }

    public int    getPaymentTypeID()           { return paymentTypeID; }
    public void   setPaymentTypeID(int v)      { this.paymentTypeID = v; }

    public String getPictureName()             { return pictureName; }
    public void   setPictureName(String v)     { this.pictureName = v; }

    public String getPictureUrl()              { return pictureUrl; }
    public void   setPictureUrl(String v)      { this.pictureUrl = v; }

    public int    getUploadStatus()            { return uploadStatus; }
    public void   setUploadStatus(int v)       { this.uploadStatus = v; }
}