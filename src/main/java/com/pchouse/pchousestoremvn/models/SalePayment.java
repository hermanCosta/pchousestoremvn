package com.pchouse.pchousestoremvn.models;

import java.io.Serializable;
import java.util.Date;
import com.pchouse.pchousestoremvn.enums.PayMethod;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "SALE_PAYMENT")
public class SalePayment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SALE_PAYMENT")
    private long idSalePayment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_SALE", referencedColumnName = "ID_SALE")
    private Sale sale;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_METHOD")
    private PayMethod payMethod; 

    @Column(name = "AMOUNT_DUE")
    private double amountDue;

    @Column(name = "AMOUNT_PAID")
    private double amountPaid;

    @Column(name = "CHANGE_AMOUNT")
    private double changeAmount;

    @Temporal(TemporalType.DATE)
    @Column(name = "DT_TRANSACTION")
    private Date dtTransaction;

    public SalePayment() {
    }

    public SalePayment(Sale sale, PayMethod payMethod, double amountDue, double amountPaid, double changeAmount, Date dtTransaction) {
        this.sale = sale;
        this.payMethod = payMethod;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.changeAmount = changeAmount;
        this.dtTransaction = dtTransaction;
    }

    // Getters and setters

    public long getIdSalePayment() {
        return idSalePayment;
    }

    public void setIdSalePayment(long idSalePayment) {
        this.idSalePayment = idSalePayment;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PayMethod payMethod) {
        this.payMethod = payMethod;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Date getDtTransaction() {
        return dtTransaction;
    }

    public void setDtTransaction(Date dtTransaction) {
        this.dtTransaction = dtTransaction;
    }
}
