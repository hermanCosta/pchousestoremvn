package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.CompanyDAO;
import com.pchouse.pchousestoremvn.models.Company;
import java.util.List;

public class CompanyController {
    private final CompanyDAO companyDAO = new CompanyDAO();

    // Retrieve all companies
    public List<Company> getAllCompanies() {
        try {
            return companyDAO.getAllCompaniesDAO();
        } catch (Exception e) {
            System.err.println("Error retrieving all companies: " + e.getMessage());
            return null;
        }
    }

    // Retrieve a specific company by name and password
    public Company getCompany(String name, String password) {
        try {
            return companyDAO.getCompanyDAO(name, password);
        } catch (Exception e) {
            System.err.println("Error retrieving company: " + e.getMessage());
            return null;
        }
    }
}
