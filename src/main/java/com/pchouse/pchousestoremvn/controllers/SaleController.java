package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.SaleDAO;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.SaleProdServ;
import java.util.ArrayList;
import java.util.List;

public class SaleController {

    private final SaleDAO SALE_DAO = new SaleDAO();

    // Add a new sale to the database
    public long addSale(Sale pSale) throws BusinessException {
        try {
            return SALE_DAO.addSaleDAO(pSale);
        } catch (Exception e) {
            System.err.println("Error adding sale: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("Unable to add sale.");
        }
    }

    // Get sale details by ID
    public Sale getItemSale(long pIdSale) {
        try {
            return SALE_DAO.getItemSaleDAO(pIdSale);
        } catch (Exception e) {
            System.err.println("Error retrieving sale by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve all sales for a specific company
    public List<Sale> getAllSales(Company pCompany) {
        try {
            return SALE_DAO.getAllSaleDAO(pCompany);
        } catch (Exception e) {
            System.err.println("Error retrieving all sales: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Search sales for a specific company based on a search term
    public List<Sale> searchSale(Company pCompany, String pSearch) {
        try {
            return SALE_DAO.searchSaleDAO(pCompany, pSearch);
        } catch (Exception e) {
            System.err.println("Error searching sales: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Update an existing sale
    public boolean updateSale(Sale pSaleModel) {
        try {
            return SALE_DAO.updateSaleDAO(pSaleModel);
        } catch (Exception e) {
            System.err.println("Error updating sale: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public long addOrderSale(
        Sale sale,
        List<SaleProdServ> items,
        List<SalePayment> payments,
        Deposit deposit,
        OrderNote note
    ) throws BusinessException {
        try {
            return SALE_DAO.addOrderSaleDAO(sale, items, payments, deposit, note);
        } catch (Exception e) {
            throw new BusinessException("Failed to add sale with payment", e);
        }
    }
}
