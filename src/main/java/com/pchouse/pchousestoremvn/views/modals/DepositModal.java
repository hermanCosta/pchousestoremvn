package com.pchouse.pchousestoremvn.views.modals;

import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonStrings;
import com.pchouse.pchousestoremvn.controllers.DepositController;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class DepositModal extends javax.swing.JDialog {

    /* For invoking this JDialog in a JInternalFrame
     NoteView noteView = new NoteView(new MainMenuView(CommonSetting.COMPANY), true);
        noteView.setVisible(true);
     */
    private final DepositController _depositController;
    private final DefaultTableModel _dtmOrderDeposit;
    private List<Deposit> _listOrderDeposit;
    private ServiceOrder _createdOrderView;

    public DepositModal(ServiceOrder orderModel, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this._createdOrderView = orderModel;
        this._depositController = new DepositController();
        this._createdOrderView = orderModel;
        this._dtmOrderDeposit = (DefaultTableModel) this.table_view_deposits.getModel();
        loadOrderDepositLisTable();
    }

    private void loadOrderDepositLisTable() {
        this._listOrderDeposit = this._depositController.getOrderDeposit(_createdOrderView);

        this.lbl_order_no.setText(CommonStrings.formatOrderNumber(_createdOrderView.getIdServiceOrder()));
        _dtmOrderDeposit.setRowCount(0);
        double totalDeposit = 0;

        if (this._listOrderDeposit != null) {
            for (Deposit depositItem : _listOrderDeposit) {
                _dtmOrderDeposit.addRow(
                        new Object[]{
                            depositItem.getIdDeposit(),
                            CommonStrings.formatDateToString(depositItem.getCreated()),
                            CommonExtension.formatEuroCurrency(depositItem.getAmount()),
                            depositItem.getServiceOrderPayment().getPayMethod(),
                            depositItem.getEmployee().getUsername()
                        }
                );

                totalDeposit += depositItem.getAmount();
            }
        }

        this.lbl_total.setText(CommonExtension.formatEuroCurrency(totalDeposit));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        panel_notes = new javax.swing.JPanel();
        scroll_pane_notes = new javax.swing.JScrollPane();
        table_view_deposits = new javax.swing.JTable();
        panel_note_input = new javax.swing.JPanel();
        hdn_txt_note_id = new javax.swing.JTextField();
        lbl_deposit_paid = new javax.swing.JLabel();
        lbl_total = new javax.swing.JLabel();
        lbl_order_deposit = new javax.swing.JLabel();
        lbl_order_no = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Deposit History View");
        setModal(true);

        panel_notes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        table_view_deposits.setAutoCreateRowSorter(true);
        table_view_deposits.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N
        table_view_deposits.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IDOrderDeposit", "Date", "Amount", "Paid By", "User"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scroll_pane_notes.setViewportView(table_view_deposits);
        if (table_view_deposits.getColumnModel().getColumnCount() > 0) {
            table_view_deposits.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_deposits.getColumnModel().getColumn(0).setPreferredWidth(0);
            table_view_deposits.getColumnModel().getColumn(0).setMaxWidth(0);
            table_view_deposits.getColumnModel().getColumn(1).setPreferredWidth(120);
            table_view_deposits.getColumnModel().getColumn(1).setMaxWidth(150);
            table_view_deposits.getColumnModel().getColumn(2).setPreferredWidth(120);
            table_view_deposits.getColumnModel().getColumn(2).setMaxWidth(120);
            table_view_deposits.getColumnModel().getColumn(3).setPreferredWidth(80);
            table_view_deposits.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        panel_note_input.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        hdn_txt_note_id.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        hdn_txt_note_id.setEnabled(false);
        hdn_txt_note_id.setMinimumSize(new java.awt.Dimension(80, 32));
        hdn_txt_note_id.setPreferredSize(new java.awt.Dimension(0, 0));

        lbl_deposit_paid.setText("Total deposit paid:");

        javax.swing.GroupLayout panel_note_inputLayout = new javax.swing.GroupLayout(panel_note_input);
        panel_note_input.setLayout(panel_note_inputLayout);
        panel_note_inputLayout.setHorizontalGroup(
            panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_note_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hdn_txt_note_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_note_inputLayout.createSequentialGroup()
                        .addComponent(lbl_deposit_paid)
                        .addGap(9, 9, 9)
                        .addComponent(lbl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_note_inputLayout.setVerticalGroup(
            panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_note_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_deposit_paid))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(hdn_txt_note_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );

        lbl_order_deposit.setText("Order No.");

        javax.swing.GroupLayout panel_notesLayout = new javax.swing.GroupLayout(panel_notes);
        panel_notes.setLayout(panel_notesLayout);
        panel_notesLayout.setHorizontalGroup(
            panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_notesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_note_input, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scroll_pane_notes, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                    .addGroup(panel_notesLayout.createSequentialGroup()
                        .addComponent(lbl_order_deposit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_order_no, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_notesLayout.setVerticalGroup(
            panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_notesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_order_deposit)
                    .addComponent(lbl_order_no, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scroll_pane_notes, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_note_input, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(panel_notes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField hdn_txt_note_id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lbl_deposit_paid;
    private javax.swing.JLabel lbl_order_deposit;
    private javax.swing.JLabel lbl_order_no;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JPanel panel_note_input;
    private javax.swing.JPanel panel_notes;
    private javax.swing.JScrollPane scroll_pane_notes;
    private javax.swing.JTable table_view_deposits;
    // End of variables declaration//GEN-END:variables
}
