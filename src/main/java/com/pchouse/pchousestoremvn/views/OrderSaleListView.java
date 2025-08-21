/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.DepositController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderController;
import com.pchouse.pchousestoremvn.controllers.SaleController;
import com.pchouse.pchousestoremvn.controllers.SalePaymentController;
import com.pchouse.pchousestoremvn.controllers.SaleProdServController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderFaultController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderPaymentController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderProdServController;
import com.pchouse.pchousestoremvn.enums.OrderStatus;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.SaleProdServ;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderFault;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import java.beans.PropertyVetoException;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class OrderSaleListView extends javax.swing.JInternalFrame {

    private List<ServiceOrder> _listOrder;
    private List<Sale> _listSale;
    private final DepositController _orderDepositController;
    private final ServiceOrderController _serviceOrderController;
    private final ServiceOrderFaultController _serviceOrderFaultController;
    private final ServiceOrderProdServController _serviceOrderProdServController;
    private final SaleController _saleController;
    private final SaleProdServController _saleProdServController;
    private final SalePaymentController _salePaymentController;
    private final ServiceOrderPaymentController _serviceOrderPaymentController;
    private final DefaultTableModel _dtmOrder;
    private final DefaultTableModel _dtmSale;

    public OrderSaleListView() {
        initComponents();

        CommonSetting.requestTxtFocus(this.txt_search_order);
        CommonSetting.tableSettings(this.table_view_order_list);
        CommonSetting.tableSettings(this.table_view_sale_list);

        this._serviceOrderController = new ServiceOrderController();
        this._saleController = new SaleController();
        this._saleProdServController = new SaleProdServController();
        this._salePaymentController = new SalePaymentController();
        this._serviceOrderPaymentController = new ServiceOrderPaymentController();
        this._serviceOrderFaultController = new ServiceOrderFaultController();
        this._serviceOrderProdServController = new ServiceOrderProdServController();
        this._orderDepositController = new DepositController();

        this._dtmOrder = (DefaultTableModel) this.table_view_order_list.getModel();
        this._dtmSale = (DefaultTableModel) this.table_view_sale_list.getModel();
        loadOrderListTable();
        loadSaleListTable();
    }

    private void loadOrderListTable() {
        try {
            this._listOrder = _serviceOrderController.getAllOrders(CommonSetting.COMPANY);
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
                this._listOrder = _serviceOrderController.searchOrder(CommonSetting.COMPANY, searchTerm);
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

    private void table_view_order_listMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            int selectedRow = table_view_order_list.getSelectedRow();
            if (selectedRow < 0) {
                return;
            }

            Long orderId = (Long) table_view_order_list.getValueAt(selectedRow, 0);

            try {
                ServiceOrder orderModel = _serviceOrderController.getServiceOrderById(orderId);
                List<ServiceOrderFault> listOrderFault = _serviceOrderFaultController.getOrderFaults(orderModel);
                List<ServiceOrderProdServ> listOrderProdServ = _serviceOrderProdServController.getOrderProdServ(orderModel);
                List<Deposit> listOrderDeposit = _orderDepositController.getOrderDeposit(orderModel);
                List<ServiceOrderPayment> listServiceOrderPayment = _serviceOrderPaymentController.getServiceOrderPayments(orderModel);

                if (orderModel.getStatus() == OrderStatus.IN_PROGRESS) {
                    CreatedOrderView createdOrderView = new CreatedOrderView(orderModel, listOrderFault, listOrderProdServ, listOrderDeposit);
                    openInternalFrame(createdOrderView, "Order In Progress: " + orderId);
                } else if (orderModel.getStatus() == OrderStatus.FIXED) {
                    FixedOrderView fixedOrderView = new FixedOrderView(orderModel, listOrderFault, listOrderProdServ, listOrderDeposit);
                    openInternalFrame(fixedOrderView, "Order Fixed" + orderId);
                } else if (orderModel.getStatus() == OrderStatus.NOT_FIXED) {
                    NotFixedOrderView notFixedOrderView = new NotFixedOrderView(orderModel, listOrderFault, listOrderProdServ, listOrderDeposit);
                    openInternalFrame(notFixedOrderView, "Order Not Fixed" + orderId);
                } else if (orderModel.getStatus() == OrderStatus.PICKED) {
                    PickedOrderView pickedOrderView = new PickedOrderView(orderModel, listOrderFault, listOrderProdServ, listOrderDeposit, listServiceOrderPayment);
                    openInternalFrame(pickedOrderView, "Picked Order: " + orderId);
                } else if (orderModel.getStatus() == OrderStatus.REFUNDED) {
                    RefundOrderView refundOrderView = new RefundOrderView(orderModel, listOrderFault, listOrderProdServ, listOrderDeposit);
                    openInternalFrame(refundOrderView, "Order Refunded" + orderId);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "An error occurred while opening the order details:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void table_view_sale_listMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            int selectedRow = table_view_sale_list.getSelectedRow();
            if (selectedRow < 0) {
                return;
            }

            Long saleId = (Long) table_view_sale_list.getValueAt(selectedRow, 0);

            try {
                Sale saleModel = _saleController.getItemSale(saleId);
                List<SaleProdServ> listSaleProdServ = _saleProdServController.getSaleProdServ(saleModel);
                List<Deposit> listSaleDeposit = _orderDepositController.getSaleDeposit(saleModel);
                List<SalePayment> salePayments = _salePaymentController.getSalePayments(saleModel);

                if (saleModel.getStatus() == OrderStatus.PICKED) {
                    CreatedSaleView createdSaleView = new CreatedSaleView(saleModel, listSaleProdServ, listSaleDeposit, salePayments);
                    openInternalFrame(createdSaleView, "Sale: " + saleId);
                } else if (saleModel.getStatus() == OrderStatus.REFUNDED) {
                    RefundSaleView refundSaleView = new RefundSaleView(saleModel, listSaleProdServ, listSaleDeposit, salePayments);
                    openInternalFrame(refundSaleView, "Refunded Sale" + saleId);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "An error occurred while opening the sale details:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openInternalFrame(JInternalFrame frame, String title) {
        frame.setTitle(title);
        frame.setClosable(true);
        frame.setIconifiable(true);
        frame.setMaximizable(true);
        frame.setResizable(true);

        JDesktopPane desktop = CommonSetting.MAIN_MENU_DESKTOP_PANE;

        for (JInternalFrame openFrame : desktop.getAllFrames()) {
            try {
                openFrame.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        desktop.add(frame);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            try {
                frame.setSelected(true);
                frame.setMaximum(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
        });
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

        table_view_order_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_order_listMouseClicked(evt);
            }
        });

        table_view_sale_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_sale_listMouseClicked(evt);
            }
        });

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
