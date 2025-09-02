package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.CashOutRegistryDAO;
import com.pchouse.pchousestoremvn.models.CashOutRegistry;
import com.pchouse.pchousestoremvn.models.Company;

import java.util.ArrayList;
import java.util.List;

public class CashOutRegistryController {

    private final CashOutRegistryDAO cashOutRegistryDAO = new CashOutRegistryDAO();

    /**
     * Retrieves all cash-out records for a specific company.
     */
    public List<CashOutRegistry> getAllCashOut(Company company) {
        try {
            return cashOutRegistryDAO.getAllCashOutByEmployee(company);
        } catch (Exception e) {
            System.err.println("Error retrieving all cash-outs: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Inserts a new cash-out record into the database.
     */
    public boolean addCashOut(CashOutRegistry cashOutRegistry) {
        try {
            return cashOutRegistryDAO.insertCashOut(cashOutRegistry);
        } catch (Exception e) {
            System.err.println("Error inserting cash-out: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
