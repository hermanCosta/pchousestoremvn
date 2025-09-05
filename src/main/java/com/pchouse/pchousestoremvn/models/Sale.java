package com.pchouse.pchousestoremvn.models;

import com.pchouse.pchousestoremvn.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @ManyToOne
    @JoinColumn(name = "ID_CUSTOMER", referencedColumnName = "ID_CUSTOMER")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "ID_COMPANY", referencedColumnName = "ID_COMPANY")
    private Company company;

    @Column(name = "TOTAL", nullable = false)
    private double total;

    @Column(name = "REMAINING")
    private Double remaining;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private OrderStatus status;

    @Column(name = "IMPORTANT_NOTES")
    private String importantNotes;

    public Sale() {
    }

    public Sale(Customer customer, Employee employee, Company company, double total, Double remaining, Date created, OrderStatus status) {
        this.customer = customer;
        this.employee = employee;
        this.company = company;
        this.total = total;
        this.remaining = remaining;
        this.created = created;
        this.status = status;
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

    public Double getRemaining() {
        return remaining;
    }

    public void setRemaining(Double remaining) {
        if (remaining != null && remaining < 0) {
            throw new IllegalArgumentException("Remaining cannot be negative.");
        }
        this.remaining = remaining;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getImportantNotes() {
        return importantNotes;
    }

    public void setImportantNotes(String importantNotes) {
        this.importantNotes = importantNotes;
    }

    @Override
    public String toString() {
        return "Sale{"
                + "idSale=" + idSale
                + ", customer=" + customer
                + ", employee=" + employee
                + ", company=" + company
                + ", total=" + total
                + ", created=" + created
                + ", remaining=" + remaining
                + ", status='" + status + '\''
                + '}';
    }
}
