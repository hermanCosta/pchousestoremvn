package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.ServiceOrderDAO;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderFault;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrderController {

    private final ServiceOrderDAO ORDER_DAO = new ServiceOrderDAO();

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
    
// Get order details by ID
    public ServiceOrder getServiceOrderById(long pIdOrder) {
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
    public boolean updateServiceOrderStatus(ServiceOrder pOrderModel) {
        try {
            return ORDER_DAO.updateServiceOrderStatusDAO(pOrderModel);
        } catch (Exception e) {
            System.err.println("Error updating order: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public long addServiceOrder(ServiceOrder order,
            List<ServiceOrderFault> faults,
            List<ServiceOrderProdServ> prodServs,
            List<ServiceOrderPayment> payment,
            Deposit deposit,
            OrderNote orderNote) throws BusinessException {

        try {
            return ORDER_DAO.addServiceOrderDAO(order, faults, prodServs, payment, deposit, orderNote);
        } catch (Exception e) {
            System.err.println("Error adding order: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("Unable to add order.");
        }
    }

    public boolean updateServiceOrder(
            ServiceOrder order,
            List<ServiceOrderFault> faults,
            List<ServiceOrderProdServ> prodServs,
            List<ServiceOrderPayment> payments,
            Deposit deposit,
            OrderNote orderNote) throws BusinessException {
        try {
            return ORDER_DAO.updateServiceOrderDAO(order, faults, prodServs, payments, deposit, orderNote);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException("Error updating order: " + e.getMessage());
        }
    }
}
