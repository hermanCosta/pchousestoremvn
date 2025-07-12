package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.OrderProdServDAO;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ServiceOrderProdServController {

    private final OrderProdServDAO ORDER_PROD_SERV_DAO = new OrderProdServDAO();

// Add a new order product/service
    public long addOrderProdServ(ServiceOrderProdServ pOrderProdServ) {
        try {
            return ORDER_PROD_SERV_DAO.addOrderProdServDAO(pOrderProdServ);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error while adding product/service to the order: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

// Get all products/services for a specific order
    public List<ServiceOrderProdServ> getOrderProdServ(ServiceOrder pOrder) {
        try {
            return ORDER_PROD_SERV_DAO.getOrderProdServDAO(pOrder);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error while retrieving products/services for the order: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

// Delete an order product/service
    public long deleteOrderProdServ(long pIdOrderProdServ) {
        try {
            return ORDER_PROD_SERV_DAO.deleteOrderProdServDAO(pIdOrderProdServ);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error while deleting product/service from the order: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

}
