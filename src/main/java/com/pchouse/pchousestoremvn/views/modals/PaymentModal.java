package com.pchouse.pchousestoremvn.views.modals;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderPaymentController;
import com.pchouse.pchousestoremvn.controllers.SalePaymentController;
import com.pchouse.pchousestoremvn.enums.PayMethod;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class PaymentModal extends javax.swing.JDialog {

    private ServiceOrder _serviceOrderModel;
    private Sale _saleModel;
    private final ServiceOrderPaymentController _orderPaymentController;
    private final SalePaymentController _salePaymentController;
    private ServiceOrderPayment _orderPayment;
    private SalePayment _salePayment;
    private String _amountToPay;

    public PaymentModal(ServiceOrder serviceOrder, String amountToPay, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this._orderPaymentController = new ServiceOrderPaymentController();
        this._salePaymentController = new SalePaymentController();
        this._serviceOrderModel = serviceOrder;
        this._amountToPay = amountToPay;

        loadOrderPaymentFields(serviceOrder.getIdServiceOrder(), amountToPay);
    }

    public PaymentModal(Sale sale, String amountToPay, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this._orderPaymentController = new ServiceOrderPaymentController();
        this._salePaymentController = new SalePaymentController();
        this._saleModel = sale;
        this._amountToPay = amountToPay;

        loadOrderPaymentFields(sale.getIdSale(), amountToPay);
    }

    public ServiceOrderPayment getOrderPayment() {
        return _orderPayment;
    }

    public SalePayment getSalePayment() {
        return _salePayment;
    }

    private void loadOrderPaymentFields(long id, String amount) {
        this.lbl_order_value.setText(String.valueOf(id));
        this.lbl_amount_value.setText(CommonExtension.formatEuroCurrency(Double.parseDouble(amount)));
        this.txt_payment_value.setText("");
        this.lbl_change_value.setText("");
    }

    public void confirmPayment() {
        String paymentValueText = this.txt_payment_value.getText().trim();
        Object selectedItem = this.combo_box_pay_method.getSelectedItem();

        if (selectedItem == null || selectedItem.toString().isEmpty() || paymentValueText.isEmpty()) {
            return;
        }

        double paymentValue;
        double amountToPay;

        try {
            paymentValue = Double.parseDouble(paymentValueText);
            amountToPay = Double.parseDouble(_amountToPay);
        } catch (NumberFormatException e) {
            return;
        }

        if (paymentValue < amountToPay) {
            JOptionPane.showMessageDialog(this, CommonConstant.ERROR_ORDER_DIVERG_PAYMENT);
            return;
        }

        PayMethod selectedPayMethod = (PayMethod) selectedItem;

        double changeAmount = paymentValue - amountToPay;

        if (_serviceOrderModel != null) {
            _orderPayment = new ServiceOrderPayment(
                    _serviceOrderModel,
                    selectedPayMethod,
                    amountToPay,
                    paymentValue,
                    changeAmount,
                    _serviceOrderModel.getCreated()
            );
        } else if (_saleModel != null) {
            _salePayment = new SalePayment(
                    _saleModel,
                    selectedPayMethod,
                    amountToPay,
                    paymentValue,
                    changeAmount,
                    _saleModel.getCreated()
            );
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_notes = new javax.swing.JPanel();
        panel_note_input = new javax.swing.JPanel();
        lbl_change = new javax.swing.JLabel();
        lbl_change_value = new javax.swing.JLabel();
        lbl_order_no = new javax.swing.JLabel();
        lbl_order_value = new javax.swing.JLabel();
        lbl_pay_method = new javax.swing.JLabel();
        combo_box_pay_method = new javax.swing.JComboBox<PayMethod>();
        lbl_amount = new javax.swing.JLabel();
        lbl_amount_value = new javax.swing.JLabel();
        lbl_payment = new javax.swing.JLabel();
        txt_payment_value = new javax.swing.JTextField();
        panel_fault_buttons = new javax.swing.JPanel();
        btn_pay = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Payment");
        setModal(true);

        panel_notes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        panel_note_input.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_change.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_change.setText("Change:");

        lbl_change_value.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_change_value.setText("change");

        javax.swing.GroupLayout panel_note_inputLayout = new javax.swing.GroupLayout(panel_note_input);
        panel_note_input.setLayout(panel_note_inputLayout);
        panel_note_inputLayout.setHorizontalGroup(
            panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_note_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_change)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_change_value, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_note_inputLayout.setVerticalGroup(
            panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_note_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_change_value, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_note_inputLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbl_change)))
                .addContainerGap())
        );

        lbl_order_no.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_order_no.setText("Order No.");

        lbl_order_value.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lbl_order_value.setText("orderValue");

        lbl_pay_method.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_pay_method.setText("Pay Method:");

        combo_box_pay_method.setModel(new DefaultComboBoxModel<>(PayMethod.values()));

        lbl_amount.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_amount.setText("Amount to pay");

        lbl_amount_value.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lbl_amount_value.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_amount_value.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbl_payment.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_payment.setText("Payment:");

        txt_payment_value.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_payment_value.setText("payValue");
        txt_payment_value.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_payment_valueKeyReleased(evt);
            }
        });

        panel_fault_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        javax.swing.GroupLayout panel_fault_buttonsLayout = new javax.swing.GroupLayout(panel_fault_buttons);
        panel_fault_buttons.setLayout(panel_fault_buttonsLayout);
        panel_fault_buttonsLayout.setHorizontalGroup(
            panel_fault_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_fault_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_pay)
                .addGap(18, 18, 18)
                .addComponent(btn_cancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_fault_buttonsLayout.setVerticalGroup(
            panel_fault_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_fault_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_fault_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_fault_buttonsLayout.createSequentialGroup()
                        .addComponent(btn_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btn_pay, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_notesLayout = new javax.swing.GroupLayout(panel_notes);
        panel_notes.setLayout(panel_notesLayout);
        panel_notesLayout.setHorizontalGroup(
            panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_notesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_notesLayout.createSequentialGroup()
                        .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panel_note_input, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panel_fault_buttons, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panel_notesLayout.createSequentialGroup()
                                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panel_notesLayout.createSequentialGroup()
                                        .addComponent(lbl_payment)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_payment_value, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panel_notesLayout.createSequentialGroup()
                                        .addComponent(lbl_order_no)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbl_order_value, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(50, 50, 50))
                                    .addGroup(panel_notesLayout.createSequentialGroup()
                                        .addComponent(lbl_pay_method)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(combo_box_pay_method, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_notesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbl_amount)
                        .addGap(195, 195, 195))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_notesLayout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addComponent(lbl_amount_value, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        panel_notesLayout.setVerticalGroup(
            panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_notesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_order_no)
                    .addComponent(lbl_order_value, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 14, Short.MAX_VALUE)
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(combo_box_pay_method, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_pay_method))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_payment_value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_payment))
                .addGap(18, 18, 18)
                .addComponent(lbl_amount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_amount_value, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_note_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_fault_buttons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panel_notes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_payActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_payActionPerformed
        confirmPayment();

        // Tenta obter o pagamento de service ordem
        ServiceOrderPayment addOrderPayment = getOrderPayment();

        // Tenta obter o pagamento de venda
        SalePayment addSalePayment = getSalePayment();

        if (addOrderPayment != null) {
            long idOrderPaymentAdded = _orderPaymentController.addOrderPayment(addOrderPayment);
            if (idOrderPaymentAdded > 0) {
                addOrderPayment.setIdOrderPayment(idOrderPaymentAdded);
                this._orderPayment = addOrderPayment;
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, CommonConstant.ERROR_ORDER_PAYMENT, null, JOptionPane.ERROR_MESSAGE);
            }
        } else if (addSalePayment != null) {
            long idSalePaymentAdded = _salePaymentController.addOrderPayment(addSalePayment);
            if (idSalePaymentAdded > 0) {
                addSalePayment.setIdSalePayment(idSalePaymentAdded);
                this._salePayment = addSalePayment;
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, CommonConstant.ERROR_ORDER_PAYMENT, null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_payActionPerformed

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_cancelActionPerformed

    private void txt_payment_valueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_payment_valueKeyReleased
        double amountToPay = CommonExtension.formatEuroToDouble(this.lbl_amount_value.getText());
        boolean clearField = false;

        if (!this.txt_payment_value.getText().trim().isEmpty()) {
            double payment = Double.parseDouble(this.txt_payment_value.getText());

            if (payment > amountToPay) {
                this.lbl_change_value.setText(CommonExtension.formatEuroCurrency(payment - amountToPay));
            } else {
                clearField = true;
            }
        }

        if (clearField) {
            this.lbl_change_value.setText("");
        }
    }//GEN-LAST:event_txt_payment_valueKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_pay;
    private javax.swing.JComboBox<PayMethod> combo_box_pay_method;
    private javax.swing.JLabel lbl_amount;
    private javax.swing.JLabel lbl_amount_value;
    private javax.swing.JLabel lbl_change;
    private javax.swing.JLabel lbl_change_value;
    private javax.swing.JLabel lbl_order_no;
    private javax.swing.JLabel lbl_order_value;
    private javax.swing.JLabel lbl_pay_method;
    private javax.swing.JLabel lbl_payment;
    private javax.swing.JPanel panel_fault_buttons;
    private javax.swing.JPanel panel_note_input;
    private javax.swing.JPanel panel_notes;
    private javax.swing.JTextField txt_payment_value;
    // End of variables declaration//GEN-END:variables
}
