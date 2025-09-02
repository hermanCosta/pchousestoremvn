/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.CashInRegistryDAO;
import com.pchouse.pchousestoremvn.models.CashInRegistry;
import com.pchouse.pchousestoremvn.models.Company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CashInRegistryController {

    private final CashInRegistryDAO cashInRegistryDAO = new CashInRegistryDAO();

    // Retrieve all cash-in records for a specific company
    public List<CashInRegistry> getAllCashIn(Company company) {
        try {
            return cashInRegistryDAO.getAllCashInByEmployee(company);
        } catch (Exception e) {
            System.err.println("Error retrieving all cash-ins: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Add a new cash-in record
    public boolean addCashIn(CashInRegistry cashInRegistry) {
        try {
            return cashInRegistryDAO.insertCashIn(cashInRegistry);
        } catch (Exception e) {
            System.err.println("Error inserting cash-in: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<CashInRegistry> getAllCashInByDateRange(Company currentCompany, Date from, Date to) {
        try {
            return cashInRegistryDAO.getAllCashInByDateRange(currentCompany, from, to);
        } catch (Exception ex) {
            // You can log this exception or display an error dialog depending on the app type
            System.err.println("Error fetching cash-in records by date range: " + ex.getMessage());
            ex.printStackTrace();
            return Collections.emptyList(); // Return empty list on failure
        }
    }
}
