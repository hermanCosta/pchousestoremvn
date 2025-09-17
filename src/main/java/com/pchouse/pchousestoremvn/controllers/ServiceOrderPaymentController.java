package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.ServiceOrderPaymentDAO;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceOrderPaymentController {

    private final ServiceOrderPaymentDAO ORDER_PAYMENT_DAO = new ServiceOrderPaymentDAO();

    // Add a new order payment
    public long addOrderPayment(List<ServiceOrderPayment> pOrderPayments, OrderNote pOrderNote) throws BusinessException {
        try {
            return ORDER_PAYMENT_DAO.addOrderPaymentDAO(pOrderPayments, pOrderNote);
        } catch (Exception e) {
            throw new BusinessException("Error in controller while adding service order payment: " + e.getMessage());
        }
    }

    // Get all payments for a specific service order
    public List<ServiceOrderPayment> getServiceOrderPayments(ServiceOrder pServiceOrder) {
        try {
            return ORDER_PAYMENT_DAO.getServiceOrderPaymentDAO(pServiceOrder);
        } catch (Exception e) {
            System.err.println("Error in controller while fetching sale payments: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all payments for a specific date
    public List<ServiceOrderPayment> getServiceOrderPaymentsByDate(Date date) {
        try {
            return ORDER_PAYMENT_DAO.getAllByDate(date);
        } catch (Exception e) {
            System.err.println("Error in controller while fetching sale payments: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
