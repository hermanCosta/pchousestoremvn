package com.pchouse.pchousestoremvn.models;

import com.pchouse.pchousestoremvn.enums.PayMethod;
import com.pchouse.pchousestoremvn.enums.PaymentType;

import java.util.Date;

public class PaymentSummary {
    private long id;
    private PaymentType type;
    private PayMethod method;
    private double cash;
    private double card;
    private double total;
    private Date date;

    public PaymentSummary(long id, PaymentType type, PayMethod method, double cash, double card, Date date) {
        this.id = id;
        this.type = type;
        this.method = method;
        this.cash = cash;
        this.card = card;
        this.total = cash + card;
        this.date = date;
    }

    public long getId() { return id; }
    public PaymentType getType() { return type; }
    public PayMethod getMethod() { return method; }
    public double getCash() { return cash; }
    public double getCard() { return card; }
    public double getTotal() { return total; }
    public Date getDate() { return date; }
}
