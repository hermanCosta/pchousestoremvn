package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.DepositDAO;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DepositController {

    private final DepositDAO DEPOSIT_DAO = new DepositDAO();

// Adds a new deposit and returns the generated ID
    public long addDeposit(Deposit pDeposit) {
        try {
            return DEPOSIT_DAO.addDepositDAO(pDeposit);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error while adding deposit: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

// Returns the list of deposits associated with a specific service order
    public List<Deposit> getOrderDeposit(ServiceOrder pOrder) {
        try {
            return DEPOSIT_DAO.getOrderDepositDAO(pOrder);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error while retrieving deposits for the service order: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

// Returns the list of deposits associated with a specific sale
    public List<Deposit> getOrderDeposit(Sale pSale) {
        try {
            return DEPOSIT_DAO.getSaleDepositDAO(pSale);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error while retrieving deposits for the sale: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

}
