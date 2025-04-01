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
import java.io.Serializable;

@Entity
@Table(name = "SALE_PROD_SERV")
public class SaleProdServ implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SALE_PROD_SERV")
    private long idSaleProdServ;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_SALE", referencedColumnName = "ID_SALE")
    private Sale sale;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_PROD_SERV", referencedColumnName = "ID_PROD_SERV")
    private ProductService productService;

    @Column(name = "QTY", nullable = false)
    private int qty;

    @Column(name = "TOTAL", nullable = false)
    private double total;

    public SaleProdServ() {
    }

    public SaleProdServ(Sale sale, ProductService productService, int qty, double total) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (total < 0) {
            throw new IllegalArgumentException("Total cannot be negative.");
        }
        this.sale = sale;
        this.productService = productService;
        this.qty = qty;
        this.total = total;
    }

    public long getIdSaleProdServ() {
        return idSaleProdServ;
    }

    public void setIdSaleProdServ(long idSaleProdServ) {
        this.idSaleProdServ = idSaleProdServ;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.qty = qty;
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

    @Override
    public String toString() {
        return "SaleProdServ{" +
                "idSaleProdServ=" + idSaleProdServ +
                ", sale=" + sale +
                ", saleProdServ=" + productService +
                ", qty=" + qty +
                ", total=" + total +
                '}';
    }
}
