package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.OrderDAO;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrderController {

    private final OrderDAO ORDER_DAO = new OrderDAO();

    // Retrieve the ID of the last order
    public long getLastOrderId() {
        try {
            return ORDER_DAO.getLastOrderIdDAO();
        } catch (Exception e) {
            System.err.println("Error retrieving last order ID: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

// Add a new order to the database
    public long addOrder(ServiceOrder pOrder) throws BusinessException {
        try {
            return ORDER_DAO.addOrderDAO(pOrder);
        } catch (Exception e) {
            System.err.println("Error adding order: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("Unable to add order.");
        }
    }

// Get order details by ID
    public ServiceOrder getItemOrder(long pIdOrder) {
        try {
            return ORDER_DAO.getItemOrderDAO(pIdOrder);
        } catch (Exception e) {
            System.err.println("Error retrieving order by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

// Retrieve all orders for a specific company
    public List<ServiceOrder> getAllOrders(Company pCompany) {
        try {
            return ORDER_DAO.getAllOrderDAO(pCompany);
        } catch (Exception e) {
            System.err.println("Error retrieving all orders: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

// Search orders for a specific company based on a search term
    public List<ServiceOrder> searchOrder(Company pCompany, String pSearch) {
        try {
            return ORDER_DAO.searchOrderDAO(pCompany, pSearch);
        } catch (Exception e) {
            System.err.println("Error searching orders: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

// Update an existing order
    public boolean updateOrder(ServiceOrder pOrderModel) {
        try {
            return ORDER_DAO.updateOrderDAO(pOrderModel);
        } catch (Exception e) {
            System.err.println("Error updating order: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
