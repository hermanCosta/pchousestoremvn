package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.models.Company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuViewTest extends JFrame {

    private JDesktopPane desktopPane;
    private JPanel sidePanel;
    private JLabel lblShopName;
    private JLabel lblTimeStamp;
    private static JFrame jFrameWindow;
    Company _company;

    public MenuViewTest(Company company) {
        setTitle("PC House Store - Main Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        CommonSetting.COMPANY = company;
        _company = company;

        initComponents();
        CommonSetting.MAIN_MENU_DESKTOP_PANE = this.desktopPane;
    }

    private void initComponents() {
        desktopPane = new JDesktopPane();
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(220, 0));
        sidePanel.setBackground(Color.LIGHT_GRAY);
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        // Topo: Shop Name com ícone
        JLabel lblShopNameTitle = new JLabel("Shop Name");
        lblShopNameTitle.setForeground(Color.WHITE);
        lblShopNameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblShopNameTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidePanel.add(lblShopNameTitle);

        lblShopName = new JLabel(_company.getName());
        lblShopName.setForeground(Color.WHITE);
        lblShopName.setFont(new Font("Arial", Font.BOLD, 16));
        lblShopName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblShopName.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));
        sidePanel.add(lblShopName);

        // Painel para o logo
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.LIGHT_GRAY);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setLayout(new BorderLayout());

        JLabel lblLogo = new JLabel();
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_logo_header_md.png")));
        logoPanel.add(lblLogo, BorderLayout.CENTER);
        sidePanel.add(logoPanel);

        // Informações da loja
        JLabel lblShopAddress = new JLabel();
        lblShopAddress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_address.png")));
        lblShopAddress.setText(_company.getAddress());
        lblShopAddress.setForeground(Color.WHITE);
        lblShopAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblShopAddress.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidePanel.add(lblShopAddress);

        JLabel lblShopTel = new JLabel();
        lblShopTel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_phone_number.png")));
        lblShopTel.setText(_company.getContactOne());
        lblShopTel.setForeground(Color.WHITE);
        lblShopTel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblShopTel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidePanel.add(lblShopTel);

        // Rodapé: Timestamp
        lblTimeStamp = new JLabel(getFormattedTimestamp());
        lblTimeStamp.setForeground(Color.WHITE);
        lblTimeStamp.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTimeStamp.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTimeStamp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidePanel.add(lblTimeStamp);

        // Adicionar o sidePanel ao desktopPane ou onde for necessário
        desktopPane.add(sidePanel);

        JMenuBar menuBar = new JMenuBar();

        // --- CREATE ---
        JMenu menuCreate = new JMenu("Create");
        JMenuItem menuItemNewOrder = new JMenuItem("New Order");
        menuItemNewOrder.setAccelerator(KeyStroke.getKeyStroke("F1"));
        menuItemNewOrder.addActionListener(this::menuItemNewOrderActionPerformed);
        menuCreate.add(menuItemNewOrder);

        JMenuItem menuItemNewSale = new JMenuItem("New Sale");
        menuItemNewSale.setAccelerator(KeyStroke.getKeyStroke("F2"));
        menuItemNewSale.addActionListener(this::menuItemNewSaleActionPerformed);
        menuCreate.add(menuItemNewSale);

        JMenuItem menuItemNewRefurbSale = new JMenuItem("New Refurb Sale");
        menuItemNewRefurbSale.setAccelerator(KeyStroke.getKeyStroke("F3"));
        menuItemNewRefurbSale.addActionListener(this::menuItemNewRefurbSaleActionPerformed);
        menuCreate.add(menuItemNewRefurbSale);

        // --- MANAGE ---
        JMenu menuManage = new JMenu("Manage");
        JMenuItem menuItemCustomer = new JMenuItem("Customers");
        menuItemCustomer.setAccelerator(KeyStroke.getKeyStroke("F4"));
        menuItemCustomer.addActionListener(this::menuItemCustomerActionPerformed);
        menuManage.add(menuItemCustomer);

        JMenuItem menuItemUsers = new JMenuItem("Users");
        menuItemUsers.setAccelerator(KeyStroke.getKeyStroke("F5"));
        menuItemUsers.addActionListener(this::menuItemUsersActionPerformed);
        menuManage.add(menuItemUsers);

        JMenuItem menuItemProducts = new JMenuItem("Products");
        menuItemProducts.setAccelerator(KeyStroke.getKeyStroke("F6"));
        menuItemProducts.addActionListener(this::menuItemProductsActionPerformed);
        menuManage.add(menuItemProducts);

        JMenuItem menuItemFault = new JMenuItem("Faults");
        menuItemFault.setAccelerator(KeyStroke.getKeyStroke("F7"));
        menuItemFault.addActionListener(this::menuItemFaultActionPerformed);
        menuManage.add(menuItemFault);

        // --- REFURBS ---
        // --- REFURBS ---
        JMenu menuRefurbs = new JMenu("Refurbs");
        menuRefurbs.setMnemonic(KeyEvent.VK_R); // Alt+R opens Refurbs menu

        JMenuItem menuItemRefurbs = new JMenuItem("All Refurbs");
        menuItemRefurbs.setMnemonic(KeyEvent.VK_A);
        menuItemRefurbs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK)); // Alt+R
        menuItemRefurbs.addActionListener(this::menuItemRefurbsActionPerformed);
        menuRefurbs.add(menuItemRefurbs);

        JMenuItem menuItemComputer = new JMenuItem("Computer");
        menuItemComputer.setMnemonic(KeyEvent.VK_C);
        menuItemComputer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK)); // Alt+C
        menuItemComputer.addActionListener(this::menuItemComputerActionPerformed);
        menuRefurbs.add(menuItemComputer);

        JMenuItem menuItemMonitor = new JMenuItem("Monitor");
        menuItemMonitor.setMnemonic(KeyEvent.VK_M);
        menuItemMonitor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK)); // Alt+M
        menuItemMonitor.addActionListener(this::menuItemMonitorActionPerformed);
        menuRefurbs.add(menuItemMonitor);

        JMenuItem menuItemTelevision = new JMenuItem("Television");
        menuItemTelevision.setMnemonic(KeyEvent.VK_T);
        menuItemTelevision.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK)); // Alt+T
        menuItemTelevision.addActionListener(this::menuItemTelevisionActionPerformed);
        menuRefurbs.add(menuItemTelevision);

        JMenuItem menuItemConsole = new JMenuItem("Console");
        menuItemConsole.setMnemonic(KeyEvent.VK_O); // Use O to avoid conflict with C
        menuItemConsole.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_DOWN_MASK)); // Alt+O
        menuItemConsole.addActionListener(this::menuItemConsoleActionPerformed);
        menuRefurbs.add(menuItemConsole);

        JMenuItem menuItemCustom = new JMenuItem("Custom");
        menuItemCustom.setMnemonic(KeyEvent.VK_U); // Use U for "cUstom"
        menuItemCustom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.ALT_DOWN_MASK)); // Alt+U
        menuItemCustom.addActionListener(this::menuItemCustomActionPerformed);
        menuRefurbs.add(menuItemCustom);

        // --- CONSULT ---
        JMenu menuConsult = new JMenu("Consult");
        JMenuItem menuItemOrders = new JMenuItem("Orders");
        menuItemOrders.setAccelerator(KeyStroke.getKeyStroke("F8"));
        menuItemOrders.addActionListener(this::menuItemOrdersActionPerformed);
        menuConsult.add(menuItemOrders);

        JMenuItem menuItemSales = new JMenuItem("Sales");
        menuItemSales.setAccelerator(KeyStroke.getKeyStroke("F9"));
        menuItemSales.addActionListener(this::menuItemSalesActionPerformed);
        menuConsult.add(menuItemSales);

        // --- REPORT ---
        JMenu menuReport = new JMenu("Report");
        JMenuItem menuItemCloseTill = new JMenuItem("Close Till");
        menuItemCloseTill.setAccelerator(KeyStroke.getKeyStroke("F10"));
        menuItemCloseTill.addActionListener(this::menuItemCloseTillActionPerformed);
        menuReport.add(menuItemCloseTill);

        // --- CASH ---
        JMenu menuCash = new JMenu("Cash");
        JMenuItem menuItemCashIn = new JMenuItem("Cash In");
        menuItemCashIn.setAccelerator(KeyStroke.getKeyStroke("F11"));
        menuItemCashIn.addActionListener(this::menuItemCashInActionPerformed);
        menuCash.add(menuItemCashIn);

        JMenuItem menuItemCashOut = new JMenuItem("Cash Out");
        menuItemCashOut.setAccelerator(KeyStroke.getKeyStroke("F12"));
        menuItemCashOut.addActionListener(this::menuItemCashOutActionPerformed);
        menuCash.add(menuItemCashOut);

        // Add menus to menuBar
        menuBar.add(menuCreate);
        menuBar.add(menuManage);
        menuBar.add(menuRefurbs);
        menuBar.add(menuConsult);
        menuBar.add(menuReport);
        menuBar.add(menuCash);

        setJMenuBar(menuBar);

        if (isDevMode()) {
            JButton btnReloadUI = new JButton("Reload All");
            btnReloadUI.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnReloadUI.setFocusable(false);
            btnReloadUI.setBackground(new Color(153,153,153));
            btnReloadUI.setForeground(Color.WHITE);
            btnReloadUI.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnReloadUI.setMaximumSize(new Dimension(150, 30));
            btnReloadUI.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            btnReloadUI.addActionListener(e -> {
                this.dispose();
                reloadMenuView();
            });

            sidePanel.add(Box.createVerticalStrut(30));
            sidePanel.add(btnReloadUI);
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(desktopPane, BorderLayout.CENTER);
        getContentPane().add(sidePanel, BorderLayout.EAST);
    }

    private String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return formatter.format(LocalDateTime.now());
    }

    private void openInternalFrame(JInternalFrame frame) {
        desktopPane.removeAll();
        desktopPane.add(frame);
        frame.setVisible(true);
        frame.setSize(desktopPane.getSize());
        frame.setLocation(0, 0);
        try {
            frame.setMaximum(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void menuItemNewOrderActionPerformed(ActionEvent evt) {
        openInternalFrame(new NewServiceOrderView());
    }

    private void menuItemNewSaleActionPerformed(ActionEvent evt) {
        openInternalFrame(new NewSaleView());
    }

    private void menuItemNewRefurbSaleActionPerformed(ActionEvent e) {
        openInternalFrame(new NewRefurbSaleView());
    }

    private void menuItemCustomerActionPerformed(ActionEvent evt) {
        openInternalFrame(new CustomerView());
    }

    private void menuItemUsersActionPerformed(ActionEvent evt) {
        openInternalFrame(new EmployeeView());
    }

    private void menuItemProductsActionPerformed(ActionEvent evt) {
        openInternalFrame(new ProductServiceView());
    }

    private void menuItemRefurbsActionPerformed(ActionEvent evt) {
        openInternalFrame(new RefurbView());
    }

    private void menuItemComputerActionPerformed(ActionEvent evt) {
        openInternalFrame(new RefurbComputerView());
    }

    private void menuItemMonitorActionPerformed(ActionEvent evt) {
        openInternalFrame(new RefurbMonitorView());
    }

    private void menuItemTelevisionActionPerformed(ActionEvent evt) {
        openInternalFrame(new RefurbTelevisionView());
    }

    private void menuItemConsoleActionPerformed(ActionEvent evt) {
        openInternalFrame(new RefurbConsoleView());
    }

    private void menuItemCustomActionPerformed(ActionEvent evt) {
        openInternalFrame(new RefurbCustomView());
    }

    private void menuItemFaultActionPerformed(ActionEvent evt) {
        openInternalFrame(new FaultView());
    }

    private void menuItemOrdersActionPerformed(ActionEvent evt) {
        openInternalFrame(new OrderSaleListView(OrderSaleListView.TAB_ORDER));
    }

    private void menuItemSalesActionPerformed(ActionEvent evt) {
        openInternalFrame(new OrderSaleListView(OrderSaleListView.TAB_SALE));
    }

    private void menuItemCloseTillActionPerformed(ActionEvent evt) {
        openInternalFrame(new ClosingTillView());
    }

    private void menuItemCashInActionPerformed(ActionEvent evt) {
        openInternalFrame(new CashInRegistryView());
    }

    private void menuItemCashOutActionPerformed(ActionEvent evt) {
        openInternalFrame(new CashOutRegistryView());
    }

    private boolean isDevMode() {
        return Boolean.getBoolean("devMode");
    }

    public static void reloadMenuView() {
        if (jFrameWindow != null) {
            jFrameWindow.dispose();  // Fecha a janela anterior se existir
        }

        jFrameWindow = new MenuViewTest(CommonSetting.COMPANY);
        jFrameWindow.setVisible(true);
    }
}
