package com.pchouse.pchousestoremvn.models;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "REFURB_SALE")
public class RefurbSale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SALE_REFURB")
    private long idSaleRefurb;

    @ManyToOne
    @JoinColumn(name = "ID_SALE", referencedColumnName = "ID_SALE", nullable = false)
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "ID_REFURB", referencedColumnName = "ID_REFURB", nullable = false)
    private Refurb refurb;

    @Column(name = "QTY", nullable = false)
    private int qty;

    @Column(name = "TOTAL", nullable = false)
    private double total;

    // === Constructors ===
    public RefurbSale() {
    }

    public RefurbSale(Sale sale, Refurb refurb, int qty, double total) {
        this.sale = sale;
        this.refurb = refurb;
        this.qty = qty;
        this.total = total;
    }

    // === Getters and Setters ===
    public long getIdSaleRefurb() {
        return idSaleRefurb;
    }

    public void setIdSaleRefurb(long idSaleRefurb) {
        this.idSaleRefurb = idSaleRefurb;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Refurb getRefurb() {
        return refurb;
    }

    public void setRefurb(Refurb refurb) {
        this.refurb = refurb;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    // Getters para o JasperReport
    public String getDescription() {
        return refurb.getCategory() + " | " + refurb.getBrand() + " - " + refurb.getModel();
    }

    public Double getUnitPrice() {
        return refurb != null ? refurb.getPrice() : 0.0;
    }

    // === Optional: toString ===
    @Override
    public String toString() {
        return "SaleRefurb{"
                + "idSaleRefurb=" + idSaleRefurb
                + ", sale=" + (sale != null ? sale.getIdSale() : null)
                + ", refurb=" + (refurb != null ? refurb.getIdRefurb() : null)
                + ", qty=" + qty
                + ", total=" + total
                + '}';
    }
}
