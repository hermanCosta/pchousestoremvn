/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.RefundDAO;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.Refund;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import java.util.ArrayList;
import java.util.List;

public class RefundController {

    private final RefundDAO REFUND_DAO = new RefundDAO();

    // Retrieve all refunds for a specific company
    public List<Refund> getAllRefunds(Company pCompany) {
        try {
            return REFUND_DAO.getAllRefundDAO(pCompany);
        } catch (Exception e) {
            System.err.println("Error retrieving all refunds: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Add a new refund to the database
    public long addServiceOrderRefund(Refund pRefund, OrderNote pOrderNote, List<ServiceOrderPayment> originalPayments) throws BusinessException {
        try {
            return REFUND_DAO.addServiceOrderRefundDAO(pRefund, pOrderNote, originalPayments);

        } catch (Exception e) {
            System.err.println("Error adding Order refund: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("Unable to add refund.");
        }
    }
    
    public long addSaleRefund(Refund pRefund, OrderNote pOrderNote, List<SalePayment> originalPayments) throws BusinessException {
        try {
            return REFUND_DAO.addSaleRefundDAO(pRefund, pOrderNote, originalPayments);

        } catch (Exception e) {
            System.err.println("Error adding Sale refund: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("Unable to add refund.");
        }
    }
}
