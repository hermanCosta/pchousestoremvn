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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CashInRegistryViewTeste extends JInternalFrame {

    private DefaultTableModel dtmCashIn;
    private final CashInRegistryController cashInController;
    private final EmployeeController employeeController;

    private JDateChooser dateFrom, dateTo;
    private JTextField txtAmount, txtNote;
    private JButton btnAdd, btnSearch, btnClear;
    private JTable tableCashIn;

    public CashInRegistryViewTeste() {
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
        setLayout(new BorderLayout());

        // === Main Wrapper Panel with 6px padding ===
        JPanel wrapperPanel = new JPanel(new BorderLayout(10, 10));
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // === Table Setup ===
        dtmCashIn = new DefaultTableModel(new Object[]{"ID", "Amount", "Notes", "Date", "User"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableCashIn = new JTable(dtmCashIn);
        JScrollPane scrollPane = new JScrollPane(tableCashIn);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        scrollPane.setPreferredSize(new Dimension(1000, 300));
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);

        // === Input + Buttons Panel ===
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEtchedBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1: Amount | From Date
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Amount"), gbc);
        txtAmount = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(txtAmount, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("From Date"), gbc);
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateFrom.getDateEditor().getUiComponent().setForeground(Color.WHITE);

        gbc.gridx = 3;
        inputPanel.add(dateFrom, gbc);

        // Row 2: Notes | To Date
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Notes"), gbc);
        txtNote = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(txtNote, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("To Date"), gbc);
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        dateTo.getDateEditor().getUiComponent().setForeground(Color.WHITE);
        gbc.gridx = 3;
        inputPanel.add(dateTo, gbc);

        // === Buttons inside the input panel, left-aligned ===
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonsPanel.setBorder(BorderFactory.createEtchedBorder());

        btnAdd = new JButton("Add", CommonExtension.loadIcon("/icons/icon_add.png"));
        btnSearch = new JButton("Search", CommonExtension.loadIcon("/icons/icon_search.png"));
        btnClear = new JButton("Clear", CommonExtension.loadIcon("/icons/icon_clear.png"));
        JButton btnPrint = new JButton("Print", CommonExtension.loadIcon("/icons/icon_print.png"));

        Color btnColor = new Color(21, 76, 121);
        Color txtColor = Color.WHITE;
        for (JButton btn : new JButton[]{btnAdd, btnSearch, btnClear, btnPrint}) {
            btn.setBackground(btnColor);
            btn.setForeground(txtColor);
            buttonsPanel.add(btn);
        }

        inputPanel.add(buttonsPanel, gbc);

        // === Bottom Area Panel ===
        JPanel bottomAreaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomAreaPanel.setBorder(BorderFactory.createEtchedBorder());
        bottomAreaPanel.add(inputPanel);

        // Add to wrapper
        wrapperPanel.add(bottomAreaPanel, BorderLayout.SOUTH);

        // Add wrapper to frame
        add(wrapperPanel, BorderLayout.CENTER);

        // === Listeners ===
        btnAdd.addActionListener(e -> addCashIn());
        btnSearch.addActionListener(e -> searchCashIns());
        btnClear.addActionListener(e -> clearAll());

        // Table settings
        CommonSetting.tableSettings(tableCashIn);
        resizeTableColumns();

        // Load data
        Date today = new Date();
        loadCashInTable(CommonSetting.getStartOfDay(today), CommonSetting.getEndOfDay(today));
    }

    private void loadCashInTable(Date from, Date to) {
        // Validate inputs
        if (from == null || to == null) {
            JOptionPane.showMessageDialog(this, "Both 'from' and 'to' dates must be provided.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate range: max 6 months
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(from);

        Calendar calTo = Calendar.getInstance();
        calTo.setTime(to);

        calFrom.add(Calendar.MONTH, 6);
        if (calFrom.before(calTo)) {
            JOptionPane.showMessageDialog(this, "Search period cannot exceed 6 months.", "Invalid Date Range", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Normalize to start/end of day
        from = CommonSetting.getStartOfDay(from);
        to = CommonSetting.getEndOfDay(to);

        List<CashInRegistry> list = cashInController.getAllCashInByDateRange(CommonSetting.COMPANY, from, to);
        dtmCashIn.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // Fixed pattern (no double /dd)

        if (list != null && !list.isEmpty()) {
            for (CashInRegistry c : list) {
                dtmCashIn.addRow(new Object[]{
                    c.getIdCashInRegistry(),
                    c.getAmount(),
                    c.getNote(),
                    sdf.format(c.getTransactionDate()),
                    c.getEmployee() != null ? c.getEmployee().getUsername() : ""
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
        entry.setCompany(CommonSetting.COMPANY);

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

    private void resizeTableColumns() {
        if (tableCashIn.getColumnModel().getColumnCount() < 5) {
            return;
        }

        // Hide ID column (column 0)
        tableCashIn.getColumnModel().getColumn(0).setMinWidth(0);
        tableCashIn.getColumnModel().getColumn(0).setMaxWidth(0);
        tableCashIn.getColumnModel().getColumn(0).setPreferredWidth(0);

        // Amount column
        tableCashIn.getColumnModel().getColumn(1).setPreferredWidth(100);

        // Notes column - wider
        tableCashIn.getColumnModel().getColumn(2).setPreferredWidth(350);

        // Date column
        tableCashIn.getColumnModel().getColumn(3).setPreferredWidth(180);

        // User column
        tableCashIn.getColumnModel().getColumn(4).setPreferredWidth(150);
    }
}
