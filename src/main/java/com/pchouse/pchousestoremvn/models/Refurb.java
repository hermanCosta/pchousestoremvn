package com.pchouse.pchousestoremvn.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "REFURB")
public class Refurb implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REFURB")
    private long idRefurb;

    @Column(name = "CATEGORY", nullable = false, length = 45)
    private String category;

    @Column(name = "BRAND", nullable = false, length = 100)
    private String brand;

    @Column(name = "MODEL", nullable = false, length = 150)
    private String model;

    @Column(name = "PRICE", nullable = false)
    private double price;

    @Column(name = "QTY", nullable = false)
    private int qty = 1;

    @Column(name = "SERIAL_NUMBER", unique = true, length = 100)
    private String serialNumber;

    @Column(name = "NOTE", length = 400)
    private String note;

    @Column(name = "SCREEN", length = 150)
    private String screen;

    @Column(name = "PROCESSOR", length = 150)
    private String processor;

    @Column(name = "RAM_MEMORY", length = 150)
    private String ramMemory;

    @Column(name = "STORAGE", length = 150)
    private String storage;

    @Column(name = "GPU_BOARD", length = 150)
    private String gpuBoard;

    @Column(name = "BATTERY_HEALTH", length = 45)
    private String batteryHealth;

    @Column(name = "CUSTOM_1", length = 100)
    private String custom1;

    @Column(name = "CUSTOM_2", length = 100)
    private String custom2;

    @Column(name = "CUSTOM_3", length = 100)
    private String custom3;

    @Column(name = "CUSTOM_4", length = 100)
    private String custom4;

    @Column(name = "CUSTOM_5", length = 100)
    private String custom5;

    @Column(name = "CUSTOM_6", length = 100)
    private String custom6;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPANY", nullable = false)
    private Company company;

    public Refurb() {}

    public Refurb(String category, String brand, String model, double price, int qty, String serialNumber,
                  String note, String screen, String processor, String ramMemory, String storage,
                  String gpuBoard, String batteryHealth, String custom1, String custom2, String custom3,
                  String custom4, String custom5, String custom6, Company company) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (qty < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.qty = qty;
        this.serialNumber = serialNumber;
        this.note = note;
        this.screen = screen;
        this.processor = processor;
        this.ramMemory = ramMemory;
        this.storage = storage;
        this.gpuBoard = gpuBoard;
        this.batteryHealth = batteryHealth;
        this.custom1 = custom1;
        this.custom2 = custom2;
        this.custom3 = custom3;
        this.custom4 = custom4;
        this.custom5 = custom5;
        this.custom6 = custom6;
        this.company = company;
    }

    // Getters e Setters

    public long getIdRefurb() {
        return idRefurb;
    }

    public void setIdRefurb(long idRefurb) {
        this.idRefurb = idRefurb;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getRamMemory() {
        return ramMemory;
    }

    public void setRamMemory(String ramMemory) {
        this.ramMemory = ramMemory;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getGpuBoard() {
        return gpuBoard;
    }

    public void setGpuBoard(String gpuBoard) {
        this.gpuBoard = gpuBoard;
    }

    public String getBatteryHealth() {
        return batteryHealth;
    }

    public void setBatteryHealth(String batteryHealth) {
        this.batteryHealth = batteryHealth;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getCustom4() {
        return custom4;
    }

    public void setCustom4(String custom4) {
        this.custom4 = custom4;
    }

    public String getCustom5() {
        return custom5;
    }

    public void setCustom5(String custom5) {
        this.custom5 = custom5;
    }

    public String getCustom6() {
        return custom6;
    }

    public void setCustom6(String custom6) {
        this.custom6 = custom6;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    
}
