package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.SalePaymentDAO;
import com.pchouse.pchousestoremvn.models.SalePayment;

public class SalePaymentController {

    private final SalePaymentDAO SALE_PAYMENT_DAO = new SalePaymentDAO();

    // Add a new order payment
    public long addOrderPayment(SalePayment pOrderPayment) {
        return SALE_PAYMENT_DAO.addSalePaymentDAO(pOrderPayment);
    }
}
