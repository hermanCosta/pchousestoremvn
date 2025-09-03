package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.FaultController;
import com.pchouse.pchousestoremvn.models.Fault;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CashInRegistryView extends javax.swing.JInternalFrame {

    private long hdnFaultId;
    private final DefaultTableModel _dtmFault;
    private final FaultController _faultController;
    private List<Fault> _listFaults;

    public CashInRegistryView() {
        initComponents();

        //CommonSetting.requestTxtFocus(this.txt_search_fault);
        CommonSetting.tableSettings(this.table_view_cash_ins);

        this._dtmFault = (DefaultTableModel) this.table_view_cash_ins.getModel();
        this._faultController = new FaultController();

        loadFaultListTable();
    }

    private void loadFaultListTable() {
        this._listFaults = this._faultController.getAllFault();

        _dtmFault.setRowCount(0);

        if (this._listFaults != null) {
            for (Fault fault : _listFaults) {
                _dtmFault.addRow(
                        new Object[]{
                            fault.getIdFault(),
                            fault.getDescription(),}
                );
            }
        }
    }

    private Fault getFaultFields() {
        Fault getFault = null;

        if (txt_amount.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, this.getTitle(), JOptionPane.WARNING_MESSAGE);

            return getFault;
        } else {
            getFault = new Fault(this.txt_amount.getText().toUpperCase());

            long idFault = hdnFaultId;
            getFault.setIdFault(idFault);

            return getFault;
        }
    }

    private void setFaultFields(Fault pFault) {
        hdnFaultId = pFault.getIdFault();
        this.txt_amount.setText(pFault.getDescription());
    }

    private void getItemFault(long idFault) {
        if (idFault != 0) {
            Fault faultItem = _faultController.getItemFault(idFault);

            this._dtmFault.setRowCount(0);
            _dtmFault.addRow(
                    new Object[]{
                        faultItem.getIdFault(),
                        faultItem.getDescription(),}
            );
        } else {
            loadFaultListTable();
        }
    }

    private void clearFields() {
        this.hdnFaultId = 0;
       // this.txt_search_fault.setText("");
        this.txt_amount.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_cash_in_registry = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_view_cash_ins = new javax.swing.JTable();
        panel_cash_in_input = new javax.swing.JPanel();
        lbl_amount = new javax.swing.JLabel();
        txt_amount = new javax.swing.JTextField();
        lbl_first_name_star = new javax.swing.JLabel();
        lbl_first_name_star1 = new javax.swing.JLabel();
        lbl_notes = new javax.swing.JLabel();
        txt_notes = new javax.swing.JTextField();
        lbl_amount1 = new javax.swing.JLabel();
        txt_amount1 = new javax.swing.JTextField();
        lbl_amount2 = new javax.swing.JLabel();
        txt_amount2 = new javax.swing.JTextField();
        panel_cash_buttons = new javax.swing.JPanel();
        btn_clear_fields = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_add = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Faults");
        setMaximumSize(new java.awt.Dimension(0, 0));
        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(1050, 650));

        panel_cash_in_registry.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_cash_in_registry.setPreferredSize(new java.awt.Dimension(1026, 607));

        table_view_cash_ins.setAutoCreateRowSorter(true);
        table_view_cash_ins.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        table_view_cash_ins.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Amount", "Notes", "Date", "User"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_cash_ins.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_cash_insMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_view_cash_ins);
        if (table_view_cash_ins.getColumnModel().getColumnCount() > 0) {
            table_view_cash_ins.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_cash_ins.getColumnModel().getColumn(0).setPreferredWidth(20);
            table_view_cash_ins.getColumnModel().getColumn(0).setMaxWidth(20);
            table_view_cash_ins.getColumnModel().getColumn(1).setPreferredWidth(50);
            table_view_cash_ins.getColumnModel().getColumn(1).setMaxWidth(60);
            table_view_cash_ins.getColumnModel().getColumn(3).setPreferredWidth(80);
            table_view_cash_ins.getColumnModel().getColumn(3).setMaxWidth(80);
            table_view_cash_ins.getColumnModel().getColumn(4).setPreferredWidth(80);
            table_view_cash_ins.getColumnModel().getColumn(4).setMaxWidth(80);
        }

        panel_cash_in_input.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_amount.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_amount.setText("Amount");

        txt_amount.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_amount.setPreferredSize(new java.awt.Dimension(600, 25));

        lbl_first_name_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_first_name_star.setForeground(java.awt.Color.red);
        lbl_first_name_star.setText("*");

        lbl_first_name_star1.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_first_name_star1.setForeground(java.awt.Color.red);
        lbl_first_name_star1.setText("*");

        lbl_notes.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_notes.setText("Notes");

        txt_notes.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_notes.setPreferredSize(new java.awt.Dimension(600, 25));

        lbl_amount1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_amount1.setText("From Date");

        txt_amount1.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_amount1.setPreferredSize(new java.awt.Dimension(600, 25));

        lbl_amount2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_amount2.setText("From Date");

        txt_amount2.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_amount2.setPreferredSize(new java.awt.Dimension(600, 25));

        javax.swing.GroupLayout panel_cash_in_inputLayout = new javax.swing.GroupLayout(panel_cash_in_input);
        panel_cash_in_input.setLayout(panel_cash_in_inputLayout);
        panel_cash_in_inputLayout.setHorizontalGroup(
            panel_cash_in_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_cash_in_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_cash_in_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_cash_in_inputLayout.createSequentialGroup()
                        .addComponent(lbl_first_name_star1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_notes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_notes, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_amount2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_amount2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_cash_in_inputLayout.createSequentialGroup()
                        .addComponent(lbl_first_name_star)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_amount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_amount1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_amount1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );
        panel_cash_in_inputLayout.setVerticalGroup(
            panel_cash_in_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_cash_in_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_cash_in_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_amount)
                    .addComponent(lbl_first_name_star, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_amount1)
                    .addComponent(txt_amount1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_cash_in_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_cash_in_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_amount2)
                        .addComponent(txt_amount2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_cash_in_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_notes)
                        .addComponent(lbl_first_name_star1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_notes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_cash_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_clear_fields.setBackground(new java.awt.Color(21, 76, 121));
        btn_clear_fields.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_clear_fields.setForeground(new java.awt.Color(255, 255, 255));
        btn_clear_fields.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_clear.png"))); // NOI18N
        btn_clear_fields.setText("Clear");
        btn_clear_fields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_fieldsActionPerformed(evt);
            }
        });

        btn_update.setBackground(new java.awt.Color(21, 76, 121));
        btn_update.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_update.setForeground(new java.awt.Color(255, 255, 255));
        btn_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search.png"))); // NOI18N
        btn_update.setText("Search");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });

        btn_add.setBackground(new java.awt.Color(21, 76, 121));
        btn_add.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_add.setForeground(new java.awt.Color(255, 255, 255));
        btn_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_add.png"))); // NOI18N
        btn_add.setText("Add");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });

        btn_delete.setBackground(new java.awt.Color(21, 76, 121));
        btn_delete.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_delete.setForeground(new java.awt.Color(255, 255, 255));
        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_print.png"))); // NOI18N
        btn_delete.setText("Print");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_cash_buttonsLayout = new javax.swing.GroupLayout(panel_cash_buttons);
        panel_cash_buttons.setLayout(panel_cash_buttonsLayout);
        panel_cash_buttonsLayout.setHorizontalGroup(
            panel_cash_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_cash_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_add)
                .addGap(12, 12, 12)
                .addComponent(btn_update)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_clear_fields)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_delete)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_cash_buttonsLayout.setVerticalGroup(
            panel_cash_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_cash_buttonsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panel_cash_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_clear_fields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout panel_cash_in_registryLayout = new javax.swing.GroupLayout(panel_cash_in_registry);
        panel_cash_in_registry.setLayout(panel_cash_in_registryLayout);
        panel_cash_in_registryLayout.setHorizontalGroup(
            panel_cash_in_registryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_cash_in_registryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_cash_in_registryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addComponent(panel_cash_buttons, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(panel_cash_in_input, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_cash_in_registryLayout.setVerticalGroup(
            panel_cash_in_registryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_cash_in_registryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel_cash_in_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel_cash_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_cash_in_registry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_cash_in_registry, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("Cash IN Registry");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        Fault addFault = this.getFaultFields();

        if (addFault != null) {
            long idExistFault = _faultController.checkIfFaultExists(addFault.getDescription());
            if (addFault.getIdFault() == 0 && idExistFault == 0) {

                long idFault = this._faultController.addFault(addFault);
                if (idFault > 0) {
                    getItemFault(idFault);
                    this.txt_amount.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.ERROR_SAVE, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, CommonConstant.WARN_EXIST_ITEM, this.getTitle(), JOptionPane.WARNING_MESSAGE);
                getItemFault(idExistFault);
            }

        }
    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        Fault updateFault = this.getFaultFields();

        if (updateFault != null) {
            int confirmEditing = JOptionPane.showConfirmDialog(this, CommonConstant.CONFIRM_UPDATE, this.getTitle(), JOptionPane.YES_NO_OPTION);

            if (confirmEditing == 0) {
                boolean isUpdated = this._faultController.updateFault(updateFault);

                if (isUpdated) {
                    getItemFault(updateFault.getIdFault());
                    this.txt_amount.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.ERROR_UPDATE, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void table_view_cash_insMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_cash_insMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = this.table_view_cash_ins.getSelectedRow();

            Fault updateFault = new Fault(
                    _dtmFault.getValueAt(selectedRow, 1).toString()
            );

            updateFault.setIdFault((long) this._dtmFault.getValueAt(selectedRow, 0));
            setFaultFields(updateFault);
        }
    }//GEN-LAST:event_table_view_cash_insMouseClicked

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        int selectedRow = this.table_view_cash_ins.getSelectedRow();

        if (selectedRow >= 0) {

            Fault deleteFault = new Fault();

            deleteFault.setIdFault((Integer) _dtmFault.getValueAt(selectedRow, 0));
            deleteFault.setDescription(_dtmFault.getValueAt(selectedRow, 1).toString());

            int confirmDeletion = JOptionPane.showConfirmDialog(this, CommonConstant.CONFIRM_DELETE, this.getTitle(), JOptionPane.YES_NO_OPTION);

            if (confirmDeletion == 0) {
                boolean isDeleted = this._faultController.deleteFault(deleteFault);

                if (isDeleted) {
                    loadFaultListTable();
                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.ERROR_DELETE, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_clear_fieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clear_fieldsActionPerformed
        clearFields();
        loadFaultListTable();
    }//GEN-LAST:event_btn_clear_fieldsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_clear_fields;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_update;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_amount;
    private javax.swing.JLabel lbl_amount1;
    private javax.swing.JLabel lbl_amount2;
    private javax.swing.JLabel lbl_first_name_star;
    private javax.swing.JLabel lbl_first_name_star1;
    private javax.swing.JLabel lbl_notes;
    private javax.swing.JPanel panel_cash_buttons;
    private javax.swing.JPanel panel_cash_in_input;
    private javax.swing.JPanel panel_cash_in_registry;
    private javax.swing.JTable table_view_cash_ins;
    private javax.swing.JTextField txt_amount;
    private javax.swing.JTextField txt_amount1;
    private javax.swing.JTextField txt_amount2;
    private javax.swing.JTextField txt_notes;
    // End of variables declaration//GEN-END:variables
}
