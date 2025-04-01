package com.pchouse.pchousestoremvn.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "REFUND")
public class Refund implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REFUND")
    private int idRefund;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_SERVICE_ORDER", referencedColumnName = "ID_SERVICE_ORDER")
    private ServiceOrder serviceOrder;

    @Column(name = "AMOUNT", nullable = false)
    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_CREATED", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date dtCreated;

    public Refund() {
    }

    public Refund(ServiceOrder serviceOrder, double amount, Date dtCreated) {
        this.serviceOrder = serviceOrder;
        this.amount = amount;
        this.dtCreated = dtCreated;
    }

    public int getIdRefund() {
        return idRefund;
    }

    public void setIdRefund(int idRefund) {
        this.idRefund = idRefund;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        this.amount = amount;
    }

    public Date getDtCreated() {
        return dtCreated;
    }

    public void setDtCreated(Date dtCreated) {
        this.dtCreated = dtCreated;
    }

    @Override
    public String toString() {
        return "Refund{" +
                "idRefund=" + idRefund +
                ", serviceOrder=" + serviceOrder +
                ", amount=" + amount +
                ", dtCreated=" + dtCreated +
                '}';
    }
}
