package com.pchouse.pchousestoremvn.models;

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

    @ManyToOne
    @JoinColumn(name = "ID_SERVICE_ORDER", referencedColumnName = "ID_SERVICE_ORDER", nullable = true)
    private ServiceOrder serviceOrder;

    @ManyToOne
    @JoinColumn(name = "ID_SALE", referencedColumnName = "ID_SALE", nullable = true)
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "ID_SERVICE_ORDER_PAYMENT", referencedColumnName = "ID_SERVICE_ORDER_PAYMENT", nullable = true)
    private ServiceOrderPayment serviceOrderPayment;

    @ManyToOne
    @JoinColumn(name = "ID_SALE_PAYMENT", referencedColumnName = "ID_SALE_PAYMENT", nullable = true)
    private SalePayment salePayment;

    @Column(name = "AMOUNT", nullable = false)
    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_CREATED", nullable = false)
    private Date created;

    public Deposit() {
    }

    // Constructor for ServiceOrder
    public Deposit(ServiceOrder serviceOrder, Employee employee, double amount, Date created) {
        this.serviceOrder = serviceOrder;
        this.employee = employee;
        this.amount = amount;
        this.created = created;
    }

    // Constructor for Sale
    public Deposit(Sale sale, Employee employee, double amount, Date created) {
        this.sale = sale;
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

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
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

    public SalePayment getSalePayment() {
        return salePayment;
    }

    public void setSalePayment(SalePayment salePayment) {
        this.salePayment = salePayment;
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
