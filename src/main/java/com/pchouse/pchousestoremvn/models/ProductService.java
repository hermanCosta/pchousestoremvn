package com.pchouse.pchousestoremvn.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "PROD_SERV")
public class ProductService implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROD_SERV")
    private long idProductService;
    
    @Column(name = "NAME")
    private String prodServName;
    
    @Column(name = "QTY")
    private int qty;
        
    @Column(name = "PRICE")
    private double price;
    
    @Column(name = "MIN_QTY")
    private int minQty;
    
    @Column(name = "CATEGORY")
    private String category;
    
    @Column(name = "NOTE")
    private String note;
        
    @OneToOne
    @JoinColumn(name = "ID_COMPANY", referencedColumnName = "ID_COMPANY")
    private Company company;

    public ProductService() {}

    public ProductService(String prodServName, int qty, double price, int minQty, String category, String note, Company company) {
        this.prodServName = prodServName;
        this.qty = qty;
        this.price = price;
        this.minQty = minQty;
        this.category = category;
        this.note = note;
        this.company = company;
    }

    public long getIdProductService() {
        return idProductService;
    }

    public void setIdProductService(long idProductService) {
        this.idProductService = idProductService;
    }

    public String getProdServName() {
        return prodServName;
    }

    public void setProdServName(String prodServName) {
        this.prodServName = prodServName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        if (qty >= 0) {
            this.qty = qty;
        } else {
            throw new IllegalArgumentException("Qty cannot be negative.");
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
    }

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        if (minQty >= 0) {
            this.minQty = minQty;
        } else {
            throw new IllegalArgumentException("Minimum qty cannot be negative.");
        }
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return prodServName; 
    }
    
//    @Override
//    public String toString() {
//        return "ProductService{" +
//                "idProductService=" + idProductService +
//                ", prodServName='" + prodServName + '\'' +
//                ", price=" + price + " €" +  // Adicionando o símbolo do euro
//                '}';
//    }
}
