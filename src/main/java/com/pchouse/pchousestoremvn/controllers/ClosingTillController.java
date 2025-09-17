package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.DailyClosingDAO;
import com.pchouse.pchousestoremvn.models.*;

import java.util.Date;

public class ClosingTillController {

    private final DailyClosingDAO DAILY_CLOSING_TILL = new DailyClosingDAO();

    // Save till closing
    public boolean saveClosing(Date date, double totalCash, double totalCard, int totalTransactions, String notes, Employee emp) {
        try {
            if (DAILY_CLOSING_TILL.isDateAlreadyClosedDAO(date)) {
                return false;
            }

            DailyClosing closing = new DailyClosing();
            closing.setEmployee(emp);
            closing.setCashTotal(totalCash);
            closing.setCardTotal(totalCard);
            closing.setTotalTransactions(totalTransactions);
            closing.setNote(notes);
            closing.setClosingDate(date);

            int id = DAILY_CLOSING_TILL.addDailyClosingDAO(closing);
            return id > 0;

        } catch (Exception e) {
            System.err.println("Error closing till: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
