package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.OrderNoteDAO;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.OrderNote;
import java.util.List;

public class OrderNoteController {

    private final OrderNoteDAO ORDER_NOTE_DAO = new OrderNoteDAO();

    // Add a new order note
    public long addOrderNote(OrderNote pOrderNote) {
        return ORDER_NOTE_DAO.addOrderNoteDAO(pOrderNote);
    }

    // Retrieve all order notes for a specific order
    public List<OrderNote> getOrderNotes(ServiceOrder pOrder) {
        return ORDER_NOTE_DAO.getAllOrderNoteDAO(pOrder);
    }

    // Check if an order note already exists for a specific order
    public Long checkExistingOrderNote(String pNote, ServiceOrder pOrder) {
        return ORDER_NOTE_DAO.checkExistingOrderNoteDAO(pNote, pOrder);
    }

    // Search for order notes by a specific term
    public List<OrderNote> searchOrderNotes(ServiceOrder pOrder, String pSearch) {
        return ORDER_NOTE_DAO.searchOrderNoteDAO(pOrder, pSearch);
    }
}
