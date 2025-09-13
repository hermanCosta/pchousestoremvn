package com.pchouse.pchousestoremvn.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "DAILY_CLOSING", uniqueConstraints = {
    @UniqueConstraint(columnNames = "CLOSING_DATE")
})
public class DailyClosing implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DAILY_CLOSING")
    private int idDailyClosing;

    @Temporal(TemporalType.DATE)
    @Column(name = "CLOSING_DATE", nullable = false)
    private Date closingDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CLOSING_DATETIME", nullable = false)
    private Date closingDateTime;

    @Column(name = "CASH_TOTAL", nullable = false)
    private double cashTotal;

    @Column(name = "CARD_TOTAL", nullable = false)
    private double cardTotal;

    @Column(name = "TOTAL_TRANSACTIONS", nullable = false)
    private int totalTransactions;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String note;

    @OneToOne
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE", nullable = false)
    private Employee employee;

    // === Constructors ===
    public DailyClosing() {
    }

    public DailyClosing(Date closingDate, Date closingDateTime, double cashTotal, double cardTotal,
            int totalTransactions, String notes, Employee employee) {
        this.closingDate = closingDate;
        this.closingDateTime = closingDateTime;
        this.cashTotal = cashTotal;
        this.cardTotal = cardTotal;
        this.totalTransactions = totalTransactions;
        this.note = notes;
        this.employee = employee;
    }

    // === Getters and Setters ===
    public int getIdDailyClosing() {
        return idDailyClosing;
    }

    public void setIdDailyClosing(int idDailyClosing) {
        this.idDailyClosing = idDailyClosing;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public Date getClosingDateTime() {
        return closingDateTime;
    }

    public void setClosingDateTime(Date closingDateTime) {
        this.closingDateTime = closingDateTime;
    }

    public double getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(double cashTotal) {
        this.cashTotal = cashTotal;
    }

    public double getCardTotal() {
        return cardTotal;
    }

    public void setCardTotal(double cardTotal) {
        this.cardTotal = cardTotal;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
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

    // === Utility Method ===
    public double getTotalAmount() {
        return this.cashTotal + this.cardTotal;
    }
}
