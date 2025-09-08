package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.SaleProdServDAO;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.RefurbSale;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SaleProdServ;
import java.util.ArrayList;

import java.util.List;

public class SaleProdServController {

    private final SaleProdServDAO SALE_PROD_SERV_DAO = new SaleProdServDAO();

    // Add a new sale product/service
    public long addSaleProdServ(SaleProdServ pSaleProdServ) throws BusinessException {
        try {
            return SALE_PROD_SERV_DAO.addSaleProdServDAO(pSaleProdServ);
        } catch (Exception e) {
            throw new BusinessException("Error in controller while adding sale product/service: " + e.getMessage(), e);
        }
    }

    // Get all products/services for a specific sale
    public List<SaleProdServ> getSaleProdServ(Sale pSale) {
        try {
            return SALE_PROD_SERV_DAO.getSaleProdServDAO(pSale);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Delete a sale product/service
    public long deleteSaleProdServ(long pIdSaleProdServ) {
        try {
            return SALE_PROD_SERV_DAO.deleteSaleProdServDAO(pIdSaleProdServ);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Get refurb sales for a specific sale
    public List<RefurbSale> getRefurbSale(Sale pSale) {
        try {
            return SALE_PROD_SERV_DAO.getRefurbSaleDAO(pSale);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
