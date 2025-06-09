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
@Table(name = "DEPOSIT")
public class Deposit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DEPOSIT")
    private long idDeposit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_SERVICE_ORDER", referencedColumnName = "ID_SERVICE_ORDER", nullable = false)
    private ServiceOrder serviceOrder;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE", nullable = false)
    private Employee employee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_SERVICE_ORDER_PAYMENT", referencedColumnName = "ID_SERVICE_ORDER_PAYMENT", nullable = true)
    private ServiceOrderPayment serviceOrderPayment;

    @Column(name = "AMOUNT", nullable = false)
    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_CREATED", nullable = false)
    private Date created;

    public Deposit() {
    }

    public Deposit(ServiceOrder serviceOrder, Employee employee, double amount, Date created) {
        this.serviceOrder = serviceOrder;
        this.employee = employee;
        this.amount = amount;
        this.created = created;
    }

    public long getIdDeposit() {
        return idDeposit;
    }

    public void setIdDeposit(long idDeposit) {
        this.idDeposit = idDeposit;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ServiceOrderPayment getServiceOrderPayment() {
        return serviceOrderPayment;
    }

    public void setServiceOrderPayment(ServiceOrderPayment serviceOrderPayment) {
        this.serviceOrderPayment = serviceOrderPayment;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
