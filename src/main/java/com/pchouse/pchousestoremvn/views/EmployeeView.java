package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.EmployeeController;
import com.pchouse.pchousestoremvn.controllers.PersonController;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.Person;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class EmployeeView extends javax.swing.JInternalFrame {

    private final DefaultTableModel _dtmEmployee;
    private final EmployeeController _employeeController;
    private List<Employee> _listEmployee;

    public EmployeeView() {
        initComponents();
        //avoid auto old value by focus loosing
        this.txt_contact.setFocusLostBehavior(JFormattedTextField.PERSIST);

        CommonSetting.tableSettings(this.table_view_employees);

        this._employeeController = new EmployeeController();
        this._dtmEmployee = (DefaultTableModel) this.table_view_employees.getModel();

        //checkEmailFormat();
        loadEmployeeListTable();
    }

    private void loadEmployeeListTable() {
        this._listEmployee = this._employeeController.getAllEmployee();

        this._dtmEmployee.setRowCount(0);

        if (this._listEmployee != null) {
            this._listEmployee.forEach((empItem) -> {
                this._dtmEmployee.addRow(
                        new Object[]{
                            empItem.getIdEmployee(),
                            empItem.getPerson().getFirstName(),
                            empItem.getPerson().getLastName(),
                            formatContactNo(empItem.getPerson().getContactNo()),
                            empItem.getPerson().getEmail(),
                            empItem.getUsername(),
                            empItem.getAccessLevel(),
                            empItem.getPerson().getIdPerson()
                        }
                );
            });
        }
    }

    private Employee getEmployeeFields() {
        Employee getEmployee = null;

        if (this.txt_first_name.getText().trim().isEmpty() || this.txt_last_name.getText().trim().isEmpty()
                || this.txt_contact.getText().trim().isEmpty() || this.txt_username.getText().trim().isEmpty()
                || this.txt_password.getPassword().length == 0
                || this.txt_confirm_pwd.getPassword().length == 0
                || this.combo_box_access_level.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, this.getTitle(), JOptionPane.WARNING_MESSAGE);

            return getEmployee;
        } else if (!Arrays.equals(this.txt_password.getPassword(), this.txt_confirm_pwd.getPassword())) {

            JOptionPane.showMessageDialog(this, CommonConstant.NOT_MATCH_PASSWORD, this.getTitle(), JOptionPane.ERROR_MESSAGE);

            return getEmployee;
        } else {

            Person person = new Person(
                    this.txt_first_name.getText().toUpperCase(),
                    this.txt_last_name.getText().toUpperCase(),
                    this.txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""),
                    this.txt_email.getText().toLowerCase());

            person.setIdPerson(CommonExtension.setIdExtension(this.hdn_txt_person_id));

            getEmployee = new Employee(person, this.txt_username.getText().toUpperCase(), this.combo_box_access_level.getSelectedItem().toString().toUpperCase());

            getEmployee.setIdEmployee(CommonExtension.setIdExtension(this.hdn_txt_employee_id));
            getEmployee.setPassword(CommonExtension.encryptPassword(this.txt_password));

            return getEmployee;
        }
    }

    private void setEmployeeFields(Employee pEmployee) {
        this.txt_contact.setFormatterFactory(null);
        this.hdn_txt_employee_id.setText(String.valueOf(pEmployee.getIdEmployee()));
        this.hdn_txt_person_id.setText(String.valueOf(pEmployee.getPerson().getIdPerson()));
        this.txt_first_name.setText(pEmployee.getPerson().getFirstName());
        this.txt_last_name.setText(pEmployee.getPerson().getLastName());
        this.txt_contact.setText(pEmployee.getPerson().getContactNo());
        this.txt_email.setText(pEmployee.getPerson().getEmail());
        this.txt_username.setText(pEmployee.getUsername());
        this.combo_box_access_level.setSelectedItem(pEmployee.getAccessLevel());
    }

    private void searchEmployee() {
        PersonController personController = new PersonController();
        Employee searchCustomer = null;

        if (!this.txt_search_employee.getText().trim().isEmpty()) {
            List<Person> _listPerson = personController.searchPerson(this.txt_search_employee.getText());

            if (_listPerson != null) {
                _dtmEmployee.setRowCount(0);

                for (Person person : _listPerson) {
                    searchCustomer = _employeeController.getEmployee(person);

                    if (searchCustomer != null) {
                        _dtmEmployee.addRow(
                                new Object[]{
                                    searchCustomer.getIdEmployee(),
                                    searchCustomer.getPerson().getFirstName(),
                                    searchCustomer.getPerson().getLastName(),
                                    formatContactNo(searchCustomer.getPerson().getContactNo()),
                                    searchCustomer.getPerson().getEmail(),
                                    searchCustomer.getUsername(),
                                    searchCustomer.getAccessLevel(),
                                    searchCustomer.getPerson().getIdPerson()
                                }
                        );
                    }
                }
            }
        } else {
            loadEmployeeListTable();
        }
    }

    private void getItemEmployee(long idEmployee) {
        if (idEmployee != 0) {
            Employee customerItem = _employeeController.getItemEmployee(idEmployee);

            this._dtmEmployee.setRowCount(0);
            _dtmEmployee.addRow(
                    new Object[]{
                        customerItem.getIdEmployee(),
                        customerItem.getPerson().getFirstName(),
                        customerItem.getPerson().getLastName(),
                        formatContactNo(customerItem.getPerson().getContactNo()),
                        customerItem.getPerson().getEmail(),
                        customerItem.getUsername(),
                        customerItem.getAccessLevel(),
                        customerItem.getPerson().getIdPerson()
                    }
            );
        } else {
            loadEmployeeListTable();
        }
    }

    private void clearFields() {
        this.txt_search_employee.setText("");
        this.hdn_txt_employee_id.setText("");
        this.hdn_txt_person_id.setText("");
        this.txt_first_name.setText("");
        this.txt_last_name.setText("");
        this.txt_contact.setText("");
        this.txt_email.setText("");
        this.txt_username.setText("");
        this.txt_password.setText("");
        this.txt_confirm_pwd.setText("");
        this.combo_box_access_level.setSelectedIndex(0);

        this.txt_search_employee.requestFocus();
    }

    private final void checkEmailFormat() {
        this.txt_email.setInputVerifier(new InputVerifier() {

            Border originalBorder;
            String emailFormat = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            String email = txt_email.getText();

            @Override
            public boolean verify(JComponent input) {
                JTextField comp = (JTextField) input;

                return comp.getText().matches(emailFormat) | comp.getText().trim().isEmpty();
            }

            @Override
            public boolean shouldYieldFocus(JComponent input) {
                boolean isValid = verify(input);

                if (!isValid) {
                    originalBorder = originalBorder == null ? input.getBorder() : originalBorder;

                    input.setBorder(new LineBorder(Color.RED));
                } else {
                    if (originalBorder != null) {
                        input.setBorder(originalBorder);
                        originalBorder = null;
                    }
                }
                return isValid;
            }
        });
    }

    private String formatContactNo(String pNumber) {
        String format = "";
        if (!pNumber.startsWith("+") && !pNumber.startsWith("00")) {
            if (pNumber.length() == 9 && pNumber.startsWith("0")) {
                format = pNumber.replaceFirst("(\\d{2})(\\d{3})(\\d+)", "($1) $2-$3");
            } else if (pNumber.length() == 10 && pNumber.startsWith("0")) {
                format = pNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
            }
        } else {
            format = pNumber;
        }

        return format;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_employees = new javax.swing.JPanel();
        lbl_search_icon = new javax.swing.JLabel();
        txt_search_employee = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_view_employees = new javax.swing.JTable();
        panel_employee_input = new javax.swing.JPanel();
        btn_international_number = new javax.swing.JButton();
        lbl_first_name = new javax.swing.JLabel();
        txt_contact = new javax.swing.JFormattedTextField();
        txt_last_name = new javax.swing.JTextField();
        lbl_last_name = new javax.swing.JLabel();
        lbl_contact = new javax.swing.JLabel();
        hdn_txt_employee_id = new javax.swing.JTextField();
        txt_first_name = new javax.swing.JTextField();
        btn_copy = new javax.swing.JButton();
        lbl_email = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        lbl_first_name_star = new javax.swing.JLabel();
        lbl_last_name_star = new javax.swing.JLabel();
        lbl_contact_star = new javax.swing.JLabel();
        lbl_username_star = new javax.swing.JLabel();
        lbl_username = new javax.swing.JLabel();
        txt_username = new javax.swing.JTextField();
        lbl_password_star = new javax.swing.JLabel();
        lbl_password = new javax.swing.JLabel();
        lbl_repeat_pwd_star = new javax.swing.JLabel();
        lbl_confirm_pwd = new javax.swing.JLabel();
        lbl_access_level_star = new javax.swing.JLabel();
        lbl_access_level = new javax.swing.JLabel();
        combo_box_access_level = new javax.swing.JComboBox<>();
        txt_password = new javax.swing.JPasswordField();
        txt_confirm_pwd = new javax.swing.JPasswordField();
        hdn_txt_person_id = new javax.swing.JTextField();
        panel_employee_buttons = new javax.swing.JPanel();
        btn_add = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_clear_fields = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Employee List");
        setMaximumSize(new java.awt.Dimension(0, 0));
        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(1050, 650));
        setVisible(true);

        panel_employees.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_employees.setPreferredSize(new java.awt.Dimension(0, 0));

        lbl_search_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search_black.png"))); // NOI18N

        txt_search_employee.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        txt_search_employee.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_search_employee.setNextFocusableComponent(txt_first_name);
        txt_search_employee.setPreferredSize(new java.awt.Dimension(500, 30));
        txt_search_employee.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_search_employeeKeyReleased(evt);
            }
        });

        table_view_employees.setAutoCreateRowSorter(true);
        table_view_employees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IDEmployee", "First Name", "Last Name", "Contact", "Email", "Username", "Access Level", "IDPerson"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_employees.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        table_view_employees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_employeesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_view_employees);
        if (table_view_employees.getColumnModel().getColumnCount() > 0) {
            table_view_employees.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_employees.getColumnModel().getColumn(0).setPreferredWidth(0);
            table_view_employees.getColumnModel().getColumn(0).setMaxWidth(0);
            table_view_employees.getColumnModel().getColumn(1).setPreferredWidth(200);
            table_view_employees.getColumnModel().getColumn(1).setMaxWidth(250);
            table_view_employees.getColumnModel().getColumn(2).setPreferredWidth(200);
            table_view_employees.getColumnModel().getColumn(2).setMaxWidth(250);
            table_view_employees.getColumnModel().getColumn(3).setPreferredWidth(180);
            table_view_employees.getColumnModel().getColumn(3).setMaxWidth(200);
            table_view_employees.getColumnModel().getColumn(5).setPreferredWidth(150);
            table_view_employees.getColumnModel().getColumn(5).setMaxWidth(200);
            table_view_employees.getColumnModel().getColumn(6).setPreferredWidth(90);
            table_view_employees.getColumnModel().getColumn(6).setMaxWidth(150);
            table_view_employees.getColumnModel().getColumn(7).setMinWidth(0);
            table_view_employees.getColumnModel().getColumn(7).setPreferredWidth(0);
            table_view_employees.getColumnModel().getColumn(7).setMaxWidth(0);
        }

        panel_employee_input.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_employee_input.setPreferredSize(new java.awt.Dimension(1000, 141));

        btn_international_number.setBackground(new java.awt.Color(0, 0, 0));
        btn_international_number.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_international_number.png"))); // NOI18N
        btn_international_number.setPreferredSize(new java.awt.Dimension(40, 35));
        btn_international_number.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_international_numberActionPerformed(evt);
            }
        });

        lbl_first_name.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_first_name.setText("First Name");

        try {
            txt_contact.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(0##) ###-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_contact.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_contact.setNextFocusableComponent(txt_email);
        txt_contact.setPreferredSize(new java.awt.Dimension(260, 25));

        txt_last_name.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_last_name.setNextFocusableComponent(txt_contact);
        txt_last_name.setPreferredSize(new java.awt.Dimension(300, 25));

        lbl_last_name.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_last_name.setText("Last Name");

        lbl_contact.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_contact.setText("Contact No.");

        hdn_txt_employee_id.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        hdn_txt_employee_id.setEnabled(false);
        hdn_txt_employee_id.setMinimumSize(new java.awt.Dimension(80, 32));
        hdn_txt_employee_id.setPreferredSize(new java.awt.Dimension(0, 0));

        txt_first_name.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_first_name.setNextFocusableComponent(txt_last_name);
        txt_first_name.setPreferredSize(new java.awt.Dimension(300, 25));

        btn_copy.setBackground(new java.awt.Color(0, 0, 0));
        btn_copy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_copy.png"))); // NOI18N
        btn_copy.setToolTipText("Copy to clipboard");
        btn_copy.setPreferredSize(new java.awt.Dimension(40, 35));
        btn_copy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_copyActionPerformed(evt);
            }
        });

        lbl_email.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_email.setText("Email");

        txt_email.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_email.setNextFocusableComponent(txt_username);
        txt_email.setPreferredSize(new java.awt.Dimension(422, 25));

        lbl_first_name_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_first_name_star.setForeground(java.awt.Color.red);
        lbl_first_name_star.setText("*");

        lbl_last_name_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_last_name_star.setForeground(java.awt.Color.red);
        lbl_last_name_star.setText("*");

        lbl_contact_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_contact_star.setForeground(java.awt.Color.red);
        lbl_contact_star.setText("*");

        lbl_username_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_username_star.setForeground(java.awt.Color.red);
        lbl_username_star.setText("*");

        lbl_username.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_username.setText("Username");

        txt_username.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_username.setNextFocusableComponent(txt_password);
        txt_username.setPreferredSize(new java.awt.Dimension(300, 25));

        lbl_password_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_password_star.setForeground(java.awt.Color.red);
        lbl_password_star.setText("*");

        lbl_password.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_password.setText("Password");

        lbl_repeat_pwd_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_repeat_pwd_star.setForeground(java.awt.Color.red);
        lbl_repeat_pwd_star.setText("*");

        lbl_confirm_pwd.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_confirm_pwd.setText("Confirm");

        lbl_access_level_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_access_level_star.setForeground(java.awt.Color.red);
        lbl_access_level_star.setText("*");

        lbl_access_level.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_access_level.setText("Access Level");

        combo_box_access_level.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECT", "ADMIN", "STANDARD" }));
        combo_box_access_level.setNextFocusableComponent(btn_add);

        txt_password.setNextFocusableComponent(txt_confirm_pwd);
        txt_password.setPreferredSize(new java.awt.Dimension(102, 25));

        txt_confirm_pwd.setNextFocusableComponent(combo_box_access_level);
        txt_confirm_pwd.setPreferredSize(new java.awt.Dimension(102, 25));

        hdn_txt_person_id.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        hdn_txt_person_id.setEnabled(false);
        hdn_txt_person_id.setMinimumSize(new java.awt.Dimension(80, 32));
        hdn_txt_person_id.setPreferredSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout panel_employee_inputLayout = new javax.swing.GroupLayout(panel_employee_input);
        panel_employee_input.setLayout(panel_employee_inputLayout);
        panel_employee_inputLayout.setHorizontalGroup(
            panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_employee_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_employee_inputLayout.createSequentialGroup()
                        .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_employee_inputLayout.createSequentialGroup()
                                .addComponent(lbl_last_name_star)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_last_name))
                            .addGroup(panel_employee_inputLayout.createSequentialGroup()
                                .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(hdn_txt_employee_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_first_name_star))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_first_name)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_employee_inputLayout.createSequentialGroup()
                                .addComponent(txt_contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_international_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_copy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addComponent(lbl_repeat_pwd_star)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_confirm_pwd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_confirm_pwd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panel_employee_inputLayout.createSequentialGroup()
                                .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txt_last_name, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_first_name, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel_employee_inputLayout.createSequentialGroup()
                                        .addComponent(lbl_password_star)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbl_password))
                                    .addGroup(panel_employee_inputLayout.createSequentialGroup()
                                        .addComponent(lbl_username_star)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbl_username)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_username, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                                    .addComponent(txt_password, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(panel_employee_inputLayout.createSequentialGroup()
                        .addComponent(lbl_contact_star)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_contact)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panel_employee_inputLayout.createSequentialGroup()
                        .addComponent(lbl_email)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_access_level_star)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_access_level)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(combo_box_access_level, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_employee_inputLayout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(hdn_txt_person_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1000, Short.MAX_VALUE)))
        );
        panel_employee_inputLayout.setVerticalGroup(
            panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_employee_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_employee_inputLayout.createSequentialGroup()
                        .addComponent(hdn_txt_employee_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_first_name)
                            .addComponent(txt_first_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_first_name_star))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_last_name)
                            .addComponent(lbl_last_name_star)
                            .addComponent(txt_last_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panel_employee_inputLayout.createSequentialGroup()
                        .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_username)
                            .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_username_star))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_password)
                            .addComponent(lbl_password_star)
                            .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_contact_star)
                        .addComponent(lbl_contact)
                        .addComponent(txt_contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_international_number, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_copy, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_confirm_pwd)
                        .addComponent(lbl_repeat_pwd_star)
                        .addComponent(txt_confirm_pwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_email)
                        .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_access_level)
                        .addComponent(lbl_access_level_star)
                        .addComponent(combo_box_access_level, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(panel_employee_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_employee_inputLayout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(hdn_txt_person_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(122, Short.MAX_VALUE)))
        );

        panel_employee_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        javax.swing.GroupLayout panel_employee_buttonsLayout = new javax.swing.GroupLayout(panel_employee_buttons);
        panel_employee_buttons.setLayout(panel_employee_buttonsLayout);
        panel_employee_buttonsLayout.setHorizontalGroup(
            panel_employee_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_employee_buttonsLayout.createSequentialGroup()
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
        panel_employee_buttonsLayout.setVerticalGroup(
            panel_employee_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_employee_buttonsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panel_employee_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_clear_fields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout panel_employeesLayout = new javax.swing.GroupLayout(panel_employees);
        panel_employees.setLayout(panel_employeesLayout);
        panel_employeesLayout.setHorizontalGroup(
            panel_employeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_employeesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_employeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_employeesLayout.createSequentialGroup()
                        .addComponent(lbl_search_icon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_search_employee, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE))
                    .addComponent(panel_employee_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_employee_input, javax.swing.GroupLayout.DEFAULT_SIZE, 1020, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        panel_employeesLayout.setVerticalGroup(
            panel_employeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_employeesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_employeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_search_employee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_search_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel_employee_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel_employee_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_employees, javax.swing.GroupLayout.DEFAULT_SIZE, 1036, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_employees, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBounds(0, 0, 1050, 650);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_copyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_copyActionPerformed
        StringSelection stringSelection = new StringSelection(txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));
        if (!this.txt_contact.getText().trim().isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        }
    }//GEN-LAST:event_btn_copyActionPerformed

    private void btn_international_numberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_international_numberActionPerformed
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
    }//GEN-LAST:event_btn_international_numberActionPerformed

    private void btn_clear_fieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clear_fieldsActionPerformed
        clearFields();
        loadEmployeeListTable();
    }//GEN-LAST:event_btn_clear_fieldsActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        int selectedRow = this.table_view_employees.getSelectedRow();

        if (selectedRow >= 0) {
            Employee deleteEmployee = new Employee();
            deleteEmployee.setIdEmployee((Integer) this._dtmEmployee.getValueAt(selectedRow, 0));

            int confirmDeletion = JOptionPane.showConfirmDialog(this, CommonConstant.CONFIRM_DELETE, this.getTitle(), JOptionPane.YES_NO_OPTION);

            if (confirmDeletion == 0) {
                boolean isDeleted = _employeeController.deleteEmployee(deleteEmployee.getIdEmployee());

                if (isDeleted) {
                    clearFields();
                    loadEmployeeListTable();
                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.ERROR_DELETE, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void txt_search_employeeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search_employeeKeyReleased
        searchEmployee();
    }//GEN-LAST:event_txt_search_employeeKeyReleased

    private void table_view_employeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_employeesMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = this.table_view_employees.getSelectedRow();
            long idEmployee = (long) this._dtmEmployee.getValueAt(selectedRow, 0);

            Employee updateEmployee = _employeeController.getItemEmployee(idEmployee);
            setEmployeeFields(updateEmployee);
        }
    }//GEN-LAST:event_table_view_employeesMouseClicked

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        Employee updateEmployee = this.getEmployeeFields();
        if (updateEmployee != null) {
            if (updateEmployee.getIdEmployee() > 0) {
                long confirmEditing = JOptionPane.showConfirmDialog(this, CommonConstant.CONFIRM_UPDATE, this.getTitle(), JOptionPane.YES_NO_OPTION);

                if (confirmEditing == 0) {
                    boolean isUpdated = this._employeeController.updateEmployee(updateEmployee);
                    if (isUpdated) {
                        
                        getItemEmployee(updateEmployee.getIdEmployee());
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, CommonConstant.ERROR_UPDATE, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        Employee addEmployee = this.getEmployeeFields();

        if (addEmployee != null) {
            if (addEmployee.getIdEmployee() == 0) {

                Employee checkEmployee = _employeeController.searchEmployeeByContactNo(this.txt_contact.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));

                if (checkEmployee != null) {
                    JOptionPane.showMessageDialog(this, CommonConstant.WARN_EXIST_PERSON, this.getTitle(), JOptionPane.WARNING_MESSAGE);

                    getItemEmployee(checkEmployee.getIdEmployee());
                } else {

                    long idCustomerAdded = this._employeeController.addEmployee(addEmployee);
                    if (idCustomerAdded > 0) {

                        getItemEmployee(idCustomerAdded);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, CommonConstant.ERROR_SAVE, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }//GEN-LAST:event_btn_addActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_clear_fields;
    private javax.swing.JButton btn_copy;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_international_number;
    private javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> combo_box_access_level;
    private javax.swing.JTextField hdn_txt_employee_id;
    private javax.swing.JTextField hdn_txt_person_id;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_access_level;
    private javax.swing.JLabel lbl_access_level_star;
    private javax.swing.JLabel lbl_confirm_pwd;
    private javax.swing.JLabel lbl_contact;
    private javax.swing.JLabel lbl_contact_star;
    private javax.swing.JLabel lbl_email;
    private javax.swing.JLabel lbl_first_name;
    private javax.swing.JLabel lbl_first_name_star;
    private javax.swing.JLabel lbl_last_name;
    private javax.swing.JLabel lbl_last_name_star;
    private javax.swing.JLabel lbl_password;
    private javax.swing.JLabel lbl_password_star;
    private javax.swing.JLabel lbl_repeat_pwd_star;
    private javax.swing.JLabel lbl_search_icon;
    private javax.swing.JLabel lbl_username;
    private javax.swing.JLabel lbl_username_star;
    private javax.swing.JPanel panel_employee_buttons;
    private javax.swing.JPanel panel_employee_input;
    private javax.swing.JPanel panel_employees;
    private javax.swing.JTable table_view_employees;
    private javax.swing.JPasswordField txt_confirm_pwd;
    private javax.swing.JFormattedTextField txt_contact;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_first_name;
    private javax.swing.JTextField txt_last_name;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_search_employee;
    private javax.swing.JTextField txt_username;
    // End of variables declaration//GEN-END:variables
}
