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
@Table(name = "SERVICE_ORDER_PROD_SERV")
public class ServiceOrderProdServ implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SERVICE_ORDER_PROD_SERV")
    private long idServiceOrderProdServ;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_SERVICE_ORDER", referencedColumnName = "ID_SERVICE_ORDER")
    private ServiceOrder serviceOrder;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_PROD_SERV", referencedColumnName = "ID_PROD_SERV")
    private ProductService prodServ;

    @Column(name = "QTY")
    private int qty;

    @Column(name = "TOTAL")
    private Double total;

    public ServiceOrderProdServ() {}

    public ServiceOrderProdServ(ServiceOrder serviceOrder, ProductService prodServ, int qty, Double total) {
        this.serviceOrder = serviceOrder;
        this.prodServ = prodServ;
        this.qty = qty;
        this.total = total;
    }

    public long getIdServiceOrderProdServ() {
        return idServiceOrderProdServ;
    }

    public void setIdServiceOrderProdServ(long idServiceOrderProdServ) {
        this.idServiceOrderProdServ = idServiceOrderProdServ;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public ProductService getProdServ() {
        return prodServ;
    }

    public void setProdServ(ProductService prodServ) {
        this.prodServ = prodServ;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
