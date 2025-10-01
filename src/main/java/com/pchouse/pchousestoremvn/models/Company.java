package com.pchouse.pchousestoremvn.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "COMPANY")
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COMPANY")
    private long idCompany;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "CONTACT_ONE", nullable = false)
    private String contactOne;

    @Column(name = "CONTACT_TWO")
    private String contactTwo;

    @Column(name = "EMAIL", unique = true, nullable = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    public Company() {}

    public Company(long idCompany, String name, String address, String contactOne, String contactTwo, String email, String password) {
        this.idCompany = idCompany;
        this.name = name;
        this.address = address;
        this.contactOne = contactOne;
        this.contactTwo = contactTwo;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(long idCompany) {
        this.idCompany = idCompany;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactOne() {
        return contactOne;
    }

    public void setContactOne(String contactOne) {
        this.contactOne = contactOne;
    }

    public String getContactTwo() {
        return contactTwo;
    }

    public void setContactTwo(String contactTwo) {
        this.contactTwo = contactTwo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Company{" +
                "idCompany=" + idCompany +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactOne='" + contactOne + '\'' +
                ", contactTwo='" + contactTwo + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
