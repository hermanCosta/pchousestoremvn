package com.pchouse.pchousestoremvn.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "CASH_IN_REGISTRY")
public class CashInRegistry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CASH_IN_REGISTRY")
    private long idCashInRegistry;

    @Column(name = "AMOUNT")
    private double amount;

    @Column(name = "NOTE", nullable = false, length = 300)
    private String note;

    @ManyToOne()
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE")
    private Employee employee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_COMPANY", referencedColumnName = "ID_COMPANY")
    private Company company;
    
    @Column(name = "DT_TRANSACTION", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime transactionDate;

    public CashInRegistry() {
        // Default constructor
    }

    public CashInRegistry(double amount, String note, Employee employee,Company company,  LocalDateTime transactionDate) {
        this.amount = amount;
        this.note = note;
        this.employee = employee;
        this.company = company;
        this.transactionDate = transactionDate;
    }

    public long getIdCashInRegistry() {
        return idCashInRegistry;
    }

    public void setIdCashInRegistry(long idCashInRegistry) {
        this.idCashInRegistry = idCashInRegistry;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "CashInRegistry{" +
                "idCashInRegistry=" + idCashInRegistry +
                ", amount=" + amount +
                ", note='" + note + '\'' +
                ", employee=" + (employee != null ? employee.getIdEmployee() : null) +
                ", transactionDate=" + transactionDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CashInRegistry)) return false;
        CashInRegistry that = (CashInRegistry) o;
        return idCashInRegistry == that.idCashInRegistry;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(idCashInRegistry);
    }
}
