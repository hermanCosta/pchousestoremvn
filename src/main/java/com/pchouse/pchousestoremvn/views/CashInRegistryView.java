package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.controllers.CashInRegistryController;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.models.CashInRegistry;
import com.pchouse.pchousestoremvn.models.Employee;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class CashInRegistryView extends JInternalFrame {

    private DefaultTableModel dtmCashIn;
    private final CashInRegistryController cashInController;
    private final EmployeeController employeeController;

    private JDateChooser dateFrom, dateTo;
    private JTextField txtAmount, txtNote;
    private JButton btnAdd, btnSearch, btnClear;
    private JTable tableCashIn;

    public CashInRegistryView() {
        this.cashInController = new CashInRegistryController();
        this.employeeController = new EmployeeController();

        initComponents();
        CommonSetting.requestTxtFocus(txtAmount);
        CommonSetting.tableSettings(tableCashIn);
        dtmCashIn.setRowCount(0);
        loadCashInTable(null, null);
    }

    private void initComponents() {
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cash-In Registry");
        setPreferredSize(new Dimension(1050, 650));
        setLayout(new BorderLayout(10, 10));

        // Main panel with border styling
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEtchedBorder());

        //  Top: Filters & Input
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Transaction Filters & Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Amount:"), gbc);
        txtAmount = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        topPanel.add(txtAmount, gbc);

        // Note
        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(new JLabel("Note:"), gbc);
        txtNote = new JTextField();
        gbc.gridx = 1;
        topPanel.add(txtNote, gbc);

        // From Date
        gbc.gridx = 2;
        gbc.gridy = 0;
        topPanel.add(new JLabel("From Date:"), gbc);
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("yyyy-MM-dd");
        gbc.gridx = 3;
        topPanel.add(dateFrom, gbc);

        // To Date
        gbc.gridx = 2;
        gbc.gridy = 1;
        topPanel.add(new JLabel("To Date:"), gbc);
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("yyyy-MM-dd");
        gbc.gridx = 3;
        topPanel.add(dateTo, gbc);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        btnAdd = new JButton("Add", CommonExtension.loadIcon("/icons/icon_add.png"));
        btnAdd.setBackground(new Color(21, 76, 121));
        btnAdd.setForeground(Color.WHITE);
        buttonsPanel.add(btnAdd);

        btnSearch = new JButton("Search", CommonExtension.loadIcon("/icons/icon_search.png"));
        buttonsPanel.add(btnSearch);

        btnClear = new JButton("Clear", CommonExtension.loadIcon("/icons/icon_clear.png"));
        buttonsPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        topPanel.add(buttonsPanel, gbc);

        panel.add(topPanel, BorderLayout.NORTH);

        // Center: Table view
        dtmCashIn = new DefaultTableModel(new Object[]{"ID", "Amount", "Note", "Date"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableCashIn = new JTable(dtmCashIn);
        JScrollPane scrollPane = new JScrollPane(tableCashIn);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Cash-In Records"));
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);

        // Action listeners
        btnAdd.addActionListener(e -> addCashIn());
        btnSearch.addActionListener(e -> searchCashIns());
        btnClear.addActionListener(e -> clearAll());
    }

    private void loadCashInTable(Date from, Date to) {
        List<CashInRegistry> list = cashInController.getAllCashInByDateRange(CommonSetting.COMPANY, from, to);
        dtmCashIn.setRowCount(0); // clear existing rows

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (list != null && !list.isEmpty()) {
            for (CashInRegistry c : list) {
                dtmCashIn.addRow(new Object[]{
                    c.getIdCashInRegistry(),
                    c.getAmount(),
                    c.getNote(),
                    sdf.format(c.getTransactionDate())
                });
            }
        }
    }

    private void addCashIn() {
        Employee emp = authorizeUser();
        if (emp == null) {
            return;
        }

        String amt = txtAmount.getText().trim();
        if (amt.isEmpty()) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, getTitle(), JOptionPane.WARNING_MESSAGE);
            txtAmount.requestFocus();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amt);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount", getTitle(), JOptionPane.WARNING_MESSAGE);
            txtAmount.requestFocus();
            return;
        }

        CashInRegistry entry = new CashInRegistry();
        entry.setAmount(amount);
        entry.setNote(txtNote.getText().trim());
        entry.setTransactionDate(LocalDateTime.now());
        entry.setEmployee(emp);

        boolean ok = cashInController.addCashIn(entry);
        if (ok) {
            JOptionPane.showMessageDialog(this, CommonConstant.SUCCESS_SAVE, getTitle(), JOptionPane.INFORMATION_MESSAGE);
            clearAll();
        } else {
            JOptionPane.showMessageDialog(this, CommonConstant.ERROR_SAVE, getTitle(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private Employee authorizeUser() {
        String password = CommonExtension.requestUserPassword();
        Employee emp = employeeController.getEmployeeByPass(password);
        if (emp == null) {
            JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, getTitle(), JOptionPane.ERROR_MESSAGE);
        }
        return emp;
    }

    private void searchCashIns() {
        Date from = dateFrom.getDate();
        Date to = dateTo.getDate();
        if (from != null && to != null && from.after(to)) {
            JOptionPane.showMessageDialog(this, "'From Date' must be before 'To Date'", getTitle(), JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadCashInTable(from, to);
    }

    private void clearAll() {
        txtAmount.setText("");
        txtNote.setText("");
        dateFrom.setDate(null);
        dateTo.setDate(null);
        loadCashInTable(null, null);
    }
}
