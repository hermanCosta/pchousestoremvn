package com.pchouse.pchousestoremvn.models;

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
@Table(name = "SERVICE_ORDER_FAULT")
public class ServiceOrderFault implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SERVICE_ORDER_FAULT")
    private long idServiceOrderFault;

    @ManyToOne
    @JoinColumn(name = "ID_SERVICE_ORDER", referencedColumnName = "ID_SERVICE_ORDER")
    private ServiceOrder serviceOrder;

    @ManyToOne
    @JoinColumn(name = "ID_FAULT", referencedColumnName = "ID_FAULT")
    private Fault fault;

    public ServiceOrderFault() {
    }

    public ServiceOrderFault(ServiceOrder serviceOrder, Fault fault) {
        this.serviceOrder = serviceOrder;
        this.fault = fault;
    }

    public long getIdServiceOrderFault() {
        return idServiceOrderFault;
    }

    public void setIdServiceOrderFault(long idServiceOrderFault) {
        this.idServiceOrderFault = idServiceOrderFault;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public Fault getFault() {
        return fault;
    }

    public void setFault(Fault fault) {
        this.fault = fault;
    }

    @Override
    public String toString() {
        return "ServiceOrderFault{"
                + "idServiceOrderFault=" + idServiceOrderFault
                + ", serviceOrder=" + serviceOrder
                + ", fault=" + fault
                + '}';
    }
}
