package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.common.CommonStrings;
import com.pchouse.pchousestoremvn.models.Company;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.Timer;

public class MainMenuView extends javax.swing.JFrame {

    Company _company;

    public MainMenuView(Company company) {
        initComponents();
        this._company = company;
        CommonSetting.MAIN_MENU_DESKTOP_PANE = this.desktop_pane_menu;
        CommonSetting.ID_COMPANY = this._company.getIdCompany();
        CommonSetting.COMPANY = company;

        //makeFrameFullSize(this);
        loadHeaderDetails();
    }

    private static void makeFrameFullSize(JFrame aFrame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        aFrame.setSize(screenSize.width, screenSize.height);
    }

    MainMenuView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadHeaderDetails() {
        Timer updateTimer;
        int DELAY = 100;
        updateTimer = new Timer(DELAY, (ActionEvent e) -> {
            this.lbl_time_stamp.setText(CommonStrings.formatDateToString(new Date()));
        });
        updateTimer.start();

        this.lbl_shop_name.setText(this._company.getName().toUpperCase());

        //  label.setText("<html><p style=\"width:100px\">"+paragraph+"</p></html>");
        this.lbl_shop_address.setText("<html><p style=\"width:100px\">" + this._company.getAddress() + "</p></html>");
        this.lbl_shop_tel.setText(this._company.getContactOne());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktop_pane_menu = new javax.swing.JDesktopPane();
        panel_menu_side = new javax.swing.JPanel();
        lbl_logo = new javax.swing.JLabel();
        lbl_time_stamp = new javax.swing.JLabel();
        lbl_shop_name = new javax.swing.JLabel();
        lbl_shop_address = new javax.swing.JLabel();
        lbl_shop_tel = new javax.swing.JLabel();
        menu_bar = new javax.swing.JMenuBar();
        menu_create = new javax.swing.JMenu();
        menu_item_new_order = new javax.swing.JMenuItem();
        menu_item_new_sale = new javax.swing.JMenuItem();
        menu_reports = new javax.swing.JMenu();
        menu_admin = new javax.swing.JMenu();
        menu_item_customer = new javax.swing.JMenuItem();
        menu_item_fault = new javax.swing.JMenuItem();
        nenu_report = new javax.swing.JMenu();
        menu_item_close_till = new javax.swing.JMenuItem();
        menu_manage = new javax.swing.JMenu();
        menu_item_users = new javax.swing.JMenuItem();
        menu_consult = new javax.swing.JMenu();
        menu_item_orders = new javax.swing.JMenuItem();
        menu_item_products = new javax.swing.JMenuItem();
        menu_item_sales = new javax.swing.JMenuItem();
        menu_item_refurbs = new javax.swing.JMenuItem();
        menu_refurbs = new javax.swing.JMenu();
        menu_item_computer = new javax.swing.JMenuItem();
        menu_item_television = new javax.swing.JMenuItem();
        menu_item_monitor = new javax.swing.JMenuItem();
        menu_item_console = new javax.swing.JMenuItem();
        menu_item_custom = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Main Menu");
        setName("Main Menu"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1280, 720));
        setSize(new java.awt.Dimension(1280, 720));

        desktop_pane_menu.setMaximumSize(new java.awt.Dimension(2147483647, 0));
        desktop_pane_menu.setPreferredSize(new java.awt.Dimension(1050, 649));

        javax.swing.GroupLayout desktop_pane_menuLayout = new javax.swing.GroupLayout(desktop_pane_menu);
        desktop_pane_menu.setLayout(desktop_pane_menuLayout);
        desktop_pane_menuLayout.setHorizontalGroup(
            desktop_pane_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1050, Short.MAX_VALUE)
        );
        desktop_pane_menuLayout.setVerticalGroup(
            desktop_pane_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        panel_menu_side.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_menu_side.setPreferredSize(new java.awt.Dimension(0, 0));

        lbl_logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/logo_header.png"))); // NOI18N
        lbl_logo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lbl_logo.setPreferredSize(new java.awt.Dimension(0, 0));

        lbl_time_stamp.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        lbl_time_stamp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_time_stamp.setText("timeStamp");
        lbl_time_stamp.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_shop_name.setFont(new java.awt.Font("sansserif", 2, 16)); // NOI18N
        lbl_shop_name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_shop_name.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icon_address.png"))); // NOI18N
        lbl_shop_name.setText("shopName");
        lbl_shop_name.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_shop_address.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icon_address.png"))); // NOI18N
        lbl_shop_address.setText("shopAddress");
        lbl_shop_address.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_shop_tel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icon_phone_number.png"))); // NOI18N
        lbl_shop_tel.setText("shopTel");
        lbl_shop_tel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panel_menu_sideLayout = new javax.swing.GroupLayout(panel_menu_side);
        panel_menu_side.setLayout(panel_menu_sideLayout);
        panel_menu_sideLayout.setHorizontalGroup(
            panel_menu_sideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_menu_sideLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_menu_sideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_shop_address, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_shop_tel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_menu_sideLayout.createSequentialGroup()
                        .addComponent(lbl_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lbl_time_stamp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_menu_sideLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbl_shop_name, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel_menu_sideLayout.setVerticalGroup(
            panel_menu_sideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_menu_sideLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_shop_name, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 319, Short.MAX_VALUE)
                .addComponent(lbl_shop_address, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_shop_tel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(lbl_time_stamp, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menu_bar.setPreferredSize(new java.awt.Dimension(268, 25));

        menu_create.setText("Create");
        menu_create.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N

        menu_item_new_order.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        menu_item_new_order.setText("New Order");
        menu_item_new_order.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_new_orderActionPerformed(evt);
            }
        });
        menu_create.add(menu_item_new_order);

        menu_item_new_sale.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        menu_item_new_sale.setText("New Sale");
        menu_item_new_sale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_new_saleActionPerformed(evt);
            }
        });
        menu_create.add(menu_item_new_sale);

        menu_bar.add(menu_create);
        menu_bar.add(menu_reports);

        menu_admin.setText("Register");
        menu_admin.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N

        menu_item_customer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        menu_item_customer.setText("Customer");
        menu_item_customer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_customerActionPerformed(evt);
            }
        });
        menu_admin.add(menu_item_customer);

        menu_item_fault.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        menu_item_fault.setText("Fault");
        menu_item_fault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_faultActionPerformed(evt);
            }
        });
        menu_admin.add(menu_item_fault);

        menu_bar.add(menu_admin);

        nenu_report.setText("Report");
        nenu_report.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N

        menu_item_close_till.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F10, 0));
        menu_item_close_till.setText("Close Till");
        nenu_report.add(menu_item_close_till);

        menu_bar.add(nenu_report);

        menu_manage.setText("Manage");
        menu_manage.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N

        menu_item_users.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        menu_item_users.setText("Users");
        menu_item_users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_usersActionPerformed(evt);
            }
        });
        menu_manage.add(menu_item_users);

        menu_bar.add(menu_manage);

        menu_consult.setText("Consult");

        menu_item_orders.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        menu_item_orders.setText("Orders");
        menu_item_orders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_ordersActionPerformed(evt);
            }
        });
        menu_consult.add(menu_item_orders);

        menu_item_products.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        menu_item_products.setText("Products");
        menu_item_products.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_productsActionPerformed(evt);
            }
        });
        menu_consult.add(menu_item_products);

        menu_item_sales.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        menu_item_sales.setText("Sales");
        menu_consult.add(menu_item_sales);

        menu_item_refurbs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        menu_item_refurbs.setText("All Refurbs");
        menu_item_refurbs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_refurbsActionPerformed(evt);
            }
        });
        menu_consult.add(menu_item_refurbs);

        menu_bar.add(menu_consult);

        menu_refurbs.setText("Refurbs");

        menu_item_computer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_DOWN_MASK));
        menu_item_computer.setText("Computers");
        menu_item_computer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_computerActionPerformed(evt);
            }
        });
        menu_refurbs.add(menu_item_computer);

        menu_item_television.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_DOWN_MASK));
        menu_item_television.setText("Televisions");
        menu_item_television.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_televisionActionPerformed(evt);
            }
        });
        menu_refurbs.add(menu_item_television);

        menu_item_monitor.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_DOWN_MASK));
        menu_item_monitor.setText("Monitors");
        menu_item_monitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_monitorActionPerformed(evt);
            }
        });
        menu_refurbs.add(menu_item_monitor);

        menu_item_console.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_DOWN_MASK));
        menu_item_console.setText("Consoles");
        menu_item_console.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_consoleActionPerformed(evt);
            }
        });
        menu_refurbs.add(menu_item_console);

        menu_item_custom.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_DOWN_MASK));
        menu_item_custom.setText("Custom");
        menu_item_custom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_customActionPerformed(evt);
            }
        });
        menu_refurbs.add(menu_item_custom);

        menu_bar.add(menu_refurbs);

        setJMenuBar(menu_bar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(desktop_pane_menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_menu_side, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_menu_side, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                    .addComponent(desktop_pane_menu, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        setSize(new java.awt.Dimension(1280, 722));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menu_item_new_orderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_new_orderActionPerformed
        //Customer customer = null;
        NewOrderView newOrderView = new NewOrderView();
        //NewOrderViewTeste newOrderView = new NewOrderViewTeste();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(newOrderView).setVisible(true);
        CommonSetting.setMaxInternalFrame(newOrderView);
    }//GEN-LAST:event_menu_item_new_orderActionPerformed

    private void menu_item_new_saleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_new_saleActionPerformed
        NewSaleView newSaleView = new NewSaleView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(newSaleView).setVisible(true);
        CommonSetting.setMaxInternalFrame(newSaleView);
    }//GEN-LAST:event_menu_item_new_saleActionPerformed

    private void menu_item_refurbsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_refurbsActionPerformed
        RefurbView refurbListView = new RefurbView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(refurbListView).setVisible(true);
        CommonSetting.setMaxInternalFrame(refurbListView);
    }//GEN-LAST:event_menu_item_refurbsActionPerformed

    private void menu_item_customerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_customerActionPerformed
        CustomerView customerView = new CustomerView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(customerView).setVisible(true);
        CommonSetting.setMaxInternalFrame(customerView);
    }//GEN-LAST:event_menu_item_customerActionPerformed

    private void menu_item_usersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_usersActionPerformed
        EmployeeView employeeView = new EmployeeView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(employeeView).setVisible(true);
        CommonSetting.setMaxInternalFrame(employeeView);
    }//GEN-LAST:event_menu_item_usersActionPerformed

    private void menu_item_productsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_productsActionPerformed
        ProductServiceView productServiceView = new ProductServiceView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(productServiceView).setVisible(true);
        CommonSetting.setMaxInternalFrame(productServiceView);
    }//GEN-LAST:event_menu_item_productsActionPerformed

    private void menu_item_faultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_faultActionPerformed
        FaultView faultsView = new FaultView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(faultsView).setVisible(true);
        CommonSetting.setMaxInternalFrame(faultsView);
    }//GEN-LAST:event_menu_item_faultActionPerformed

    private void menu_item_computerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_computerActionPerformed
        RefurbComputerView computerView = new RefurbComputerView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(computerView).setVisible(true);
        CommonSetting.setMaxInternalFrame(computerView);
    }//GEN-LAST:event_menu_item_computerActionPerformed

    private void menu_item_televisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_televisionActionPerformed
        RefurbTelevisionView televisionView = new RefurbTelevisionView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(televisionView).setVisible(true);
        CommonSetting.setMaxInternalFrame(televisionView);
    }//GEN-LAST:event_menu_item_televisionActionPerformed

    private void menu_item_monitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_monitorActionPerformed
        RefurbMonitorView monitorView = new RefurbMonitorView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(monitorView).setVisible(true);
        CommonSetting.setMaxInternalFrame(monitorView);
    }//GEN-LAST:event_menu_item_monitorActionPerformed

    private void menu_item_consoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_consoleActionPerformed
        RefurbConsoleView consoleView = new RefurbConsoleView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(consoleView).setVisible(true);
        CommonSetting.setMaxInternalFrame(consoleView);
    }//GEN-LAST:event_menu_item_consoleActionPerformed

    private void menu_item_customActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_customActionPerformed
        RefurbCustomView customView = new RefurbCustomView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(customView).setVisible(true);
        CommonSetting.setMaxInternalFrame(customView);
    }//GEN-LAST:event_menu_item_customActionPerformed

    private void menu_item_ordersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_ordersActionPerformed
        OrderListView orderView = new OrderListView();
        this.desktop_pane_menu.removeAll();
        this.desktop_pane_menu.add(orderView).setVisible(true);
        CommonSetting.setMaxInternalFrame(orderView);
    }//GEN-LAST:event_menu_item_ordersActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktop_pane_menu;
    private javax.swing.JLabel lbl_logo;
    private javax.swing.JLabel lbl_shop_address;
    private javax.swing.JLabel lbl_shop_name;
    private javax.swing.JLabel lbl_shop_tel;
    private javax.swing.JLabel lbl_time_stamp;
    private javax.swing.JMenu menu_admin;
    private javax.swing.JMenuBar menu_bar;
    private javax.swing.JMenu menu_consult;
    private javax.swing.JMenu menu_create;
    private javax.swing.JMenuItem menu_item_close_till;
    private javax.swing.JMenuItem menu_item_computer;
    private javax.swing.JMenuItem menu_item_console;
    private javax.swing.JMenuItem menu_item_custom;
    private javax.swing.JMenuItem menu_item_customer;
    private javax.swing.JMenuItem menu_item_fault;
    private javax.swing.JMenuItem menu_item_monitor;
    private javax.swing.JMenuItem menu_item_new_order;
    private javax.swing.JMenuItem menu_item_new_sale;
    private javax.swing.JMenuItem menu_item_orders;
    private javax.swing.JMenuItem menu_item_products;
    private javax.swing.JMenuItem menu_item_refurbs;
    private javax.swing.JMenuItem menu_item_sales;
    private javax.swing.JMenuItem menu_item_television;
    private javax.swing.JMenuItem menu_item_users;
    private javax.swing.JMenu menu_manage;
    private javax.swing.JMenu menu_refurbs;
    private javax.swing.JMenu menu_reports;
    private javax.swing.JMenu nenu_report;
    private javax.swing.JPanel panel_menu_side;
    // End of variables declaration//GEN-END:variables
}
