/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.models.Company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuViewTest extends JFrame {

    private JDesktopPane desktopPane;
    private JPanel sidePanel;
    private JLabel lblShopName;
    private JLabel lblShopAddress;
    private JLabel lblShopTel;
    private JLabel lblTimeStamp;
    Company _company;

    public MenuViewTest(Company company) {
        setTitle("pcHouseStore - Main Menu");
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

        // Ícone do logo
        JLabel lblLogo = new JLabel();
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_logo_header_md.png"))); // Ícone do logo
        logoPanel.add(lblLogo, BorderLayout.CENTER);
        sidePanel.add(logoPanel);

        // Informações da loja com ícones
        JLabel lblShopAddress = new JLabel();
        lblShopAddress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_address.png"))); // Ícone de endereço
        lblShopAddress.setText(_company.getAddress());
        lblShopAddress.setForeground(Color.WHITE);
        lblShopAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblShopAddress.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidePanel.add(lblShopAddress);

        JLabel lblShopTel = new JLabel();
        lblShopTel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_phone_number.png"))); // Ícone de contato
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

        JMenu menuCreate = new JMenu("Create");
        JMenuItem menuItemNewOrder = new JMenuItem("New Order");
        menuItemNewOrder.addActionListener(this::menuItemNewOrderActionPerformed);
        menuCreate.add(menuItemNewOrder);

        JMenuItem menuItemNewSale = new JMenuItem("New Sale");
        menuItemNewSale.addActionListener(this::menuItemNewSaleActionPerformed);
        menuCreate.add(menuItemNewSale);

        JMenu menuManage = new JMenu("Manage");
        JMenuItem menuItemCustomer = new JMenuItem("Customers");
        menuItemCustomer.addActionListener(this::menuItemCustomerActionPerformed);
        menuManage.add(menuItemCustomer);

        JMenuItem menuItemUsers = new JMenuItem("Users");
        menuItemUsers.addActionListener(this::menuItemUsersActionPerformed);
        menuManage.add(menuItemUsers);

        JMenuItem menuItemProducts = new JMenuItem("Products");
        menuItemProducts.addActionListener(this::menuItemProductsActionPerformed);
        menuManage.add(menuItemProducts);

        JMenuItem menuItemFault = new JMenuItem("Faults");
        menuItemFault.addActionListener(this::menuItemFaultActionPerformed);
        menuManage.add(menuItemFault);

        JMenu menuRefurbs = new JMenu("Refurbs");
        JMenuItem menuItemRefurbs = new JMenuItem("All Refurbs");
        menuItemRefurbs.addActionListener(this::menuItemRefurbsActionPerformed);
        menuRefurbs.add(menuItemRefurbs);

        JMenuItem menuItemComputer = new JMenuItem("Computer");
        menuItemComputer.addActionListener(this::menuItemComputerActionPerformed);
        menuRefurbs.add(menuItemComputer);

        JMenuItem menuItemMonitor = new JMenuItem("Monitor");
        menuItemMonitor.addActionListener(this::menuItemMonitorActionPerformed);
        menuRefurbs.add(menuItemMonitor);

        JMenuItem menuItemTelevision = new JMenuItem("Television");
        menuItemTelevision.addActionListener(this::menuItemTelevisionActionPerformed);
        menuRefurbs.add(menuItemTelevision);

        JMenuItem menuItemConsole = new JMenuItem("Console");
        menuItemConsole.addActionListener(this::menuItemConsoleActionPerformed);
        menuRefurbs.add(menuItemConsole);

        JMenuItem menuItemCustom = new JMenuItem("Custom");
        menuItemCustom.addActionListener(this::menuItemCustomActionPerformed);
        menuRefurbs.add(menuItemCustom);

        JMenu menuConsult = new JMenu("Consult");
        JMenuItem menuItemOrders = new JMenuItem("Orders");
        menuItemOrders.addActionListener(this::menuItemOrdersActionPerformed);
        menuConsult.add(menuItemOrders);

        menuBar.add(menuCreate);
        menuBar.add(menuManage);
        menuBar.add(menuRefurbs);
        menuBar.add(menuConsult);

        setJMenuBar(menuBar);

        if (isDevMode()) {
            JButton btnReloadUI = new JButton("⟳ Recarregar UI");
            btnReloadUI.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnReloadUI.setFocusable(false);
            btnReloadUI.setBackground(new Color(200, 80, 80));
            btnReloadUI.setForeground(Color.WHITE);
            btnReloadUI.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnReloadUI.setMaximumSize(new Dimension(180, 40));
            btnReloadUI.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            btnReloadUI.addActionListener(e -> {
                this.dispose();
                com.pchouse.pchousestoremvn.MainApp.carregarTelaInicial(); // Recarrega toda a aplicação
            });

            sidePanel.add(Box.createVerticalStrut(30)); // espaço antes do botão
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
        openInternalFrame(new NewOrderView());
    }

    private void menuItemNewSaleActionPerformed(ActionEvent evt) {
        openInternalFrame(new NewSaleView());
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
        //openInternalFrame(new OrderListView());
        openInternalFrame(new OrderSaleListView());
    }

    private boolean isDevMode() {
        return Boolean.getBoolean("devMode");
    }
}
