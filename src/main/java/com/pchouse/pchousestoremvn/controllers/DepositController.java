package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.DepositDAO;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import java.util.List;

public class DepositController {

    private final DepositDAO DEPOSIT_DAO = new DepositDAO();

    // Adiciona um novo depósito e retorna o ID gerado
    public long addDeposit(Deposit pDeposit) {
        return DEPOSIT_DAO.addDepositDAO(pDeposit);
    }

    // Retorna a lista de depósitos associados a um pedido específico
    public List<Deposit> getOrderDeposit(ServiceOrder pOrder) {
        return DEPOSIT_DAO.getOrderDepositDAO(pOrder);
    }
}
