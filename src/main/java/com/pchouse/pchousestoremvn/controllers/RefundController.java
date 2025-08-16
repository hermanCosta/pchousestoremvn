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
    public long addRefund(Refund pRefund, OrderNote pOrderNote) throws BusinessException {
        try {
            return REFUND_DAO.addRefundDAO(pRefund, pOrderNote);

        } catch (Exception e) {
            System.err.println("Error adding refund: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("Unable to add refund.");
        }
    }
}
