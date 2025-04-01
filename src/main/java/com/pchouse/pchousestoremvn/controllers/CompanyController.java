package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.CompanyDAO;
import com.pchouse.pchousestoremvn.models.Company;
import java.util.List;

public class CompanyController {
    private final CompanyDAO companyDAO = new CompanyDAO();

    // Retrieve all companies
    public List<Company> getAllCompanies() {
        return companyDAO.getAllCompaniesDAO();
    }

    // Retrieve a specific company by name and password
    public Company getCompany(String name, String password) {
        return companyDAO.getCompanyDAO(name, password);
    }
}
