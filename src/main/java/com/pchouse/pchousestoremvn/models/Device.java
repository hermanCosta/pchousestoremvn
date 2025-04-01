package com.pchouse.pchousestoremvn.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "DEVICE")
public class Device implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DEVICE")
    private long idDevice;

    @Column(name = "BRAND")
    private String brand;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    public Device() {
    }

    public Device(String brand, String model, String serialNumber) {
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
    }

    public long getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(long idDevice) {
        this.idDevice = idDevice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "Device{" + "idDevice=" + idDevice + ", brand='" + brand + '\'' + ", model='" + model + '\'' + ", serialNumber='" + serialNumber + '\'' + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Device device = (Device) obj;
        return idDevice == device.idDevice;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(idDevice);
    }
}
