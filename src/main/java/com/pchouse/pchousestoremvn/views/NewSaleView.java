package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.CustomerController;
import com.pchouse.pchousestoremvn.controllers.DepositController;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.controllers.SaleController;
import com.pchouse.pchousestoremvn.controllers.OrderNoteController;
import com.pchouse.pchousestoremvn.controllers.SaleProdServController;
import com.pchouse.pchousestoremvn.controllers.ProductServiceController;
import com.pchouse.pchousestoremvn.enums.OrderStatus;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.Person;
import com.pchouse.pchousestoremvn.models.ProductService;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SaleProdServ;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.views.modals.CustomerModal;
import com.pchouse.pchousestoremvn.views.modals.PaymentModal;
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

public class NewSaleView extends javax.swing.JInternalFrame {

    private long hdnCustomerId;
    private List<ProductService> _listProdServ;
    private Sale _saleModel;
    public ServiceOrderPayment _orderPayment = null;
    private final SaleController _saleController;
    private final ProductServiceController _productServiceController;
    private final CustomerController _customerController;
    private final DepositController _depositController;
    private final EmployeeController _employeeController;
    private final SaleProdServController _saleProdServController;
    private final OrderNoteController _saleNoteController;
    private final DefaultTableModel _dtmProdServ;
    private final DefaultListModel _defaultListModelProdServ;
    Frame _parentFrame = JOptionPane.getFrameForComponent(this);

    public NewSaleView() {
        initComponents();

        //avoid auto old value by focus loosing
        this.txt_contact.setFocusLostBehavior(JFormattedTextField.PERSIST);

        CommonExtension.checkEmailFormat(this.txt_email);
        CommonSetting.requestTxtFocus(txt_first_name);
        CommonSetting.tableSettings(table_view_products);

        this._saleController = new SaleController();
        this._productServiceController = new ProductServiceController();
        this._customerController = new CustomerController();
        this._depositController = new DepositController();
        this._employeeController = new EmployeeController();
        this._saleProdServController = new SaleProdServController();
        this._saleNoteController = new OrderNoteController();
        this._dtmProdServ = (DefaultTableModel) this.table_view_products.getModel();
        this._defaultListModelProdServ = new DefaultListModel();
        this.list_prod_serv_search.setModel(_defaultListModelProdServ);

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
            this.hdn_txt_customer_id.setText(String.valueOf(customer.getIdCustomer()));
            this.txt_first_name.setText(customer.getPerson().getFirstName());
            this.txt_last_name.setText(customer.getPerson().getLastName());
            this.txt_contact.setText(customer.getPerson().getContactNo());
            this.txt_email.setText(customer.getPerson().getEmail());
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
        this.hdn_txt_customer_id.setText("");
        this.txt_first_name.setText("");
        this.txt_last_name.setText("");
        this.txt_contact.setText("");
        this.txt_email.setText("");
        this.lbl_total_field.setText("");
        this.txt_deposit.setText("");
        this.lbl_due_field.setText("");

        this._dtmProdServ.setRowCount(0);

        this.txt_first_name.requestFocus();
    }

    private Sale getSaleFields() {
        Sale saleDetails = null;
        Customer customer = null;
        String password;
        Employee employee;
        boolean isNewCustomer = true;

        if (this.txt_first_name.getText().trim().isEmpty() || this.txt_last_name.getText().trim().isEmpty()
                || this.txt_contact.getText().trim().isEmpty()
                || this.table_view_products.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, this.getTitle(), JOptionPane.WARNING_MESSAGE);

            return saleDetails;
        } else {
            password = CommonExtension.requestUserPassword();
            employee = _employeeController.getEmployeeByPass(password);
            if (employee != null) {
                int idCustomer = CommonExtension.setIdExtension(this.hdn_txt_customer_id);

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
                        this.hdn_txt_customer_id.setText("");
                        return saleDetails;
                    }
                }

                if (isNewCustomer) {
                    Customer checkCustomer = _customerController.searchCustomerByContactNo(this.txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));

                    if (checkCustomer != null) {
                        JOptionPane.showMessageDialog(this, CommonConstant.WARN_EXIST_PERSON, this.getTitle(), JOptionPane.WARNING_MESSAGE);

                        CustomerModal customerModal = new CustomerModal(this, _parentFrame, true, checkCustomer);
                        customerModal.setLocationRelativeTo(this);
                        customerModal.setVisible(true);

                        return saleDetails;
                    } else {

                        Person person = new Person(
                                this.txt_first_name.getText().toUpperCase(),
                                this.txt_last_name.getText().toUpperCase(),
                                this.txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""),
                                this.txt_email.getText().toLowerCase());

                        customer = new Customer(person, CommonSetting.COMPANY);
                    }
                }

                saleDetails = new Sale(
                        customer,
                        employee,
                        CommonSetting.COMPANY,
                        CommonExtension.formatEuroToDouble(this.lbl_total_field.getText()),
                        new Date(),
                        null,
                        null,
                        OrderStatus.FINISHED.toString());

            } else {
                JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, null, JOptionPane.ERROR_MESSAGE);
            }
        }

        return saleDetails;
    }

    private List<SaleProdServ> getSaleProdServ(Sale sale) {
        List<SaleProdServ> listSaleProdServ = new ArrayList<>();

        if (this.table_view_products.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_ADD_ITEM + "product|service", this.getTitle(), JOptionPane.WARNING_MESSAGE);

            return listSaleProdServ;
        } else {

            for (int i = 0; i < _dtmProdServ.getRowCount(); i++) {
                ProductService prodServItem = new ProductService();
                SaleProdServ saleProdServItem = new SaleProdServ();

                prodServItem = _productServiceController.getProductServiceById(Integer.parseInt(_dtmProdServ.getValueAt(i, 0).toString()));

                saleProdServItem.setSale(sale);
                saleProdServItem.setProdServ(prodServItem);
                saleProdServItem.setQty(Integer.parseInt(_dtmProdServ.getValueAt(i, 2).toString()));
                saleProdServItem.setTotal(Double.parseDouble(_dtmProdServ.getValueAt(i, 3).toString()));

                listSaleProdServ.add(saleProdServItem);
            }

        }
        return listSaleProdServ;
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_sale_details = new javax.swing.JPanel();
        panel_input_detail = new javax.swing.JPanel();
        lbl_first_name_star = new javax.swing.JLabel();
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
        lbl_last_name_star = new javax.swing.JLabel();
        lbl_contact_star = new javax.swing.JLabel();
        hdn_txt_customer_id = new javax.swing.JTextField();
        btn_international_number1 = new javax.swing.JButton();
        panel_total_amount = new javax.swing.JPanel();
        lbl_total = new javax.swing.JLabel();
        lbl_deposit = new javax.swing.JLabel();
        txt_deposit = new javax.swing.JTextField();
        lbl_due = new javax.swing.JLabel();
        lbl_total_field = new javax.swing.JLabel();
        lbl_due_field = new javax.swing.JLabel();
        panel_sale_buttons = new javax.swing.JPanel();
        btn_save_sale = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();
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

        panel_sale_details.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_sale_details.setPreferredSize(new java.awt.Dimension(1026, 607));

        panel_input_detail.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_first_name_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_first_name_star.setForeground(java.awt.Color.red);
        lbl_first_name_star.setText("*");

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

        txt_email.setPreferredSize(new java.awt.Dimension(388, 25));

        lbl_last_name_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_last_name_star.setForeground(java.awt.Color.red);
        lbl_last_name_star.setText("*");

        lbl_contact_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_contact_star.setForeground(java.awt.Color.red);
        lbl_contact_star.setText("*");

        hdn_txt_customer_id.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        hdn_txt_customer_id.setEnabled(false);
        hdn_txt_customer_id.setMinimumSize(new java.awt.Dimension(80, 32));
        hdn_txt_customer_id.setPreferredSize(new java.awt.Dimension(0, 0));

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
                        .addComponent(lbl_email)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_input_detailLayout.createSequentialGroup()
                        .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_last_name_star, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_contact_star, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_first_name_star, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_last_name)
                                    .addComponent(lbl_first_name))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_first_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_last_name, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))))))
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
                .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_first_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_first_name))
                    .addComponent(lbl_first_name_star, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(lbl_email)))
                    .addComponent(btn_international_number1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panel_input_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_input_detailLayout.createSequentialGroup()
                    .addContainerGap(43, Short.MAX_VALUE)
                    .addComponent(hdn_txt_customer_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(388, Short.MAX_VALUE)))
        );

        panel_total_amount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_total.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_total.setText("Total:");

        lbl_deposit.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_deposit.setText("Deposit:");

        txt_deposit.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N
        txt_deposit.setForeground(new java.awt.Color(51, 51, 255));
        txt_deposit.setNextFocusableComponent(btn_save_sale);
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
                    .addComponent(txt_deposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_due)
                    .addComponent(lbl_due_field))
                .addContainerGap())
        );

        panel_sale_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_save_sale.setBackground(new java.awt.Color(21, 76, 121));
        btn_save_sale.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_save_sale.setForeground(new java.awt.Color(255, 255, 255));
        btn_save_sale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_save.png"))); // NOI18N
        btn_save_sale.setText("Save");
        btn_save_sale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_save_saleActionPerformed(evt);
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

        javax.swing.GroupLayout panel_sale_buttonsLayout = new javax.swing.GroupLayout(panel_sale_buttons);
        panel_sale_buttons.setLayout(panel_sale_buttonsLayout);
        panel_sale_buttonsLayout.setHorizontalGroup(
            panel_sale_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_sale_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_save_sale)
                .addGap(18, 18, 18)
                .addComponent(btn_cancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_sale_buttonsLayout.setVerticalGroup(
            panel_sale_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_sale_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_sale_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_save_sale)
                    .addComponent(btn_cancel))
                .addContainerGap())
        );

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

        layered_pane_list_prod_serv.add(scroll_pane_products, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 545, 410));

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(layered_pane_list_prod_serv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel_sale_detailsLayout.createSequentialGroup()
                                .addComponent(txt_search_prod_serv, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_search_prod_serv_icon))))
                    .addComponent(panel_total_amount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_sale_detailsLayout.setVerticalGroup(
            panel_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_sale_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_sale_detailsLayout.createSequentialGroup()
                        .addGroup(panel_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_search_prod_serv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_search_prod_serv_icon, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addComponent(layered_pane_list_prod_serv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panel_input_detail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
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
                .addComponent(panel_sale_details, javax.swing.GroupLayout.DEFAULT_SIZE, 1027, Short.MAX_VALUE)
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

    private void btn_save_saleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_save_saleActionPerformed
        Sale addSale = getSaleFields();
        boolean isAdded = false;

        if (addSale != null) {
            // Add device

            try {
                long idOrderAdded = this._saleController.addSale(addSale);
                if (idOrderAdded > 0) {
                    addSale.setIdSale(idOrderAdded);

                    List<SaleProdServ> listSaleProdServ = getSaleProdServ(addSale);
                    if (listSaleProdServ != null) {
                        for (SaleProdServ prodServItem : listSaleProdServ) {
                            long idSaleProdServAdded = _saleProdServController.addSaleProdServ(prodServItem);
                            System.out.println("ProdServ Added: " + idSaleProdServAdded);

                            if (idSaleProdServAdded > 0) {
                                isAdded = true;
                            } else {
                                isAdded = false;
                                System.out.println("Error to add ProdServ");
                                return;
                            }
                        }
                    }

                    if (!this.txt_deposit.getText().trim().isEmpty()) {

                        PaymentModal paymentModal = new PaymentModal(addSale, this.txt_deposit.getText(), new MainMenuView(CommonSetting.COMPANY), true);
                        paymentModal.setVisible(true);

                        Deposit deposit = new Deposit(addSale, addSale.getEmployee(), CommonExtension.salePayment, Double.parseDouble(this.txt_deposit.getText()), addSale.getCreated());
                        deposit.setSalePayment(paymentModal.getSalePayment());
                        
                        long idDepositAdded = this._depositController.addDeposit(deposit);
                        if (idDepositAdded > 0) {

                            isAdded = true;
                        } else {
                            JOptionPane.showMessageDialog(this, CommonConstant.ERROR_ADD_DEPOSIT, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                if (isAdded) {
                    // Add creating note
                    OrderNote saleNote = new OrderNote(addSale, addSale.getEmployee(), CommonConstant.SALE_CREATED_NOTE, new Date());

                    _saleNoteController.addOrderNote(saleNote);

                    JOptionPane.showMessageDialog(this, CommonConstant.SUCCESS_SAVE);
                    clearFields();
                }

            } catch (BusinessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }
    }//GEN-LAST:event_btn_save_saleActionPerformed

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

    private void btn_seacrh_customerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_seacrh_customerActionPerformed
        this.hdn_txt_customer_id.setText("");
        CustomerModal customerModal = new CustomerModal(this, new MainMenuView(CommonSetting.COMPANY), true, null);
        customerModal.setVisible(true);
    }//GEN-LAST:event_btn_seacrh_customerActionPerformed

    private void btn_copyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_copyActionPerformed
        StringSelection stringSelection = new StringSelection(txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));
        if (!this.txt_contact.getText().trim().isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        }
    }//GEN-LAST:event_btn_copyActionPerformed

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
    private javax.swing.JButton btn_save_sale;
    private javax.swing.JButton btn_seacrh_customer;
    private javax.swing.JTextField hdn_txt_customer_id;
    private javax.swing.JLayeredPane layered_pane_list_prod_serv;
    private javax.swing.JLabel lbl_contact;
    private javax.swing.JLabel lbl_contact_star;
    private javax.swing.JLabel lbl_deposit;
    private javax.swing.JLabel lbl_due;
    private javax.swing.JLabel lbl_due_field;
    private javax.swing.JLabel lbl_email;
    private javax.swing.JLabel lbl_first_name;
    private javax.swing.JLabel lbl_first_name_star;
    private javax.swing.JLabel lbl_last_name;
    private javax.swing.JLabel lbl_last_name_star;
    private javax.swing.JLabel lbl_search_prod_serv_icon;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JLabel lbl_total_field;
    private javax.swing.JList<String> list_prod_serv_search;
    private javax.swing.JPanel panel_input_detail;
    private javax.swing.JPanel panel_sale_buttons;
    private javax.swing.JPanel panel_sale_details;
    private javax.swing.JPanel panel_total_amount;
    private javax.swing.JScrollPane scroll_pane_products;
    private javax.swing.JTable table_view_products;
    private javax.swing.JFormattedTextField txt_contact;
    private javax.swing.JTextField txt_deposit;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_first_name;
    private javax.swing.JTextField txt_last_name;
    private javax.swing.JTextField txt_search_prod_serv;
    // End of variables declaration//GEN-END:variables

}
