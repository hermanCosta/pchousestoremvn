package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.controllers.ClosingTillController;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.PaymentSummary;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ClosingTillView extends JInternalFrame {

    private JDateChooser dateChooser;
    private JTable tableSalePayments, tableServicePayments;
    private DefaultTableModel dtmSale, dtmService;
    private JTextField txtTotalCash, txtTotalCard, txtTotalCombined;
    private JTextArea txtNotes;
    private JButton btnCloseTill, btnRefresh;

    private final ClosingTillController controller;
    private final EmployeeController employeeController;

    public ClosingTillView() {
        this.controller = new ClosingTillController();
        this.employeeController = new EmployeeController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Close Till of the Day");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1100, 700));

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === North: Date Picker ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Date:"));

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setDate(new Date());
        topPanel.add(dateChooser);

        btnRefresh = new JButton("Load", CommonExtension.loadIcon("/icons/icon_refresh.png"));
        topPanel.add(btnRefresh);
        wrapper.add(topPanel, BorderLayout.NORTH);

        // === Center: Tables ===
        dtmSale = new DefaultTableModel(new Object[]{"ID", "Type", "Method", "Cash", "Card", "Total", "Date"}, 0);
        tableSalePayments = new JTable(dtmSale);
        JScrollPane scrollSale = new JScrollPane(tableSalePayments);
        scrollSale.setBorder(BorderFactory.createTitledBorder("Sale Payments"));

        dtmService = new DefaultTableModel(new Object[]{"ID", "Type", "Method", "Cash", "Card", "Total", "Date"}, 0);
        tableServicePayments = new JTable(dtmService);
        JScrollPane scrollService = new JScrollPane(tableServicePayments);
        scrollService.setBorder(BorderFactory.createTitledBorder("Service Order Payments"));

        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        tablesPanel.add(scrollSale);
        tablesPanel.add(scrollService);

        wrapper.add(tablesPanel, BorderLayout.CENTER);

        // === South: Summary + Notes + Button ===
        JPanel summaryPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTotalCash = new JTextField(10);
        txtTotalCard = new JTextField(10);
        txtTotalCombined = new JTextField(10);
        txtNotes = new JTextArea(3, 50);

        txtTotalCash.setEditable(false);
        txtTotalCard.setEditable(false);
        txtTotalCombined.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 0; summaryPanel.add(new JLabel("Cash Total:"), gbc);
        gbc.gridx = 1; summaryPanel.add(txtTotalCash, gbc);
        gbc.gridx = 2; summaryPanel.add(new JLabel("Card Total:"), gbc);
        gbc.gridx = 3; summaryPanel.add(txtTotalCard, gbc);
        gbc.gridx = 4; summaryPanel.add(new JLabel("Combined Total:"), gbc);
        gbc.gridx = 5; summaryPanel.add(txtTotalCombined, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 6;
        summaryPanel.add(new JLabel("Closing Notes:"), gbc);

        gbc.gridy = 2;
        summaryPanel.add(new JScrollPane(txtNotes), gbc);

        btnCloseTill = new JButton("Close Till", CommonExtension.loadIcon("/icons/icon_save.png"));
        btnCloseTill.setBackground(new Color(0, 102, 0));
        btnCloseTill.setForeground(Color.WHITE);

        gbc.gridy = 3;
        summaryPanel.add(btnCloseTill, gbc);

        wrapper.add(summaryPanel, BorderLayout.SOUTH);
        add(wrapper);

        // === Event Listeners ===
        btnRefresh.addActionListener(e -> loadPayments());
        btnCloseTill.addActionListener(e -> closeTill());

        loadPayments();
    }

    private void loadPayments() {
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) return;

        dtmSale.setRowCount(0);
        dtmService.setRowCount(0);

        var data = controller.getPaymentDataByDate(selectedDate);

        double totalCash = 0, totalCard = 0;

        for (PaymentSummary p : data.getSales()) {
            dtmSale.addRow(new Object[]{
                p.getId(), p.getType(), p.getMethod(),
                p.getCash(), p.getCard(), p.getTotal(), p.getDate()
            });
            totalCash += p.getCash();
            totalCard += p.getCard();
        }

        for (PaymentSummary p : data.getServices()) {
            dtmService.addRow(new Object[]{
                p.getId(), p.getType(), p.getMethod(),
                p.getCash(), p.getCard(), p.getTotal(), p.getDate()
            });
            totalCash += p.getCash();
            totalCard += p.getCard();
        }

        txtTotalCash.setText(String.format("%.2f", totalCash));
        txtTotalCard.setText(String.format("%.2f", totalCard));
        txtTotalCombined.setText(String.format("%.2f", totalCash + totalCard));
    }

    private void closeTill() {
        Employee emp = authorizeUser();
        if (emp == null) return;

        Date closingDate = dateChooser.getDate();
        String notes = txtNotes.getText().trim();

        double totalCash = Double.parseDouble(txtTotalCash.getText());
        double totalCard = Double.parseDouble(txtTotalCard.getText());
        int totalTransactions = dtmSale.getRowCount() + dtmService.getRowCount();

        boolean success = controller.saveClosing(closingDate, totalCash, totalCard, totalTransactions, notes, emp);

        if (success) {
            JOptionPane.showMessageDialog(this, "Till closed successfully!", getTitle(), JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Closing failed. It might already be closed.", getTitle(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private Employee authorizeUser() {
        String password = CommonExtension.requestUserPassword();
        return employeeController.getEmployeeByPass(password);
    }
}

