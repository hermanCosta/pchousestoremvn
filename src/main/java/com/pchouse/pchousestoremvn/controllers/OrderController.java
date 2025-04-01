package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.OrderDAO;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import java.util.List;

public class OrderController {

    private final OrderDAO ORDER_DAO = new OrderDAO();

    // Retrieve the ID of the last order
    public long getLastOrderId() {
        return ORDER_DAO.getLastOrderIdDAO();
    }

    // Add a new order to the database
    public long addOrder(ServiceOrder pOrder) {
        return ORDER_DAO.addOrderDAO(pOrder);
    }

    // Get order details by ID
    public ServiceOrder getItemOrder(long pIdOrder) {
        return ORDER_DAO.getItemOrderDAO(pIdOrder);
    }

    // Retrieve all orders for a specific company
    public List<ServiceOrder> getAllOrders(Company pCompany) {
        return ORDER_DAO.getAllOrderDAO(pCompany);
    }

    // Search orders for a specific company based on a search term
    public List<ServiceOrder> searchOrder(Company pCompany, String pSearch) {
        return ORDER_DAO.searchOrderDAO(pCompany, pSearch);
    }

    // Update an existing order
    public boolean updateOrder(ServiceOrder pOrderModel) {
        return ORDER_DAO.updateOrderDAO(pOrderModel);
    }
}
