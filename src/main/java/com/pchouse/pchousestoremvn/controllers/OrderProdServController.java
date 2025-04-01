package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.OrderProdServDAO;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import java.util.List;

public class OrderProdServController {

    private final OrderProdServDAO ORDER_PROD_SERV_DAO = new OrderProdServDAO();

    // Add a new order product/service
    public long addOrderProdServ(ServiceOrderProdServ pOrderProdServ) {
        return ORDER_PROD_SERV_DAO.addOrderProdServDAO(pOrderProdServ);
    }

    // Get all products/services for a specific order
    public List<ServiceOrderProdServ> getOrderProdServ(ServiceOrder pOrder) {
        return ORDER_PROD_SERV_DAO.getOrderProdServDAO(pOrder);
    }

    // Delete an order product/service
    public long deleteOrderProdServ(long pIdOrderProdServ) {
        return ORDER_PROD_SERV_DAO.deleteOrderProdServDAO(pIdOrderProdServ);
    }
}
