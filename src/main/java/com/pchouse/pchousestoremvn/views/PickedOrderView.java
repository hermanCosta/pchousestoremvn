package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.common.CommonStrings;
import com.pchouse.pchousestoremvn.controllers.DeviceController;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.controllers.OrderNoteController;
import com.pchouse.pchousestoremvn.controllers.RefundController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderFaultController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderProdServController;
import com.pchouse.pchousestoremvn.enums.OrderStatus;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Device;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.Refund;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderFault;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import com.pchouse.pchousestoremvn.util.ReportGenerator;
import com.pchouse.pchousestoremvn.views.modals.DepositModal;
import com.pchouse.pchousestoremvn.views.modals.NoteModal;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PickedOrderView extends javax.swing.JInternalFrame {

    private final ServiceOrderController _orderController;
    private final EmployeeController _employeeController;
    private final ServiceOrderProdServController _orderProdServController;
    private final ServiceOrderFaultController _orderFaultController;
    private final OrderNoteController _orderNoteController;
    private final DeviceController _deviceController;
    private final RefundController _refundController;
    private final DefaultTableModel _dtmProdServ;
    private final DefaultTableModel _dtmFault;
    Frame _parentFrame = JOptionPane.getFrameForComponent(this);

    private ServiceOrder _serviceOrderModel;
    private List<ServiceOrderFault> _serviceOrderFaults;
    private List<ServiceOrderProdServ> _serviceOrderProdServs;
    private List<Deposit> _orderDeposits;
    private List<ServiceOrderPayment> _serviceOrderPayments;
    
    public PickedOrderView(ServiceOrder orderModel, List<ServiceOrderFault> listOrderFault, List<ServiceOrderProdServ> listOrderProdServ, List<Deposit> listOrderDeposit, List<ServiceOrderPayment> serviceOrderPayments ) {
        initComponents();

        //avoid auto old value by focus loosing
        this.txt_contact.setFocusLostBehavior(JFormattedTextField.PERSIST);

        CommonExtension.checkEmailFormat(this.txt_email);
        CommonSetting.requestTxtFocus(txt_first_name);
        CommonSetting.tableSettings(table_view_faults);
        CommonSetting.tableSettings(table_view_products);

        this._orderController = new ServiceOrderController();
        this._employeeController = new EmployeeController();
        this._orderProdServController = new ServiceOrderProdServController();
        this._orderFaultController = new ServiceOrderFaultController();
        this._orderNoteController = new OrderNoteController();
        this._deviceController = new DeviceController();
        this._refundController = new RefundController();
        this._dtmProdServ = (DefaultTableModel) this.table_view_products.getModel();
        this._dtmFault = (DefaultTableModel) this.table_view_faults.getModel();

        this._serviceOrderModel = orderModel;
        this._serviceOrderFaults = listOrderFault;
        this._serviceOrderProdServs = listOrderProdServ;
        this._orderDeposits = listOrderDeposit;
        this._serviceOrderPayments = serviceOrderPayments;
        loadOrderFields(orderModel, listOrderFault, listOrderProdServ, listOrderDeposit);
    }

    private void loadOrderFields(ServiceOrder orderModel, List<ServiceOrderFault> listOrderFault, List<ServiceOrderProdServ> listOrderProdServ, List<Deposit> listOrderDeposit) {
        setCustomerFields(orderModel.getCustomer());
        setDeviceFields(orderModel.getDevice());

        this.lbl_auto_order_no.setText(CommonStrings.formatOrderNumber(orderModel.getIdServiceOrder()));
        this.spn_bad_sectors.setValue(orderModel.getBadSector());
        this.editor_pane_notes.setText(orderModel.getNote());
        this.lbl_total_field.setText(CommonExtension.formatEuroCurrency(orderModel.getTotal()));
        this.lbl_due_field.setText(CommonExtension.formatEuroCurrency(orderModel.getDue()));

        loadOrderDeposit(listOrderDeposit);
        loadOrderFault(listOrderFault);
        loadOrderProdServ(listOrderProdServ);
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

    private void loadOrderFault(List<ServiceOrderFault> listOrderFault) {
        if (listOrderFault != null) {
            _dtmFault.setRowCount(0);
            for (ServiceOrderFault orderFault : listOrderFault) {
                _dtmFault.addRow(new Object[]{
                    orderFault.getFault().getIdFault(),
                    orderFault.getFault().getDescription(),
                    orderFault.getIdServiceOrderFault()
                });
            }
        }
    }

    private void loadOrderProdServ(List<ServiceOrderProdServ> listOrderProdServ) {
        if (listOrderProdServ != null) {
            _dtmProdServ.setRowCount(0);
            for (ServiceOrderProdServ orderProdServ : listOrderProdServ) {
                _dtmProdServ.addRow(new Object[]{
                    orderProdServ.getProdServ().getIdProductService(),
                    orderProdServ.getProdServ().getProdServName(),
                    orderProdServ.getQty(),
                    orderProdServ.getProdServ().getPrice(),
                    orderProdServ.getTotal(),
                    orderProdServ.getIdServiceOrderProdServ()
                });
            }
        }
    }

    // Method must be public, because receive data back from customerModal
    public void setCustomerFields(Customer customer) {
        if (customer != null) {
            this.txt_contact.setFormatterFactory(null);
            this.hdn_txt_customer_id.setText(String.valueOf(customer.getIdCustomer()));
            this.txt_first_name.setText(customer.getPerson().getFirstName());
            this.txt_last_name.setText(customer.getPerson().getLastName());
            this.txt_contact.setText(customer.getPerson().getContactNo());
            this.txt_email.setText(customer.getPerson().getEmail());

            this.txt_brand.requestFocus();
        }
    }

    private void setDeviceFields(Device device) {
        if (device != null) {
            this.txt_brand.setText(device.getBrand());
            this.txt_model.setText(device.getModel());
            this.txt_serial_number.setText(device.getSerialNumber());
        }
    }

    private void getPriceSum() {
        double sum = 0;
        for (int i = 0; i < this._dtmProdServ.getRowCount(); i++) {
            sum += Double.parseDouble(this._dtmProdServ.getValueAt(i, 3).toString());
        }

        this.lbl_total_field.setText(String.valueOf(sum));
        this.lbl_due_field.setText(this.lbl_total_field.getText());
    }
   
    private void refundServiceOrder() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                CommonConstant.CONFIRM_REFUND_ORDER,
                "Confirm Action",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String password = CommonExtension.requestUserPassword();
                Employee employee = _employeeController.getEmployeeByPass(password);

                if (employee != null) {
                    Date createdDate = new Date();

                    Refund orderRefund = new Refund(CommonSetting.COMPANY, employee, _serviceOrderModel, _serviceOrderModel.getTotal(), createdDate);
                    OrderNote orderNoteRefundNote = new OrderNote(_serviceOrderModel, employee, CommonConstant.ORDER_REFUND_NOTE, createdDate);
                    long refundId = _refundController.addRefund(orderRefund, orderNoteRefundNote);

                    if (refundId > 0) {
                        JOptionPane.showMessageDialog(this, CommonConstant.SUCCESS_REFUND);

                        RefundOrderView refundOrderView = new RefundOrderView(_serviceOrderModel, _serviceOrderFaults, _serviceOrderProdServs, _orderDeposits);
                        CommonSetting.openInternalFrame(refundOrderView, "Refunded Order: " + _serviceOrderModel.getIdServiceOrder());
                    }

                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, null, JOptionPane.ERROR_MESSAGE);
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

        panel_order_details = new javax.swing.JPanel();
        panel_input_detail = new javax.swing.JPanel();
        lbl_order_no = new javax.swing.JLabel();
        lbl_auto_order_no = new javax.swing.JLabel();
        lbl_first_name = new javax.swing.JLabel();
        txt_first_name = new javax.swing.JTextField();
        lbl_last_name = new javax.swing.JLabel();
        txt_last_name = new javax.swing.JTextField();
        lbl_contact = new javax.swing.JLabel();
        txt_contact = new javax.swing.JFormattedTextField();
        btn_copy = new javax.swing.JButton();
        lbl_email = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        lbl_brand = new javax.swing.JLabel();
        txt_brand = new javax.swing.JTextField();
        lbl_model = new javax.swing.JLabel();
        txt_model = new javax.swing.JTextField();
        lbl_sn = new javax.swing.JLabel();
        txt_serial_number = new javax.swing.JTextField();
        scroll_pane_notes = new javax.swing.JScrollPane();
        editor_pane_notes = new javax.swing.JEditorPane();
        lbl_first_name_star = new javax.swing.JLabel();
        lbl_last_name_star = new javax.swing.JLabel();
        lbl_contact_star = new javax.swing.JLabel();
        lbl_dev_model_star = new javax.swing.JLabel();
        lbl_serial_number_star = new javax.swing.JLabel();
        lbl_bad_sectors_star = new javax.swing.JLabel();
        lbl_bad_sectors = new javax.swing.JLabel();
        lbl_dev_brand_star = new javax.swing.JLabel();
        hdn_txt_customer_id = new javax.swing.JTextField();
        spn_bad_sectors = new javax.swing.JSpinner();
        lbl_auto_order_no1 = new javax.swing.JLabel();
        panel_total_amount = new javax.swing.JPanel();
        lbl_total = new javax.swing.JLabel();
        lbl_deposit = new javax.swing.JLabel();
        lbl_due = new javax.swing.JLabel();
        lbl_total_field = new javax.swing.JLabel();
        lbl_due_field = new javax.swing.JLabel();
        lbl_deposit_paid = new javax.swing.JLabel();
        panel_order_buttons = new javax.swing.JPanel();
        btn_notes = new javax.swing.JButton();
        btn_deposit = new javax.swing.JButton();
        btn_refund_sale = new javax.swing.JButton();
        btn_print = new javax.swing.JButton();
        scroll_pane_products = new javax.swing.JScrollPane();
        table_view_products = new javax.swing.JTable();
        scroll_pane_faults = new javax.swing.JScrollPane();
        table_view_faults = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Created Order View");
        setMaximumSize(new java.awt.Dimension(1049, 700));
        setPreferredSize(new java.awt.Dimension(1050, 650));

        panel_order_details.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_order_details.setPreferredSize(new java.awt.Dimension(1026, 607));

        panel_input_detail.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_order_no.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_order_no.setText("Order");

        lbl_auto_order_no.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_auto_order_no.setText("autoGen");

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
        txt_email.setNextFocusableComponent(txt_brand);
        txt_email.setPreferredSize(new java.awt.Dimension(388, 25));

        lbl_brand.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_brand.setText("Device Brand");

        txt_brand.setEnabled(false);
        txt_brand.setMinimumSize(new java.awt.Dimension(63, 20));
        txt_brand.setNextFocusableComponent(txt_model);
        txt_brand.setPreferredSize(new java.awt.Dimension(322, 25));
        txt_brand.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_brandKeyPressed(evt);
            }
        });

        lbl_model.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_model.setText("Device Model");

        txt_model.setEnabled(false);
        txt_model.setNextFocusableComponent(txt_serial_number);
        txt_model.setPreferredSize(new java.awt.Dimension(320, 25));
        txt_model.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_modelKeyPressed(evt);
            }
        });

        lbl_sn.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_sn.setText("Serial Number");

        txt_serial_number.setEnabled(false);
        txt_serial_number.setNextFocusableComponent(editor_pane_notes);
        txt_serial_number.setPreferredSize(new java.awt.Dimension(313, 25));
        txt_serial_number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_serial_numberKeyPressed(evt);
            }
        });

        scroll_pane_notes.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll_pane_notes.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll_pane_notes.setVerifyInputWhenFocusTarget(false);

        editor_pane_notes.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Important Notes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 14))); // NOI18N
        editor_pane_notes.setEnabled(false);
        editor_pane_notes.setFocusCycleRoot(false);
        editor_pane_notes.setPreferredSize(new java.awt.Dimension(403, 58));
        editor_pane_notes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                editor_pane_notesKeyPressed(evt);
            }
        });
        scroll_pane_notes.setViewportView(editor_pane_notes);

        lbl_first_name_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_first_name_star.setForeground(java.awt.Color.red);
        lbl_first_name_star.setText("*");

        lbl_last_name_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_last_name_star.setForeground(java.awt.Color.red);
        lbl_last_name_star.setText("*");

        lbl_contact_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_contact_star.setForeground(java.awt.Color.red);
        lbl_contact_star.setText("*");

        lbl_dev_model_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_dev_model_star.setForeground(java.awt.Color.red);
        lbl_dev_model_star.setText("*");

        lbl_serial_number_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_serial_number_star.setForeground(java.awt.Color.red);
        lbl_serial_number_star.setText("*");

        lbl_bad_sectors_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_bad_sectors_star.setForeground(java.awt.Color.red);
        lbl_bad_sectors_star.setText("*");

        lbl_bad_sectors.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_bad_sectors.setText("Bad Sectors");

        lbl_dev_brand_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_dev_brand_star.setForeground(java.awt.Color.red);
        lbl_dev_brand_star.setText("*");

        hdn_txt_customer_id.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        hdn_txt_customer_id.setEnabled(false);
        hdn_txt_customer_id.setMinimumSize(new java.awt.Dimension(80, 32));
        hdn_txt_customer_id.setPreferredSize(new java.awt.Dimension(0, 0));

        spn_bad_sectors.setEnabled(false);

        lbl_auto_order_no1.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_auto_order_no1.setForeground(new java.awt.Color(0, 153, 204));
        lbl_auto_order_no1.setText("ORDER PICKED");

        javax.swing.GroupLayout panel_input_detailLayout = new javax.swing.GroupLayout(panel_input_detail);
        panel_input_detail.setLayout(panel_input_detailLayout);
        panel_input_detailLayout.setHorizontalGroup(
            panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_input_detailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_last_name_star, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_contact_star, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_input_detailLayout.createSequentialGroup()
                                .addComponent(lbl_contact)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_contact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_copy, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_input_detailLayout.createSequentialGroup()
                                .addComponent(lbl_last_name)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_last_name, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
                            .addGroup(panel_input_detailLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txt_first_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(scroll_pane_notes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbl_bad_sectors_star, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_serial_number_star, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_dev_model_star, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_dev_brand_star, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_input_detailLayout.createSequentialGroup()
                                .addComponent(lbl_brand)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_brand, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_input_detailLayout.createSequentialGroup()
                                .addComponent(lbl_sn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_serial_number, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panel_input_detailLayout.createSequentialGroup()
                                .addComponent(lbl_bad_sectors)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spn_bad_sectors))
                            .addGroup(panel_input_detailLayout.createSequentialGroup()
                                .addComponent(lbl_model)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_model, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addComponent(lbl_email)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_input_detailLayout.createSequentialGroup()
                                .addComponent(lbl_first_name_star, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_first_name))
                            .addGroup(panel_input_detailLayout.createSequentialGroup()
                                .addComponent(lbl_order_no)
                                .addGap(7, 7, 7)
                                .addComponent(lbl_auto_order_no)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_auto_order_no1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_input_detailLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(hdn_txt_customer_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(437, Short.MAX_VALUE)))
        );
        panel_input_detailLayout.setVerticalGroup(
            panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_input_detailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_auto_order_no)
                    .addComponent(lbl_order_no)
                    .addComponent(lbl_auto_order_no1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_first_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_first_name_star, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_first_name))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_last_name)
                    .addComponent(txt_last_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_last_name_star, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_copy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_contact)
                        .addComponent(txt_contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbl_contact_star, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_email))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_brand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_brand)
                    .addComponent(lbl_dev_brand_star, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_model, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_model))
                    .addComponent(lbl_dev_model_star, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_serial_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_sn)
                    .addComponent(lbl_serial_number_star))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_bad_sectors)
                    .addComponent(lbl_bad_sectors_star)
                    .addComponent(spn_bad_sectors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(scroll_pane_notes, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_input_detailLayout.createSequentialGroup()
                    .addContainerGap(27, Short.MAX_VALUE)
                    .addComponent(hdn_txt_customer_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(373, Short.MAX_VALUE)))
        );

        panel_total_amount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_total.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_total.setText("Total:");

        lbl_deposit.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_deposit.setText("Deposit:");

        lbl_due.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_due.setText("Due:");

        lbl_total_field.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_total_field.setText("totalFiled");

        lbl_due_field.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_due_field.setText("totalDue");

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
                        .addComponent(lbl_total_field))
                    .addGroup(panel_total_amountLayout.createSequentialGroup()
                        .addComponent(lbl_due)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_due_field))
                    .addGroup(panel_total_amountLayout.createSequentialGroup()
                        .addComponent(lbl_deposit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_deposit_paid)))
                .addContainerGap(293, Short.MAX_VALUE))
        );
        panel_total_amountLayout.setVerticalGroup(
            panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_total_amountLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_total)
                    .addComponent(lbl_total_field))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_deposit)
                    .addComponent(lbl_deposit_paid))
                .addGap(14, 14, 14)
                .addGroup(panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_due)
                    .addComponent(lbl_due_field))
                .addContainerGap())
        );

        panel_order_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_notes.setBackground(new java.awt.Color(21, 76, 121));
        btn_notes.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_notes.setForeground(new java.awt.Color(255, 255, 255));
        btn_notes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_notes.png"))); // NOI18N
        btn_notes.setText("Notes");
        btn_notes.setNextFocusableComponent(txt_first_name);
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
        btn_deposit.setNextFocusableComponent(txt_first_name);
        btn_deposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_depositActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout panel_order_buttonsLayout = new javax.swing.GroupLayout(panel_order_buttons);
        panel_order_buttons.setLayout(panel_order_buttonsLayout);
        panel_order_buttonsLayout.setHorizontalGroup(
            panel_order_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_order_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_refund_sale)
                .addGap(12, 12, 12)
                .addComponent(btn_notes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_deposit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_print)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_order_buttonsLayout.setVerticalGroup(
            panel_order_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_order_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_order_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_notes)
                    .addComponent(btn_deposit)
                    .addComponent(btn_refund_sale)
                    .addComponent(btn_print))
                .addContainerGap())
        );

        table_view_products.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product | Service", "Qty", "Unit €", "Total €", "OrderProdServId"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_products.setToolTipText(CommonConstant.TBL_REMOVE_ITEM_TOOL_TIP);
        table_view_products.setEnabled(false);
        table_view_products.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_productsMouseClicked(evt);
            }
        });
        table_view_products.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                table_view_productsKeyReleased(evt);
            }
        });
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
            table_view_products.getColumnModel().getColumn(5).setMinWidth(0);
            table_view_products.getColumnModel().getColumn(5).setPreferredWidth(0);
            table_view_products.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        table_view_faults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fault Description", "OrderFaultID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_faults.setToolTipText(CommonConstant.TBL_REMOVE_ITEM_TOOL_TIP);
        table_view_faults.setEnabled(false);
        table_view_faults.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_faultsMouseClicked(evt);
            }
        });
        scroll_pane_faults.setViewportView(table_view_faults);
        if (table_view_faults.getColumnModel().getColumnCount() > 0) {
            table_view_faults.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_faults.getColumnModel().getColumn(0).setPreferredWidth(0);
            table_view_faults.getColumnModel().getColumn(0).setMaxWidth(0);
            table_view_faults.getColumnModel().getColumn(2).setMinWidth(0);
            table_view_faults.getColumnModel().getColumn(2).setPreferredWidth(0);
            table_view_faults.getColumnModel().getColumn(2).setMaxWidth(0);
        }

        javax.swing.GroupLayout panel_order_detailsLayout = new javax.swing.GroupLayout(panel_order_details);
        panel_order_details.setLayout(panel_order_detailsLayout);
        panel_order_detailsLayout.setHorizontalGroup(
            panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_order_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_order_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_order_detailsLayout.createSequentialGroup()
                        .addGroup(panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panel_input_detail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panel_total_amount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scroll_pane_products, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                            .addComponent(scroll_pane_faults))))
                .addContainerGap())
        );
        panel_order_detailsLayout.setVerticalGroup(
            panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_order_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_order_detailsLayout.createSequentialGroup()
                        .addComponent(scroll_pane_faults, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(23, 23, 23)
                        .addComponent(scroll_pane_products, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_order_detailsLayout.createSequentialGroup()
                        .addComponent(panel_input_detail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panel_total_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_order_buttons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_order_details, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_order_details, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_brandKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_brandKeyPressed
        //Suggest autoComplete brand from Database
        List<String> listBrand = _deviceController.searchBrand(this.txt_brand.getText().toUpperCase());

        if (listBrand != null) {
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_BACK_SPACE:
                    break;

                case KeyEvent.VK_ENTER:
                    txt_brand.setText(this.txt_brand.getText());
                    break;
                default:
                    EventQueue.invokeLater(() -> {
                        CommonExtension.autoCompleteTextField(listBrand, this.txt_brand.getText(), this.txt_brand);
                    });
            }
        }
    }//GEN-LAST:event_txt_brandKeyPressed

    private void txt_modelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_modelKeyPressed
        //Suggest autoComplete model from Database
        List<String> listModel = _deviceController.searchModel(this.txt_brand.getText().toUpperCase(), this.txt_model.getText().toUpperCase());
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
                break;

            case KeyEvent.VK_ENTER:
                txt_model.setText(this.txt_model.getText());
                break;
            default:
                EventQueue.invokeLater(() -> {
                    CommonExtension.autoCompleteTextField(listModel, this.txt_model.getText(), this.txt_model);
                });
        }
    }//GEN-LAST:event_txt_modelKeyPressed

    private void editor_pane_notesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editor_pane_notesKeyPressed
        if (evt.isShiftDown() && evt.getKeyCode() == KeyEvent.VK_TAB) {
            txt_serial_number.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
        }
    }//GEN-LAST:event_editor_pane_notesKeyPressed

    private void btn_copyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_copyActionPerformed
        StringSelection stringSelection = new StringSelection(txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));
        if (!this.txt_contact.getText().trim().isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        }
    }//GEN-LAST:event_btn_copyActionPerformed

    private void table_view_faultsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_faultsMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = this.table_view_faults.getSelectedRow();

            if (this._dtmFault.getValueAt(selectedRow, 2) != null) {
                String password = CommonExtension.requestUserPassword();
                Employee employee = _employeeController.getEmployeeByPass(password);
                if (employee != null) {

                    long idOrderFaultDeleted = this._orderFaultController.deleteOrderFault((long) this._dtmFault.getValueAt(selectedRow, 2));

                    if (idOrderFaultDeleted > 0) {
                        this._dtmFault.removeRow(table_view_faults.getSelectedRow());
                    } else {
                        JOptionPane.showMessageDialog(this, CommonConstant.ERROR_DELETE_ITEM, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                this._dtmFault.removeRow(table_view_faults.getSelectedRow());
            }
        }
    }//GEN-LAST:event_table_view_faultsMouseClicked

    private void table_view_productsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_productsMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = this.table_view_products.getSelectedRow();

            if (this._dtmProdServ.getValueAt(selectedRow, 5) != null) {
                String password = CommonExtension.requestUserPassword();
                Employee employee = _employeeController.getEmployeeByPass(password);

                if (employee != null) {
                    long idDeleteOrderProdServ = this._orderProdServController.deleteOrderProdServ((long) this._dtmProdServ.getValueAt(selectedRow, 5));

                    if (idDeleteOrderProdServ > 0) {
                        this._dtmProdServ.removeRow(this.table_view_products.getSelectedRow());
                        // Sum price column and set into total textField
                        getPriceSum();

                        _serviceOrderModel.setTotal(Double.parseDouble(this.lbl_total_field.getText()));
                        _serviceOrderModel.setDue(Double.parseDouble(this.lbl_due_field.getText()));
                        _orderController.updateServiceOrderStatus(_serviceOrderModel);
                    } else {
                        JOptionPane.showMessageDialog(this, CommonConstant.ERROR_DELETE_ITEM, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                this._dtmProdServ.removeRow(this.table_view_products.getSelectedRow());
            }
        }
    }//GEN-LAST:event_table_view_productsMouseClicked

    private void table_view_productsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_table_view_productsKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            double sum = 0;
            for (int i = 0; i < this._dtmProdServ.getRowCount(); i++) {
                double unitPrice = Double.parseDouble(this._dtmProdServ.getValueAt(i, 3).toString());
                int qty = Integer.parseInt(this._dtmProdServ.getValueAt(i, 2).toString());

                this._dtmProdServ.setValueAt(unitPrice, i, 3);
                double priceTotal = unitPrice * qty;
                this._dtmProdServ.setValueAt(priceTotal, i, 4);
                sum += priceTotal;
            }

            this.lbl_total_field.setText(String.valueOf((sum)));
            this.lbl_due_field.setText(String.valueOf(this.lbl_total_field.getText()));
        }
    }//GEN-LAST:event_table_view_productsKeyReleased

    private void txt_serial_numberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_serial_numberKeyPressed
        //Suggest autoComplete model from Database
        List<String> listSerialNumber = _deviceController.searchSerialNumber(this.txt_brand.getText().toUpperCase(), this.txt_serial_number.getText().toUpperCase());
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
                break;

            case KeyEvent.VK_ENTER:
                this.txt_serial_number.setText(this.txt_serial_number.getText());
                break;
            default:
                EventQueue.invokeLater(() -> {
                    CommonExtension.autoCompleteTextField(listSerialNumber, this.txt_serial_number.getText(), this.txt_serial_number);
                });
        }
    }//GEN-LAST:event_txt_serial_numberKeyPressed

    private void btn_notesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_notesActionPerformed

        NoteModal noteModal = new NoteModal(_serviceOrderModel, _parentFrame, true);
        noteModal.setLocationRelativeTo(this);
        noteModal.setVisible(true);
    }//GEN-LAST:event_btn_notesActionPerformed

    private void btn_depositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_depositActionPerformed
        DepositModal depositModal = new DepositModal(_serviceOrderModel, _parentFrame, true);
        depositModal.setLocationRelativeTo(this);
        depositModal.setVisible(true);
    }//GEN-LAST:event_btn_depositActionPerformed

    private void btn_refund_saleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refund_saleActionPerformed
        refundServiceOrder();

    }//GEN-LAST:event_btn_refund_saleActionPerformed

    private void btn_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_printActionPerformed
        // Genarate and display the report
        new ReportGenerator().generateServiceOrderReceiptReport(_serviceOrderModel, _serviceOrderProdServs, _serviceOrderPayments);
    }//GEN-LAST:event_btn_printActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_copy;
    private javax.swing.JButton btn_deposit;
    private javax.swing.JButton btn_notes;
    private javax.swing.JButton btn_print;
    private javax.swing.JButton btn_refund_sale;
    private javax.swing.JEditorPane editor_pane_notes;
    private javax.swing.JTextField hdn_txt_customer_id;
    private javax.swing.JLabel lbl_auto_order_no;
    private javax.swing.JLabel lbl_auto_order_no1;
    private javax.swing.JLabel lbl_bad_sectors;
    private javax.swing.JLabel lbl_bad_sectors_star;
    private javax.swing.JLabel lbl_brand;
    private javax.swing.JLabel lbl_contact;
    private javax.swing.JLabel lbl_contact_star;
    private javax.swing.JLabel lbl_deposit;
    private javax.swing.JLabel lbl_deposit_paid;
    private javax.swing.JLabel lbl_dev_brand_star;
    private javax.swing.JLabel lbl_dev_model_star;
    private javax.swing.JLabel lbl_due;
    private javax.swing.JLabel lbl_due_field;
    private javax.swing.JLabel lbl_email;
    private javax.swing.JLabel lbl_first_name;
    private javax.swing.JLabel lbl_first_name_star;
    private javax.swing.JLabel lbl_last_name;
    private javax.swing.JLabel lbl_last_name_star;
    private javax.swing.JLabel lbl_model;
    private javax.swing.JLabel lbl_order_no;
    private javax.swing.JLabel lbl_serial_number_star;
    private javax.swing.JLabel lbl_sn;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JLabel lbl_total_field;
    private javax.swing.JPanel panel_input_detail;
    private javax.swing.JPanel panel_order_buttons;
    private javax.swing.JPanel panel_order_details;
    private javax.swing.JPanel panel_total_amount;
    private javax.swing.JScrollPane scroll_pane_faults;
    private javax.swing.JScrollPane scroll_pane_notes;
    private javax.swing.JScrollPane scroll_pane_products;
    private javax.swing.JSpinner spn_bad_sectors;
    private javax.swing.JTable table_view_faults;
    private javax.swing.JTable table_view_products;
    private javax.swing.JTextField txt_brand;
    private javax.swing.JFormattedTextField txt_contact;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_first_name;
    private javax.swing.JTextField txt_last_name;
    private javax.swing.JTextField txt_model;
    private javax.swing.JTextField txt_serial_number;
    // End of variables declaration//GEN-END:variables

}
