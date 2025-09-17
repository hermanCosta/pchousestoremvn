package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.ClosingTillController;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.controllers.SalePaymentController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderPaymentController;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.views.modals.CloseTillSummaryDialog;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ClosingTillView extends JInternalFrame {

    private JDateChooser dateChooser;
    private JTable tableSalePayments, tableServicePayments;
    private DefaultTableModel dtmSale, dtmService;
    private JTextField txtTotalCash, txtTotalCard;
    private JTextArea txtNotes;
    private JButton btnCloseTill, btnRefresh, btnPrint;

    private final ClosingTillController _closingTillController;
    private final EmployeeController _employeeController;
    private final SalePaymentController _salePaymentController;
    private final ServiceOrderPaymentController _serviceOrderPaymentController;

    private double cachedTotalCash = 0;
    private double cachedTotalCard = 0;
    private double cachedTotalCombined = 0;

    public ClosingTillView() {
        this._closingTillController = new ClosingTillController();
        this._employeeController = new EmployeeController();
        this._salePaymentController = new SalePaymentController();
        this._serviceOrderPaymentController = new ServiceOrderPaymentController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Close Till of the Day");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1100, 700));

        // === Outer Wrapper Panel with Etched Border ===
        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setBorder(BorderFactory.createEtchedBorder());

        // === Center: Tables ===
        dtmSale = new DefaultTableModel(new Object[]{"Sale No.", "Type", "Cash", "Card", "Date"}, 0);
        tableSalePayments = new JTable(dtmSale);
        JScrollPane scrollSale = new JScrollPane(tableSalePayments);
        scrollSale.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sale Payments"));

        dtmService = new DefaultTableModel(new Object[]{"Order No.", "Type", "Cash", "Card", "Date"}, 0);
        tableServicePayments = new JTable(dtmService);
        JScrollPane scrollService = new JScrollPane(tableServicePayments);
        scrollService.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Service Order Payments"));

        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        tablesPanel.add(scrollSale);
        tablesPanel.add(scrollService);

        wrapper.add(tablesPanel, BorderLayout.CENTER);

        add(wrapper);

        // === Table Settings ===
        CommonSetting.tableSettings(tableSalePayments);
        CommonSetting.tableSettings(tableServicePayments);

        resizeTableColumns(tableSalePayments);
        resizeTableColumns(tableServicePayments);

        // === North: Date Picker ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEtchedBorder());

        topPanel.add(new JLabel("Select Date:"));

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setDate(new Date());
        topPanel.add(dateChooser);

        // Load Button
        btnRefresh = new JButton("Load", CommonExtension.loadIcon("/icons/icon_search.png"));
        btnRefresh.setBackground(new Color(21, 76, 121));
        btnRefresh.setForeground(Color.WHITE);
        topPanel.add(btnRefresh);

        // Print  Button
        btnPrint = new JButton("Print", CommonExtension.loadIcon("/icons/icon_print.png"));
        btnPrint.setBackground(new Color(21, 76, 121));
        btnPrint.setForeground(Color.WHITE);
        topPanel.add(btnPrint);

        // Close Till  Button
        btnCloseTill = new JButton("Close Till", CommonExtension.loadIcon("/icons/icon_till_records.png"));
        btnCloseTill.setBackground(new Color(21, 76, 121));
        btnCloseTill.setForeground(Color.WHITE);
        topPanel.add(btnCloseTill);

        // === Listeners ===        
        btnRefresh.addActionListener(e -> loadPayments());
        btnCloseTill.addActionListener(e -> showSummaryDialog());

        wrapper.add(topPanel, BorderLayout.NORTH);

        loadPayments();
    }

    private void resizeTableColumns(JTable table) {
        if (table.getColumnModel().getColumnCount() < 6) {
            return;
        }

        // Sale/Order No.
        table.getColumnModel().getColumn(0).setPreferredWidth(80);

        // Type
        table.getColumnModel().getColumn(1).setPreferredWidth(120);

        // Cash
        table.getColumnModel().getColumn(2).setPreferredWidth(100);

        // Card
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Date
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
    }

    private void loadPayments() {
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            return;
        }

        dtmSale.setRowCount(0);
        dtmService.setRowCount(0);

        double totalCash = 0;
        double totalCard = 0;

        // ---- Handle Sale Payments ----
        List<SalePayment> salePayments = _salePaymentController.getSalePaymentsByDate(selectedDate);
        if (salePayments != null && !salePayments.isEmpty()) {
            // Group by: "saleId|paymentType"
            Map<String, double[]> groupedSales = new HashMap<>();

            for (SalePayment sp : salePayments) {
                String key = sp.getSale().getIdSale() + "|" + sp.getPaymentType().name();

                double[] totals = groupedSales.getOrDefault(key, new double[2]); // [cash, card]
                totals[0] += sp.getCashAmount() != null ? sp.getCashAmount() : 0;
                totals[1] += sp.getCardAmount() != null ? sp.getCardAmount() : 0;

                groupedSales.put(key, totals);
            }

            // Add grouped rows to sale table
            for (Map.Entry<String, double[]> entry : groupedSales.entrySet()) {
                String[] parts = entry.getKey().split("\\|");
                String saleId = parts[0];
                String paymentType = parts[1];
                double[] totals = entry.getValue();

                dtmSale.addRow(new Object[]{
                    saleId,
                    paymentType,
                    totals[0], // Cash
                    totals[1], // Card
                    CommonExtension.formatDateTime(selectedDate)
                });

                // Add to overall totals
                totalCash += totals[0];
                totalCard += totals[1];
            }
        }

        // ---- Handle Service Order Payments ----
        List<ServiceOrderPayment> serviceOrderPayments = _serviceOrderPaymentController.getServiceOrderPaymentsByDate(selectedDate);
        if (serviceOrderPayments != null && !serviceOrderPayments.isEmpty()) {
            // Group by: "serviceOrderId|paymentType"
            Map<String, double[]> groupedServiceOrders = new HashMap<>();

            for (ServiceOrderPayment sop : serviceOrderPayments) {
                String key = sop.getServiceOrder().getIdServiceOrder() + "|" + sop.getPaymentType().name();

                double[] totals = groupedServiceOrders.getOrDefault(key, new double[2]); // [cash, card]
                totals[0] += sop.getCashAmount() != null ? sop.getCashAmount() : 0;
                totals[1] += sop.getCardAmount() != null ? sop.getCardAmount() : 0;

                groupedServiceOrders.put(key, totals);
            }

            // Add grouped rows to service order table
            for (Map.Entry<String, double[]> entry : groupedServiceOrders.entrySet()) {
                String[] parts = entry.getKey().split("\\|");
                String serviceOrderId = parts[0];
                String paymentType = parts[1];
                double[] totals = entry.getValue();

                dtmService.addRow(new Object[]{
                    serviceOrderId,
                    paymentType,
                    totals[0], // Cash
                    totals[1], // Card
                    CommonExtension.formatDateTime(selectedDate)
                });

                // Add to overall totals
                totalCash += totals[0];
                totalCard += totals[1];
            }
        }
        
        // ---- Final Totals ----
        this.cachedTotalCash = totalCash;
        this.cachedTotalCard = totalCard;
        this.cachedTotalCombined = totalCash + totalCard; 

        // Optionally update UI labels here if you have them:
        // labelCashTotal.setText("Cash: " + totalCash);
        // labelCardTotal.setText("Card: " + totalCard);
        // labelTotal.setText("Total: " + (totalCash + totalCard));
    }

    private void closeTill() {
        Employee emp = authorizeUser();
        if (emp == null) {
            return;
        }

        Date closingDate = dateChooser.getDate();
        String notes = txtNotes.getText().trim();

        double totalCash = Double.parseDouble(txtTotalCash.getText());
        double totalCard = Double.parseDouble(txtTotalCard.getText());
        int totalTransactions = dtmSale.getRowCount() + dtmService.getRowCount();

        boolean success = _closingTillController.saveClosing(closingDate, totalCash, totalCard, totalTransactions, notes, emp);

        if (success) {
            JOptionPane.showMessageDialog(this, "Till closed successfully!", getTitle(), JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Closing failed. It might already be closed.", getTitle(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private Employee authorizeUser() {
        String password = CommonExtension.requestUserPassword();
        return _employeeController.getEmployeeByPass(password);
    }

    private void showSummaryDialog() {
        CloseTillSummaryDialog dialog = new CloseTillSummaryDialog(SwingUtilities.getWindowAncestor(this));

        dialog.txtTotalCash.setText(String.format("%.2f", cachedTotalCash));
        dialog.txtTotalCard.setText(String.format("%.2f", cachedTotalCard));
        dialog.txtTotalCombined.setText(String.format("%.2f", cachedTotalCombined));

        dialog.btnCloseTill.addActionListener(e -> {
            closeTill();
            dialog.dispose();
        });

        dialog.setVisible(true);
    }
}
