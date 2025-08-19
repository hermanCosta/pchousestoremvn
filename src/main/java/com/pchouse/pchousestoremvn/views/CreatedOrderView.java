package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.common.CommonStrings;
import com.pchouse.pchousestoremvn.controllers.CustomerController;
import com.pchouse.pchousestoremvn.controllers.DeviceController;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.controllers.FaultController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderController;
import com.pchouse.pchousestoremvn.controllers.OrderNoteController;
import com.pchouse.pchousestoremvn.controllers.ProductServiceController;
import com.pchouse.pchousestoremvn.controllers.RefundController;
import com.pchouse.pchousestoremvn.enums.OrderStatus;
import com.pchouse.pchousestoremvn.enums.PaymentType;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Device;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.Fault;
import com.pchouse.pchousestoremvn.models.Person;
import com.pchouse.pchousestoremvn.models.ProductService;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderFault;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import com.pchouse.pchousestoremvn.util.ReportGenerator;
import com.pchouse.pchousestoremvn.views.modals.CustomerModal;
import com.pchouse.pchousestoremvn.views.modals.DepositModal;
import com.pchouse.pchousestoremvn.views.modals.NoteModal;
import com.pchouse.pchousestoremvn.views.modals.PaymentModal;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class CreatedOrderView extends javax.swing.JInternalFrame {

    private long hdnCustomerId;
    private List<ProductService> _listProdServ;
    private List<Fault> _listFault;
    private List<Deposit> _orderDeposits;
    private final ServiceOrder _serviceOrderModel;
    private final List<ServiceOrderFault> _listServiceOrderFault;
    private final List<ServiceOrderProdServ> _listServiceOrderProdServ;
    private final List<Deposit> _listOrderDeposit;
    private final ServiceOrderController _orderController;
    private final ProductServiceController _productServiceController;
    private final FaultController _faultController;
    private final CustomerController _customerController;
    private final EmployeeController _employeeController;
    private final OrderNoteController _orderNoteController;
    private final DeviceController _deviceController;
    private final RefundController _refundController;
    private final DefaultTableModel _dtmProdServ;
    private final DefaultTableModel _dtmFault;
    private final DefaultListModel _defaultListModelProdServ;
    private final DefaultListModel _defaultListModelFault;

    Frame _parentFrame = JOptionPane.getFrameForComponent(this);

    public CreatedOrderView(ServiceOrder orderModel, List<ServiceOrderFault> listOrderFault, List<ServiceOrderProdServ> listOrderProdServ, List<Deposit> listOrderDeposit) {
        initComponents();

        //avoid auto old value by focus loosing
        this.txt_contact.setFocusLostBehavior(JFormattedTextField.PERSIST);

        CommonExtension.checkEmailFormat(this.txt_email);
        CommonSetting.requestTxtFocus(txt_first_name);
        CommonSetting.tableSettings(table_view_faults);
        CommonSetting.tableSettings(table_view_products);

        this._orderController = new ServiceOrderController();
        this._productServiceController = new ProductServiceController();
        this._faultController = new FaultController();
        this._customerController = new CustomerController();
        this._employeeController = new EmployeeController();
        this._orderNoteController = new OrderNoteController();
        this._deviceController = new DeviceController();
        this._refundController = new RefundController();
        this._dtmProdServ = (DefaultTableModel) this.table_view_products.getModel();
        this._dtmFault = (DefaultTableModel) this.table_view_faults.getModel();
        this._defaultListModelProdServ = new DefaultListModel();
        this._defaultListModelFault = new DefaultListModel();
        this.list_prod_serv_search.setModel(_defaultListModelProdServ);
        this.list_fault_search.setModel(_defaultListModelFault);

        this._serviceOrderModel = orderModel;
        this._listServiceOrderFault = listOrderFault;
        this._listServiceOrderProdServ = listOrderProdServ;
        this._listOrderDeposit = listOrderDeposit;
        this._orderDeposits = listOrderDeposit;
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
            this.hdnCustomerId = customer.getIdCustomer();
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

    private void searchFault() {
        this._defaultListModelFault.removeAllElements();

        if (!this.txt_search_fault.getText().trim().isEmpty()) {
            this._listFault = _faultController.searchOrderFaults(this.txt_search_fault.getText());
            if (!this._listFault.isEmpty()) {
                _listFault.forEach((fault) -> {
                    this._defaultListModelFault.addElement(fault);
                });
            }
        }
    }

    private void searchProdServ() {
        this._defaultListModelProdServ.removeAllElements();

        if (!this.txt_search_prod_serv.getText().trim().isEmpty()) {
            this._listProdServ = _productServiceController.searchOrderProductService(this.txt_search_prod_serv.getText());
            if (!this._listProdServ.isEmpty()) {
                _listProdServ.forEach((prodServ) -> {
                    this._defaultListModelProdServ.addElement(prodServ);
                });
            }
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

    private ServiceOrder getOrderFields() {
        ServiceOrder getOrderDetails = null;
        Customer customer = null;
        Employee employee;
        boolean isNewCustomer = true;

        if (this.txt_first_name.getText().trim().isEmpty() || this.txt_last_name.getText().trim().isEmpty()
                || this.txt_contact.getText().trim().isEmpty() || this.txt_brand.getText().trim().isEmpty()
                || this.txt_model.getText().trim().isEmpty() || this.txt_serial_number.getText().trim().isEmpty()
                || this.table_view_products.getRowCount() == 0 || this.table_view_faults.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, this.getTitle(), JOptionPane.WARNING_MESSAGE);

            return getOrderDetails;
        } else {
            String password = CommonExtension.requestUserPassword();
            employee = _employeeController.getEmployeeByPass(password);
            if (employee != null) {

                long idCustomer = hdnCustomerId;

                if (idCustomer > 0) {
                    isNewCustomer = false;
                    customer = this._customerController.getCustomerById(idCustomer);

                    if (!customer.getPerson().getFirstName().trim().equals(this.txt_first_name.getText().trim())
                            || !customer.getPerson().getLastName().trim().equals(this.txt_last_name.getText().trim())
                            || !CommonExtension.formatContactNo(customer.getPerson().getContactNo()).equals(CommonExtension.formatContactNo(this.txt_contact.getText()))
                            || !customer.getPerson().getEmail().trim().equals(this.txt_email.getText().trim())) {

                        JOptionPane.showMessageDialog(this, CommonConstant.WARN_CUSTOMER_MATCHING, this.getTitle(), JOptionPane.WARNING_MESSAGE);
                        CustomerModal customerModal = new CustomerModal(this, new MainMenuView(CommonSetting.COMPANY), true, customer);
                        customerModal.setVisible(true);
                        this.hdnCustomerId = 0;
                        return getOrderDetails;
                    }
                }

                if (isNewCustomer) {

                    Customer checkCustomer = _customerController.searchCustomerByContactNo(this.txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));

                    if (checkCustomer != null) {
                        JOptionPane.showMessageDialog(this, CommonConstant.WARN_EXIST_PERSON, this.getTitle(), JOptionPane.WARNING_MESSAGE);

                        CustomerModal customerModal = new CustomerModal(this, new MainMenuView(CommonSetting.COMPANY), true, checkCustomer);
                        customerModal.setVisible(true);

                        return getOrderDetails;
                    } else {

                        Person person = new Person(
                                this.txt_first_name.getText().toUpperCase(),
                                this.txt_last_name.getText().toUpperCase(),
                                this.txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""),
                                this.txt_email.getText().toLowerCase());

                        customer = new Customer(person, CommonSetting.COMPANY);
                    }
                }

                Device device = new Device(this.txt_brand.getText().toUpperCase(),
                        this.txt_model.getText().toUpperCase(),
                        this.txt_serial_number.getText().toUpperCase());

                getOrderDetails = new ServiceOrder(
                        customer,
                        device,
                        employee,
                        CommonSetting.COMPANY,
                        CommonExtension.formatEuroToDouble(this.lbl_total_field.getText()),
                        CommonExtension.formatEuroToDouble(this.lbl_due_field.getText()),
                        OrderStatus.IN_PROGRESS,
                        new Date(),
                        null,
                        null,
                        (int) this.spn_bad_sectors.getValue(),
                        this.editor_pane_notes.getText());

                getOrderDetails.setIdServiceOrder(Integer.valueOf(this.lbl_auto_order_no.getText()));

            } else {
                JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, this.getTitle(), JOptionPane.ERROR_MESSAGE);
            }
        }

        return getOrderDetails;
    }

    private List<ServiceOrderProdServ> getOrderProdServ(ServiceOrder order) {
        List<ServiceOrderProdServ> listOrderProdServ = new ArrayList<>();

        if (this.table_view_products.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_ADD_ITEM + this.getTitle(), this.getTitle(), JOptionPane.WARNING_MESSAGE);

            return listOrderProdServ;
        } else {

            for (int i = 0; i < _dtmProdServ.getRowCount(); i++) {
                ProductService prodServItem = new ProductService();
                ServiceOrderProdServ orderProdServItem = new ServiceOrderProdServ();

                prodServItem = _productServiceController.getProductServiceById(Integer.parseInt(_dtmProdServ.getValueAt(i, 0).toString()));

                orderProdServItem.setServiceOrder(order);
                orderProdServItem.setProdServ(prodServItem);
                orderProdServItem.setQty(Integer.parseInt(_dtmProdServ.getValueAt(i, 2).toString()));
                orderProdServItem.setTotal(Double.parseDouble(_dtmProdServ.getValueAt(i, 3).toString()));
                orderProdServItem.setIdServiceOrderProdServ(CommonExtension.setLongIdExtension(this._dtmProdServ.getValueAt(i, 5)));

                listOrderProdServ.add(orderProdServItem);
            }

        }
        return listOrderProdServ;
    }

    private List<ServiceOrderFault> getOrderFault(ServiceOrder order) {
        List<ServiceOrderFault> listOrderFault = new ArrayList<>();

        if (this.table_view_faults.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_ADD_ITEM + "fault", this.getTitle(), JOptionPane.WARNING_MESSAGE);

            return listOrderFault;
        } else {
            for (int i = 0; i < _dtmFault.getRowCount(); i++) {
                Fault faultItem = new Fault();
                ServiceOrderFault orderFaultItem = new ServiceOrderFault();

                faultItem = _faultController.getItemFault(Integer.parseInt(_dtmFault.getValueAt(i, 0).toString()));

                orderFaultItem.setServiceOrder(order);
                orderFaultItem.setFault(faultItem);
                orderFaultItem.setIdServiceOrderFault(CommonExtension.setLongIdExtension(this._dtmFault.getValueAt(i, 2)));

                listOrderFault.add(orderFaultItem);
            }
        }
        return listOrderFault;
    }

    private OrderNote getOrderNote(ServiceOrder order) {
        OrderNote orderNote = null;
        Employee employee = this._employeeController.getItemEmployee(2);
        Date date = new Date();
        Date createdDate = new Timestamp(date.getTime());

        if (this.editor_pane_notes.getText().trim().isEmpty()) {
            return orderNote;
        } else {
            orderNote = new OrderNote(order, employee, this.editor_pane_notes.getText(), createdDate);
        }
        return orderNote;
    }

    private void updateOrderStatus(OrderStatus newStatus, String confirmMessage, String noteMessage) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                confirmMessage,
                "Confirm Action",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String password = CommonExtension.requestUserPassword();
            Employee employee = _employeeController.getEmployeeByPass(password);

            if (employee != null) {
                _serviceOrderModel.setStatus(newStatus);
                _serviceOrderModel.setEmployee(employee);

                boolean isOrderUpdated = this._orderController.updateServiceOrderStatus(_serviceOrderModel);
                if (isOrderUpdated) {
                    OrderNote orderNote = new OrderNote(
                            _serviceOrderModel,
                            employee,
                            noteMessage,
                            new Date()
                    );

                    long idOrderNote = _orderNoteController.addOrderNote(orderNote);

                    if (idOrderNote > 0) {
                        JOptionPane.showMessageDialog(this, CommonConstant.SUCCESS_UPDATE);
                    } else {
                        JOptionPane.showMessageDialog(this, CommonConstant.ERROR_ADD_NOTE, null, JOptionPane.ERROR_MESSAGE);
                    }

                    if (newStatus == OrderStatus.FIXED) {
                        FixedOrderView fixedOrderView = new FixedOrderView(
                                _serviceOrderModel,
                                _listServiceOrderFault,
                                _listServiceOrderProdServ,
                                _listOrderDeposit
                        );
                        CommonSetting.openInternalFrame(fixedOrderView, "Order Fixed: " + _serviceOrderModel.getIdServiceOrder());
                    } else if (newStatus == OrderStatus.NOT_FIXED) {
                        NotFixedOrderView notFixedOrderView = new NotFixedOrderView(
                                _serviceOrderModel,
                                _listServiceOrderFault,
                                _listServiceOrderProdServ,
                                _listOrderDeposit
                        );
                        CommonSetting.openInternalFrame(notFixedOrderView, "Order Not Fixed: " + _serviceOrderModel.getIdServiceOrder());
                    }

                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.ERROR_UPDATE, null, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, getTitle(), JOptionPane.ERROR_MESSAGE);
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
        btn_seacrh_customer = new javax.swing.JButton();
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
        btn_international_number1 = new javax.swing.JButton();
        spn_bad_sectors = new javax.swing.JSpinner();
        panel_total_amount = new javax.swing.JPanel();
        lbl_total = new javax.swing.JLabel();
        lbl_deposit = new javax.swing.JLabel();
        txt_deposit = new javax.swing.JTextField();
        lbl_due = new javax.swing.JLabel();
        lbl_total_field = new javax.swing.JLabel();
        lbl_due_field = new javax.swing.JLabel();
        lbl_deposit_paid = new javax.swing.JLabel();
        panel_order_buttons = new javax.swing.JPanel();
        btn_save_order = new javax.swing.JButton();
        btn_print = new javax.swing.JButton();
        btn_notes = new javax.swing.JButton();
        btn_fix = new javax.swing.JButton();
        btn_not_fix = new javax.swing.JButton();
        btn_deposit = new javax.swing.JButton();
        txt_search_fault = new javax.swing.JTextField();
        lbl_search_fault_icon = new javax.swing.JLabel();
        layered_pane_list_fault = new javax.swing.JLayeredPane();
        list_fault_search = new javax.swing.JList<>();
        scroll_pane_faults = new javax.swing.JScrollPane();
        table_view_faults = new javax.swing.JTable();
        txt_search_prod_serv = new javax.swing.JTextField();
        lbl_search_prod_serv_icon = new javax.swing.JLabel();
        layered_pane_list_prod_serv = new javax.swing.JLayeredPane();
        list_prod_serv_search = new javax.swing.JList<>();
        scroll_pane_products = new javax.swing.JScrollPane();
        table_view_products = new javax.swing.JTable();

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

        txt_first_name.setFocusCycleRoot(true);
        txt_first_name.setNextFocusableComponent(txt_last_name);
        txt_first_name.setPreferredSize(new java.awt.Dimension(339, 25));

        lbl_last_name.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_last_name.setText("Last Name");

        txt_last_name.setNextFocusableComponent(txt_contact);
        txt_last_name.setPreferredSize(new java.awt.Dimension(342, 25));

        lbl_contact.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_contact.setText("Contact No.");

        try {
            txt_contact.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(0##) ###-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_contact.setNextFocusableComponent(txt_email);
        txt_contact.setPreferredSize(new java.awt.Dimension(224, 25));

        btn_seacrh_customer.setBackground(new java.awt.Color(0, 0, 0));
        btn_seacrh_customer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search_customer.png"))); // NOI18N
        btn_seacrh_customer.setPreferredSize(new java.awt.Dimension(35, 25));
        btn_seacrh_customer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_seacrh_customerActionPerformed(evt);
            }
        });

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

        txt_email.setNextFocusableComponent(txt_brand);
        txt_email.setPreferredSize(new java.awt.Dimension(388, 25));

        lbl_brand.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_brand.setText("Device Brand");

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

        txt_model.setNextFocusableComponent(txt_serial_number);
        txt_model.setPreferredSize(new java.awt.Dimension(320, 25));
        txt_model.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_modelKeyPressed(evt);
            }
        });

        lbl_sn.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_sn.setText("Serial Number");

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
        editor_pane_notes.setFocusCycleRoot(false);
        editor_pane_notes.setNextFocusableComponent(txt_search_fault);
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

        btn_international_number1.setBackground(new java.awt.Color(0, 0, 0));
        btn_international_number1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_international_number.png"))); // NOI18N
        btn_international_number1.setPreferredSize(new java.awt.Dimension(35, 25));
        btn_international_number1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_international_number1ActionPerformed(evt);
            }
        });

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
                                .addComponent(txt_contact, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_international_number1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_seacrh_customer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                                .addComponent(lbl_order_no)
                                .addGap(7, 7, 7)
                                .addComponent(lbl_auto_order_no))
                            .addGroup(panel_input_detailLayout.createSequentialGroup()
                                .addComponent(lbl_first_name_star, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_first_name)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_input_detailLayout.setVerticalGroup(
            panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_input_detailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_auto_order_no)
                    .addComponent(lbl_order_no))
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
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_copy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_seacrh_customer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(scroll_pane_notes, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addComponent(btn_international_number1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panel_total_amount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_total.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_total.setText("Total:");

        lbl_deposit.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_deposit.setText("Deposit:");

        txt_deposit.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N
        txt_deposit.setNextFocusableComponent(btn_save_order);
        txt_deposit.setPreferredSize(new java.awt.Dimension(100, 25));
        txt_deposit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_depositKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_depositKeyReleased(evt);
            }
        });

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
                        .addComponent(lbl_deposit_paid)
                        .addGap(18, 18, 18)
                        .addComponent(txt_deposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(lbl_deposit_paid)
                    .addComponent(txt_deposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_due)
                    .addComponent(lbl_due_field))
                .addContainerGap())
        );

        panel_order_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_save_order.setBackground(new java.awt.Color(21, 76, 121));
        btn_save_order.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_save_order.setForeground(new java.awt.Color(255, 255, 255));
        btn_save_order.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_save.png"))); // NOI18N
        btn_save_order.setText("Save");
        btn_save_order.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_save_orderActionPerformed(evt);
            }
        });

        btn_print.setBackground(new java.awt.Color(21, 76, 121));
        btn_print.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_print.setForeground(new java.awt.Color(255, 255, 255));
        btn_print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_print.png"))); // NOI18N
        btn_print.setText("Print");
        btn_print.setNextFocusableComponent(txt_first_name);
        btn_print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_printActionPerformed(evt);
            }
        });

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

        btn_fix.setBackground(new java.awt.Color(0, 153, 102));
        btn_fix.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_fix.setForeground(new java.awt.Color(255, 255, 255));
        btn_fix.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_fix_order.png"))); // NOI18N
        btn_fix.setText("Fixed");
        btn_fix.setNextFocusableComponent(txt_first_name);
        btn_fix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_fixActionPerformed(evt);
            }
        });

        btn_not_fix.setBackground(new java.awt.Color(255, 51, 51));
        btn_not_fix.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_not_fix.setForeground(new java.awt.Color(255, 255, 255));
        btn_not_fix.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_not_fix.png"))); // NOI18N
        btn_not_fix.setText("Not Fixed");
        btn_not_fix.setNextFocusableComponent(txt_first_name);
        btn_not_fix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_not_fixActionPerformed(evt);
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

        javax.swing.GroupLayout panel_order_buttonsLayout = new javax.swing.GroupLayout(panel_order_buttons);
        panel_order_buttons.setLayout(panel_order_buttonsLayout);
        panel_order_buttonsLayout.setHorizontalGroup(
            panel_order_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_order_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_save_order)
                .addGap(18, 18, 18)
                .addComponent(btn_print)
                .addGap(18, 18, 18)
                .addComponent(btn_notes)
                .addGap(18, 18, 18)
                .addComponent(btn_deposit)
                .addGap(18, 18, 18)
                .addComponent(btn_fix)
                .addGap(18, 18, 18)
                .addComponent(btn_not_fix)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_order_buttonsLayout.setVerticalGroup(
            panel_order_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_order_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_order_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_save_order)
                    .addComponent(btn_print)
                    .addComponent(btn_notes)
                    .addComponent(btn_fix)
                    .addComponent(btn_not_fix)
                    .addComponent(btn_deposit))
                .addContainerGap())
        );

        txt_search_fault.setNextFocusableComponent(txt_search_prod_serv);
        txt_search_fault.setPreferredSize(new java.awt.Dimension(518, 25));
        txt_search_fault.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_search_faultFocusLost(evt);
            }
        });
        txt_search_fault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_search_faultActionPerformed(evt);
            }
        });
        txt_search_fault.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_search_faultKeyReleased(evt);
            }
        });

        lbl_search_fault_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search_small_left.png"))); // NOI18N

        layered_pane_list_fault.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        list_fault_search.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        list_fault_search.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list_fault_search.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        list_fault_search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                list_fault_searchMousePressed(evt);
            }
        });
        layered_pane_list_fault.add(list_fault_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 0, 510, -1));

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

        layered_pane_list_fault.add(scroll_pane_faults, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 545, 231));

        txt_search_prod_serv.setNextFocusableComponent(txt_deposit);
        txt_search_prod_serv.setPreferredSize(new java.awt.Dimension(518, 25));
        txt_search_prod_serv.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_search_prod_servFocusLost(evt);
            }
        });
        txt_search_prod_serv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_search_prod_servKeyReleased(evt);
            }
        });

        lbl_search_prod_serv_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search_small_left.png"))); // NOI18N

        layered_pane_list_prod_serv.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        list_prod_serv_search.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        list_prod_serv_search.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list_prod_serv_search.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        list_prod_serv_search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                list_prod_serv_searchMousePressed(evt);
            }
        });
        layered_pane_list_prod_serv.add(list_prod_serv_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 0, 510, -1));

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

        layered_pane_list_prod_serv.add(scroll_pane_products, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 545, 220));

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
                            .addGroup(panel_order_detailsLayout.createSequentialGroup()
                                .addComponent(txt_search_fault, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_search_fault_icon))
                            .addComponent(layered_pane_list_prod_serv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel_order_detailsLayout.createSequentialGroup()
                                .addComponent(txt_search_prod_serv, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_search_prod_serv_icon))
                            .addComponent(layered_pane_list_fault, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_order_detailsLayout.setVerticalGroup(
            panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_order_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_order_detailsLayout.createSequentialGroup()
                        .addGroup(panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_search_fault, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_search_fault_icon))
                        .addGap(0, 0, 0)
                        .addComponent(layered_pane_list_fault)
                        .addGap(18, 18, 18)
                        .addGroup(panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_search_prod_serv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_search_prod_serv_icon, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addComponent(layered_pane_list_prod_serv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(panel_order_details, javax.swing.GroupLayout.DEFAULT_SIZE, 1029, Short.MAX_VALUE)
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

    private void btn_save_orderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_save_orderActionPerformed
        ServiceOrder updateOrder = getOrderFields();
        List<ServiceOrderFault> listOrderFault = getOrderFault(updateOrder);
        List<ServiceOrderProdServ> listOrderProdServ = getOrderProdServ(updateOrder);
        boolean isUpdated = false;

        if (updateOrder != null) {
            try {
                // Try to find existing device by serial number
                Device device = _deviceController.searchDeviceBySerialNumber(updateOrder.getDevice().getSerialNumber());
                if (device != null) {
                    updateOrder.setDevice(device);
                }

                List<ServiceOrderPayment> payments = null;
                Deposit deposit = null;
                OrderNote orderNote = null;

                if (!this.txt_deposit.getText().trim().isEmpty()) {
                    PaymentModal paymentModal = new PaymentModal(updateOrder, this.txt_deposit.getText(), new MainMenuView(CommonSetting.COMPANY), true);
                    paymentModal.setVisible(true);

                    payments = paymentModal.getServiceOrderPayments();

                    if (payments == null || payments.isEmpty()) {
                        showError("Payment was not completed.");
                        return;
                    }

                    // Mark each payment with the type
                    for (ServiceOrderPayment payment : payments) {
                        payment.setPaymentType(PaymentType.DEPOSIT);
                    }

                    deposit = new Deposit(updateOrder, updateOrder.getEmployee(), Double.parseDouble(this.txt_deposit.getText()), updateOrder.getCreated());
                }

                orderNote = new OrderNote(updateOrder, updateOrder.getEmployee(), CommonConstant.ORDER_UPDATED_NOTE, new Date());

                boolean updated = this._orderController.updateServiceOrder(
                        updateOrder, listOrderFault, listOrderProdServ, payments, deposit, orderNote);

                if (updated) {
                    isUpdated = true;
                }

                if (isUpdated) {
                    JOptionPane.showMessageDialog(this, CommonConstant.SUCCESS_UPDATE);
                    this.txt_deposit.setText("");
                }
            } catch (BusinessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btn_save_orderActionPerformed

    private void btn_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_printActionPerformed
        // Genarate and display the report
        new ReportGenerator().generateServiceOrderReport(_serviceOrderModel, _listServiceOrderFault, _listServiceOrderProdServ);
    }//GEN-LAST:event_btn_printActionPerformed

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

    private void txt_search_faultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_search_faultActionPerformed
        if (!this.txt_search_fault.getText().trim().isEmpty() && this._defaultListModelFault.isEmpty()) {

            this._listFault = _faultController.searchFault(this.txt_search_fault.getText());
            if (this._listFault.isEmpty() || this._listFault == null) {
                Fault addFault = new Fault(this.txt_search_fault.getText().toUpperCase());

                int confirmInsertion = JOptionPane.showConfirmDialog(this, CommonConstant.CONFIRM_ADD_ITEM, this.getTitle(), JOptionPane.YES_NO_OPTION);
                if (confirmInsertion == 0) {
                    long idFault = this._faultController.addFault(addFault);
                    if (idFault > 0) {

                        this._dtmFault.addRow(
                                new Object[]{
                                    idFault,
                                    addFault.getDescription()
                                });
                    }

                    this.txt_search_fault.setText("");
                    this.txt_search_fault.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txt_search_faultActionPerformed

    private void editor_pane_notesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editor_pane_notesKeyPressed
        if (evt.isShiftDown() && evt.getKeyCode() == KeyEvent.VK_TAB) {
            txt_serial_number.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            txt_search_fault.requestFocus();
        }
    }//GEN-LAST:event_editor_pane_notesKeyPressed

    private void txt_search_faultKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search_faultKeyReleased
        searchFault();
    }//GEN-LAST:event_txt_search_faultKeyReleased

    private void btn_seacrh_customerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_seacrh_customerActionPerformed
        this.hdnCustomerId = 0;
        CustomerModal customerModal = new CustomerModal(this, new MainMenuView(CommonSetting.COMPANY), true, null);
        customerModal.setVisible(true);
    }//GEN-LAST:event_btn_seacrh_customerActionPerformed

    private void btn_copyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_copyActionPerformed
        StringSelection stringSelection = new StringSelection(txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));
        if (!this.txt_contact.getText().trim().isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        }
    }//GEN-LAST:event_btn_copyActionPerformed

    private void table_view_faultsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_faultsMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = this.table_view_faults.getSelectedRow();
            this._dtmFault.removeRow(table_view_faults.getSelectedRow());

//            if (this._dtmFault.getValueAt(selectedRow, 2) != null) {
//                String password = CommonExtension.requestUserPassword();
//                Employee employee = _employeeController.getEmployeeByPass(password);
//                if (employee != null) {
//
//                    long idOrderFaultDeleted = this._orderFaultController.deleteOrderFault((long) this._dtmFault.getValueAt(selectedRow, 2));
//
//                    if (idOrderFaultDeleted > 0) {
//                        this._dtmFault.removeRow(table_view_faults.getSelectedRow());
//                    } else {
//                        JOptionPane.showMessageDialog(this, CommonConstant.ERROR_DELETE_ITEM, this.getTitle(), JOptionPane.ERROR_MESSAGE);
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, this.getTitle(), JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                this._dtmFault.removeRow(table_view_faults.getSelectedRow());
//            }
        }
    }//GEN-LAST:event_table_view_faultsMouseClicked

    private void txt_search_prod_servFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_search_prod_servFocusLost
        this.txt_search_prod_serv.setText("");
        this._defaultListModelProdServ.removeAllElements();
    }//GEN-LAST:event_txt_search_prod_servFocusLost

    private void txt_search_prod_servKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search_prod_servKeyReleased
        searchProdServ();
    }//GEN-LAST:event_txt_search_prod_servKeyReleased

    private void list_prod_serv_searchMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_prod_serv_searchMousePressed
        if (!this.list_prod_serv_search.isSelectionEmpty()) {
            ProductService prodServ = (ProductService) this._defaultListModelProdServ.getElementAt(this.list_prod_serv_search.getSelectedIndex());
            boolean isAdded = false;

            if (this._dtmProdServ.getRowCount() > 0) {
                for (int i = 0; i < _dtmProdServ.getRowCount(); i++) {
                    int idProdServ = Integer.valueOf(this._dtmProdServ.getValueAt(i, 0).toString());
                    if (idProdServ == prodServ.getIdProductService()) {
                        isAdded = true;
                        break;
                    }
                }
            }

            if (!isAdded) {
                this._dtmProdServ.addRow(
                        new Object[]{
                            prodServ.getIdProductService(),
                            prodServ.getProdServName(),
                            CommonConstant.DEFAULT_QTY,
                            CommonExtension.formatToPriceField(prodServ.getPrice()),
                            CommonExtension.formatToPriceField(prodServ.getPrice() * 1)
                        }
                );

                this._defaultListModelProdServ.removeAllElements();
                this.txt_search_prod_serv.setText("");
                this.txt_search_prod_serv.requestFocus();
                getPriceSum();
            }
        }
    }//GEN-LAST:event_list_prod_serv_searchMousePressed

    private void table_view_productsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_productsMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = this.table_view_products.getSelectedRow();
            this._dtmProdServ.removeRow(table_view_products.getSelectedRow());

//            if (this._dtmProdServ.getValueAt(selectedRow, 5) != null) {
//                String password = CommonExtension.requestUserPassword();
//                Employee employee = _employeeController.getEmployeeByPass(password);
//
//                if (employee != null) {
//                    long idDeleteOrderProdServ = this._orderProdServController.deleteOrderProdServ((long) this._dtmProdServ.getValueAt(selectedRow, 5));
//
//                    if (idDeleteOrderProdServ > 0) {
//                        this._dtmProdServ.removeRow(this.table_view_products.getSelectedRow());
//                        // Sum price column and set into total textField
//                        getPriceSum();
//
//                        _serviceOrderModel.setTotal(Double.parseDouble(this.lbl_total_field.getText()));
//                        _serviceOrderModel.setDue(Double.parseDouble(this.lbl_due_field.getText()));
//                        _orderController.updateOrder(_serviceOrderModel);
//                    } else {
//                        JOptionPane.showMessageDialog(this, CommonConstant.ERROR_DELETE_ITEM, this.getTitle(), JOptionPane.ERROR_MESSAGE);
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, this.getTitle(), JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                this._dtmProdServ.removeRow(this.table_view_products.getSelectedRow());
//            }
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

    private void list_fault_searchMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_fault_searchMousePressed
        if (!this.list_fault_search.isSelectionEmpty()) {
            Fault fault = (Fault) this._defaultListModelFault.getElementAt(this.list_fault_search.getSelectedIndex());
            boolean isAdded = false;

            if (this.table_view_faults.getRowCount() > 0) {
                for (int i = 0; i < this.table_view_faults.getRowCount(); i++) {
                    int idFault = Integer.valueOf(_dtmFault.getValueAt(i, 0).toString());
                    if (idFault == fault.getIdFault()) {
                        isAdded = true;
                        break;
                    }
                }
            }

            if (!isAdded) {
                this._dtmFault.addRow(
                        new Object[]{
                            fault.getIdFault(),
                            fault.getDescription()
                        });
            }

            this._defaultListModelFault.removeAllElements();
            this.txt_search_fault.setText("");
            this.txt_search_fault.requestFocus();
        }
    }//GEN-LAST:event_list_fault_searchMousePressed

    private void txt_search_faultFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_search_faultFocusLost
        this.txt_search_fault.setText("");
        this._defaultListModelFault.removeAllElements();
    }//GEN-LAST:event_txt_search_faultFocusLost

    private void btn_international_number1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_international_number1ActionPerformed
        if (this.txt_contact.getFormatterFactory() != null) {
            this.txt_contact.setFormatterFactory(null);
            this.txt_contact.setText("+");
            this.txt_contact.requestFocus();
        } else {
            this.txt_contact.setText("");
            try {
                this.txt_contact.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("(0##) ###-####")));
            } catch (java.text.ParseException ex) {
            }
            this.txt_contact.requestFocus();
        }
    }//GEN-LAST:event_btn_international_number1ActionPerformed

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

    private void btn_fixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_fixActionPerformed
        updateOrderStatus(
                OrderStatus.FIXED,
                CommonConstant.CONFIRM_ORDER_FIXED,
                CommonConstant.ORDER_FIXED_NOTE
        );
    }//GEN-LAST:event_btn_fixActionPerformed

    private void btn_not_fixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_not_fixActionPerformed
        updateOrderStatus(
                OrderStatus.NOT_FIXED,
                CommonConstant.CONFIRM_ORDER_NOT_FIXED,
                CommonConstant.ORDER_NOT_FIXED_NOTE
        );
    }//GEN-LAST:event_btn_not_fixActionPerformed

    private void btn_depositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_depositActionPerformed
        DepositModal depositModal = new DepositModal(_serviceOrderModel, _parentFrame, true);
        depositModal.setLocationRelativeTo(this);
        depositModal.setVisible(true);
    }//GEN-LAST:event_btn_depositActionPerformed

    private void txt_depositKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_depositKeyReleased
        double totalPrice = CommonExtension.formatEuroToDouble(this.lbl_total_field.getText());
        if (!this.txt_deposit.getText().trim().isEmpty()) {
            double depositPaid = CommonExtension.formatEuroToDouble(this.lbl_deposit_paid.getText());
            double deposit = Double.parseDouble(this.txt_deposit.getText());

            this.lbl_due_field.setText(CommonExtension.formatEuroCurrency(totalPrice - (depositPaid + deposit)));
        } else {
            this.lbl_due_field.setText(CommonExtension.formatEuroCurrency(totalPrice));
        }
    }//GEN-LAST:event_txt_depositKeyReleased

    private void txt_depositKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_depositKeyPressed
        //Accepts number characters only
        if (Character.isLetter(evt.getKeyChar())) {
            this.txt_deposit.setEditable(false);
        } else {
            this.txt_deposit.setEditable(true);
        }
    }//GEN-LAST:event_txt_depositKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_copy;
    private javax.swing.JButton btn_deposit;
    private javax.swing.JButton btn_fix;
    private javax.swing.JButton btn_international_number1;
    private javax.swing.JButton btn_not_fix;
    private javax.swing.JButton btn_notes;
    private javax.swing.JButton btn_print;
    private javax.swing.JButton btn_save_order;
    private javax.swing.JButton btn_seacrh_customer;
    private javax.swing.JEditorPane editor_pane_notes;
    private javax.swing.JLayeredPane layered_pane_list_fault;
    private javax.swing.JLayeredPane layered_pane_list_prod_serv;
    private javax.swing.JLabel lbl_auto_order_no;
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
    private javax.swing.JLabel lbl_search_fault_icon;
    private javax.swing.JLabel lbl_search_prod_serv_icon;
    private javax.swing.JLabel lbl_serial_number_star;
    private javax.swing.JLabel lbl_sn;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JLabel lbl_total_field;
    private javax.swing.JList<String> list_fault_search;
    private javax.swing.JList<String> list_prod_serv_search;
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
    private javax.swing.JTextField txt_deposit;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_first_name;
    private javax.swing.JTextField txt_last_name;
    private javax.swing.JTextField txt_model;
    private javax.swing.JTextField txt_search_fault;
    private javax.swing.JTextField txt_search_prod_serv;
    private javax.swing.JTextField txt_serial_number;
    // End of variables declaration//GEN-END:variables

}
