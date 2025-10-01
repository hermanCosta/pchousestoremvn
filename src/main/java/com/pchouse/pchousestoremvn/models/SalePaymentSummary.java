package com.pchouse.pchousestoremvn.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SalePaymentSummary {

    private SalePayment salePayment;
    private String paymentType;
    private LocalDate date;
    private double totalAmountDue;
    private double totalAmountPaid;
    private double totalCash;
    private double totalCard;

    public SalePaymentSummary(SalePayment salePayment, String paymentType, Date date, double totalDue, double totalPaid, double totalCash, double totalCard) {
        this.salePayment = salePayment;
        this.paymentType = paymentType;
        this.date = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.totalAmountDue = totalDue;
        this.totalAmountPaid = totalPaid;
        this.totalCash = totalCash;
        this.totalCard = totalCard;
    }

    // Getters
    public SalePayment getSalePayment() {
        return salePayment;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getTotalAmountDue() {
        return totalAmountDue;
    }

    public double getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public double getTotalCash() {
        return totalCash;
    }

    public double getTotalCard() {
        return totalCard;
    }
}
