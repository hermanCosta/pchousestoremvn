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
@Table(name = "SERVICE_ORDER_NOTE")
public class ServiceOrderNote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SERVICE_ORDER_NOTE")
    private int idServiceOrderNote;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_SERVICE_ORDER", referencedColumnName = "ID_SERVICE_ORDER")
    private ServiceOrder serviceOrder;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE")
    private Employee employee;

    @Column(name = "NOTE", length = 1000)
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    public ServiceOrderNote() {
    }

    public ServiceOrderNote(ServiceOrder serviceOrder, Employee employee, String note, Date created) {
        this.serviceOrder = serviceOrder;
        this.employee = employee;
        this.note = note;
        this.created = created;
    }

    public int getIdServiceOrderNote() {
        return idServiceOrderNote;
    }

    public void setIdServiceOrderNote(int idServiceOrderNote) {
        this.idServiceOrderNote = idServiceOrderNote;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "ServiceOrderNote{" +
                "idServiceOrderNote=" + idServiceOrderNote +
                ", serviceOrder=" + serviceOrder +
                ", employee=" + employee +
                ", note='" + note + '\'' +
                ", created=" + created +
                '}';
    }
}
