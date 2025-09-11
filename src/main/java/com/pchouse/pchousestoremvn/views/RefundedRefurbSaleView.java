package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.common.CommonStrings;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.controllers.RefundController;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.RefurbSale;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.Refund;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.views.modals.DepositModal;
import com.pchouse.pchousestoremvn.views.modals.NoteModal;
import com.pchouse.pchousestoremvn.views.modals.PaymentHistoryModal;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Date;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RefundedRefurbSaleView extends javax.swing.JInternalFrame {

    private Sale _saleModel;    
    private List<Deposit> _orderDeposits;
    private List<SalePayment> _listSalePayments;
    private final RefundController _refundController;
    private final EmployeeController _employeeController;    
    private final DefaultTableModel _dtmRefurb;
    Frame _parentFrame = JOptionPane.getFrameForComponent(this);

    public RefundedRefurbSaleView(Sale saleModel, List<RefurbSale> listRefurbsSale, List<Deposit> listOrderDeposit, List<SalePayment> salePayments) {
        initComponents();

        //avoid auto old value by focus loosing
        this.txt_contact.setFocusLostBehavior(JFormattedTextField.PERSIST);

        CommonExtension.checkEmailFormat(this.txt_email);
        CommonSetting.requestTxtFocus(txt_first_name);
        CommonSetting.tableSettings(table_view_refurbs);

        this._saleModel = saleModel;        
        this._orderDeposits = listOrderDeposit;
        this._listSalePayments = salePayments;

        this._refundController = new RefundController();
        this._employeeController = new EmployeeController();       

        this._dtmRefurb = (DefaultTableModel) this.table_view_refurbs.getModel();

        loadRefurSaleFields(saleModel, listRefurbsSale);
    }

    private void loadRefurSaleFields(Sale saleModel, List<RefurbSale> listRefurbsSale) {
        setCustomerFields(saleModel.getCustomer());

        this.editor_pane_notes.setText(saleModel.getImportantNotes());
        this.lbl_auto_sale_no.setText(CommonStrings.formatOrderNumber(saleModel.getIdSale()));
        this.lbl_total_amount.setText(CommonExtension.formatEuroCurrency(saleModel.getTotal()));
        this.lbl_remaining_amount.setText(CommonExtension.formatEuroCurrency(saleModel.getRemaining()));

        loadRefurbSaleItems(listRefurbsSale);
    }

    private void loadRefurbSaleItems(List<RefurbSale> listRefurbsSale) {
        if (listRefurbsSale != null) {
            _dtmRefurb.setRowCount(0);
            for (RefurbSale orderProdServ : listRefurbsSale) {
                _dtmRefurb.addRow(new Object[]{
                    orderProdServ.getIdSaleRefurb(),
                    orderProdServ.getRefurb().toString(),
                    orderProdServ.getQty(),
                    orderProdServ.getRefurb().getPrice(),
                    orderProdServ.getTotal(),});
            }
        }

        loadOrderDeposit(_orderDeposits);
    }

    private void loadOrderDeposit(List<Deposit> listOrderDeposit) {
        if (listOrderDeposit != null) {
            double totalDeposit = 0;
            for (Deposit orderDeposit : listOrderDeposit) {
                totalDeposit += orderDeposit.getAmount();
            }
            this.lbl_deposit_paid.setText(CommonExtension.formatEuroCurrency(totalDeposit));
        }
    }

    private void setCustomerFields(Customer customer) {
        if (customer != null) {
            this.txt_contact.setFormatterFactory(null);
            this.txt_first_name.setText(customer.getPerson().getFirstName());
            this.txt_last_name.setText(customer.getPerson().getLastName());
            this.txt_contact.setText(customer.getPerson().getContactNo());
            this.txt_email.setText(customer.getPerson().getEmail());
        }
    }    

    private void refundSale() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                CommonConstant.CONFIRM_REFUND_SALE,
                "Confirm Action",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String password = CommonExtension.requestUserPassword();
                Employee employee = _employeeController.getEmployeeByPass(password);

                if (employee == null) {
                    JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, null, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Date createdDate = new Date();
                Refund saleRefund = new Refund(CommonSetting.COMPANY, employee, _saleModel, _saleModel.getTotal(), createdDate);

                OrderNote saleRefundNote = new OrderNote(_saleModel, employee, CommonConstant.SALE_REFUND_NOTE, createdDate);
                long refundId = _refundController.addSaleRefund(saleRefund, saleRefundNote, _listSalePayments);

                if (refundId > 0) {
                    JOptionPane.showMessageDialog(this, CommonConstant.SUCCESS_REFUND);

                }

            } catch (BusinessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_refurb_sale_details = new javax.swing.JPanel();
        panel_input_detail = new javax.swing.JPanel();
        lbl_first_name = new javax.swing.JLabel();
        txt_first_name = new javax.swing.JTextField();
        lbl_last_name = new javax.swing.JLabel();
        txt_last_name = new javax.swing.JTextField();
        lbl_contact = new javax.swing.JLabel();
        txt_contact = new javax.swing.JFormattedTextField();
        btn_copy = new javax.swing.JButton();
        lbl_email = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        scroll_pane_notes = new javax.swing.JScrollPane();
        editor_pane_notes = new javax.swing.JEditorPane();
        lbl_sale_no = new javax.swing.JLabel();
        lbl_auto_sale_no = new javax.swing.JLabel();
        lbl_refurb_sale_picked = new javax.swing.JLabel();
        panel_total_amount = new javax.swing.JPanel();
        lbl_total = new javax.swing.JLabel();
        lbl_deposit = new javax.swing.JLabel();
        lbl_remaining = new javax.swing.JLabel();
        lbl_total_amount = new javax.swing.JLabel();
        lbl_remaining_amount = new javax.swing.JLabel();
        lbl_deposit_paid = new javax.swing.JLabel();
        panel_sale_buttons = new javax.swing.JPanel();
        btn_notes = new javax.swing.JButton();
        btn_deposit = new javax.swing.JButton();
        btn_payments = new javax.swing.JButton();
        scroll_pane_refurbs = new javax.swing.JScrollPane();
        table_view_refurbs = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Refunded Refurb Sale");
        setMaximumSize(new java.awt.Dimension(1049, 700));
        setPreferredSize(new java.awt.Dimension(1050, 650));

        panel_refurb_sale_details.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_refurb_sale_details.setPreferredSize(new java.awt.Dimension(1026, 607));

        panel_input_detail.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_first_name.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_first_name.setText("First Name");
        lbl_first_name.setEnabled(false);

        txt_first_name.setEnabled(false);
        txt_first_name.setFocusCycleRoot(true);
        txt_first_name.setNextFocusableComponent(txt_last_name);
        txt_first_name.setPreferredSize(new java.awt.Dimension(339, 25));

        lbl_last_name.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_last_name.setText("Last Name");
        lbl_last_name.setEnabled(false);

        txt_last_name.setEnabled(false);
        txt_last_name.setNextFocusableComponent(txt_contact);
        txt_last_name.setPreferredSize(new java.awt.Dimension(342, 25));

        lbl_contact.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_contact.setText("Contact No.");
        lbl_contact.setEnabled(false);

        try {
            txt_contact.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(0##) ###-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_contact.setEnabled(false);
        txt_contact.setNextFocusableComponent(txt_email);
        txt_contact.setPreferredSize(new java.awt.Dimension(224, 25));

        btn_copy.setBackground(new java.awt.Color(0, 0, 0));
        btn_copy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_copy.png"))); // NOI18N
        btn_copy.setPreferredSize(new java.awt.Dimension(35, 25));
        btn_copy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_copyActionPerformed(evt);
            }
        });

        lbl_email.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_email.setText("Email");
        lbl_email.setEnabled(false);

        txt_email.setEnabled(false);
        txt_email.setPreferredSize(new java.awt.Dimension(388, 25));

        scroll_pane_notes.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll_pane_notes.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll_pane_notes.setVerifyInputWhenFocusTarget(false);

        editor_pane_notes.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Important Notes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 14))); // NOI18N
        editor_pane_notes.setEnabled(false);
        editor_pane_notes.setFocusCycleRoot(false);
        editor_pane_notes.setPreferredSize(new java.awt.Dimension(403, 58));
        scroll_pane_notes.setViewportView(editor_pane_notes);

        lbl_sale_no.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_sale_no.setText("Sale");
        lbl_sale_no.setEnabled(false);

        lbl_auto_sale_no.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_auto_sale_no.setText("autoGen");
        lbl_auto_sale_no.setEnabled(false);

        lbl_refurb_sale_picked.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_refurb_sale_picked.setForeground(new java.awt.Color(255, 102, 102));
        lbl_refurb_sale_picked.setText("Refurb Sale Refunded");

        javax.swing.GroupLayout panel_input_detailLayout = new javax.swing.GroupLayout(panel_input_detail);
        panel_input_detail.setLayout(panel_input_detailLayout);
        panel_input_detailLayout.setHorizontalGroup(
            panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_input_detailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addComponent(lbl_email)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE))
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addComponent(lbl_contact)
                        .addGap(19, 19, 19)
                        .addComponent(txt_contact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_copy, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_first_name)
                            .addComponent(lbl_last_name))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_last_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_first_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addComponent(lbl_sale_no)
                        .addGap(7, 7, 7)
                        .addComponent(lbl_auto_sale_no)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_refurb_sale_picked)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_input_detailLayout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(scroll_pane_notes, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        panel_input_detailLayout.setVerticalGroup(
            panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_input_detailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_refurb_sale_picked, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_auto_sale_no)
                        .addComponent(lbl_sale_no)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_first_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_first_name))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_last_name)
                    .addComponent(txt_last_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_copy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_contact)
                        .addComponent(txt_contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_email))
                .addGap(281, 281, 281))
            .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_input_detailLayout.createSequentialGroup()
                    .addContainerGap(157, Short.MAX_VALUE)
                    .addComponent(scroll_pane_notes, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        panel_total_amount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_total.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_total.setText("Total:");

        lbl_deposit.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_deposit.setText("Deposit:");

        lbl_remaining.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_remaining.setText("Remaining");

        lbl_total_amount.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_total_amount.setText("totalAmount");

        lbl_remaining_amount.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_remaining_amount.setText("remainingAmount");

        lbl_deposit_paid.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_deposit_paid.setText("depositPaid");

        javax.swing.GroupLayout panel_total_amountLayout = new javax.swing.GroupLayout(panel_total_amount);
        panel_total_amount.setLayout(panel_total_amountLayout);
        panel_total_amountLayout.setHorizontalGroup(
            panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_total_amountLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_total_amountLayout.createSequentialGroup()
                        .addComponent(lbl_total)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_total_amount))
                    .addGroup(panel_total_amountLayout.createSequentialGroup()
                        .addComponent(lbl_deposit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_deposit_paid))
                    .addGroup(panel_total_amountLayout.createSequentialGroup()
                        .addComponent(lbl_remaining)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_remaining_amount)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_total_amountLayout.setVerticalGroup(
            panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_total_amountLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_total)
                    .addComponent(lbl_total_amount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_deposit)
                    .addComponent(lbl_deposit_paid))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_remaining)
                    .addComponent(lbl_remaining_amount))
                .addContainerGap())
        );

        panel_sale_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_notes.setBackground(new java.awt.Color(21, 76, 121));
        btn_notes.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_notes.setForeground(new java.awt.Color(255, 255, 255));
        btn_notes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_notes.png"))); // NOI18N
        btn_notes.setText("Notes");
        btn_notes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_notesActionPerformed(evt);
            }
        });

        btn_deposit.setBackground(new java.awt.Color(21, 76, 121));
        btn_deposit.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_deposit.setForeground(new java.awt.Color(255, 255, 255));
        btn_deposit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_cash_entries.png"))); // NOI18N
        btn_deposit.setText("Deposit");
        btn_deposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_depositActionPerformed(evt);
            }
        });

        btn_payments.setBackground(new java.awt.Color(21, 76, 121));
        btn_payments.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_payments.setForeground(new java.awt.Color(255, 255, 255));
        btn_payments.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_payments_white.png"))); // NOI18N
        btn_payments.setText("Payments");
        btn_payments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_paymentsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_sale_buttonsLayout = new javax.swing.GroupLayout(panel_sale_buttons);
        panel_sale_buttons.setLayout(panel_sale_buttonsLayout);
        panel_sale_buttonsLayout.setHorizontalGroup(
            panel_sale_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_sale_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_notes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_deposit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_payments)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_sale_buttonsLayout.setVerticalGroup(
            panel_sale_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_sale_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_sale_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_notes)
                    .addComponent(btn_deposit)
                    .addComponent(btn_payments))
                .addContainerGap())
        );

        table_view_refurbs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Refurbs", "Qty", "Unit €", "Total €"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_refurbs.setToolTipText(CommonConstant.TBL_REMOVE_ITEM_TOOL_TIP);
        table_view_refurbs.setEnabled(false);
        scroll_pane_refurbs.setViewportView(table_view_refurbs);
        if (table_view_refurbs.getColumnModel().getColumnCount() > 0) {
            table_view_refurbs.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_refurbs.getColumnModel().getColumn(0).setPreferredWidth(0);
            table_view_refurbs.getColumnModel().getColumn(0).setMaxWidth(0);
            table_view_refurbs.getColumnModel().getColumn(2).setPreferredWidth(40);
            table_view_refurbs.getColumnModel().getColumn(2).setMaxWidth(60);
            table_view_refurbs.getColumnModel().getColumn(3).setPreferredWidth(80);
            table_view_refurbs.getColumnModel().getColumn(3).setMaxWidth(120);
            table_view_refurbs.getColumnModel().getColumn(4).setPreferredWidth(80);
            table_view_refurbs.getColumnModel().getColumn(4).setMaxWidth(120);
        }

        javax.swing.GroupLayout panel_refurb_sale_detailsLayout = new javax.swing.GroupLayout(panel_refurb_sale_details);
        panel_refurb_sale_details.setLayout(panel_refurb_sale_detailsLayout);
        panel_refurb_sale_detailsLayout.setHorizontalGroup(
            panel_refurb_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_refurb_sale_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurb_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel_sale_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_refurb_sale_detailsLayout.createSequentialGroup()
                        .addComponent(panel_input_detail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(scroll_pane_refurbs, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panel_total_amount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_refurb_sale_detailsLayout.setVerticalGroup(
            panel_refurb_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_sale_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurb_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel_input_detail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scroll_pane_refurbs))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panel_total_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_sale_buttons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_refurb_sale_details, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_refurb_sale_details, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("New Sale");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_notesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_notesActionPerformed
        NoteModal noteModal = new NoteModal(_saleModel, _parentFrame, true);
        noteModal.setLocationRelativeTo(this);
        noteModal.setVisible(true);
    }//GEN-LAST:event_btn_notesActionPerformed

    private void btn_copyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_copyActionPerformed
        StringSelection stringSelection = new StringSelection(txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));
        if (!this.txt_contact.getText().trim().isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        }
    }//GEN-LAST:event_btn_copyActionPerformed

    private void btn_depositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_depositActionPerformed
        DepositModal depositModal = new DepositModal(_saleModel, _parentFrame, true);
        depositModal.setLocationRelativeTo(this);
        depositModal.setVisible(true);
    }//GEN-LAST:event_btn_depositActionPerformed

    private void btn_paymentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_paymentsActionPerformed
        PaymentHistoryModal paymentHistoryModal = new PaymentHistoryModal(_saleModel, _listSalePayments, _parentFrame, true);
        paymentHistoryModal.setLocationRelativeTo(this);
        paymentHistoryModal.setVisible(true);
    }//GEN-LAST:event_btn_paymentsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_copy;
    private javax.swing.JButton btn_deposit;
    private javax.swing.JButton btn_notes;
    private javax.swing.JButton btn_payments;
    private javax.swing.JEditorPane editor_pane_notes;
    private javax.swing.JLabel lbl_auto_sale_no;
    private javax.swing.JLabel lbl_contact;
    private javax.swing.JLabel lbl_deposit;
    private javax.swing.JLabel lbl_deposit_paid;
    private javax.swing.JLabel lbl_email;
    private javax.swing.JLabel lbl_first_name;
    private javax.swing.JLabel lbl_last_name;
    private javax.swing.JLabel lbl_refurb_sale_picked;
    private javax.swing.JLabel lbl_remaining;
    private javax.swing.JLabel lbl_remaining_amount;
    private javax.swing.JLabel lbl_sale_no;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JLabel lbl_total_amount;
    private javax.swing.JPanel panel_input_detail;
    private javax.swing.JPanel panel_refurb_sale_details;
    private javax.swing.JPanel panel_sale_buttons;
    private javax.swing.JPanel panel_total_amount;
    private javax.swing.JScrollPane scroll_pane_notes;
    private javax.swing.JScrollPane scroll_pane_refurbs;
    private javax.swing.JTable table_view_refurbs;
    private javax.swing.JFormattedTextField txt_contact;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_first_name;
    private javax.swing.JTextField txt_last_name;
    // End of variables declaration//GEN-END:variables

}
