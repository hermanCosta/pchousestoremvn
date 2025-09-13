package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.common.CommonStrings;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.controllers.RefundController;
import com.pchouse.pchousestoremvn.controllers.SalePaymentController;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.Refund;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.SaleProdServ;
import com.pchouse.pchousestoremvn.util.ReportGenerator;
import com.pchouse.pchousestoremvn.views.modals.DepositModal;
import com.pchouse.pchousestoremvn.views.modals.NoteModal;
import com.pchouse.pchousestoremvn.views.modals.PaymentHistoryModal;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class PickedSaleView extends javax.swing.JInternalFrame {

    private Sale _saleModel;
    private List<SaleProdServ> _listSaleProdServs;
    private List<SalePayment> _salePayments;
    private List<Deposit> _listOrderDeposit;
    public SalePayment _orderPayment = null;
    private final EmployeeController _employeeController;
    private final RefundController _refundController;
    private final SalePaymentController _salePaymentController;
    private final DefaultTableModel _dtmProdServ;

    public PickedSaleView(Sale saleModel, List<SaleProdServ> listSaleProdServ, List<Deposit> listOrderDeposit, List<SalePayment> salePayments) {
        initComponents();

        CommonExtension.checkEmailFormat(this.txt_email);
        CommonSetting.requestTxtFocus(txt_first_name);
        CommonSetting.tableSettings(table_view_products);

        this._saleModel = saleModel;
        this._listSaleProdServs = listSaleProdServ;
        this._listOrderDeposit = listOrderDeposit;
        this._salePayments = salePayments;
        this._employeeController = new EmployeeController();
        this._refundController = new RefundController();
        this._salePaymentController = new SalePaymentController();
        this._dtmProdServ = (DefaultTableModel) this.table_view_products.getModel();

        loadSaleFields(saleModel, listSaleProdServ);
    }

    private void loadSaleFields(Sale orderModel, List<SaleProdServ> listSaleProdServ) {
        setCustomerFields(orderModel.getCustomer());

        this.lbl_auto_sale_no.setText(CommonStrings.formatOrderNumber(orderModel.getIdSale()));
        this.lbl_total_field.setText(CommonExtension.formatEuroCurrency(orderModel.getTotal()));

        loadSaleProdServ(listSaleProdServ);
    }

    private void loadSaleProdServ(List<SaleProdServ> listSaleProdServ) {
        if (listSaleProdServ != null) {
            _dtmProdServ.setRowCount(0);
            for (SaleProdServ orderProdServ : listSaleProdServ) {
                _dtmProdServ.addRow(new Object[]{
                    orderProdServ.getProdServ().getIdProductService(),
                    orderProdServ.getProdServ().getProdServName(),
                    orderProdServ.getQty(),
                    orderProdServ.getProdServ().getPrice(),
                    orderProdServ.getTotal(),
                    orderProdServ.getIdSaleProdServ()
                });
            }
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
                long refundId = _refundController.addSaleRefund(saleRefund, saleRefundNote, _salePayments);

                if (refundId > 0) {
                    _salePayments = _salePaymentController.getSalePayments(_saleModel);
                    RefundedSaleView refundedSaleView = new RefundedSaleView(_saleModel, _listSaleProdServs, _listOrderDeposit, _salePayments);
                    CommonSetting.openInternalFrame(refundedSaleView, "Refunded Sale: " + _saleModel.getIdSale());
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

        panel_sale_details = new javax.swing.JPanel();
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
        lbl_sale_no = new javax.swing.JLabel();
        lbl_auto_sale_no = new javax.swing.JLabel();
        lbl_sale_picked = new javax.swing.JLabel();
        panel_total_amount = new javax.swing.JPanel();
        lbl_total = new javax.swing.JLabel();
        lbl_total_field = new javax.swing.JLabel();
        panel_sale_buttons = new javax.swing.JPanel();
        btn_refund_sale = new javax.swing.JButton();
        btn_notes = new javax.swing.JButton();
        btn_deposit = new javax.swing.JButton();
        btn_print = new javax.swing.JButton();
        btn_payments = new javax.swing.JButton();
        scroll_pane_products = new javax.swing.JScrollPane();
        table_view_products = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Sale");
        setMaximumSize(new java.awt.Dimension(1049, 700));
        setPreferredSize(new java.awt.Dimension(1050, 650));

        panel_sale_details.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_sale_details.setPreferredSize(new java.awt.Dimension(1026, 607));

        panel_input_detail.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_first_name.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_first_name.setText("First Name");

        txt_first_name.setEnabled(false);
        txt_first_name.setFocusCycleRoot(true);
        txt_first_name.setNextFocusableComponent(txt_last_name);
        txt_first_name.setPreferredSize(new java.awt.Dimension(339, 25));

        lbl_last_name.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_last_name.setText("Last Name");

        txt_last_name.setEnabled(false);
        txt_last_name.setNextFocusableComponent(txt_contact);
        txt_last_name.setPreferredSize(new java.awt.Dimension(342, 25));

        lbl_contact.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_contact.setText("Contact No.");

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

        txt_email.setEnabled(false);
        txt_email.setPreferredSize(new java.awt.Dimension(388, 25));

        lbl_sale_no.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_sale_no.setText("Sale");

        lbl_auto_sale_no.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_auto_sale_no.setText("autoGen");

        lbl_sale_picked.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_sale_picked.setForeground(new java.awt.Color(0, 153, 204));
        lbl_sale_picked.setText("SALE PICKED");

        javax.swing.GroupLayout panel_input_detailLayout = new javax.swing.GroupLayout(panel_input_detail);
        panel_input_detail.setLayout(panel_input_detailLayout);
        panel_input_detailLayout.setHorizontalGroup(
            panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_input_detailLayout.createSequentialGroup()
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_email)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE))
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_first_name)
                            .addComponent(lbl_last_name))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_last_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_first_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addComponent(lbl_contact)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_contact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_copy, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addComponent(lbl_sale_no)
                        .addGap(7, 7, 7)
                        .addComponent(lbl_auto_sale_no)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_sale_picked)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_input_detailLayout.setVerticalGroup(
            panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_input_detailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_auto_sale_no)
                    .addComponent(lbl_sale_no)
                    .addComponent(lbl_sale_picked))
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
                .addContainerGap(315, Short.MAX_VALUE))
        );

        panel_total_amount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_total.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_total.setText("Total:");

        lbl_total_field.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

        javax.swing.GroupLayout panel_total_amountLayout = new javax.swing.GroupLayout(panel_total_amount);
        panel_total_amount.setLayout(panel_total_amountLayout);
        panel_total_amountLayout.setHorizontalGroup(
            panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_total_amountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_total)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_total_field, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_total_amountLayout.setVerticalGroup(
            panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_total_amountLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_total_field, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_sale_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_refund_sale.setBackground(new java.awt.Color(0, 0, 0));
        btn_refund_sale.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_refund_sale.setForeground(new java.awt.Color(255, 255, 255));
        btn_refund_sale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_refund.png"))); // NOI18N
        btn_refund_sale.setText("Refund");
        btn_refund_sale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refund_saleActionPerformed(evt);
            }
        });

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

        btn_print.setBackground(new java.awt.Color(21, 76, 121));
        btn_print.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_print.setForeground(new java.awt.Color(255, 255, 255));
        btn_print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_print.png"))); // NOI18N
        btn_print.setText("Print");
        btn_print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_printActionPerformed(evt);
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
                .addComponent(btn_refund_sale)
                .addGap(12, 12, 12)
                .addComponent(btn_notes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_deposit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_payments)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_print)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_sale_buttonsLayout.setVerticalGroup(
            panel_sale_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_sale_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_sale_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_refund_sale)
                    .addComponent(btn_notes)
                    .addComponent(btn_deposit)
                    .addComponent(btn_print)
                    .addComponent(btn_payments))
                .addContainerGap())
        );

        table_view_products.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product | Service", "Qty", "Unit €", "Total €"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_products.setToolTipText(CommonConstant.TBL_REMOVE_ITEM_TOOL_TIP);
        table_view_products.setEnabled(false);
        scroll_pane_products.setViewportView(table_view_products);
        if (table_view_products.getColumnModel().getColumnCount() > 0) {
            table_view_products.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_products.getColumnModel().getColumn(0).setPreferredWidth(0);
            table_view_products.getColumnModel().getColumn(0).setMaxWidth(0);
            table_view_products.getColumnModel().getColumn(2).setPreferredWidth(40);
            table_view_products.getColumnModel().getColumn(2).setMaxWidth(60);
            table_view_products.getColumnModel().getColumn(3).setPreferredWidth(80);
            table_view_products.getColumnModel().getColumn(3).setMaxWidth(120);
            table_view_products.getColumnModel().getColumn(4).setPreferredWidth(80);
            table_view_products.getColumnModel().getColumn(4).setMaxWidth(120);
        }

        javax.swing.GroupLayout panel_sale_detailsLayout = new javax.swing.GroupLayout(panel_sale_details);
        panel_sale_details.setLayout(panel_sale_detailsLayout);
        panel_sale_detailsLayout.setHorizontalGroup(
            panel_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_sale_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel_sale_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_sale_detailsLayout.createSequentialGroup()
                        .addComponent(panel_input_detail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scroll_pane_products, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addComponent(panel_total_amount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_sale_detailsLayout.setVerticalGroup(
            panel_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_sale_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroll_pane_products, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                    .addComponent(panel_input_detail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
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
                .addComponent(panel_sale_details, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_sale_details, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_copyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_copyActionPerformed
        StringSelection stringSelection = new StringSelection(txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));
        if (!this.txt_contact.getText().trim().isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        }
    }//GEN-LAST:event_btn_copyActionPerformed

    private void btn_refund_saleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refund_saleActionPerformed
        refundSale();
    }//GEN-LAST:event_btn_refund_saleActionPerformed

    private void btn_notesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_notesActionPerformed
        NoteModal noteModal = new NoteModal(_saleModel, SwingUtilities.getWindowAncestor(this), true);
        noteModal.setLocationRelativeTo(this);
        noteModal.setVisible(true);
    }//GEN-LAST:event_btn_notesActionPerformed

    private void btn_depositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_depositActionPerformed
        DepositModal depositModal = new DepositModal(_saleModel, SwingUtilities.getWindowAncestor(this), true);
        depositModal.setLocationRelativeTo(this);
        depositModal.setVisible(true);
    }//GEN-LAST:event_btn_depositActionPerformed

    private void btn_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_printActionPerformed
        // Genarate and display the report
        new ReportGenerator().generateSaleReceiptReport(_saleModel, _listSaleProdServs, _salePayments);
    }//GEN-LAST:event_btn_printActionPerformed

    private void btn_paymentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_paymentsActionPerformed
        PaymentHistoryModal paymentHistoryModal = new PaymentHistoryModal(_saleModel, _salePayments, SwingUtilities.getWindowAncestor(this), true);
        paymentHistoryModal.setLocationRelativeTo(this);
        paymentHistoryModal.setVisible(true);
    }//GEN-LAST:event_btn_paymentsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_copy;
    private javax.swing.JButton btn_deposit;
    private javax.swing.JButton btn_notes;
    private javax.swing.JButton btn_payments;
    private javax.swing.JButton btn_print;
    private javax.swing.JButton btn_refund_sale;
    private javax.swing.JLabel lbl_auto_sale_no;
    private javax.swing.JLabel lbl_contact;
    private javax.swing.JLabel lbl_email;
    private javax.swing.JLabel lbl_first_name;
    private javax.swing.JLabel lbl_last_name;
    private javax.swing.JLabel lbl_sale_no;
    private javax.swing.JLabel lbl_sale_picked;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JLabel lbl_total_field;
    private javax.swing.JPanel panel_input_detail;
    private javax.swing.JPanel panel_sale_buttons;
    private javax.swing.JPanel panel_sale_details;
    private javax.swing.JPanel panel_total_amount;
    private javax.swing.JScrollPane scroll_pane_products;
    private javax.swing.JTable table_view_products;
    private javax.swing.JFormattedTextField txt_contact;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_first_name;
    private javax.swing.JTextField txt_last_name;
    // End of variables declaration//GEN-END:variables

}
