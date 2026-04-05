package com.example.myadermoshop;

import java.util.List;
import java.util.Map;

public class ClosingSummary {
    private String date;
    private double totalPurchasePrice;
    private double totalSalePrice;
    private Map<String, Double> salesByPaymentType;
    private List<CartItem> salesSummary;
    private List<Stock> stockSummary;

    public ClosingSummary(String date, double totalPurchasePrice, double totalSalePrice,
                          Map<String, Double> salesByPaymentType, List<CartItem> salesSummary,
                          List<Stock> stockSummary) {
        this.date = date;
        this.totalPurchasePrice = totalPurchasePrice;
        this.totalSalePrice = totalSalePrice;
        this.salesByPaymentType = salesByPaymentType;
        this.salesSummary = salesSummary;
        this.stockSummary = stockSummary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalPurchasePrice() {
        return totalPurchasePrice;
    }

    public void setTotalPurchasePrice(double totalPurchasePrice) {
        this.totalPurchasePrice = totalPurchasePrice;
    }

    public double getTotalSalePrice() {
        return totalSalePrice;
    }

    public void setTotalSalePrice(double totalSalePrice) {
        this.totalSalePrice = totalSalePrice;
    }

    public Map<String, Double> getSalesByPaymentType() {
        return salesByPaymentType;
    }

    public void setSalesByPaymentType(Map<String, Double> salesByPaymentType) {
        this.salesByPaymentType = salesByPaymentType;
    }

    public List<CartItem> getSalesSummary() {
        return salesSummary;
    }

    public void setSalesSummary(List<CartItem> salesSummary) {
        this.salesSummary = salesSummary;
    }

    public List<Stock> getStockSummary() {
        return stockSummary;
    }

    public void setStockSummary(List<Stock> stockSummary) {
        this.stockSummary = stockSummary;
    }
}
