package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.RefurbDAO;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Refurb;
import java.util.Collections;
import java.util.List;

public class RefurbController {

    private final RefurbDAO refurbDAO = new RefurbDAO();

    public List<Refurb> getAllRefurbProducts(Company company) {
        return refurbDAO.getAllRefurbProdDAO(company);
    }

    public long addRefurbProduct(Refurb refurb) {
        try {
            return refurbDAO.addRefurbProductDAO(refurb);
        } catch (Exception e) {
            System.out.println("Error in controller: " + e.getMessage());
            e.printStackTrace(); // optional for debugging
            return 0;
        }
    }

    public Refurb getRefurbProductById(long id) {
        return refurbDAO.getItemRefurbProdDAO(id);
    }

    public boolean updateRefurbProduct(Refurb refurb) {
        try {
            return refurbDAO.updateRefurbProdDAO(refurb);
        } catch (Exception e) {
            System.out.println("Error in controller: " + e.getMessage());
            e.printStackTrace(); // Optional: remove or replace with logger in production
            return false;
        }
    }

    public boolean deleteRefurbProduct(Refurb refurb) {
        try {
            return refurbDAO.deleteRefurbProdDAO(refurb);
        } catch (Exception e) {
            System.out.println("Error in deleteRefurbProduct: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Refurb> searchRefurbProducts(String searchTerm) {
        try {
            return refurbDAO.searchRefurbDAO(searchTerm);
        } catch (Exception e) {
            System.out.println("Error in searchRefurbProducts: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Refurb> getRefurbProductsByCategory(Company company, String category) {
        try {
            return refurbDAO.getAllRefurbByCategoryDAO(company, category);
        } catch (Exception e) {
            System.out.println("Error in getRefurbProductsByCategory: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Refurb> searchRefurbByCategory(Company company, String category, String searchTerm) {
        try {
            return refurbDAO.searchRefurbByCategoryDAO(company, category, searchTerm);
        } catch (Exception e) {
            System.out.println("Error in searchRefurbByCategory: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Refurb> getCustomRefurbProducts(Company company) {
        try {
            return refurbDAO.getAllCustomRefurbProdDAO(company);
        } catch (Exception e) {
            System.out.println("Error in getCustomRefurbProducts: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Refurb> searchCustomRefurbProducts(Company company, String searchTerm) {
        try {
            return refurbDAO.searchCustomRefurbDAO(company, searchTerm);
        } catch (Exception e) {
            System.out.println("Error in searchCustomRefurbProducts: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
