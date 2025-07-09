/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderController;
import com.pchouse.pchousestoremvn.controllers.SaleController;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class OrderSaleListView extends javax.swing.JInternalFrame {

    private final ServiceOrderController _orderController;
    private final SaleController _saleController;
    private final DefaultTableModel _dtmOrder;
    private final DefaultTableModel _dtmSale;
    private List<ServiceOrder> _listOrder;
    private List<Sale> _listSale;

    public OrderSaleListView() {
        initComponents();

        CommonSetting.requestTxtFocus(this.txt_search_order);
        CommonSetting.tableSettings(this.table_view_order_list);
        CommonSetting.tableSettings(this.table_view_sale_list);

        this._orderController = new ServiceOrderController();
        this._saleController = new SaleController();
        this._dtmOrder = (DefaultTableModel) this.table_view_order_list.getModel();
        this._dtmSale = (DefaultTableModel) this.table_view_sale_list.getModel();

        loadOrderListTable();
        loadSaleListTable();
    }

    private void loadOrderListTable() {
        try {
            this._listOrder = _orderController.getAllOrders(CommonSetting.COMPANY);
            _dtmOrder.setRowCount(0);
            if (_listOrder != null) {
                for (ServiceOrder order : _listOrder) {
                    _dtmOrder.addRow(new Object[]{
                        order.getIdServiceOrder(),
                        order.getCustomer().getPerson().getFirstName() + " " + order.getCustomer().getPerson().getLastName(),
                        order.getCustomer().getPerson().getContactNo(),
                        order.getDevice().getBrand(),
                        order.getDevice().getModel(),
                        order.getDevice().getSerialNumber(),
                        order.getStatus()
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading service order list: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadSaleListTable() {
        try {
            this._listSale = _saleController.getAllSales(CommonSetting.COMPANY);
            _dtmSale.setRowCount(0);
            if (_listSale != null) {
                for (Sale sale : _listSale) {
                    _dtmSale.addRow(new Object[]{
                        sale.getIdSale(),
                        sale.getCustomer().getPerson().getFirstName() + " " + sale.getCustomer().getPerson().getLastName(),
                        sale.getCustomer().getPerson().getContactNo(),
                        sale.getTotal(),
                        sale.getStatus()
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading sale list: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchOrder() {
        try {
            String searchTerm = this.txt_search_order.getText().trim().toUpperCase();
            int selectedTab = jTabbedPane1.getSelectedIndex();

            if (searchTerm.isEmpty() || searchTerm.length() < 3) {
                loadOrderListTable();
                loadSaleListTable();
                return;
            }

            if (selectedTab == 0) {
                this._listOrder = _orderController.searchOrder(CommonSetting.COMPANY, searchTerm);
                _dtmOrder.setRowCount(0);
                if (_listOrder != null) {
                    for (ServiceOrder order : _listOrder) {
                        _dtmOrder.addRow(new Object[]{
                            order.getIdServiceOrder(),
                            order.getCustomer().getPerson().getFirstName() + " " + order.getCustomer().getPerson().getLastName(),
                            order.getCustomer().getPerson().getContactNo(),
                            order.getDevice().getBrand(),
                            order.getDevice().getModel(),
                            order.getDevice().getSerialNumber(),
                            order.getStatus()
                        });
                    }
                }
            } else if (selectedTab == 1) {
                this._listSale = _saleController.searchSale(CommonSetting.COMPANY, searchTerm);
                _dtmSale.setRowCount(0);
                if (_listSale != null) {
                    for (Sale sale : _listSale) {
                        _dtmSale.addRow(new Object[]{
                            sale.getIdSale(),
                            sale.getCustomer().getPerson().getFirstName() + " " + sale.getCustomer().getPerson().getLastName(),
                            sale.getCustomer().getPerson().getContactNo(),
                            sale.getTotal(),
                            sale.getStatus()
                        });
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error searching orders or sales: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panel_order_list = new javax.swing.JPanel();
        txt_search_order = new javax.swing.JTextField();
        lbl_search_icon = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_view_order_list = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_view_sale_list = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Order & Sale List");
        setPreferredSize(new java.awt.Dimension(1050, 650));

        panel_order_list.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txt_search_order.setPreferredSize(new java.awt.Dimension(500, 30));
        txt_search_order.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchOrder();
            }
        });

        lbl_search_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search_black.png")));

        table_view_order_list.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Order No", "Customer", "Contact", "Brand", "Model", "S/N", "Status"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(table_view_order_list);
        jTabbedPane1.addTab("Service Orders", jScrollPane1);

        table_view_sale_list.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Sale No", "Customer", "Contact", "Total", "Status"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane2.setViewportView(table_view_sale_list);
        jTabbedPane1.addTab("Sale Orders", jScrollPane2);

        javax.swing.GroupLayout panel_order_listLayout = new javax.swing.GroupLayout(panel_order_list);
        panel_order_list.setLayout(panel_order_listLayout);
        panel_order_listLayout.setHorizontalGroup(
                panel_order_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panel_order_listLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel_order_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panel_order_listLayout.createSequentialGroup()
                                                .addComponent(lbl_search_icon)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txt_search_order, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE))
                                        .addComponent(jTabbedPane1))
                                .addContainerGap())
        );
        panel_order_listLayout.setVerticalGroup(
                panel_order_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panel_order_listLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel_order_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txt_search_order, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbl_search_icon))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTabbedPane1)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel_order_list, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel_order_list, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbl_search_icon;
    private javax.swing.JPanel panel_order_list;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable table_view_order_list;
    private javax.swing.JTable table_view_sale_list;
    private javax.swing.JTextField txt_search_order;
}
