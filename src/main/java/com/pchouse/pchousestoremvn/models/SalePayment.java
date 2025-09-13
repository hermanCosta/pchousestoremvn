package com.pchouse.pchousestoremvn.models;

import java.io.Serializable;
import java.util.Date;
import com.pchouse.pchousestoremvn.enums.PayMethod;
import com.pchouse.pchousestoremvn.enums.PaymentType;
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
import org.hibernate.annotations.Check;

@Check(constraints = "PAYMENT_TYPE = 'REFUND' OR (AMOUNT_DUE >= 0 AND AMOUNT_PAID >= 0)")
@Entity
@Table(name = "SALE_PAYMENT")
public class SalePayment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SALE_PAYMENT")
    private long idSalePayment;

    @OneToOne
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE")
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "ID_SALE", referencedColumnName = "ID_SALE")
    private Sale sale;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_TYPE")
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_METHOD")
    private PayMethod payMethod;

    @Column(name = "AMOUNT_DUE")
    private double amountDue;

    @Column(name = "AMOUNT_PAID")
    private double amountPaid;

    @Column(name = "CARD_AMOUNT")
    private Double cardAmount;

    @Column(name = "CASH_AMOUNT")
    private Double cashAmount;

    @Column(name = "CHANGE_AMOUNT")
    private double changeAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_TRANSACTION", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date dtTransaction;

    public SalePayment() {
    }

    public SalePayment(Employee employee, Sale sale, PaymentType paymentType, PayMethod payMethod, double amountDue, double amountPaid, Double cardAmount, Double cashAmount, double changeAmount, Date dtTransaction) {
        this.employee = employee;
        this.sale = sale;
        this.paymentType = paymentType;
        this.payMethod = payMethod;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.cardAmount = cardAmount;
        this.cashAmount = cashAmount;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
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

    public Double getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(Double cardAmount) {
        this.cardAmount = cardAmount;
    }

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Double cashAmount) {
        this.cashAmount = cashAmount;
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
