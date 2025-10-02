package com.pchouse.pchousestoremvn.models;

public class PaymentSummary {
    private long orderNumber;
    private String paymentTypeStr;
    private double cashAmount;
    private double cardAmount;
    private String transactionDate; // formatted string

    public PaymentSummary(long orderNumber, String paymentTypeStr, double cashAmount, double cardAmount, String transactionDate) {
        this.orderNumber = orderNumber;
        this.paymentTypeStr = paymentTypeStr;
        this.cashAmount = cashAmount;
        this.cardAmount = cardAmount;
        this.transactionDate = transactionDate;
    }

    // Getters
    public long getOrderNumber() { return orderNumber; }
    public String getPaymentTypeStr() { return paymentTypeStr; }
    public double getCashAmount() { return cashAmount; }
    public double getCardAmount() { return cardAmount; }
    public String getTransactionDate() { return transactionDate; }
}

