package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.CustomerController;
import com.pchouse.pchousestoremvn.controllers.DeviceController;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.controllers.FaultController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderController;
import com.pchouse.pchousestoremvn.controllers.ProductServiceController;
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
import com.pchouse.pchousestoremvn.views.modals.PaymentModal;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class NewOrderView extends javax.swing.JInternalFrame {

    private long hdnCustomerId;
    private List<ProductService> _listProdServ;
    private List<Fault> _listFault;
    public ServiceOrderPayment _orderPayment = null;
    private final ServiceOrderController _orderController;
    private final ProductServiceController _productServiceController;
    private final FaultController _faultController;
    private final CustomerController _customerController;
    private final EmployeeController _employeeController;
    private final DeviceController _deviceController;
    private final DefaultTableModel _dtmProdServ;
    private final DefaultTableModel _dtmFault;
    private final DefaultListModel _defaultListModelProdServ;
    private final DefaultListModel _defaultListModelFault;
    Frame _parentFrame = JOptionPane.getFrameForComponent(this);

    public NewOrderView() {
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
        this._deviceController = new DeviceController();
        this._dtmProdServ = (DefaultTableModel) this.table_view_products.getModel();
        this._dtmFault = (DefaultTableModel) this.table_view_faults.getModel();
        this._defaultListModelProdServ = new DefaultListModel();
        this._defaultListModelFault = new DefaultListModel();
        this.list_prod_serv_search.setModel(_defaultListModelProdServ);
        this.list_fault_search.setModel(_defaultListModelFault);

        list_prod_serv_search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!list_prod_serv_search.isSelectionEmpty()) {
                        // addProductService();
                    }
                } else if (evt.getKeyCode() == KeyEvent.VK_UP && list_prod_serv_search.getSelectedIndex() == 0) {
                    txt_search_prod_serv.requestFocusInWindow();
                }
            }
        });
    }

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

    private void searchFault() {
        this._defaultListModelFault.removeAllElements();

        if (!this.txt_search_fault.getText().trim().isEmpty()) {
            this._listFault = _faultController.searchFault(this.txt_search_fault.getText());
            if (!this._listFault.isEmpty()) {
                _listFault.forEach((fault) -> {
                    this._defaultListModelFault.addElement(fault);
                });
            }
        }
    }

    private void searchProdServ() {
        String searchText = txt_search_prod_serv.getText().trim();

        if (!searchText.isEmpty()) {
            List<ProductService> newList = _productServiceController.searchOrderProductService(searchText);

            if (!newList.equals(this._listProdServ)) {  // Only update if the list has changed
                _defaultListModelProdServ.removeAllElements();
                this._listProdServ = newList;
                newList.forEach(prodServ -> _defaultListModelProdServ.addElement(prodServ));
            }
        }
    }

    private void getPriceSum() {
        double sum = 0;
        for (int i = 0; i < this._dtmProdServ.getRowCount(); i++) {
            sum += Double.parseDouble(this._dtmProdServ.getValueAt(i, 3).toString());
        }

        this.lbl_total_field.setText(CommonExtension.formatEuroCurrency(sum));
        this.lbl_due_field.setText(this.lbl_total_field.getText());
    }

    private void clearFields() {
        this.hdnCustomerId = 0;
        this.txt_first_name.setText("");
        this.txt_last_name.setText("");
        this.txt_contact.setText("");
        this.txt_email.setText("");
        this.txt_brand.setText("");
        this.txt_model.setText("");
        this.txt_serial_number.setText("");
        this.txt_search_fault.setText("");
        this.editor_pane_notes.setText("");
        this.lbl_total_field.setText("");
        this.txt_deposit.setText("");
        this.lbl_due_field.setText("");

        this._dtmFault.setRowCount(0);
        this._dtmProdServ.setRowCount(0);

        this.txt_first_name.requestFocus();
    }

    private ServiceOrder getOrderFields() {
        ServiceOrder getOrderDetails = null;
        Customer customer = null;
        String password;
        Employee employee;
        boolean isNewCustomer = true;

        if (this.txt_first_name.getText().trim().isEmpty() || this.txt_last_name.getText().trim().isEmpty()
                || this.txt_contact.getText().trim().isEmpty() || this.txt_brand.getText().trim().isEmpty()
                || this.txt_model.getText().trim().isEmpty() || this.txt_serial_number.getText().trim().isEmpty()
                || this.table_view_products.getRowCount() == 0 || this.table_view_faults.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, this.getTitle(), JOptionPane.WARNING_MESSAGE);

            return getOrderDetails;
        } else {
            password = CommonExtension.requestUserPassword();
            employee = _employeeController.getEmployeeByPass(password);
            if (employee != null) {
                long idCustomer = hdnCustomerId;

                if (idCustomer > 0) {
                    isNewCustomer = false;
                    customer = this._customerController.getCustomerById(idCustomer);
                    if (!customer.getPerson().getFirstName().equals(this.txt_first_name.getText())
                            || !customer.getPerson().getLastName().equals(this.txt_last_name.getText())
                            || !CommonExtension.formatContactNo(customer.getPerson().getContactNo()).equals(CommonExtension.formatContactNo(this.txt_contact.getText()))
                            || !customer.getPerson().getEmail().equals(this.txt_email.getText())) {

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

                        CustomerModal customerModal = new CustomerModal(this, _parentFrame, true, checkCustomer);
                        customerModal.setLocationRelativeTo(this);
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
                        OrderStatus.IN_PROGRESS, new Date(), null, null, (int) this.spn_bad_sectors.getValue(), this.editor_pane_notes.getText());

            } else {
                JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, null, JOptionPane.ERROR_MESSAGE);
            }
        }

        return getOrderDetails;
    }

    private List<ServiceOrderProdServ> getOrderProdServ(ServiceOrder order) {
        List<ServiceOrderProdServ> listOrderProdServ = new ArrayList<>();

        if (this.table_view_products.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_ADD_ITEM + "product|service", this.getTitle(), JOptionPane.WARNING_MESSAGE);

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

                listOrderFault.add(orderFaultItem);
            }
        }
        return listOrderFault;
    }

    private void addProductService(ProductService prodServ) {
        boolean isAdded = false;

        for (int i = 0; i < _dtmProdServ.getRowCount(); i++) {
            int idProdServ = Integer.parseInt(_dtmProdServ.getValueAt(i, 0).toString());
            if (idProdServ == prodServ.getIdProductService()) {
                isAdded = true;
                break;
            }
        }

        if (!isAdded) {
            _dtmProdServ.addRow(new Object[]{
                prodServ.getIdProductService(),
                prodServ.getProdServName(),
                CommonConstant.DEFAULT_QTY,
                CommonExtension.formatToPriceField(prodServ.getPrice()),
                CommonExtension.formatToPriceField(prodServ.getPrice() * 1)
            });
        }

        _defaultListModelProdServ.removeAllElements();
        txt_search_prod_serv.setText("");
        txt_search_prod_serv.requestFocus();
    }

    private void ensureListIsVisible() {
        if (!list_prod_serv_search.isShowing()) {
            list_prod_serv_search.getParent().setVisible(true); // Ensure parent is visible
            list_prod_serv_search.requestFocusInWindow();
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
        panel_order_buttons = new javax.swing.JPanel();
        btn_save_order = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();
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
        setTitle("New Order");
        setMaximumSize(new java.awt.Dimension(1049, 700));
        setPreferredSize(new java.awt.Dimension(1050, 650));

        panel_order_details.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_order_details.setPreferredSize(new java.awt.Dimension(1026, 607));

        panel_input_detail.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lbl_bad_sectors_star, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_serial_number_star, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_dev_model_star, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addComponent(lbl_first_name_star, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_first_name)
                        .addGap(0, 343, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_input_detailLayout.setVerticalGroup(
            panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_input_detailLayout.createSequentialGroup()
                .addContainerGap()
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
                        .addComponent(scroll_pane_notes, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
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

        lbl_due_field.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

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
                        .addComponent(lbl_deposit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_deposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_total_amountLayout.createSequentialGroup()
                        .addComponent(lbl_due)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_due_field)))
                .addContainerGap(279, Short.MAX_VALUE))
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
                    .addComponent(txt_deposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

        btn_cancel.setBackground(new java.awt.Color(21, 76, 121));
        btn_cancel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_cancel.setForeground(new java.awt.Color(255, 255, 255));
        btn_cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_cancel.png"))); // NOI18N
        btn_cancel.setText("Cancel");
        btn_cancel.setNextFocusableComponent(txt_first_name);
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_order_buttonsLayout = new javax.swing.GroupLayout(panel_order_buttons);
        panel_order_buttons.setLayout(panel_order_buttonsLayout);
        panel_order_buttonsLayout.setHorizontalGroup(
            panel_order_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_order_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_save_order)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_cancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_order_buttonsLayout.setVerticalGroup(
            panel_order_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_order_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_order_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_save_order)
                    .addComponent(btn_cancel))
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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_search_prod_servKeyPressed(evt);
            }
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
        list_prod_serv_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                list_prod_serv_searchKeyPressed(evt);
            }
        });
        layered_pane_list_prod_serv.add(list_prod_serv_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 0, 510, -1));

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
        }

        layered_pane_list_prod_serv.add(scroll_pane_products, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 545, 220));

        javax.swing.GroupLayout panel_order_detailsLayout = new javax.swing.GroupLayout(panel_order_details);
        panel_order_details.setLayout(panel_order_detailsLayout);
        panel_order_detailsLayout.setHorizontalGroup(
            panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_order_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel_order_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_order_detailsLayout.createSequentialGroup()
                        .addGroup(panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panel_input_detail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panel_total_amount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(layered_pane_list_fault)
                            .addGroup(panel_order_detailsLayout.createSequentialGroup()
                                .addGroup(panel_order_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel_order_detailsLayout.createSequentialGroup()
                                        .addComponent(txt_search_fault, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbl_search_fault_icon))
                                    .addComponent(layered_pane_list_prod_serv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panel_order_detailsLayout.createSequentialGroup()
                                        .addComponent(txt_search_prod_serv, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbl_search_prod_serv_icon)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_order_details, javax.swing.GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE)
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
        ServiceOrder addOrder = getOrderFields();
        List<ServiceOrderFault> listOrderFault = getOrderFault(addOrder);
        List<ServiceOrderProdServ> listOrderProdServ = getOrderProdServ(addOrder);
        boolean isAdded = false;

        if (addOrder != null) {
            try {
                // Try to find existing device by serial number
                Device device = _deviceController.searchDeviceBySerialNumber(addOrder.getDevice().getSerialNumber());
                if (device != null) {
                    addOrder.setDevice(device);
                }

                List<ServiceOrderPayment> payments = null;
                Deposit deposit = null;
                OrderNote orderNote = null;

                if (!this.txt_deposit.getText().trim().isEmpty()) {
                    PaymentModal paymentModal = new PaymentModal(addOrder, this.txt_deposit.getText(), new MainMenuView(CommonSetting.COMPANY), true);
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

                    deposit = new Deposit(addOrder, addOrder.getEmployee(), Double.parseDouble(this.txt_deposit.getText()), addOrder.getCreated());
                }

                orderNote = new OrderNote(addOrder, addOrder.getEmployee(), CommonConstant.ORDER_CREATED_NOTE, new Date());

                long idOrderAdded = this._orderController.addServiceOrder(
                        addOrder, listOrderFault, listOrderProdServ, payments, deposit, orderNote);

                if (idOrderAdded > 0) {
                    isAdded = true;
                    addOrder.setIdServiceOrder(idOrderAdded);
                }

                if (isAdded) {
                    JOptionPane.showMessageDialog(this, CommonConstant.SUCCESS_SAVE);
                    clearFields();

                    new ReportGenerator().generateServiceOrderReport(addOrder, listOrderFault, listOrderProdServ);
                }
            } catch (BusinessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btn_save_orderActionPerformed

    private void txt_depositKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_depositKeyReleased
        double totalPrice = CommonExtension.formatEuroToDouble(this.lbl_total_field.getText());
        if (!this.txt_deposit.getText().trim().isEmpty()) {
            //totalPrice = Double.parseDouble(this.lbl_total_field.getText());
            double deposit = CommonExtension.formatEuroToDouble(this.txt_deposit.getText());

            this.lbl_due_field.setText(CommonExtension.formatEuroCurrency(totalPrice - deposit));
        } else {
            this.lbl_due_field.setText(CommonExtension.formatEuroCurrency(totalPrice));
        }
    }//GEN-LAST:event_txt_depositKeyReleased

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        int confirmCancelling = JOptionPane.showConfirmDialog(this, CommonConstant.CONFIRM_CANCEL, this.getTitle(),
                JOptionPane.YES_NO_OPTION);
        if (confirmCancelling == 0) {
            //new MainMenu().setVisible(true);
            CommonSetting.MAIN_MENU_DESKTOP_PANE.removeAll();
        }
    }//GEN-LAST:event_btn_cancelActionPerformed

    private void txt_depositKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_depositKeyPressed
        //Accepts number characters only
        if (Character.isLetter(evt.getKeyChar())) {
            this.txt_deposit.setEditable(false);
        } else {
            this.txt_deposit.setEditable(true);
        }
    }//GEN-LAST:event_txt_depositKeyPressed

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
            this._dtmFault.removeRow(table_view_faults.getSelectedRow());
        }
    }//GEN-LAST:event_table_view_faultsMouseClicked

    private void txt_search_prod_servFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_search_prod_servFocusLost
        this.txt_search_prod_serv.setText("");
        this._defaultListModelProdServ.removeAllElements();
    }//GEN-LAST:event_txt_search_prod_servFocusLost

    private void txt_search_prod_servKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search_prod_servKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            if (!this._defaultListModelProdServ.isEmpty()) {
                list_prod_serv_search.requestFocus();
                if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                    list_prod_serv_search.setSelectedIndex(0);
                }
            }
        } else {
            searchProdServ(); // Call search only when typing
        }
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
            this._dtmProdServ.removeRow(this.table_view_products.getSelectedRow());
            // Sum price column and set into total textField
            getPriceSum();
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

    private void list_prod_serv_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_list_prod_serv_searchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!list_prod_serv_search.isSelectionEmpty()) {
                ProductService prodServ = (ProductService) _defaultListModelProdServ.getElementAt(list_prod_serv_search.getSelectedIndex());
                addProductService(prodServ);
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            txt_search_prod_serv.requestFocus(); // Return focus to search box
        }
    }//GEN-LAST:event_list_prod_serv_searchKeyPressed

    private void txt_search_prod_servKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search_prod_servKeyPressed
        System.out.println("Key Pressed: " + evt.getKeyCode());

        // Check if the list is still attached to the UI
        if (list_prod_serv_search.getParent() != null) {
            System.out.println("List Parent: " + list_prod_serv_search.getParent().getClass().getName());
        } else {
            System.out.println("List has no parent, it might have been removed!");
        }

        System.out.println("List Visible? " + list_prod_serv_search.isVisible());
        System.out.println("List Showing? " + list_prod_serv_search.isShowing());
        System.out.println("List Size: " + _defaultListModelProdServ.getSize());

        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            if (_defaultListModelProdServ.getSize() > 0) {
                System.out.println("Focusing on list...");
                ensureListIsVisible();  // Make sure it's visible
                list_prod_serv_search.setSelectedIndex(0);
                list_prod_serv_search.requestFocusInWindow();
            } else {
                System.out.println("List is empty, not moving focus.");
            }
        }
    }//GEN-LAST:event_txt_search_prod_servKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_copy;
    private javax.swing.JButton btn_international_number1;
    private javax.swing.JButton btn_save_order;
    private javax.swing.JButton btn_seacrh_customer;
    private javax.swing.JEditorPane editor_pane_notes;
    private javax.swing.JLayeredPane layered_pane_list_fault;
    private javax.swing.JLayeredPane layered_pane_list_prod_serv;
    private javax.swing.JLabel lbl_bad_sectors;
    private javax.swing.JLabel lbl_bad_sectors_star;
    private javax.swing.JLabel lbl_brand;
    private javax.swing.JLabel lbl_contact;
    private javax.swing.JLabel lbl_contact_star;
    private javax.swing.JLabel lbl_deposit;
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
