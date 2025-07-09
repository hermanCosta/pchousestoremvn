package com.pchouse.pchousestoremvn.views.modals;

import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.ProductServiceController;
import com.pchouse.pchousestoremvn.models.ProductService;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ProductServiceModal extends javax.swing.JDialog {

    private final DefaultTableModel _dtmProdServ;
    private List<ProductService> _listProdServ;
    private final ProductServiceController _productServiceController;
    private List<ProductService> _listOrderProdServ;

    public ProductServiceModal(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this._dtmProdServ = (DefaultTableModel) this.table_view_products_list.getModel();
        this._productServiceController = new ProductServiceController();

        loadProdServListTable();
    }

    private void loadProdServListTable() {
        this._listProdServ = _productServiceController.getAllProducts(CommonSetting.COMPANY);

        _dtmProdServ.setRowCount(0);
        CommonSetting.fitContentJtable(this.table_view_products_list);

        if (_listProdServ != null) {
            for (ProductService prodServ : _listProdServ) {
                _dtmProdServ.addRow(
                        new Object[]{
                            prodServ.getIdProductService(),
                            prodServ.getProdServName(),
                            CommonExtension.formatToPriceField(prodServ.getPrice())
                        }
                );
            }
        }
    }

    private void searchProductService() {
        if (!this.txt_search_prodServ.getText().trim().isEmpty()) {
            _listProdServ = _productServiceController.searchProductService(this.txt_search_prodServ.getText());
            if (_listProdServ != null) {
                _dtmProdServ.setRowCount(0);

                _listProdServ.forEach((product) -> {
                    _dtmProdServ.addRow(
                            new Object[]{
                                product.getIdProductService(),
                                product.getProdServName(),
                                product.getPrice()
                            }
                    );
                });
            } else {
                loadProdServListTable();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txt_search_prodServ = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_view_products_list = new javax.swing.JTable();
        lbl_search_icon = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btn_clear = new javax.swing.JButton();
        btn_add = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Product | Service");
        setModal(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txt_search_prodServ.setMinimumSize(new java.awt.Dimension(80, 32));
        txt_search_prodServ.setPreferredSize(new java.awt.Dimension(350, 25));
        txt_search_prodServ.setRequestFocusEnabled(false);
        txt_search_prodServ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_search_prodServKeyReleased(evt);
            }
        });

        table_view_products_list.setAutoCreateRowSorter(true);
        table_view_products_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product | Service", "Price €"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_products_list.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        table_view_products_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_products_listMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_view_products_list);
        if (table_view_products_list.getColumnModel().getColumnCount() > 0) {
            table_view_products_list.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_products_list.getColumnModel().getColumn(0).setPreferredWidth(0);
            table_view_products_list.getColumnModel().getColumn(0).setMaxWidth(0);
            table_view_products_list.getColumnModel().getColumn(2).setPreferredWidth(80);
            table_view_products_list.getColumnModel().getColumn(2).setMaxWidth(150);
        }

        lbl_search_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search_black.png"))); // NOI18N
        lbl_search_icon.setPreferredSize(new java.awt.Dimension(32, 25));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_clear.setBackground(new java.awt.Color(21, 76, 121));
        btn_clear.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_clear.setForeground(new java.awt.Color(255, 255, 255));
        btn_clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_add_new.png"))); // NOI18N
        btn_clear.setText("Clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        btn_add.setBackground(new java.awt.Color(21, 76, 121));
        btn_add.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_add.setForeground(new java.awt.Color(255, 255, 255));
        btn_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_add_new.png"))); // NOI18N
        btn_add.setText("Add");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });

        btn_cancel.setBackground(new java.awt.Color(21, 76, 121));
        btn_cancel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_cancel.setForeground(new java.awt.Color(255, 255, 255));
        btn_cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_add_new.png"))); // NOI18N
        btn_cancel.setText("Cancel");
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_add)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_clear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_cancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_clear)
                    .addComponent(btn_add)
                    .addComponent(btn_cancel))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_search_icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_search_prodServ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_search_icon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_search_prodServ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_search_prodServKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search_prodServKeyReleased
        searchProductService();
    }//GEN-LAST:event_txt_search_prodServKeyReleased

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        this.txt_search_prodServ.setText("");
        loadProdServListTable();
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_cancelActionPerformed

    private void table_view_products_listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_products_listMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = table_view_products_list.getSelectedRow();

            ProductService productService = new ProductService();

            productService.setIdProductService((Integer) _dtmProdServ.getValueAt(selectedRow, 0));
            productService.setProdServName(_dtmProdServ.getValueAt(selectedRow, 1).toString());
            productService.setPrice((Double) _dtmProdServ.getValueAt(selectedRow, 2));
            
            this._listOrderProdServ.add(productService);
        }
    }//GEN-LAST:event_table_view_products_listMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_clear;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_search_icon;
    private javax.swing.JTable table_view_products_list;
    private javax.swing.JTextField txt_search_prodServ;
    // End of variables declaration//GEN-END:variables
}
