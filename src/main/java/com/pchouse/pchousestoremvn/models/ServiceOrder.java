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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SERVICE_ORDER")
public class ServiceOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SERVICE_ORDER")
    private long idServiceOrder;
    
    @OneToOne
    @JoinColumn(name = "ID_CUSTOMER", referencedColumnName = "ID_CUSTOMER")
    private Customer customer;
    
    @OneToOne
    @JoinColumn(name = "ID_DEVICE", referencedColumnName = "ID_DEVICE")
    private Device device;
    
    @OneToOne
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE")
    private Employee employee;
    
    @OneToOne
    @JoinColumn(name = "ID_COMPANY", referencedColumnName = "ID_COMPANY")
    private Company company;
    
    @Column(name = "TOTAL")
    private double total;
    
    @Column(name = "DUE")
    private double due;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private OrderStatus status;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED")
    private Date created;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FINISHED")
    private Date finished;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "PICKED")
    private Date picked;
    
    @Column(name = "BAD_SECTOR")
    private int badSector;

    @Column(name = "NOTE")
    private String note;
    
    public ServiceOrder() {
    }

    public ServiceOrder(Customer customer, Device device, Employee employee, Company company, double total, double due, OrderStatus status, Date created, Date finished, Date picked, int badSector, String note) {
        this.customer = customer;
        this.device = device;
        this.employee = employee;
        this.company = company;
        this.total = total;
        this.due = due;
        this.status = status;
        this.created = created;
        this.finished = finished;
        this.picked = picked;
        this.badSector = badSector;
        this.note = note;
    }

    // Getters and setters

    public long getIdServiceOrder() {
        return idServiceOrder;
    }

    public void setIdServiceOrder(long idServiceOrder) {
        this.idServiceOrder = idServiceOrder;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
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
        this.total = total;
    }

    public double getDue() {
        return due;
    }

    public void setDue(double due) {
        this.due = due;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public Date getPicked() {
        return picked;
    }

    public void setPicked(Date picked) {
        this.picked = picked;
    }

    public int getBadSector() {
        return badSector;
    }

    public void setBadSector(int badSector) {
        this.badSector = badSector;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
