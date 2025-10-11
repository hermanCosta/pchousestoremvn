package com.pchouse.pchousestoremvn.views.modals;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.enums.PayMethod;
import static com.pchouse.pchousestoremvn.enums.PayMethod.CARD;
import static com.pchouse.pchousestoremvn.enums.PayMethod.CASH;
import static com.pchouse.pchousestoremvn.enums.PayMethod.COMBINE;
import com.pchouse.pchousestoremvn.enums.PaymentType;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.RefurbSale;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class PaymentModal extends javax.swing.JDialog {

    private ServiceOrder _serviceOrderModel;
    private Sale _saleModel;
    private RefurbSale _refurbSale;
    private List<ServiceOrderPayment> _serviceOrderPayments = new ArrayList<>();
    private List<SalePayment> _salePayments = new ArrayList<>();
    private String _amountToPay;
    private PaymentType _paymentType;

    public PaymentModal(Sale sale, PaymentType paymentType, String amountToPay, Window owner, boolean modal) {
        super(owner, ModalityType.APPLICATION_MODAL);
        initComponents();
        initListeners();

        this._saleModel = sale;
        this._amountToPay = amountToPay;
        this._paymentType = paymentType;
        this.setTitle(_paymentType.toString() + " Payment");

        clearFields();
        loadOrderPaymentFields(amountToPay);
    }

    public PaymentModal(ServiceOrder serviceOrder, PaymentType paymentType, String amountToPay, Window owner, boolean modal) {
        super(owner, ModalityType.APPLICATION_MODAL);
        initComponents();
        initListeners();

        this._serviceOrderModel = serviceOrder;
        this._amountToPay = amountToPay;
        this._paymentType = paymentType;
        this.setTitle(_paymentType.toString() + " Payment");

        clearFields();
        loadOrderPaymentFields(amountToPay);
    }

    public PaymentModal(RefurbSale refurbSale, PaymentType paymentType, String amountToPay, Window owner, boolean modal) {
        super(owner, ModalityType.APPLICATION_MODAL);
        initComponents();
        initListeners();

        this._refurbSale = refurbSale;
        this._amountToPay = amountToPay;
        this._paymentType = paymentType;
        this.setTitle(_paymentType.toString() + " Payment");
        clearFields();
        loadOrderPaymentFields(amountToPay);
    }

    private void initListeners() {
        txt_card_amount.setText("");
        txt_cash_amount.setText("");
        combo_box_pay_method.addActionListener(e -> {
            Object selected = combo_box_pay_method.getSelectedItem();
            if (selected == null) {
                return;
            }

            PayMethod method = (PayMethod) selected;

            switch (method) {
                case CARD -> {
                    lbl_cash_payment.setEnabled(false);
                    txt_cash_amount.setEnabled(false);
                    lbl_card_payment.setEnabled(true);
                    txt_card_amount.setEnabled(true);
                }
                case CASH -> {
                    lbl_cash_payment.setEnabled(true);
                    txt_cash_amount.setEnabled(true);
                    lbl_card_payment.setEnabled(false);
                    txt_card_amount.setEnabled(false);
                }
                case COMBINE -> {
                    lbl_cash_payment.setEnabled(true);
                    txt_cash_amount.setEnabled(true);
                    lbl_card_payment.setEnabled(true);
                    txt_card_amount.setEnabled(true);
                }
            }

            // Optional: Refresh UI if needed
            lbl_cash_payment.getParent().revalidate();
            lbl_cash_payment.getParent().repaint();
        });
    }

    public List<SalePayment> getSalePayments() {
        return this._salePayments;
    }

    public List<ServiceOrderPayment> getServiceOrderPayments() {
        return this._serviceOrderPayments;
    }

    private void loadOrderPaymentFields(String amount) {
        this.lbl_amount_to_pay_value.setText(CommonExtension.formatEuroCurrency(Double.parseDouble(amount)));
        this.txt_card_amount.setText("");
        this.lbl_change_amount.setText("");
    }

    public boolean confirmPayment() {
        PayMethod selectedPayMethod = getSelectedPayMethod();
        if (selectedPayMethod == null) {
            return false;
        }

        double amountToPay = parseAmount(_amountToPay, "Invalid amount to pay.");
        if (amountToPay < 0) {
            return false;
        }

        double cardAmount = 0.0;
        double cashAmount = 0.0;

        try {
            cardAmount = parsePaymentAmount(txt_card_amount.getText(), selectedPayMethod, PayMethod.CARD, PayMethod.COMBINE);
            cashAmount = parsePaymentAmount(txt_cash_amount.getText(), selectedPayMethod, PayMethod.CASH, PayMethod.COMBINE);
        } catch (NumberFormatException e) {
            showMessage("Invalid payment amounts.");
            return false;
        }

        if (cardAmount < 0 || cashAmount < 0) {
            showMessage("Payment amounts cannot be negative.");
            return false;
        }

        double totalPaid = cardAmount + cashAmount;
        double changeAmount = totalPaid < amountToPay ? 0 : totalPaid - amountToPay;

        if (totalPaid < amountToPay) {
            showMessage(CommonConstant.ERROR_ORDER_DIVERG_PAYMENT);
            return false;
        }

        updateLabels(totalPaid, changeAmount);
        clearPreviousPayments();

        boolean paymentSuccess = processPayments(selectedPayMethod, amountToPay, totalPaid, cardAmount, cashAmount, changeAmount);

        if (!paymentSuccess) {
            showMessage("No sale or service order selected.");
            return false;
        }

        return true;
    }

    private PayMethod getSelectedPayMethod() {
        Object selectedItem = combo_box_pay_method.getSelectedItem();
        if (selectedItem == null) {
            showMessage("Please select a payment method.");
            return null;
        }
        return (PayMethod) selectedItem;
    }

    private double parseAmount(String value, String errorMessage) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            showMessage(errorMessage);
            return -1;
        }
    }

    private double parsePaymentAmount(String text, PayMethod selected, PayMethod... validMethods) {
        for (PayMethod valid : validMethods) {
            if (selected == valid) {
                return Double.parseDouble(text.trim());
            }
        }
        return 0.0;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void updateLabels(double totalPaid, double changeAmount) {
        lbl_total_paid_amount.setText(String.format("%.2f", totalPaid));
        lbl_change_amount.setText(String.format("%.2f", changeAmount));
    }

    private void clearPreviousPayments() {
        _salePayments.clear();
        _serviceOrderPayments.clear();
    }

    private boolean processPayments(PayMethod method, double amountToPay, double totalPaid, double cardAmount, double cashAmount, double changeAmount) {
        Date now = new Date();

        if (_saleModel != null) {
            addSalePayments(_saleModel.getEmployee(), _saleModel, method, amountToPay, totalPaid, cardAmount, cashAmount, changeAmount, now);
        } else if (_serviceOrderModel != null) {
            addServiceOrderPayments(_serviceOrderModel.getEmployee(), _serviceOrderModel, method, amountToPay, totalPaid, cardAmount, cashAmount, changeAmount, now);
        } else if (_refurbSale != null) {
            var sale = _refurbSale.getSale();
            addSalePayments(sale.getEmployee(), sale, method, amountToPay, totalPaid, cardAmount, cashAmount, changeAmount, now);
        } else {
            return false;
        }
        return true;
    }

    private void addSalePayments(Employee emp, Sale sale, PayMethod method, double amountToPay, double totalPaid,
            double cardAmount, double cashAmount, double changeAmount, Date now) {

        switch (method) {
            case CARD ->
                _salePayments.add(new SalePayment(emp, sale, _paymentType, CARD, amountToPay, totalPaid, cardAmount, 0.0, 0.0, now));

            case CASH -> {
                double change = Math.max(0, cashAmount - amountToPay);
                _salePayments.add(new SalePayment(emp, sale, _paymentType, CASH, amountToPay, totalPaid, 0.0, cashAmount, change, now));
            }

            case COMBINE -> {
                double totalCombined = cardAmount + cashAmount;
                double change = Math.max(0, totalCombined - amountToPay);

                // Allocate change only to cash (as is standard practice)
                _salePayments.add(new SalePayment(emp, sale, _paymentType, CARD, amountToPay, cardAmount, cardAmount, 0.0, 0.0, now));
                _salePayments.add(new SalePayment(emp, sale, _paymentType, CASH, amountToPay, cashAmount, 0.0, cashAmount, change, now));
            }
        }
    }

    private void addServiceOrderPayments(Employee emp, ServiceOrder order, PayMethod method, double amountToPay, double totalPaid,
            double cardAmount, double cashAmount, double changeAmount, Date now) {
        switch (method) {
            case CARD -> {
                _serviceOrderPayments.add(new ServiceOrderPayment(emp, order, _paymentType, CARD, amountToPay, totalPaid, cardAmount, 0.0, changeAmount, now));
            }
            case CASH -> {
                double change = Math.max(0, cashAmount - amountToPay);
                _serviceOrderPayments.add(new ServiceOrderPayment(emp, order, _paymentType, CASH, amountToPay, totalPaid, 0.0, cashAmount, change, now));
            }
            case COMBINE -> {
                double totalCombined = cardAmount + cashAmount;
                double change = Math.max(0, totalCombined - amountToPay);

                _serviceOrderPayments.add(new ServiceOrderPayment(emp, order, _paymentType, CARD, amountToPay, cardAmount, cardAmount, 0.0, Math.max(0, cardAmount - amountToPay), now));
                _serviceOrderPayments.add(new ServiceOrderPayment(emp, order, _paymentType, CASH, amountToPay, cashAmount, 0.0, cashAmount, change, now));
            }
        }
    }

    private void calculatePaidRemainingTotal() {
        double amountToPay = CommonExtension.formatEuroToDouble(this.lbl_amount_to_pay_value.getText());
        boolean clearField = false;

        double cash = CommonExtension.parseTextFieldToDouble(this.txt_card_amount);
        double card = CommonExtension.parseTextFieldToDouble(this.txt_cash_amount);
        double total = card + cash;

        this.lbl_total_paid_amount.setText(CommonExtension.formatEuroCurrency(total));
        this.lbl_remaining_amount.setText(CommonExtension.formatEuroCurrency(amountToPay - total));

        if (total > amountToPay) {
            clearField = true;
        }

        if (clearField) {
            this.lbl_remaining_amount.setText("");
        }
    }

    private void calculateChangeAndTotal() {
        double amountToPay = CommonExtension.formatEuroToDouble(this.lbl_amount_to_pay_value.getText());
        boolean clearField = false;

        double cash = CommonExtension.parseTextFieldToDouble(this.txt_card_amount);
        double card = CommonExtension.parseTextFieldToDouble(this.txt_cash_amount);
        double total = card + cash;

        if (total > amountToPay) {
            this.lbl_change_amount.setText(CommonExtension.formatEuroCurrency(total - amountToPay));
        } else {
            clearField = true;
        }

        if (clearField) {
            this.lbl_change_amount.setText("");
        }
    }

    private void clearFields() {
        this.txt_card_amount.setText("");
        this.txt_cash_amount.setText("");
        this.lbl_total_paid_amount.setText("");
        this.lbl_remaining_amount.setText("");
        this.lbl_change_amount.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_notes = new javax.swing.JPanel();
        panel_sumary_amount = new javax.swing.JPanel();
        lbl_total_paid = new javax.swing.JLabel();
        lbl_total_paid_amount = new javax.swing.JLabel();
        lbl_remaining = new javax.swing.JLabel();
        lbl_remaining_amount = new javax.swing.JLabel();
        lbl_change = new javax.swing.JLabel();
        lbl_change_amount = new javax.swing.JLabel();
        lbl_pay_method = new javax.swing.JLabel();
        combo_box_pay_method = new javax.swing.JComboBox<PayMethod>();
        lbl_amount = new javax.swing.JLabel();
        lbl_amount_to_pay_value = new javax.swing.JLabel();
        lbl_card_payment = new javax.swing.JLabel();
        txt_card_amount = new javax.swing.JTextField();
        panel_payment_buttons = new javax.swing.JPanel();
        btn_pay = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();
        lbl_cash_payment = new javax.swing.JLabel();
        txt_cash_amount = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Payment");
        setModal(true);

        panel_notes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        panel_sumary_amount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_total_paid.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_total_paid.setText("Total Paid:");

        lbl_total_paid_amount.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        lbl_total_paid_amount.setText("totalPaidAmount");

        lbl_remaining.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_remaining.setText("Remaining:");

        lbl_remaining_amount.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        lbl_remaining_amount.setText("remainingAmount");

        lbl_change.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_change.setText("Change:");

        lbl_change_amount.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_change_amount.setText("changeAmount");

        javax.swing.GroupLayout panel_sumary_amountLayout = new javax.swing.GroupLayout(panel_sumary_amount);
        panel_sumary_amount.setLayout(panel_sumary_amountLayout);
        panel_sumary_amountLayout.setHorizontalGroup(
            panel_sumary_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_sumary_amountLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_sumary_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_sumary_amountLayout.createSequentialGroup()
                        .addComponent(lbl_total_paid)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_total_paid_amount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_sumary_amountLayout.createSequentialGroup()
                        .addComponent(lbl_remaining)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_remaining_amount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_sumary_amountLayout.createSequentialGroup()
                        .addComponent(lbl_change)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_change_amount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_sumary_amountLayout.setVerticalGroup(
            panel_sumary_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_sumary_amountLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_sumary_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_total_paid_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_total_paid))
                .addGap(3, 3, 3)
                .addGroup(panel_sumary_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_remaining_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_remaining))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_sumary_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_change_amount)
                    .addComponent(lbl_change, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lbl_pay_method.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_pay_method.setText("Pay Method:");

        combo_box_pay_method.setModel(new DefaultComboBoxModel<>(PayMethod.values()));

        lbl_amount.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_amount.setText("Amount to pay");

        lbl_amount_to_pay_value.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lbl_amount_to_pay_value.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_amount_to_pay_value.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbl_card_payment.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_card_payment.setText("Card Payment:");

        txt_card_amount.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_card_amount.setText("cardAmount");
        txt_card_amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_card_amountKeyReleased(evt);
            }
        });

        panel_payment_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_pay.setBackground(new java.awt.Color(21, 76, 121));
        btn_pay.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_pay.setForeground(new java.awt.Color(255, 255, 255));
        btn_pay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_pay.png"))); // NOI18N
        btn_pay.setText("Pay");
        btn_pay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_payActionPerformed(evt);
            }
        });

        btn_cancel.setBackground(new java.awt.Color(21, 76, 121));
        btn_cancel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_cancel.setForeground(new java.awt.Color(255, 255, 255));
        btn_cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_cancel.png"))); // NOI18N
        btn_cancel.setText("Cancel");
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_payment_buttonsLayout = new javax.swing.GroupLayout(panel_payment_buttons);
        panel_payment_buttons.setLayout(panel_payment_buttonsLayout);
        panel_payment_buttonsLayout.setHorizontalGroup(
            panel_payment_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_payment_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_pay)
                .addGap(18, 18, 18)
                .addComponent(btn_cancel)
                .addContainerGap(243, Short.MAX_VALUE))
        );
        panel_payment_buttonsLayout.setVerticalGroup(
            panel_payment_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_payment_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_payment_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_payment_buttonsLayout.createSequentialGroup()
                        .addComponent(btn_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btn_pay, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        lbl_cash_payment.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_cash_payment.setText("Cash Payment:");
        lbl_cash_payment.setEnabled(false);

        txt_cash_amount.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_cash_amount.setText("cashAmount");
        txt_cash_amount.setEnabled(false);
        txt_cash_amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_cash_amountKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panel_notesLayout = new javax.swing.GroupLayout(panel_notes);
        panel_notes.setLayout(panel_notesLayout);
        panel_notesLayout.setHorizontalGroup(
            panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_notesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_sumary_amount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_payment_buttons, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_notesLayout.createSequentialGroup()
                        .addComponent(lbl_pay_method)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(combo_box_pay_method, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lbl_amount_to_pay_value, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_notesLayout.createSequentialGroup()
                        .addComponent(lbl_card_payment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_card_amount))
                    .addGroup(panel_notesLayout.createSequentialGroup()
                        .addComponent(lbl_cash_payment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cash_amount))
                    .addGroup(panel_notesLayout.createSequentialGroup()
                        .addComponent(lbl_amount)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_notesLayout.setVerticalGroup(
            panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_notesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_amount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_amount_to_pay_value, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(combo_box_pay_method, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_pay_method))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_card_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_card_payment))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cash_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_cash_payment))
                .addGap(18, 18, 18)
                .addComponent(panel_sumary_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel_payment_buttons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_notes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_notes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_payActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_payActionPerformed
        if (!confirmPayment()) {
            return; // early exit if confirm failed
        }

        PayMethod selectedMethod = (PayMethod) combo_box_pay_method.getSelectedItem();

        // Validate if method is CARD and amounts don't match
        if (selectedMethod == PayMethod.CARD) {
            try {
                double total = CommonExtension.formatEuroToDouble(_amountToPay);
                double cardAmount = Double.parseDouble(txt_card_amount.getText().trim());

                if (Double.compare(total, cardAmount) != 0) {
                    JOptionPane.showMessageDialog(this,
                            "Card payment must match the total amount. No change is allowed.",
                            "Payment Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid number format in total or card amount.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Proceed with payment handling
        if (!_serviceOrderPayments.isEmpty()) {
            this._serviceOrderPayments = new ArrayList<>(_serviceOrderPayments);
            this.dispose();
        } else if (!_salePayments.isEmpty()) {
            this._salePayments = new ArrayList<>(_salePayments);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid payment data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_payActionPerformed

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_cancelActionPerformed

    private void txt_card_amountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_card_amountKeyReleased
        calculatePaidRemainingTotal();
        calculateChangeAndTotal();
    }//GEN-LAST:event_txt_card_amountKeyReleased

    private void txt_cash_amountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cash_amountKeyReleased
        calculatePaidRemainingTotal();
        calculateChangeAndTotal();
    }//GEN-LAST:event_txt_cash_amountKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_pay;
    private javax.swing.JComboBox<PayMethod> combo_box_pay_method;
    private javax.swing.JLabel lbl_amount;
    private javax.swing.JLabel lbl_amount_to_pay_value;
    private javax.swing.JLabel lbl_card_payment;
    private javax.swing.JLabel lbl_cash_payment;
    private javax.swing.JLabel lbl_change;
    private javax.swing.JLabel lbl_change_amount;
    private javax.swing.JLabel lbl_pay_method;
    private javax.swing.JLabel lbl_remaining;
    private javax.swing.JLabel lbl_remaining_amount;
    private javax.swing.JLabel lbl_total_paid;
    private javax.swing.JLabel lbl_total_paid_amount;
    private javax.swing.JPanel panel_notes;
    private javax.swing.JPanel panel_payment_buttons;
    private javax.swing.JPanel panel_sumary_amount;
    private javax.swing.JTextField txt_card_amount;
    private javax.swing.JTextField txt_cash_amount;
    // End of variables declaration//GEN-END:variables
}
