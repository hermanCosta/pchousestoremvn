package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.CustomerController;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.controllers.SaleController;
import com.pchouse.pchousestoremvn.controllers.RefurbController;
import com.pchouse.pchousestoremvn.enums.OrderStatus;
import com.pchouse.pchousestoremvn.enums.PaymentType;
import com.pchouse.pchousestoremvn.enums.SaleType;
import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.Person;
import com.pchouse.pchousestoremvn.models.Refurb;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.RefurbSale;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.util.ReportGenerator;
import com.pchouse.pchousestoremvn.views.modals.CustomerModal;
import com.pchouse.pchousestoremvn.views.modals.PaymentModal;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultListModel;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class NewRefurbSaleView extends javax.swing.JInternalFrame {

    private long hdnCustomerId;
    private List<Refurb> _listRefurbs;
    public ServiceOrderPayment _orderPayment = null;
    private final SaleController _saleController;
    private final RefurbController _refurbController;
    private final CustomerController _customerController;
    private final EmployeeController _employeeController;
    private final DefaultTableModel _dtmRefurb;
    private final DefaultListModel _defaultListModelRefurb;
    Frame _parentFrame = JOptionPane.getFrameForComponent(this);

    public NewRefurbSaleView() {
        initComponents();

        this.lbl_total_amount.setText("");
        this.lbl_remaining_amount.setText("");
        //avoid auto old value by focus loosing
        this.txt_contact.setFocusLostBehavior(JFormattedTextField.PERSIST);

        CommonExtension.checkEmailFormat(this.txt_email);
        CommonSetting.requestTxtFocus(txt_first_name);
        CommonSetting.tableSettings(table_view_refurbs);

        this._saleController = new SaleController();
        this._refurbController = new RefurbController();
        this._customerController = new CustomerController();
        this._employeeController = new EmployeeController();

        this._dtmRefurb = (DefaultTableModel) this.table_view_refurbs.getModel();
        this._defaultListModelRefurb = new DefaultListModel();
        this.list_refurbs_search.setModel(_defaultListModelRefurb);
    }

    public void setCustomerFields(Customer customer) {
        if (customer != null) {
            this.txt_contact.setFormatterFactory(null);
            this.hdnCustomerId = customer.getIdCustomer();
            this.txt_first_name.setText(customer.getPerson().getFirstName());
            this.txt_last_name.setText(customer.getPerson().getLastName());
            this.txt_contact.setText(customer.getPerson().getContactNo());
            this.txt_email.setText(customer.getPerson().getEmail());
        }
    }

    private void searchRefurb() {
        this._defaultListModelRefurb.removeAllElements();

        String searchText = txt_search_refurb.getText().trim();

        if (!searchText.isEmpty()) {
            this._listRefurbs = _refurbController.searchRefurbProducts(searchText);
            if (!_listRefurbs.isEmpty()) {
                _listRefurbs.forEach(refurb -> {
                    this._defaultListModelRefurb.addElement(refurb);
                });
            }
        }
    }

    private void getPriceSum() {
        double sum = 0;
        for (int i = 0; i < this._dtmRefurb.getRowCount(); i++) {
            sum += Double.parseDouble(this._dtmRefurb.getValueAt(i, 3).toString());
        }

        this.lbl_total_amount.setText(CommonExtension.formatEuroCurrency(sum));
        this.lbl_remaining_amount.setText(this.lbl_total_amount.getText());
    }

    private void clearFields() {
        this.hdnCustomerId = 0;
        this.txt_first_name.setText("");
        this.txt_last_name.setText("");
        this.txt_contact.setText("");
        this.txt_email.setText("");
        this.editor_pane_notes.setText("");
        this.lbl_total_amount.setText("");
        this.txt_deposit_amount.setText("");
        this.lbl_remaining_amount.setText("");

        this._dtmRefurb.setRowCount(0);

        this.txt_first_name.requestFocus();
    }

    private Sale getSaleFields() {
        if (!validateRequiredFields()) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, this.getTitle(), JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String password = CommonExtension.requestUserPassword();
        Employee employee = _employeeController.getEmployeeByPass(password);

        if (employee == null) {
            JOptionPane.showMessageDialog(this, CommonConstant.NOT_AUTHORIZED, null, JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Customer customer = resolveCustomer();
        if (customer == null) {
            return null;
        }

        return createSale(customer, employee);
    }

    private boolean validateRequiredFields() {
        return !txt_first_name.getText().trim().isEmpty()
                && !txt_last_name.getText().trim().isEmpty()
                && !txt_contact.getText().trim().isEmpty()
                && table_view_refurbs.getRowCount() > 0;
    }

    private Customer resolveCustomer() {
        long idCustomer = hdnCustomerId;
        String firstName = txt_first_name.getText().trim();
        String lastName = txt_last_name.getText().trim();
        String contact = CommonExtension.normalizePhone(txt_contact.getText());
        String email = txt_email.getText().trim();

        if (idCustomer > 0) {
            Customer existingCustomer = _customerController.getCustomerById(idCustomer);
            if (!dataMatches(existingCustomer, firstName, lastName, contact, email)) {
                JOptionPane.showMessageDialog(this, CommonConstant.WARN_CUSTOMER_MATCHING, this.getTitle(), JOptionPane.WARNING_MESSAGE);
                CustomerModal customerModal = new CustomerModal(this, new MainMenuView(CommonSetting.COMPANY), true, existingCustomer);
                customerModal.setVisible(true);
                this.hdnCustomerId = 0;
                return null;
            }
            return existingCustomer;
        } else {
            Customer foundCustomer = _customerController.searchCustomerByContactNo(contact);
            if (foundCustomer != null) {
                JOptionPane.showMessageDialog(this, CommonConstant.WARN_EXIST_PERSON, this.getTitle(), JOptionPane.WARNING_MESSAGE);
                CustomerModal customerModal = new CustomerModal(this, _parentFrame, true, foundCustomer);
                customerModal.setLocationRelativeTo(this);
                customerModal.setVisible(true);
                return null;
            } else {
                Person newPerson = new Person(
                        firstName.toUpperCase(),
                        lastName.toUpperCase(),
                        contact,
                        email.toLowerCase()
                );
                return new Customer(newPerson, CommonSetting.COMPANY);
            }
        }
    }

    private boolean dataMatches(Customer customer, String firstName, String lastName, String contact, String email) {
        if (customer == null || customer.getPerson() == null) {
            return false;
        }

        Person person = customer.getPerson();

        return person.getFirstName().trim().equals(firstName)
                && person.getLastName().trim().equals(lastName)
                && CommonExtension.normalizePhone(person.getContactNo()).equals(contact)
                && Objects.equals(person.getEmail().trim(), email);
    }

    private Sale createSale(Customer customer, Employee employee) {
        String importanNotes = this.editor_pane_notes.getText();
        double totalAmount = CommonExtension.formatEuroToDouble(lbl_total_amount.getText());
        double remaining = CommonExtension.formatEuroToDouble(lbl_remaining_amount.getText());

        Sale getSale = new Sale(
                customer,
                employee,
                CommonSetting.COMPANY,
                totalAmount,
                remaining,
                new Date(),
                OrderStatus.CREATED,
                SaleType.REFURB
        );

        getSale.setImportantNotes(importanNotes);

        return getSale;
    }

    private List<RefurbSale> getRefurbSales(Sale sale) {
        List<RefurbSale> listSaleRefurb = new ArrayList<>();

        if (this.table_view_refurbs.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_ADD_ITEM + "Refurbs", this.getTitle(), JOptionPane.WARNING_MESSAGE);

            return listSaleRefurb;
        } else {

            for (int i = 0; i < _dtmRefurb.getRowCount(); i++) {
                RefurbSale saleRefurbItem = new RefurbSale();

                Refurb refurbItem = _refurbController.getRefurbProductById(Integer.parseInt(_dtmRefurb.getValueAt(i, 0).toString()));

                saleRefurbItem.setSale(sale);
                saleRefurbItem.setRefurb(refurbItem);
                saleRefurbItem.setQty(Integer.parseInt(_dtmRefurb.getValueAt(i, 2).toString()));
                saleRefurbItem.setTotal(Double.parseDouble(_dtmRefurb.getValueAt(i, 3).toString()));

                listSaleRefurb.add(saleRefurbItem);
            }

        }
        return listSaleRefurb;
    }

    private void addProdServToTheTable() {
        if (!this.list_refurbs_search.isSelectionEmpty()) {
            Refurb refurb = (Refurb) this._defaultListModelRefurb.getElementAt(
                    this.list_refurbs_search.getSelectedIndex()
            );
            boolean isAdded = false;

            if (this.table_view_refurbs.getRowCount() > 0) {
                for (int i = 0; i < this.table_view_refurbs.getRowCount(); i++) {
                    int idProdServ = Integer.parseInt(_dtmRefurb.getValueAt(i, 0).toString());
                    if (idProdServ == refurb.getIdRefurb()) {
                        isAdded = true;
                        break;
                    }
                }
            }

            if (!isAdded) {
                _dtmRefurb.addRow(new Object[]{
                    refurb.getIdRefurb(),
                    refurb.toString(),
                    CommonConstant.DEFAULT_QTY,
                    CommonExtension.formatToPriceField(refurb.getPrice()),
                    CommonExtension.formatToPriceField(refurb.getPrice() * CommonConstant.DEFAULT_QTY)
                });
            }

            this._defaultListModelRefurb.removeAllElements();
            this.txt_search_refurb.setText("");
            this.txt_search_refurb.requestFocus();
            getPriceSum();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, getTitle(), JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_refurb_sale_details = new javax.swing.JPanel();
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
        btn_international_number1 = new javax.swing.JButton();
        scroll_pane_notes = new javax.swing.JScrollPane();
        editor_pane_notes = new javax.swing.JEditorPane();
        panel_total_amount = new javax.swing.JPanel();
        lbl_total = new javax.swing.JLabel();
        lbl_deposit = new javax.swing.JLabel();
        txt_deposit_amount = new javax.swing.JTextField();
        lbl_remaining = new javax.swing.JLabel();
        lbl_total_amount = new javax.swing.JLabel();
        lbl_remaining_amount = new javax.swing.JLabel();
        panel_sale_buttons = new javax.swing.JPanel();
        btn_save_sale = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();
        txt_search_refurb = new javax.swing.JTextField();
        lbl_search_refurb_icon = new javax.swing.JLabel();
        layered_pane_list_refurbs = new javax.swing.JLayeredPane();
        list_refurbs_search = new javax.swing.JList<>();
        scroll_pane_refurbs = new javax.swing.JScrollPane();
        table_view_refurbs = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("New Refurb Sale");
        setMaximumSize(new java.awt.Dimension(1049, 700));
        setPreferredSize(new java.awt.Dimension(1050, 650));

        panel_refurb_sale_details.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_refurb_sale_details.setPreferredSize(new java.awt.Dimension(1026, 607));

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

        btn_international_number1.setBackground(new java.awt.Color(0, 0, 0));
        btn_international_number1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_international_number.png"))); // NOI18N
        btn_international_number1.setPreferredSize(new java.awt.Dimension(35, 25));
        btn_international_number1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_international_number1ActionPerformed(evt);
            }
        });

        scroll_pane_notes.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll_pane_notes.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll_pane_notes.setVerifyInputWhenFocusTarget(false);

        editor_pane_notes.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Important Notes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 14))); // NOI18N
        editor_pane_notes.setFocusCycleRoot(false);
        editor_pane_notes.setPreferredSize(new java.awt.Dimension(403, 58));
        editor_pane_notes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                editor_pane_notesKeyPressed(evt);
            }
        });
        scroll_pane_notes.setViewportView(editor_pane_notes);

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
                    .addGap(10, 10, 10)
                    .addComponent(scroll_pane_notes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGap(11, 11, 11)))
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
                .addGroup(panel_input_detailLayout.createSequentialGroup()
                    .addGap(136, 136, 136)
                    .addComponent(scroll_pane_notes, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        panel_total_amount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_total.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_total.setText("Total:");

        lbl_deposit.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_deposit.setText("Deposit:");

        txt_deposit_amount.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N
        txt_deposit_amount.setNextFocusableComponent(btn_save_sale);
        txt_deposit_amount.setPreferredSize(new java.awt.Dimension(100, 25));
        txt_deposit_amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_deposit_amountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_deposit_amountKeyReleased(evt);
            }
        });

        lbl_remaining.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lbl_remaining.setText("Remaining");

        lbl_total_amount.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_total_amount.setText("totalAmount");

        lbl_remaining_amount.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_remaining_amount.setText("remainingAmount");

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
                        .addComponent(txt_deposit_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(txt_deposit_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_total_amountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_remaining)
                    .addComponent(lbl_remaining_amount))
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

        txt_search_refurb.setNextFocusableComponent(txt_deposit_amount);
        txt_search_refurb.setPreferredSize(new java.awt.Dimension(518, 25));
        txt_search_refurb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_search_refurbFocusLost(evt);
            }
        });
        txt_search_refurb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_search_refurbKeyReleased(evt);
            }
        });

        lbl_search_refurb_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search_small_left.png"))); // NOI18N

        layered_pane_list_refurbs.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        list_refurbs_search.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        list_refurbs_search.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list_refurbs_search.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        list_refurbs_search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_refurbs_searchMouseClicked(evt);
            }
        });
        list_refurbs_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                list_refurbs_searchKeyPressed(evt);
            }
        });
        layered_pane_list_refurbs.add(list_refurbs_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 0, 510, -1));

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
        table_view_refurbs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_refurbsMouseClicked(evt);
            }
        });
        table_view_refurbs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                table_view_refurbsKeyReleased(evt);
            }
        });
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

        layered_pane_list_refurbs.add(scroll_pane_refurbs, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 545, 410));

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel_refurb_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(layered_pane_list_refurbs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel_refurb_sale_detailsLayout.createSequentialGroup()
                                .addComponent(txt_search_refurb, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_search_refurb_icon))))
                    .addComponent(panel_total_amount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_refurb_sale_detailsLayout.setVerticalGroup(
            panel_refurb_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_sale_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurb_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_refurb_sale_detailsLayout.createSequentialGroup()
                        .addGroup(panel_refurb_sale_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_search_refurb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_search_refurb_icon, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addComponent(layered_pane_list_refurbs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panel_input_detail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(panel_refurb_sale_details, javax.swing.GroupLayout.DEFAULT_SIZE, 1027, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_refurb_sale_details, javax.swing.GroupLayout.PREFERRED_SIZE, 602, Short.MAX_VALUE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("New Sale");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_save_saleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_save_saleActionPerformed
        Sale addSale = getSaleFields();
        List<RefurbSale> listRefurbSales = getRefurbSales(addSale);
        boolean isAdded = false;

        if (addSale != null) {
            try {
                Deposit deposit = null;
                String amountTopay = this.lbl_remaining_amount.getText().replaceAll("[^\\d.\\-]", ""); // Keeps digits, dot, minus sign; 
                String refurbSaleStatus = CommonConstant.REFURB_SALE_PICKED_NOTE;
                var paymentType = PaymentType.REFURB;
                boolean isDeposit = false;

                if (!this.txt_deposit_amount.getText().trim().isEmpty()) {
                    amountTopay = this.txt_deposit_amount.getText();
                    deposit = new Deposit(addSale, addSale.getEmployee(), Double.parseDouble(this.txt_deposit_amount.getText()), addSale.getCreated());
                    refurbSaleStatus = CommonConstant.REFURB_SALE_CREATED_NOTE;
                    paymentType = PaymentType.DEPOSIT;
                    isDeposit = true;
                }

                PaymentModal paymentModal = new PaymentModal(addSale, paymentType, amountTopay, SwingUtilities.getWindowAncestor(this), true);
                paymentModal.setVisible(true);

                List<SalePayment> payments = paymentModal.getSalePayments();

                if (payments == null || payments.isEmpty()) {
                    showError("Payment was not completed.");
                    return;
                }

                OrderNote saleNote = new OrderNote(addSale, addSale.getEmployee(), refurbSaleStatus, new Date());

                long idSaleAdded = this._saleController.addRefurbSale(addSale, listRefurbSales, payments, deposit, saleNote);

                if (idSaleAdded > 0) {
                    isAdded = true;
                    addSale.setIdSale(idSaleAdded);
                }

                if (isAdded) {
                    clearFields();

                    if (!isDeposit) {
                        // Genarate and display the report
                        new ReportGenerator().generateRefurbSaleReceiptReport(addSale, listRefurbSales, payments);
                    }
                }

            } catch (BusinessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), this.getTitle(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_save_saleActionPerformed

    private void txt_deposit_amountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_deposit_amountKeyReleased
        double totalPrice = CommonExtension.formatEuroToDouble(this.lbl_total_amount.getText());
        if (!this.txt_deposit_amount.getText().trim().isEmpty()) {
            //totalPrice = Double.parseDouble(this.lbl_total_field.getText());
            double deposit = CommonExtension.formatEuroToDouble(this.txt_deposit_amount.getText());

            this.lbl_remaining_amount.setText(CommonExtension.formatEuroCurrency(totalPrice - deposit));
        } else {
            this.lbl_remaining_amount.setText(CommonExtension.formatEuroCurrency(totalPrice));
        }
    }//GEN-LAST:event_txt_deposit_amountKeyReleased

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        int confirmCancelling = JOptionPane.showConfirmDialog(this, CommonConstant.CONFIRM_CANCEL, this.getTitle(),
                JOptionPane.YES_NO_OPTION);
        if (confirmCancelling == 0) {
            //new MainMenu().setVisible(true);
            CommonSetting.MAIN_MENU_DESKTOP_PANE.removeAll();
        }
    }//GEN-LAST:event_btn_cancelActionPerformed

    private void txt_deposit_amountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_deposit_amountKeyPressed
        //Accepts number characters only
        if (Character.isLetter(evt.getKeyChar())) {
            this.txt_deposit_amount.setEditable(false);
        } else {
            this.txt_deposit_amount.setEditable(true);
        }
    }//GEN-LAST:event_txt_deposit_amountKeyPressed

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

    private void txt_search_refurbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_search_refurbFocusLost
        SwingUtilities.invokeLater(() -> {
            if (!list_refurbs_search.hasFocus()) {
                this.txt_search_refurb.setText("");
                this._defaultListModelRefurb.removeAllElements();
            }
        });
    }//GEN-LAST:event_txt_search_refurbFocusLost

    private void txt_search_refurbKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search_refurbKeyReleased
        int key = evt.getKeyCode();

        // Search logic
        searchRefurb();

        // Handle key navigation
        if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_TAB) && !_defaultListModelRefurb.isEmpty()) {
            list_refurbs_search.requestFocus();
            list_refurbs_search.setSelectedIndex(0);
        }
    }//GEN-LAST:event_txt_search_refurbKeyReleased

    private void table_view_refurbsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_refurbsMouseClicked
        if (evt.getClickCount() == 2) {
            this._dtmRefurb.removeRow(this.table_view_refurbs.getSelectedRow());
            // Sum price column and set into total textField
            getPriceSum();
        }
    }//GEN-LAST:event_table_view_refurbsMouseClicked

    private void table_view_refurbsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_table_view_refurbsKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            double sum = 0;
            for (int i = 0; i < this._dtmRefurb.getRowCount(); i++) {
                double unitPrice = Double.parseDouble(this._dtmRefurb.getValueAt(i, 3).toString());
                int qty = Integer.parseInt(this._dtmRefurb.getValueAt(i, 2).toString());

                this._dtmRefurb.setValueAt(unitPrice, i, 3);
                double priceTotal = unitPrice * qty;
                this._dtmRefurb.setValueAt(priceTotal, i, 4);
                sum += priceTotal;
            }

            this.lbl_total_amount.setText(String.valueOf((sum)));
            this.lbl_remaining_amount.setText(String.valueOf(this.lbl_total_amount.getText()));
        }
    }//GEN-LAST:event_table_view_refurbsKeyReleased

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

    private void list_refurbs_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_list_refurbs_searchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addProdServToTheTable();
        }
    }//GEN-LAST:event_list_refurbs_searchKeyPressed

    private void list_refurbs_searchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_refurbs_searchMouseClicked
        addProdServToTheTable();
    }//GEN-LAST:event_list_refurbs_searchMouseClicked

    private void editor_pane_notesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editor_pane_notesKeyPressed
        if (evt.isShiftDown() && evt.getKeyCode() == KeyEvent.VK_TAB) {
            txt_email.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            txt_email.requestFocus();
        }
    }//GEN-LAST:event_editor_pane_notesKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_copy;
    private javax.swing.JButton btn_international_number1;
    private javax.swing.JButton btn_save_sale;
    private javax.swing.JButton btn_seacrh_customer;
    private javax.swing.JEditorPane editor_pane_notes;
    private javax.swing.JLayeredPane layered_pane_list_refurbs;
    private javax.swing.JLabel lbl_contact;
    private javax.swing.JLabel lbl_contact_star;
    private javax.swing.JLabel lbl_deposit;
    private javax.swing.JLabel lbl_email;
    private javax.swing.JLabel lbl_first_name;
    private javax.swing.JLabel lbl_first_name_star;
    private javax.swing.JLabel lbl_last_name;
    private javax.swing.JLabel lbl_last_name_star;
    private javax.swing.JLabel lbl_remaining;
    private javax.swing.JLabel lbl_remaining_amount;
    private javax.swing.JLabel lbl_search_refurb_icon;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JLabel lbl_total_amount;
    private javax.swing.JList<String> list_refurbs_search;
    private javax.swing.JPanel panel_input_detail;
    private javax.swing.JPanel panel_refurb_sale_details;
    private javax.swing.JPanel panel_sale_buttons;
    private javax.swing.JPanel panel_total_amount;
    private javax.swing.JScrollPane scroll_pane_notes;
    private javax.swing.JScrollPane scroll_pane_refurbs;
    private javax.swing.JTable table_view_refurbs;
    private javax.swing.JFormattedTextField txt_contact;
    private javax.swing.JTextField txt_deposit_amount;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_first_name;
    private javax.swing.JTextField txt_last_name;
    private javax.swing.JTextField txt_search_refurb;
    // End of variables declaration//GEN-END:variables

}
