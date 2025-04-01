package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.FaultDAO;
import com.pchouse.pchousestoremvn.models.Fault;
import java.util.List;

public class FaultController {

    private final FaultDAO FAULT_DAO = new FaultDAO();

    // Fetches all faults from the database
    public List<Fault> getAllFault() {
        return FAULT_DAO.getAllFaultDAO();
    }

    // Adds a new fault to the database
    public long addFault(Fault pFault) {
        return FAULT_DAO.addFaultDAO(pFault);
    }

    // Updates the details of an existing fault
    public boolean updateFault(Fault pFault) {
        return FAULT_DAO.updateFaultDAO(pFault);
    }

    // Deletes a fault from the database
    public boolean deleteFault(Fault pFault) {
        return FAULT_DAO.deleteFaultDAO(pFault);
    }

    // Searches for faults based on a search string
    public List<Fault> searchFault(String pSearch) {
        return FAULT_DAO.searchFaultDAO(pSearch);
    }

    // Retrieves a fault by its ID
    public Fault getItemFault(long pIdFault) {
        return FAULT_DAO.getItemFaultDAO(pIdFault);
    }

    // Searches for faults in a specific order based on a search string
    public List<Fault> searchOrderFaults(String pSearch) {
        return FAULT_DAO.orderSearchFaultDAO(pSearch);
    }

    // Checks if a fault exists based on a search string
    public long checkIfFaultExists(String pSearch) {
        return FAULT_DAO.checkExistFaultDAO(pSearch);
    }
}
