package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.RefurbDAO;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Refurb;
import java.util.List;

public class RefurbController {

    private final RefurbDAO refurbDAO = new RefurbDAO();

    public List<Refurb> getAllRefurbProducts(Company company) {
        return refurbDAO.getAllRefurbProdDAO(company);
    }

    public long addRefurbProduct(Refurb refurb) {
        return refurbDAO.addRefurbProductDAO(refurb);
    }

    public Refurb getRefurbProductById(long id) {
        return refurbDAO.getItemRefurbProdDAO(id);
    }

    public boolean updateRefurbProduct(Refurb refurb) {
        return refurbDAO.updateRefurbProdDAO(refurb);
    }

    public boolean deleteRefurbProduct(Refurb refurb) {
        return refurbDAO.deleteRefurbProdDAO(refurb);
    }

    public List<Refurb> searchRefurbProducts(String searchTerm) {
        return refurbDAO.searchRefurbDAO(searchTerm);
    }

    public List<Refurb> getRefurbProductsByCategory(Company company, String category) {
        return refurbDAO.getAllRefurbByCategoryDAO(company, category);
    }

    public List<Refurb> searchRefurbByCategory(Company company, String category, String searchTerm) {
        return refurbDAO.searchRefurbByCategoryDAO(company, category, searchTerm);
    }

    public List<Refurb> getCustomRefurbProducts(Company company) {
        return refurbDAO.getAllCustomRefurbProdDAO(company);
    }

    public List<Refurb> searchCustomRefurbProducts(Company company, String searchTerm) {
        return refurbDAO.searchCustomRefurbDAO(company, searchTerm);
    }
}
