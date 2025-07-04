package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.SaleProdServDAO;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SaleProdServ;
import java.util.List;

public class SaleProdServController {

    private final SaleProdServDAO SALE_PROD_SERV_DAO = new SaleProdServDAO();

    // Add a new sale product/service
    public long addSaleProdServ(SaleProdServ pSaleProdServ) {
        return SALE_PROD_SERV_DAO.addSaleProdServDAO(pSaleProdServ);
    }

    // Get all products/services for a specific sale
    public List<SaleProdServ> getSaleProdServ(Sale pSale) {
        return SALE_PROD_SERV_DAO.getSaleProdServDAO(pSale);
    }

    // Delete an sale product/service
    public long deleteSaleProdServ(long pIdSaleProdServ) {
        return SALE_PROD_SERV_DAO.deleteSaleProdServDAO(pIdSaleProdServ);
    }
}
