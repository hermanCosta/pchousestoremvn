package com.pchouse.pchousestoremvn.views.modals;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.CompanyController;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderController;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.views.OrderSaleListView;
import java.awt.Window;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class TransferingOrderModal extends javax.swing.JDialog {

    private ServiceOrder _serviceOrderModel;
    private final CompanyController _companyController;
    private final ServiceOrderController _serviceOrderController;
    private final EmployeeController _employeeController;
    private Company selectedTransferCompany = null;

    public TransferingOrderModal(ServiceOrder serviceOrderModel, Window owner, boolean modal) {
        super(owner, ModalityType.APPLICATION_MODAL);
        initComponents();

        this._serviceOrderModel = serviceOrderModel;
        this._companyController = new CompanyController();
        this._serviceOrderController = new ServiceOrderController();
        this._employeeController = new EmployeeController();

        loadTransferFields();
    }

    private void loadTransferFields() {
        List<Company> companies = _companyController.getAllCompanies();

        // Exclude the logged-in company
        Company loggedCompany = CommonSetting.COMPANY;
        companies.removeIf(c -> c.getIdCompany() == loggedCompany.getIdCompany());

        // Add a placeholder as the first item
        Company placeholder = new Company();
        placeholder.setIdCompany(0);
        placeholder.setName("-- Select a company --");
        companies.add(0, placeholder);

        // Populate the combo box
        DefaultComboBoxModel<Company> model = new DefaultComboBoxModel<>(
                companies.toArray(Company[]::new)
        );
        combo_box_shop_list.setModel(model);

        // Set placeholder selected by default
        combo_box_shop_list.setSelectedIndex(0);

        // Add listener for selection changes
        combo_box_shop_list.addActionListener(e -> {
            Company selectedCompany = (Company) combo_box_shop_list.getSelectedItem();

            if (selectedCompany != null && selectedCompany.getIdCompany() != 0) {
                // Show company details
                lbl_shop_deails_value.setText(
                        "<html>"
                        + "<b>Name:</b> " + selectedCompany.getName() + "<br>"
                        + "<b>Address:</b> " + selectedCompany.getAddress() + "<br>"
                        + "<b>Contact:</b> " + selectedCompany.getContactOne()
                        + "</html>"
                );

                // Save the selected company ID for later transfer
                selectedTransferCompany = selectedCompany;

            } else {
                // Placeholder selected: clear label and ID
                lbl_shop_deails_value.setText("");                
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_notes = new javax.swing.JPanel();
        panel_sumary_amount = new javax.swing.JPanel();
        lbl_shop_deails_value = new javax.swing.JLabel();
        lbl_shop_name = new javax.swing.JLabel();
        combo_box_shop_list = new javax.swing.JComboBox<Company>();
        lbl_transfer_shops = new javax.swing.JLabel();
        panel_transfer_buttons = new javax.swing.JPanel();
        btn_transfer = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Payment");
        setModal(true);

        panel_notes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        panel_sumary_amount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_shop_deails_value.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_shop_deails_value.setText("shopDetailsValue");

        javax.swing.GroupLayout panel_sumary_amountLayout = new javax.swing.GroupLayout(panel_sumary_amount);
        panel_sumary_amount.setLayout(panel_sumary_amountLayout);
        panel_sumary_amountLayout.setHorizontalGroup(
            panel_sumary_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_sumary_amountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_shop_deails_value, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_sumary_amountLayout.setVerticalGroup(
            panel_sumary_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_sumary_amountLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lbl_shop_deails_value, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        lbl_shop_name.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_shop_name.setText("Shop name");

        lbl_transfer_shops.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbl_transfer_shops.setText("Transfer order between shoppings");

        panel_transfer_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_transfer.setBackground(new java.awt.Color(21, 76, 121));
        btn_transfer.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_transfer.setForeground(new java.awt.Color(255, 255, 255));
        btn_transfer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_pay.png"))); // NOI18N
        btn_transfer.setText("Transfer");
        btn_transfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_transferActionPerformed(evt);
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

        javax.swing.GroupLayout panel_transfer_buttonsLayout = new javax.swing.GroupLayout(panel_transfer_buttons);
        panel_transfer_buttons.setLayout(panel_transfer_buttonsLayout);
        panel_transfer_buttonsLayout.setHorizontalGroup(
            panel_transfer_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_transfer_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_transfer)
                .addGap(18, 18, 18)
                .addComponent(btn_cancel)
                .addContainerGap(208, Short.MAX_VALUE))
        );
        panel_transfer_buttonsLayout.setVerticalGroup(
            panel_transfer_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_transfer_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_transfer_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_transfer_buttonsLayout.createSequentialGroup()
                        .addComponent(btn_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btn_transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_notesLayout = new javax.swing.GroupLayout(panel_notes);
        panel_notes.setLayout(panel_notesLayout);
        panel_notesLayout.setHorizontalGroup(
            panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_notesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_sumary_amount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_transfer_buttons, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_notesLayout.createSequentialGroup()
                        .addComponent(lbl_shop_name)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(combo_box_shop_list, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_notesLayout.createSequentialGroup()
                        .addComponent(lbl_transfer_shops)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_notesLayout.setVerticalGroup(
            panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_notesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_transfer_shops)
                .addGap(18, 18, 18)
                .addGroup(panel_notesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(combo_box_shop_list, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_shop_name))
                .addGap(18, 18, 18)
                .addComponent(panel_sumary_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel_transfer_buttons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btn_transferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_transferActionPerformed
        try {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    CommonConstant.CONFIRM_TRANSFERING_ORDER,
                    "Confirm Action",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Ask for password and validate employee
            String password = CommonExtension.requestUserPassword();
            Employee employee = _employeeController.getEmployeeByPass(password);

            if (employee == null) {
                JOptionPane.showMessageDialog(
                        this,
                        CommonConstant.NOT_AUTHORIZED,
                        "Access Denied",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Ensure target company is selected
            if (selectedTransferCompany == null || selectedTransferCompany.getIdCompany() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a valid company to transfer the order.",
                        "Invalid Selection",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Set target company on model
            _serviceOrderModel.setCompany(selectedTransferCompany);

            // Create order note for logging
            OrderNote orderNote = new OrderNote(
                    _serviceOrderModel,
                    employee,
                    CommonConstant.ORDER_TRANSFER_NOTE + " " + CommonSetting.COMPANY.getName(),
                    new Date()
            );

            // Execute transfer
            boolean success = _serviceOrderController.transferServiceOrderController(_serviceOrderModel, orderNote);

            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Order successfully transferred to " + selectedTransferCompany.getName() + ".",
                        "Transfer Complete",
                        JOptionPane.INFORMATION_MESSAGE
                );
                
                this.dispose();
                 CommonSetting.openInternalFrame(new OrderSaleListView(OrderSaleListView.TAB_ORDER), "Created Order View");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to transfer order. Please check logs for details.",
                        "Transfer Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "An unexpected error occurred while transferring the order:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btn_transferActionPerformed

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_cancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_transfer;
    private javax.swing.JComboBox combo_box_shop_list;
    private javax.swing.JLabel lbl_shop_deails_value;
    private javax.swing.JLabel lbl_shop_name;
    private javax.swing.JLabel lbl_transfer_shops;
    private javax.swing.JPanel panel_notes;
    private javax.swing.JPanel panel_sumary_amount;
    private javax.swing.JPanel panel_transfer_buttons;
    // End of variables declaration//GEN-END:variables
}
