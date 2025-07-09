package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.FaultController;
import com.pchouse.pchousestoremvn.models.Fault;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FaultView extends javax.swing.JInternalFrame {

    private final DefaultTableModel _dtmFault;
    private final FaultController _faultController;
    private List<Fault> _listFaults;

    public FaultView() {
        initComponents();

        CommonSetting.requestTxtFocus(this.txt_search_fault);
        CommonSetting.tableSettings(this.table_view_faults);

        this._dtmFault = (DefaultTableModel) this.table_view_faults.getModel();
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

    private void searchFault() {
        if (!this.txt_search_fault.getText().trim().isEmpty()) {
            this._listFaults = this._faultController.searchFault(this.txt_search_fault.getText());

            if (this._listFaults != null) {
                _dtmFault.setRowCount(0);

                for (Fault fault : _listFaults) {
                    _dtmFault.addRow(
                            new Object[]{
                                fault.getIdFault(),
                                fault.getDescription(),}
                    );
                }
            }
        } else {
            loadFaultListTable();
        }
    }

    private Fault getFaultFields() {
        Fault getFault = null;

        if (txt_fault_description.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, this.getTitle(), JOptionPane.WARNING_MESSAGE);

            return getFault;
        } else {
            getFault = new Fault(this.txt_fault_description.getText().toUpperCase());

            int idFault = CommonExtension.setIdExtension(this.hdn_txt_fault_id);
            getFault.setIdFault(idFault);

            return getFault;
        }
    }

    private void setFaultFields(Fault pFault) {
        this.hdn_txt_fault_id.setText(String.valueOf(pFault.getIdFault()));
        this.txt_fault_description.setText(pFault.getDescription());
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
        this.hdn_txt_fault_id.setText("");
        this.txt_search_fault.setText("");
        this.txt_fault_description.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_faults = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_view_faults = new javax.swing.JTable();
        txt_search_fault = new javax.swing.JTextField();
        lbl_search_icon = new javax.swing.JLabel();
        panel_fault_input = new javax.swing.JPanel();
        hdn_txt_fault_id = new javax.swing.JTextField();
        lbl_fault = new javax.swing.JLabel();
        txt_fault_description = new javax.swing.JTextField();
        lbl_first_name_star = new javax.swing.JLabel();
        panel_fault_buttons = new javax.swing.JPanel();
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

        panel_faults.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_faults.setPreferredSize(new java.awt.Dimension(1026, 607));

        table_view_faults.setAutoCreateRowSorter(true);
        table_view_faults.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        table_view_faults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fault Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_faults.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_faultsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_view_faults);
        if (table_view_faults.getColumnModel().getColumnCount() > 0) {
            table_view_faults.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_faults.getColumnModel().getColumn(0).setPreferredWidth(0);
            table_view_faults.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        txt_search_fault.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        txt_search_fault.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_search_fault.setPreferredSize(new java.awt.Dimension(500, 30));
        txt_search_fault.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_search_faultKeyReleased(evt);
            }
        });

        lbl_search_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search_black.png"))); // NOI18N

        panel_fault_input.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        hdn_txt_fault_id.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        hdn_txt_fault_id.setEnabled(false);
        hdn_txt_fault_id.setMinimumSize(new java.awt.Dimension(80, 32));
        hdn_txt_fault_id.setPreferredSize(new java.awt.Dimension(0, 0));

        lbl_fault.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_fault.setText("Fault");

        txt_fault_description.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_fault_description.setPreferredSize(new java.awt.Dimension(600, 25));

        lbl_first_name_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_first_name_star.setForeground(java.awt.Color.red);
        lbl_first_name_star.setText("*");

        javax.swing.GroupLayout panel_fault_inputLayout = new javax.swing.GroupLayout(panel_fault_input);
        panel_fault_input.setLayout(panel_fault_inputLayout);
        panel_fault_inputLayout.setHorizontalGroup(
            panel_fault_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_fault_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_fault_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hdn_txt_fault_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_first_name_star))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_fault)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_fault_description, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_fault_inputLayout.setVerticalGroup(
            panel_fault_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_fault_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_fault_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_fault_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_fault)
                        .addComponent(lbl_first_name_star, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_fault_description, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(hdn_txt_fault_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_fault_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
        btn_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_save_changes.png"))); // NOI18N
        btn_update.setText("Update");
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
        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_cancel.png"))); // NOI18N
        btn_delete.setText("Delete");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_fault_buttonsLayout = new javax.swing.GroupLayout(panel_fault_buttons);
        panel_fault_buttons.setLayout(panel_fault_buttonsLayout);
        panel_fault_buttonsLayout.setHorizontalGroup(
            panel_fault_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_fault_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_add)
                .addGap(18, 18, 18)
                .addComponent(btn_update)
                .addGap(18, 18, 18)
                .addComponent(btn_delete)
                .addGap(18, 18, 18)
                .addComponent(btn_clear_fields)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_fault_buttonsLayout.setVerticalGroup(
            panel_fault_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_fault_buttonsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panel_fault_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_clear_fields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout panel_faultsLayout = new javax.swing.GroupLayout(panel_faults);
        panel_faults.setLayout(panel_faultsLayout);
        panel_faultsLayout.setHorizontalGroup(
            panel_faultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_faultsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_faultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_faultsLayout.createSequentialGroup()
                        .addComponent(lbl_search_icon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_search_fault, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE))
                    .addComponent(panel_fault_input, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_fault_buttons, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_faultsLayout.setVerticalGroup(
            panel_faultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_faultsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_faultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_search_fault, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_search_icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel_fault_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel_fault_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_faults, javax.swing.GroupLayout.DEFAULT_SIZE, 1036, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_faults, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                .addContainerGap())
        );

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
                    this.txt_fault_description.setText("");
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
                    this.txt_fault_description.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.ERROR_UPDATE, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void txt_search_faultKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search_faultKeyReleased
        searchFault();
    }//GEN-LAST:event_txt_search_faultKeyReleased

    private void table_view_faultsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_faultsMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = this.table_view_faults.getSelectedRow();

            Fault updateFault = new Fault(
                    _dtmFault.getValueAt(selectedRow, 1).toString()
            );

            updateFault.setIdFault((long) this._dtmFault.getValueAt(selectedRow, 0));
            setFaultFields(updateFault);
        }
    }//GEN-LAST:event_table_view_faultsMouseClicked

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        int selectedRow = this.table_view_faults.getSelectedRow();

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
    private javax.swing.JTextField hdn_txt_fault_id;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_fault;
    private javax.swing.JLabel lbl_first_name_star;
    private javax.swing.JLabel lbl_search_icon;
    private javax.swing.JPanel panel_fault_buttons;
    private javax.swing.JPanel panel_fault_input;
    private javax.swing.JPanel panel_faults;
    private javax.swing.JTable table_view_faults;
    private javax.swing.JTextField txt_fault_description;
    private javax.swing.JTextField txt_search_fault;
    // End of variables declaration//GEN-END:variables
}
