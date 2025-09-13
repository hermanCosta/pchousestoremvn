package com.pchouse.pchousestoremvn.views.modals;

import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonStrings;
import com.pchouse.pchousestoremvn.enums.PaymentType;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import java.awt.Window;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PaymentHistoryModal extends javax.swing.JDialog {

    private final DefaultTableModel _dtmPaymentHistory;
    private ServiceOrder _serviceOrderModel;
    private Sale _saleModel;
    private List<SalePayment> _listSalePayments;
    private List<ServiceOrderPayment> _listServiceOrderPayments;

    public PaymentHistoryModal(ServiceOrder serviceOrderModel, List<ServiceOrderPayment> listServiceOrderPayments, Window owner, boolean modal) {
        super(owner, ModalityType.APPLICATION_MODAL);
        initComponents();

        this._dtmPaymentHistory = (DefaultTableModel) this.table_view_payments.getModel();
        this._serviceOrderModel = serviceOrderModel;
        this._listServiceOrderPayments = listServiceOrderPayments;
        loadOrderPaymentListTable();
    }

    public PaymentHistoryModal(Sale saleModel, List<SalePayment> listSalePayments, Window owner, boolean modal) {
        super(owner, ModalityType.APPLICATION_MODAL);
        initComponents();

        this._dtmPaymentHistory = (DefaultTableModel) this.table_view_payments.getModel();
        this._saleModel = saleModel;
        this._listSalePayments = listSalePayments;
        loadOrderPaymentListTable();
    }

    private void loadOrderPaymentListTable() {
        try {

            if (_serviceOrderModel != null) {
                this.lbl_order_id.setText(CommonStrings.formatOrderNumber(_serviceOrderModel.getIdServiceOrder()));

                if (this._listServiceOrderPayments != null && !_listServiceOrderPayments.isEmpty()) {
                    _dtmPaymentHistory.setRowCount(0);

                    for (ServiceOrderPayment servicePayment : _listServiceOrderPayments) {

                        String serviceRefund = CommonExtension.formatEuroCurrency(servicePayment.getAmountPaid());
                        if (servicePayment.getPaymentType().equals(PaymentType.REFUND)) {
                            var refundAmount = servicePayment.getAmountPaid() - servicePayment.getChangeAmount();
                            serviceRefund = CommonExtension.formatEuroCurrency(-refundAmount);
                        }

                        _dtmPaymentHistory.addRow(new Object[]{
                            servicePayment.getIdServiceOrderPayment(),
                            CommonStrings.formatDateToString(servicePayment.getDtTransaction()),
                            serviceRefund,
                            servicePayment.getPayMethod(),
                            CommonExtension.formatEuroCurrency(servicePayment.getChangeAmount()),
                            servicePayment.getPaymentType(),
                            servicePayment.getEmployee().getUsername()
                        });
                    }
                }
            } else if (_saleModel != null) {
                this.lbl_order_id.setText(CommonStrings.formatOrderNumber(_saleModel.getIdSale()));

                if (this._listSalePayments != null && !_listSalePayments.isEmpty()) {
                    _dtmPaymentHistory.setRowCount(0);

                    for (SalePayment salePayment : _listSalePayments) {

                        String saleRefund = CommonExtension.formatEuroCurrency(salePayment.getAmountPaid());
                        if (salePayment.getPaymentType().equals(PaymentType.REFUND)) {
                            var refundAmount = salePayment.getAmountPaid() - salePayment.getChangeAmount();
                            saleRefund = CommonExtension.formatEuroCurrency(-refundAmount);
                        }

                        _dtmPaymentHistory.addRow(new Object[]{
                            salePayment.getIdSalePayment(),
                            CommonStrings.formatDateToString(salePayment.getDtTransaction()),
                            saleRefund,
                            salePayment.getPayMethod(),
                            CommonExtension.formatEuroCurrency(salePayment.getChangeAmount()),
                            salePayment.getPaymentType(),
                            salePayment.getEmployee().getUsername()
                        });
                    }
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error while loading order payments: " + e.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_payments = new javax.swing.JPanel();
        lbl_order_no = new javax.swing.JLabel();
        lbl_order_id = new javax.swing.JLabel();
        scroll_pane_deposit = new javax.swing.JScrollPane();
        table_view_payments = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Payment History View");
        setModal(true);

        panel_payments.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_order_no.setText("Order No.");

        lbl_order_id.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N

        table_view_payments.setAutoCreateRowSorter(true);
        table_view_payments.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N
        table_view_payments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdOrder", "Date", "Amount", "Paid By", "Change", "Type", "User"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scroll_pane_deposit.setViewportView(table_view_payments);
        if (table_view_payments.getColumnModel().getColumnCount() > 0) {
            table_view_payments.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_payments.getColumnModel().getColumn(0).setPreferredWidth(0);
            table_view_payments.getColumnModel().getColumn(0).setMaxWidth(0);
            table_view_payments.getColumnModel().getColumn(1).setPreferredWidth(160);
            table_view_payments.getColumnModel().getColumn(1).setMaxWidth(180);
            table_view_payments.getColumnModel().getColumn(2).setPreferredWidth(100);
            table_view_payments.getColumnModel().getColumn(2).setMaxWidth(100);
            table_view_payments.getColumnModel().getColumn(3).setPreferredWidth(80);
            table_view_payments.getColumnModel().getColumn(3).setMaxWidth(100);
            table_view_payments.getColumnModel().getColumn(4).setPreferredWidth(80);
            table_view_payments.getColumnModel().getColumn(4).setMaxWidth(100);
            table_view_payments.getColumnModel().getColumn(5).setPreferredWidth(80);
            table_view_payments.getColumnModel().getColumn(5).setMaxWidth(100);
        }

        javax.swing.GroupLayout panel_paymentsLayout = new javax.swing.GroupLayout(panel_payments);
        panel_payments.setLayout(panel_paymentsLayout);
        panel_paymentsLayout.setHorizontalGroup(
            panel_paymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_paymentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_paymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroll_pane_deposit, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
                    .addGroup(panel_paymentsLayout.createSequentialGroup()
                        .addComponent(lbl_order_no)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_order_id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_paymentsLayout.setVerticalGroup(
            panel_paymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_paymentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_paymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_order_no, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_order_id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scroll_pane_deposit, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_payments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_payments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("Payment History View");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbl_order_id;
    private javax.swing.JLabel lbl_order_no;
    private javax.swing.JPanel panel_payments;
    private javax.swing.JScrollPane scroll_pane_deposit;
    private javax.swing.JTable table_view_payments;
    // End of variables declaration//GEN-END:variables
}
