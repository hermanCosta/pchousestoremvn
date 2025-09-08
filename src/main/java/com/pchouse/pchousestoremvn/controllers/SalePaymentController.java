package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.SalePaymentDAO;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import java.util.ArrayList;
import java.util.List;

public class SalePaymentController {

    private final SalePaymentDAO SALE_PAYMENT_DAO = new SalePaymentDAO();

    // Add a new sale payment
    public long addSalePayment(SalePayment pSalePayment) {
        try {
            return SALE_PAYMENT_DAO.addSalePaymentDAO(pSalePayment);
        } catch (Exception e) {
            System.err.println("Error in controller while adding sale payment: " + e.getMessage());
            e.printStackTrace();
            return 0; // return 0 to indicate failure
        }
    }

    // Get all payments for a specific sale
    public List<SalePayment> getSalePayments(Sale pSale) {
        try {
            return SALE_PAYMENT_DAO.getSalePaymentDAO(pSale);
        } catch (Exception e) {
            System.err.println("Error in controller while fetching sale payments: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // Add a new refurb sale payment
    public long addRefurbSalePayment(List<SalePayment> pSalePayments, OrderNote pOrderNote) throws BusinessException {
        try {
            return SALE_PAYMENT_DAO.addRefurbSalePaymentDAO(pSalePayments, pOrderNote);
        } catch (Exception e) {
            throw new BusinessException("Error in controller while adding service refurb sale payment: " + e.getMessage());
        }
    }
}
