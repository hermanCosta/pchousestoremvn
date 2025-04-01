package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.OrderPaymentDAO;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;

public class OrderPaymentController {

    private final OrderPaymentDAO ORDER_PAYMENT_DAO = new OrderPaymentDAO();

    // Add a new order payment
    public long addOrderPayment(ServiceOrderPayment pOrderPayment) {
        return ORDER_PAYMENT_DAO.addOrderPaymentDAO(pOrderPayment);
    }
}
