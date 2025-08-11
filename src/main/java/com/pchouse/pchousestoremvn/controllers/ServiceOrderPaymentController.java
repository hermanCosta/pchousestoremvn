package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.OrderPaymentDAO;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrderPaymentController {

    private final OrderPaymentDAO ORDER_PAYMENT_DAO = new OrderPaymentDAO();

    // Add a new order payment
    public long addOrderPayment(ServiceOrderPayment pOrderPayment) {

        try {
            return ORDER_PAYMENT_DAO.addOrderPaymentDAO(pOrderPayment);
        } catch (Exception e) {
            System.err.println("Error in controller while adding service order payment: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Get all payments for a specific sale
    public List<ServiceOrderPayment> getServiceOrderPayments(ServiceOrder pServiceOrder) {
        try {
            return ORDER_PAYMENT_DAO.getServiceOrderPaymentDAO(pServiceOrder);
        } catch (Exception e) {
            System.err.println("Error in controller while fetching sale payments: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
