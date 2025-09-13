package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.DailyClosingDAO;
import com.pchouse.pchousestoremvn.dao.SalePaymentDAO;
import com.pchouse.pchousestoremvn.dao.ServiceOrderPaymentDAO;
import com.pchouse.pchousestoremvn.models.*;

import java.util.Date;
import java.util.List;

public class ClosingTillController {

    private final SalePaymentDAO salePaymentDAO = new SalePaymentDAO();
    private final ServiceOrderPaymentDAO servicePaymentDAO = new ServiceOrderPaymentDAO();
    private final DailyClosingDAO dailyClosingDAO = new DailyClosingDAO();

    // Load payment data grouped into DTOs
    public ClosingPaymentData getPaymentDataByDate(Date date) {
        ClosingPaymentData data = new ClosingPaymentData();

        try {
            List<SalePayment> sales = salePaymentDAO.getAllByDate(date);
            for (SalePayment s : sales) {
                data.getSales().add(new PaymentSummary(
                    s.getIdSalePayment(),
                    s.getPaymentType(),
                    s.getPayMethod(),
                    s.getCashAmount() != null ? s.getCashAmount() : 0,
                    s.getCardAmount() != null ? s.getCardAmount() : 0,
                    s.getDtTransaction()
                ));
            }

            List<ServiceOrderPayment> services = servicePaymentDAO.getAllByDate(date);
            for (ServiceOrderPayment s : services) {
                data.getServices().add(new PaymentSummary(
                    s.getIdServiceOrderPayment(),
                    s.getPaymentType(),
                    s.getPayMethod(),
                    s.getCashAmount() != null ? s.getCashAmount() : 0,
                    s.getCardAmount() != null ? s.getCardAmount() : 0,
                    s.getDtTransaction()
                ));
            }

        } catch (Exception e) {
            System.err.println("Error loading payment data: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    // Save till closing
    public boolean saveClosing(Date date, double totalCash, double totalCard, int totalTransactions, String notes, Employee emp) {
        try {
            if (dailyClosingDAO.isDateAlreadyClosedDAO(date)) {
                return false;
            }

            DailyClosing closing = new DailyClosing();
            closing.setEmployee(emp);
            closing.setCashTotal(totalCash);
            closing.setCardTotal(totalCard);
            closing.setTotalTransactions(totalTransactions);
            closing.setNote(notes);
            closing.setClosingDate(date);

            int id = dailyClosingDAO.addDailyClosingDAO(closing);
            return id > 0;

        } catch (Exception e) {
            System.err.println("Error closing till: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
