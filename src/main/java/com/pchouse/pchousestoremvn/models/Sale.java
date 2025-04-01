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
@Table(name = "SALE")
public class Sale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SALE")
    private long idSale;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_CUSTOMER", referencedColumnName = "ID_CUSTOMER")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE")
    private Employee employee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_COMPANY", referencedColumnName = "ID_COMPANY")
    private Company company;

    @Column(name = "TOTAL", nullable = false)
    private double total;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Column(name = "CASH")
    private Double cash;

    @Column(name = "CARD")
    private Double card;

    @Column(name = "AMOUNT_PAID", insertable = false, updatable = false)
    private double amountPaid;

    @Column(name = "STATUS", nullable = false)
    private String status;

    public Sale() {
    }

    public Sale(Customer customer, Employee employee, Company company, double total, Date created, Double cash,
                Double card, String status) {
        this.customer = customer;
        this.employee = employee;
        this.company = company;
        this.total = total;
        this.created = created;
        this.cash = cash;
        this.card = card;
        this.status = status;
        this.amountPaid = (cash != null ? cash : 0) + (card != null ? card : 0);
    }

    public long getIdSale() {
        return idSale;
    }

    public void setIdSale(long idSale) {
        this.idSale = idSale;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("Total cannot be negative.");
        }
        this.total = total;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        if (cash != null && cash < 0) {
            throw new IllegalArgumentException("Cash cannot be negative.");
        }
        this.cash = cash;
    }

    public Double getCard() {
        return card;
    }

    public void setCard(Double card) {
        if (card != null && card < 0) {
            throw new IllegalArgumentException("Card payment cannot be negative.");
        }
        this.card = card;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "idSale=" + idSale +
                ", customer=" + customer +
                ", employee=" + employee +
                ", company=" + company +
                ", total=" + total +
                ", created=" + created +
                ", cash=" + cash +
                ", card=" + card +
                ", amountPaid=" + amountPaid +
                ", status='" + status + '\'' +
                '}';
    }
}
