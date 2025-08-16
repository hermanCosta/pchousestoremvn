package com.pchouse.pchousestoremvn.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    @JoinColumn(name = "ID_COMPANY", referencedColumnName = "ID_COMPANY")
    private Company company;

    @OneToOne
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "ID_SERVICE_ORDER", referencedColumnName = "ID_SERVICE_ORDER")
    private ServiceOrder serviceOrder;

    @ManyToOne
    @JoinColumn(name = "ID_SALE", referencedColumnName = "ID_SALE")
    private Sale sale;

    @Column(name = "AMOUNT", nullable = false)
    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_CREATED", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date dtCreated;

    public Refund() {
    }

    public Refund(Company company, Employee employee, Sale sale, double amount, Date dtCreated) {
        this.company = company;
        this.employee = employee;
        this.sale = sale;
        this.amount = amount;
        this.dtCreated = dtCreated;
    }

    public Refund(Company company, Employee employee, ServiceOrder serviceOrder, double amount, Date dtCreated) {
        this.company = company;
        this.employee = employee;
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

    public Company getCompany() {
        return company;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
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
        return "Refund{"
                + "idRefund=" + idRefund
                + ", serviceOrder=" + serviceOrder
                + ", amount=" + amount
                + ", dtCreated=" + dtCreated
                + '}';
    }
}
