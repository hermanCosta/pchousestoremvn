package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.OrderFaultDAO;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderFault;
import java.util.List;

public class ServiceOrderFaultController {

    private final OrderFaultDAO ORDER_FAULT_DAO = new OrderFaultDAO();

    // Add a new order fault
    public long addOrderFault(ServiceOrderFault pOrderFault) {
        return ORDER_FAULT_DAO.addOrderFaultDAO(pOrderFault);
    }

    // Retrieve order faults for a specific order
    public List<ServiceOrderFault> getOrderFaults(ServiceOrder pOrder) {
        return ORDER_FAULT_DAO.getOrderFaultDAO(pOrder);
    }

    // Update an existing order fault
    public boolean updateOrderFault(ServiceOrderFault pOrderFault) {
        return ORDER_FAULT_DAO.updateOrderFaultDAO(pOrderFault);
    }

    // Delete an order fault by ID
    public long deleteOrderFault(long pIdOrderFault) {
        return ORDER_FAULT_DAO.deleteOrderFaultDAO(pIdOrderFault);
    }
}
