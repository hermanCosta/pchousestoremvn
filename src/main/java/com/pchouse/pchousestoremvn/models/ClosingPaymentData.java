package com.pchouse.pchousestoremvn.models;

import java.util.ArrayList;
import java.util.List;

public class ClosingPaymentData {
    private List<PaymentSummary> sales = new ArrayList<>();
    private List<PaymentSummary> services = new ArrayList<>();

    public List<PaymentSummary> getSales() {
        return sales;
    }

    public void setSales(List<PaymentSummary> sales) {
        this.sales = sales;
    }

    public List<PaymentSummary> getServices() {
        return services;
    }

    public void setServices(List<PaymentSummary> services) {
        this.services = services;
    }
}
