package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.DepositController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderFaultController;
import com.pchouse.pchousestoremvn.controllers.OrderNoteController;
import com.pchouse.pchousestoremvn.controllers.ServiceOrderProdServController;
import com.pchouse.pchousestoremvn.enums.OrderStatus;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderFault;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import java.beans.PropertyVetoException;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class OrderListView extends javax.swing.JInternalFrame {

    private List<ServiceOrder> _listOrder;
    private final ServiceOrderController _orderController;
    private final OrderNoteController _orderNoteController;
    private final ServiceOrderFaultController _orderFaultController;
    private final ServiceOrderProdServController _orderProdServController;
    private final DepositController _orderDeposit;
    private final DefaultTableModel _dtmOrder;

    public OrderListView() {
        initComponents();

        CommonSetting.requestTxtFocus(this.txt_search_order);
        CommonSetting.tableSettings(this.table_view_order_list);

        this._orderController = new ServiceOrderController();
        this._orderNoteController = new OrderNoteController();
        this._orderFaultController = new ServiceOrderFaultController();
        this._orderProdServController = new ServiceOrderProdServController();
        this._orderDeposit = new DepositController();
        this._dtmOrder = (DefaultTableModel) this.table_view_order_list.getModel();

        loadOrderListTable();
    }

    private void loadOrderListTable() {
        this._listOrder = _orderController.getAllOrders(CommonSetting.COMPANY);

        _dtmOrder.setRowCount(0);
        //CommonSetting.fitContentJtable(this.table_view_order_list);

        if (_listOrder != null) {
            for (ServiceOrder order : _listOrder) {
                _dtmOrder.addRow(
                        new Object[]{
                            order.getIdServiceOrder(),
                            order.getCustomer().getPerson().getFirstName() + " " + order.getCustomer().getPerson().getLastName(),
                            order.getCustomer().getPerson().getContactNo(),
                            order.getDevice().getBrand(),
                            order.getDevice().getModel(),
                            order.getDevice().getSerialNumber(),
                            order.getStatus()
                        }
                );
            }
        }
    }

    private void searchOrder() {
        if (!this.txt_search_order.getText().trim().isEmpty()) {
            this._listOrder = _orderController.searchOrder(CommonSetting.COMPANY, this.txt_search_order.getText().toUpperCase());

            if (_listOrder != null) {
                _dtmOrder.setRowCount(0);

                for (ServiceOrder order : _listOrder) {
                    _dtmOrder.addRow(
                            new Object[]{
                                order.getIdServiceOrder(),
                                order.getCustomer().getPerson().getFirstName() + " " + order.getCustomer().getPerson().getLastName(),
                                order.getCustomer().getPerson().getContactNo(),
                                order.getDevice().getBrand(),
                                order.getDevice().getModel(),
                                order.getDevice().getSerialNumber(),
                                order.getStatus()
                            }
                    );
                }
            }
        } else {
            loadOrderListTable();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_order_list = new javax.swing.JPanel();
        txt_search_order = new javax.swing.JTextField();
        lbl_search_icon = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_view_order_list = new javax.swing.JTable();

        setBorder(null);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setToolTipText(CommonConstant.TBL_OPEN_ITEM_TOOL_TIP);
        setMaximumSize(new java.awt.Dimension(1049, 700));
        setPreferredSize(new java.awt.Dimension(1050, 650));

        panel_order_list.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txt_search_order.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        txt_search_order.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_search_order.setPreferredSize(new java.awt.Dimension(500, 30));
        txt_search_order.setRequestFocusEnabled(false);
        txt_search_order.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_search_orderKeyReleased(evt);
            }
        });

        lbl_search_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search_black.png"))); // NOI18N

        table_view_order_list.setAutoCreateRowSorter(true);
        table_view_order_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order No", "Customer", "Contact", "Brand", "Model", "S/N", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_order_list.setToolTipText(CommonConstant.TBL_OPEN_ITEM_TOOL_TIP);
        table_view_order_list.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        table_view_order_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_order_listMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_view_order_list);
        if (table_view_order_list.getColumnModel().getColumnCount() > 0) {
            table_view_order_list.getColumnModel().getColumn(0).setPreferredWidth(80);
            table_view_order_list.getColumnModel().getColumn(0).setMaxWidth(100);
            table_view_order_list.getColumnModel().getColumn(2).setPreferredWidth(150);
            table_view_order_list.getColumnModel().getColumn(2).setMaxWidth(200);
            table_view_order_list.getColumnModel().getColumn(3).setPreferredWidth(100);
            table_view_order_list.getColumnModel().getColumn(3).setMaxWidth(150);
            table_view_order_list.getColumnModel().getColumn(4).setPreferredWidth(150);
            table_view_order_list.getColumnModel().getColumn(4).setMaxWidth(250);
            table_view_order_list.getColumnModel().getColumn(5).setPreferredWidth(150);
            table_view_order_list.getColumnModel().getColumn(5).setMaxWidth(200);
            table_view_order_list.getColumnModel().getColumn(6).setPreferredWidth(100);
            table_view_order_list.getColumnModel().getColumn(6).setMaxWidth(150);
        }

        jTabbedPane1.addTab("tab1", jScrollPane1);

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
                        .addComponent(txt_search_order, javax.swing.GroupLayout.DEFAULT_SIZE, 1016, Short.MAX_VALUE))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        panel_order_listLayout.setVerticalGroup(
            panel_order_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_order_listLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_order_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_search_order, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(lbl_search_icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_order_list, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_order_list, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBounds(0, 0, 1050, 650);
    }// </editor-fold>//GEN-END:initComponents

    private void table_view_order_listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_order_listMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = table_view_order_list.getSelectedRow();
            if (selectedRow < 0) {
                return;
            }

            Long orderId = (Long) _dtmOrder.getValueAt(selectedRow, 0);

            ServiceOrder orderModel = _orderController.getItemOrder(orderId);
            List<ServiceOrderFault> listOrderFault = _orderFaultController.getOrderFaults(orderModel);
            List<ServiceOrderProdServ> listOrderProdServ = _orderProdServController.getOrderProdServ(orderModel);
            List<Deposit> listOrderDeposit = _orderDeposit.getOrderDeposit(orderModel);

            JDesktopPane desktop = CommonSetting.MAIN_MENU_DESKTOP_PANE;

            for (JInternalFrame frame : desktop.getAllFrames()) {
                try {
                    frame.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (orderModel.getStatus() == OrderStatus.IN_PROGRESS) {
                CreatedOrderView createdOrderView = new CreatedOrderView(orderModel, listOrderFault, listOrderProdServ, listOrderDeposit);
                createdOrderView.setTitle("Order: " + orderId);
                createdOrderView.setClosable(true);
                createdOrderView.setIconifiable(true);
                createdOrderView.setMaximizable(true);
                createdOrderView.setResizable(true);

                desktop.add(createdOrderView);
                createdOrderView.setVisible(true);

                SwingUtilities.invokeLater(() -> {
                    try {
                        createdOrderView.setSelected(true);
                        createdOrderView.setMaximum(true);
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                });

            }
        }
    }//GEN-LAST:event_table_view_order_listMouseClicked

    private void txt_search_orderKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search_orderKeyReleased
        searchOrder();
    }//GEN-LAST:event_txt_search_orderKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbl_search_icon;
    private javax.swing.JPanel panel_order_list;
    private javax.swing.JTable table_view_order_list;
    private javax.swing.JTextField txt_search_order;
    // End of variables declaration//GEN-END:variables
}
