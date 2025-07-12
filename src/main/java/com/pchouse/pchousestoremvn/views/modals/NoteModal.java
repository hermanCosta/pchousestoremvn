package com.pchouse.pchousestoremvn.views.modals;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonStrings;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.controllers.OrderNoteController;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.Sale;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class NoteModal extends javax.swing.JDialog {

    private final OrderNoteController _orderNoteController;
    private final EmployeeController _employeeController;
    private final DefaultTableModel _dtmOrderNote;
    private List<OrderNote> _listOrderNotes;
    private ServiceOrder _serviceOrderModel;
    private Sale _saleModel;

    public NoteModal(ServiceOrder orderModel, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this._serviceOrderModel = orderModel;
        this._orderNoteController = new OrderNoteController();
        this._employeeController = new EmployeeController();
        this._serviceOrderModel = orderModel;
        this._dtmOrderNote = (DefaultTableModel) this.table_view_notes.getModel();
        loadOrderNoteListTable();
    }
    
    public NoteModal(Sale saleModel, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this._saleModel = saleModel;
        this._orderNoteController = new OrderNoteController();
        this._employeeController = new EmployeeController();
        this._dtmOrderNote = (DefaultTableModel) this.table_view_notes.getModel();
        loadOrderNoteListTable();
    }

    private void loadOrderNoteListTable() {
        this._listOrderNotes = this._orderNoteController.getOrderNotes(_serviceOrderModel);

        _dtmOrderNote.setRowCount(0);

        if (this._listOrderNotes != null) {
            for (OrderNote orderNote : _listOrderNotes) {
                _dtmOrderNote.addRow(
                        new Object[]{
                            orderNote.getIdServiceOrderNote(),
                            CommonStrings.formatDateToString(orderNote.getCreated()),
                            orderNote.getNote(),
                            orderNote.getEmployee().getUsername()
                        }
                );
            }
        }
    }

    private void searchFault() {
        if (!this.txt_search_note.getText().trim().isEmpty()) {
            this._listOrderNotes = this._orderNoteController.searchOrderNotes(_serviceOrderModel, this.txt_search_note.getText().toUpperCase());

            if (this._listOrderNotes != null) {
                _dtmOrderNote.setRowCount(0);

                for (OrderNote orderNote : _listOrderNotes) {
                    _dtmOrderNote.addRow(
                            new Object[]{
                                orderNote.getIdServiceOrderNote(),
                                CommonStrings.formatDateToString(orderNote.getCreated()),
                                orderNote.getNote(),
                                orderNote.getEmployee().getUsername()
                            }
                    );
                }
            }
        } else {
            loadOrderNoteListTable();
        }
    }

    private OrderNote getOrderNoteFields(ServiceOrder order) {
        OrderNote orderNote = null;

        if (this.editor_pane_notes.getText().trim().isEmpty() || !this.editor_pane_notes.isEnabled() || !this.hdn_txt_note_id.getText().trim().isEmpty()) {
            return orderNote;
        } else {
            Long idOrderNote = _orderNoteController.checkExistingOrderNote(this.editor_pane_notes.getText().toUpperCase(), order);
            if (idOrderNote != null) {
                JOptionPane.showMessageDialog(this, CommonConstant.WARN_EXIST_ITEM, null, JOptionPane.ERROR_MESSAGE);
                return orderNote;
            } else {
                String password = CommonExtension.requestUserPassword();
                Employee employee = _employeeController.getEmployeeByPass(password);
                if (employee != null) {
                    orderNote = new OrderNote(order, employee, this.editor_pane_notes.getText().toUpperCase(), new Date());
                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, null, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        return orderNote;
    }

    private void clearFields() {
        this.txt_search_note.setText("");
        this.editor_pane_notes.setText("");

        this.txt_search_note.requestFocus();
        this.editor_pane_notes.setEditable(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_notes = new javax.swing.JPanel();
        lbl_search_icon = new javax.swing.JLabel();
        txt_search_note = new javax.swing.JTextField();
        scroll_pane_notes = new javax.swing.JScrollPane();
        table_view_notes = new javax.swing.JTable();
        panel_note_input = new javax.swing.JPanel();
        hdn_txt_note_id = new javax.swing.JTextField();
        lbl_note_star = new javax.swing.JLabel();
        scroll_pane_notes1 = new javax.swing.JScrollPane();
        editor_pane_notes = new javax.swing.JEditorPane();
        lbl_notes = new javax.swing.JLabel();
        lbl_max_char = new javax.swing.JLabel();
        panel_fault_buttons = new javax.swing.JPanel();
        btn_clear_fields = new javax.swing.JButton();
        btn_add = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Note View");
        setModal(true);

        panel_notes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_search_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search_black.png"))); // NOI18N

        txt_search_note.setPreferredSize(new java.awt.Dimension(12, 30));
        txt_search_note.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_search_noteKeyReleased(evt);
            }
        });

        table_view_notes.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N
        table_view_notes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IDOrderNote", "Date", "Note", "User"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_notes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_notesMouseClicked(evt);
            }
        });
        scroll_pane_notes.setViewportView(table_view_notes);
        if (table_view_notes.getColumnModel().getColumnCount() > 0) {
            table_view_notes.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_notes.getColumnModel().getColumn(0).setPreferredWidth(0);
            table_view_notes.getColumnModel().getColumn(0).setMaxWidth(0);
            table_view_notes.getColumnModel().getColumn(1).setPreferredWidth(120);
            table_view_notes.getColumnModel().getColumn(1).setMaxWidth(150);
            table_view_notes.getColumnModel().getColumn(3).setPreferredWidth(100);
            table_view_notes.getColumnModel().getColumn(3).setMaxWidth(150);
        }

        panel_note_input.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        hdn_txt_note_id.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        hdn_txt_note_id.setEnabled(false);
        hdn_txt_note_id.setMinimumSize(new java.awt.Dimension(80, 32));
        hdn_txt_note_id.setPreferredSize(new java.awt.Dimension(0, 0));

        lbl_note_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_note_star.setForeground(java.awt.Color.red);
        lbl_note_star.setText("*");

        scroll_pane_notes1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll_pane_notes1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll_pane_notes1.setVerifyInputWhenFocusTarget(false);

        editor_pane_notes.setBorder(null);
        editor_pane_notes.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        editor_pane_notes.setFocusCycleRoot(false);
        editor_pane_notes.setPreferredSize(new java.awt.Dimension(403, 58));
        scroll_pane_notes1.setViewportView(editor_pane_notes);

        lbl_notes.setText("New Note");

        lbl_max_char.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        lbl_max_char.setText("max 1000 charactere");

        javax.swing.GroupLayout panel_note_inputLayout = new javax.swing.GroupLayout(panel_note_input);
        panel_note_input.setLayout(panel_note_inputLayout);
        panel_note_inputLayout.setHorizontalGroup(
            panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_note_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_note_inputLayout.createSequentialGroup()
                        .addGroup(panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hdn_txt_note_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_note_star))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_notes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_max_char)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(scroll_pane_notes1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_note_inputLayout.setVerticalGroup(
            panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_note_inputLayout.createSequentialGroup()
                .addGroup(panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_note_inputLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(hdn_txt_note_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(lbl_note_star, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_note_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_notes)
                        .addComponent(lbl_max_char)))
                .addGap(0, 0, 0)
                .addComponent(scroll_pane_notes1, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addContainerGap())
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
                .addComponent(btn_delete)
                .addGap(18, 18, 18)
                .addComponent(btn_clear_fields)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_fault_buttonsLayout.setVerticalGroup(
            panel_fault_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_fault_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_fault_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_clear_fields, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panel_fault_buttonsLayout.createSequentialGroup()
                        .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btn_add, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_notesLayout = new javax.swing.GroupLayout(panel_notes);
        panel_notes.setLayout(panel_notesLayout);
        panel_notesLayout.setHorizontalGroup(
            panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_notesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroll_pane_notes, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .addGroup(panel_notesLayout.createSequentialGroup()
                        .addComponent(lbl_search_icon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_search_note, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(panel_note_input, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_fault_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_notesLayout.setVerticalGroup(
            panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_notesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_search_note, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_search_icon, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroll_pane_notes, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_note_input, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_fault_buttons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void table_view_notesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_notesMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = table_view_notes.getSelectedRow();
            this.editor_pane_notes.setText(_dtmOrderNote.getValueAt(selectedRow, 2).toString());
            this.editor_pane_notes.setEditable(false);
        }
    }//GEN-LAST:event_table_view_notesMouseClicked

    private void btn_clear_fieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clear_fieldsActionPerformed
        clearFields();
    }//GEN-LAST:event_btn_clear_fieldsActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        OrderNote addOrderNote = getOrderNoteFields(_serviceOrderModel);
        if (addOrderNote != null) {

            long idNoteAdded = _orderNoteController.addOrderNote(addOrderNote);
            if (idNoteAdded > 0) {
                JOptionPane.showMessageDialog(this, CommonConstant.SUCCESS_SAVE_ITEM);
                loadOrderNoteListTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, CommonConstant.ERROR_SAVE_ITEM, null, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        int selectedRow = this.table_view_notes.getSelectedRow();

        if (selectedRow >= 0) {

            OrderNote deleteNote = new OrderNote();

//            deleteNote.setNote((Integer) _dtmFault.getValueAt(selectedRow, 0));
//            deleteNote.setDescription(_dtmFault.getValueAt(selectedRow, 1).toString());
//
//            int confirmDeletion = JOptionPane.showConfirmDialog(this, "Do you really want to delete '"
//                    + deleteNote.getDescription(), "Delete Fault", JOptionPane.YES_NO_OPTION);
//
//            if (confirmDeletion == 0) {
//                boolean isDeleted = this._faultController.deleteFaultController(deleteNote);
//
//                if (isDeleted) {
//                    loadFaultListTable();
//                } else {
//                    JOptionPane.showMessageDialog(this, deleteNote.getDescription() + "could not be deleted!", null, JOptionPane.ERROR_MESSAGE);
//                }
//            }
        }
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void txt_search_noteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search_noteKeyReleased
        searchFault();
    }//GEN-LAST:event_txt_search_noteKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_clear_fields;
    private javax.swing.JButton btn_delete;
    private javax.swing.JEditorPane editor_pane_notes;
    private javax.swing.JTextField hdn_txt_note_id;
    private javax.swing.JLabel lbl_max_char;
    private javax.swing.JLabel lbl_note_star;
    private javax.swing.JLabel lbl_notes;
    private javax.swing.JLabel lbl_search_icon;
    private javax.swing.JPanel panel_fault_buttons;
    private javax.swing.JPanel panel_note_input;
    private javax.swing.JPanel panel_notes;
    private javax.swing.JScrollPane scroll_pane_notes;
    private javax.swing.JScrollPane scroll_pane_notes1;
    private javax.swing.JTable table_view_notes;
    private javax.swing.JTextField txt_search_note;
    // End of variables declaration//GEN-END:variables
}
