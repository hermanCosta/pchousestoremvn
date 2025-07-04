package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.SaleDAO;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Sale;
import java.util.List;

public class SaleController {

    private final SaleDAO SALE_DAO = new SaleDAO();

    // Retrieve the ID of the last sale
    public long getLastSaleId() {
        return SALE_DAO.getLastSaleIdDAO();
    }

    // Add a new sale to the database
    public long addSale(Sale pSale) throws BusinessException {
        return SALE_DAO.addSaleDAO(pSale);
    }

    // Get sale details by ID
    public Sale getItemSale(long pIdSale) {
        return SALE_DAO.getItemSaleDAO(pIdSale);
    }

    // Retrieve all sales for a specific company
    public List<Sale> getAllSales(Company pCompany) {
        return SALE_DAO.getAllSaleDAO(pCompany);
    }

    // Search sales for a specific company based on a search term
    public List<Sale> searchSale(Company pCompany, String pSearch) {
        return SALE_DAO.searchSaleDAO(pCompany, pSearch);
    }

    // Update an existing sale
    public boolean updateSale(Sale pSaleModel) {
        return SALE_DAO.updateSaleDAO(pSaleModel);
    }
}
