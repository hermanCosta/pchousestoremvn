package com.pchouse.pchousestoremvn.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "CASH_OUT_REGISTRY")
public class CashOutRegistry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CASH_OUT_REGISTRY")
    private long idCashOutRegistry;

    @Column(name = "AMOUNT", nullable = false)
    private double amount;

    @Column(name = "NOTE", nullable = false, length = 300)
    private String note;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE")
    private Employee employee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_COMPANY", referencedColumnName = "ID_COMPANY")
    private Company company;

    @Column(name = "DT_TRANSACTION", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dtTransaction;

    public CashOutRegistry() {
        // Default constructor
    }

    public CashOutRegistry(double amount, String note, Employee employee, Company company, LocalDateTime dtTransaction) {
        this.amount = amount;
        this.note = note;
        this.employee = employee;
        this.company = company;
        this.dtTransaction = dtTransaction;
    }

    public long getIdCashOutRegistry() {
        return idCashOutRegistry;
    }

    public void setIdCashOutRegistry(long idCashOutRegistry) {
        this.idCashOutRegistry = idCashOutRegistry;
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

    public LocalDateTime getDtTransaction() {
        return dtTransaction;
    }

    public void setDtTransaction(LocalDateTime dtTransaction) {
        this.dtTransaction = dtTransaction;
    }

    @Override
    public String toString() {
        return "CashOutRegistry{"
                + "idCashOutRegistry=" + idCashOutRegistry
                + ", amount=" + amount
                + ", note='" + note + '\''
                + ", employee=" + (employee != null ? employee.getIdEmployee() : null)
                + ", transactionDate=" + dtTransaction
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CashOutRegistry)) {
            return false;
        }
        CashOutRegistry that = (CashOutRegistry) o;
        return idCashOutRegistry == that.idCashOutRegistry;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(idCashOutRegistry);
    }
}
