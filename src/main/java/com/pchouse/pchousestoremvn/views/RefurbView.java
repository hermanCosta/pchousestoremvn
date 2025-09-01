package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.controllers.RefurbController;
import com.pchouse.pchousestoremvn.models.Refurb;
import com.pchouse.pchousestoremvn.reports.PrintRefurbLabel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RefurbView extends javax.swing.JInternalFrame {

    private long refurbId;
    private final DefaultTableModel _dtmRefurb;
    private final RefurbController _refurbController;
    private List<Refurb> _listRefurb;

    public RefurbView() {
        initComponents();

        CommonSetting.requestTxtFocus(this.txt_search_refurb);
        CommonSetting.tableSettings(this.table_view_refurbs);

        this._dtmRefurb = (DefaultTableModel) this.table_view_refurbs.getModel();
        this._listRefurb = new ArrayList<>();
        this._refurbController = new RefurbController();

        this.panel_refurb_extra_info.setVisible(false);
        this.panel_refurb_custom_info.setVisible(false);
        loadRefurbProdListTable();
    }

    private void loadRefurbProdListTable() {
        this._listRefurb = this._refurbController.getAllRefurbProducts(CommonSetting.COMPANY);

        this._dtmRefurb.setRowCount(0);

        if (this._listRefurb != null) {
            _listRefurb.forEach((refurbItem) -> {
                this._dtmRefurb.addRow(
                        new Object[]{
                            refurbItem.getIdRefurb(),
                            refurbItem.getCategory(),
                            refurbItem.getBrand(),
                            refurbItem.getModel(),
                            refurbItem.getQty(),
                            refurbItem.getPrice(),
                            refurbItem.getNote()
                        }
                );
            });
        }
    }

    private Refurb getRefurbFields() {
        Refurb getRefurb = null;
        if (this.combo_box_refurb_category.getSelectedItem().toString().trim().isEmpty()
                || this.combo_box_refurb_category.getSelectedIndex() == 0
                || this.combo_box_refurb_category.getSelectedItem().toString().toUpperCase().equals("CUSTOM")
                || this.txt_brand.getText().trim().isEmpty()
                || this.txt_model.getText().trim().isEmpty()
                || this.txt_price.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, this.getTitle(), JOptionPane.WARNING_MESSAGE);

            return getRefurb;
        } else {
            getRefurb = new Refurb(
                    this.combo_box_refurb_category.getSelectedItem().toString().toUpperCase(),
                    this.txt_brand.getText().toUpperCase(),
                    this.txt_model.getText().toUpperCase(),
                    Double.parseDouble(this.txt_price.getText()),
                    (this.txt_qty.getText().trim().isEmpty() ? 1 : Integer.parseInt(this.txt_qty.getText())),
                    this.txt_serial_number.getText().toUpperCase(),
                    this.editor_pane_refurb_notes.getText().toUpperCase(), //limit for at most 300 charac.
                    this.txt_screen.getText().toUpperCase(),
                    this.txt_processor.getText().toUpperCase(),
                    this.txt_ram.getText().toUpperCase(),
                    this.txt_storage.getText().toUpperCase(),
                    this.txt_gpu.getText().toUpperCase(),
                    this.txt_battery_health.getText().toUpperCase(),
                    CommonExtension.joinCustomRefurbFields(this.lbl_custom_1, this.txt_custom_1),
                    CommonExtension.joinCustomRefurbFields(this.lbl_custom_2, this.txt_custom_2),
                    CommonExtension.joinCustomRefurbFields(this.lbl_custom_3, this.txt_custom_3),
                    CommonExtension.joinCustomRefurbFields(this.lbl_custom_4, this.txt_custom_4),
                    CommonExtension.joinCustomRefurbFields(this.lbl_custom_5, this.txt_custom_5),
                    CommonExtension.joinCustomRefurbFields(this.lbl_custom_6, this.txt_custom_6),
                    CommonSetting.COMPANY);
            
            getRefurb.setIdRefurb(refurbId);

            return getRefurb;
        }
    }

    private void setRefurbFields(Refurb refurbProd) {
        this.refurbId = refurbProd.getIdRefurb();
        this.combo_box_refurb_category.setSelectedItem(refurbProd.getCategory());
        this.txt_brand.setText(refurbProd.getBrand());
        this.txt_model.setText(refurbProd.getModel());
        this.txt_price.setText(String.valueOf(refurbProd.getPrice()));
        this.txt_qty.setText(String.valueOf(refurbProd.getQty()));
        this.txt_serial_number.setText(refurbProd.getSerialNumber());
        this.editor_pane_refurb_notes.setText(refurbProd.getNote());
        this.txt_screen.setText(refurbProd.getScreen());
        this.txt_processor.setText(refurbProd.getProcessor());
        this.txt_ram.setText(refurbProd.getRamMemory());
        this.txt_storage.setText(refurbProd.getStorage());
        this.txt_gpu.setText(refurbProd.getGpuBoard());
        this.txt_battery_health.setText(refurbProd.getBatteryHealth());

        if (checkCustomFields(refurbProd.getCustom1())) {
            this.lbl_custom_1.setText(CommonExtension.splitString(refurbProd.getCustom1(), "##")[0]);
            this.txt_custom_1.setText(CommonExtension.splitString(refurbProd.getCustom1(), "##")[1]);
        }
        if (checkCustomFields(refurbProd.getCustom2())) {
            this.lbl_custom_2.setText(CommonExtension.splitString(refurbProd.getCustom2(), "##")[0]);
            this.txt_custom_2.setText(CommonExtension.splitString(refurbProd.getCustom2(), "##")[1]);
        }
        if (checkCustomFields(refurbProd.getCustom3())) {
            this.lbl_custom_3.setText(CommonExtension.splitString(refurbProd.getCustom3(), "##")[0]);
            this.txt_custom_3.setText(CommonExtension.splitString(refurbProd.getCustom3(), "##")[1]);
        }
        if (checkCustomFields(refurbProd.getCustom4())) {
            this.lbl_custom_4.setText(CommonExtension.splitString(refurbProd.getCustom4(), "##")[0]);
            this.txt_custom_4.setText(CommonExtension.splitString(refurbProd.getCustom4(), "##")[1]);
        }
        if (checkCustomFields(refurbProd.getCustom5())) {
            this.lbl_custom_5.setText(CommonExtension.splitString(refurbProd.getCustom5(), "##")[0]);
            this.txt_custom_5.setText(CommonExtension.splitString(refurbProd.getCustom5(), "##")[1]);
        }
        if (checkCustomFields(refurbProd.getCustom6())) {
            this.lbl_custom_6.setText(CommonExtension.splitString(refurbProd.getCustom6(), "##")[0]);
            this.txt_custom_6.setText(CommonExtension.splitString(refurbProd.getCustom6(), "##")[1]);
        }

        showHideCustomPanel(refurbProd.getCategory());
    }

    private void searchProductService(String pSearch) {
        if (!pSearch.trim().isEmpty()) {
            _listRefurb = _refurbController.searchRefurbProducts(pSearch);

            this._dtmRefurb.setRowCount(0);
            if (_listRefurb != null) {

                _listRefurb.forEach((refurb) -> {
                    this._dtmRefurb.addRow(
                            new Object[]{
                                refurb.getIdRefurb(),
                                refurb.getCategory(),
                                refurb.getBrand(),
                                refurb.getModel(),
                                refurb.getQty(),
                                refurb.getPrice(),
                                refurb.getNote()
                            }
                    );
                });
            }
        } else {
            loadRefurbProdListTable();
        }
    }

    private void getItemRefurbProd(long idRefurbProd) {
        if (idRefurbProd != 0) {
            Refurb refurbProd = _refurbController.getRefurbProductById(idRefurbProd);

            this._dtmRefurb.setRowCount(0);
            this._dtmRefurb.addRow(
                    new Object[]{
                        refurbProd.getIdRefurb(),
                        refurbProd.getCategory(),
                        refurbProd.getBrand(),
                        refurbProd.getModel(),
                        refurbProd.getQty(),
                        refurbProd.getPrice(),
                        refurbProd.getNote()
                    }
            );
        } else {
            loadRefurbProdListTable();
        }
    }

    private boolean checkCustomFields(String pField) {
        boolean hasRecord = false;

        if (!pField.trim().isEmpty()) {
            if (pField.contains("##")) {
                hasRecord = true;
            }
        }
        return hasRecord;
    }

    private void showHideCustomPanel(String pCategory) {
        switch (pCategory) {
            case "SELECT":
                this.combo_box_refurb_category.setEditable(false);
                this.panel_refurb_extra_info.setVisible(false);
                this.panel_refurb_custom_info.setVisible(false);
                break;
            case "COMPUTER":
                cleanCustomInfoPanelFields();
                this.combo_box_refurb_category.setEditable(false);
                this.panel_refurb_extra_info.setVisible(true);
                this.panel_refurb_custom_info.setVisible(false);
                break;
            case "CONSOLE":
                cleanExtraInfoPanelFields();
                cleanCustomInfoPanelFields();
                this.combo_box_refurb_category.setEditable(false);
                this.panel_refurb_extra_info.setVisible(false);
                this.panel_refurb_custom_info.setVisible(false);
                break;
            case "MONITOR":
                cleanCustomInfoPanelFields();
                this.combo_box_refurb_category.setEditable(false);
                this.panel_refurb_extra_info.setVisible(true);
                this.panel_refurb_custom_info.setVisible(false);
                break;
            case "TV":
                cleanCustomInfoPanelFields();
                this.combo_box_refurb_category.setEditable(false);
                this.panel_refurb_extra_info.setVisible(true);
                this.panel_refurb_custom_info.setVisible(false);
                break;
            default:
                cleanExtraInfoPanelFields();
                this.combo_box_refurb_category.setEditable(true);
                this.panel_refurb_extra_info.setVisible(false);
                this.panel_refurb_custom_info.setVisible(true);
                break;
        }
    }

    private void cleanBasicInfoPanelFields() {
        this.refurbId = 0;
        this.txt_brand.setText("");
        this.txt_model.setText("");
        this.txt_price.setText("");
        this.txt_qty.setText("");
        this.txt_serial_number.setText("");
        this.editor_pane_refurb_notes.setText("");

        this.combo_box_refurb_category.setSelectedIndex(0);
    }

    private void cleanExtraInfoPanelFields() {
        this.txt_screen.setText("");
        this.txt_processor.setText("");
        this.txt_ram.setText("");
        this.txt_storage.setText("");
        this.txt_gpu.setText("");
        this.txt_battery_health.setText("");
    }

    private void cleanCustomInfoPanelFields() {
        this.txt_custom_1.setText("");
        this.txt_custom_2.setText("");
        this.txt_custom_3.setText("");
        this.txt_custom_4.setText("");
        this.txt_custom_5.setText("");
        this.txt_custom_6.setText("");

        this.txt_brand.requestFocus();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_refurbs = new javax.swing.JPanel();
        lbl_search_icon = new javax.swing.JLabel();
        txt_search_refurb = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_view_refurbs = new javax.swing.JTable();
        panel_refurb_basic_info = new javax.swing.JPanel();
        lbl_categ_star = new javax.swing.JLabel();
        lbl_category = new javax.swing.JLabel();
        combo_box_refurb_category = new javax.swing.JComboBox<>();
        lbl_brand_star = new javax.swing.JLabel();
        lbl_brand = new javax.swing.JLabel();
        txt_brand = new javax.swing.JTextField();
        lbl_model_star = new javax.swing.JLabel();
        lbl_model = new javax.swing.JLabel();
        txt_model = new javax.swing.JTextField();
        lbl_price_star = new javax.swing.JLabel();
        lbl_price = new javax.swing.JLabel();
        txt_price = new javax.swing.JTextField();
        txt_qty = new javax.swing.JTextField();
        lbl_serial_number = new javax.swing.JLabel();
        txt_serial_number = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        editor_pane_refurb_notes = new javax.swing.JEditorPane();
        lbl_refurb_notes = new javax.swing.JLabel();
        lbl_qty = new javax.swing.JLabel();
        panel_refurb_extra_info = new javax.swing.JPanel();
        lbl_processor = new javax.swing.JLabel();
        txt_processor = new javax.swing.JTextField();
        lbl_ram = new javax.swing.JLabel();
        txt_ram = new javax.swing.JTextField();
        lbl_storage = new javax.swing.JLabel();
        txt_storage = new javax.swing.JTextField();
        lbl_gpu = new javax.swing.JLabel();
        txt_gpu = new javax.swing.JTextField();
        lbl_screen = new javax.swing.JLabel();
        txt_screen = new javax.swing.JTextField();
        lbl_battery_health = new javax.swing.JLabel();
        txt_battery_health = new javax.swing.JTextField();
        panel_refurb_custom_info = new javax.swing.JPanel();
        lbl_custom_1 = new javax.swing.JTextField();
        txt_custom_1 = new javax.swing.JTextField();
        lbl_custom_2 = new javax.swing.JTextField();
        txt_custom_2 = new javax.swing.JTextField();
        lbl_custom_3 = new javax.swing.JTextField();
        txt_custom_3 = new javax.swing.JTextField();
        lbl_custom_4 = new javax.swing.JTextField();
        txt_custom_4 = new javax.swing.JTextField();
        lbl_custom_5 = new javax.swing.JTextField();
        txt_custom_5 = new javax.swing.JTextField();
        lbl_custom_6 = new javax.swing.JTextField();
        txt_custom_6 = new javax.swing.JTextField();
        panel_refurb_buttons = new javax.swing.JPanel();
        btn_add = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_clear_fields = new javax.swing.JButton();
        btn_gen_label = new javax.swing.JButton();
        lbl_mandatory = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Refurb List");
        setMaximumSize(new java.awt.Dimension(0, 0));
        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(1050, 650));

        panel_refurbs.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_refurbs.setPreferredSize(new java.awt.Dimension(1050, 650));

        lbl_search_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_search_black.png"))); // NOI18N

        txt_search_refurb.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        txt_search_refurb.setNextFocusableComponent(txt_brand);
        txt_search_refurb.setPreferredSize(new java.awt.Dimension(500, 25));
        txt_search_refurb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_search_refurbKeyReleased(evt);
            }
        });

        table_view_refurbs.setAutoCreateRowSorter(true);
        table_view_refurbs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IDRefurb", "Category", "Brand", "Model", "Qty", "Price", "Notes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_refurbs.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        table_view_refurbs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_refurbsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_view_refurbs);
        if (table_view_refurbs.getColumnModel().getColumnCount() > 0) {
            table_view_refurbs.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_refurbs.getColumnModel().getColumn(0).setPreferredWidth(0);
            table_view_refurbs.getColumnModel().getColumn(0).setMaxWidth(0);
            table_view_refurbs.getColumnModel().getColumn(1).setPreferredWidth(150);
            table_view_refurbs.getColumnModel().getColumn(1).setMaxWidth(200);
            table_view_refurbs.getColumnModel().getColumn(2).setPreferredWidth(150);
            table_view_refurbs.getColumnModel().getColumn(2).setMaxWidth(200);
            table_view_refurbs.getColumnModel().getColumn(3).setPreferredWidth(250);
            table_view_refurbs.getColumnModel().getColumn(3).setMaxWidth(300);
            table_view_refurbs.getColumnModel().getColumn(4).setPreferredWidth(50);
            table_view_refurbs.getColumnModel().getColumn(4).setMaxWidth(150);
            table_view_refurbs.getColumnModel().getColumn(5).setPreferredWidth(100);
            table_view_refurbs.getColumnModel().getColumn(5).setMaxWidth(150);
        }

        panel_refurb_basic_info.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_refurb_basic_info.setPreferredSize(new java.awt.Dimension(330, 210));
        panel_refurb_basic_info.setVerifyInputWhenFocusTarget(false);

        lbl_categ_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_categ_star.setForeground(java.awt.Color.red);
        lbl_categ_star.setText("*");

        lbl_category.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_category.setText("Category");

        combo_box_refurb_category.setFont(new java.awt.Font("Lucida Grande", 0, 13)); // NOI18N
        combo_box_refurb_category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECT", "COMPUTER", "CONSOLE", "MONITOR", "TV", "CUSTOM" }));
        combo_box_refurb_category.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_box_refurb_categoryItemStateChanged(evt);
            }
        });

        lbl_brand_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_brand_star.setForeground(java.awt.Color.red);
        lbl_brand_star.setText("*");

        lbl_brand.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_brand.setText("Brand");

        txt_brand.setMinimumSize(new java.awt.Dimension(12, 20));
        txt_brand.setNextFocusableComponent(txt_model);
        txt_brand.setPreferredSize(new java.awt.Dimension(0, 25));

        lbl_model_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_model_star.setForeground(java.awt.Color.red);
        lbl_model_star.setText("*");

        lbl_model.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_model.setText("Model");

        txt_model.setMinimumSize(new java.awt.Dimension(12, 20));
        txt_model.setPreferredSize(new java.awt.Dimension(0, 25));

        lbl_price_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_price_star.setForeground(java.awt.Color.red);
        lbl_price_star.setText("*");

        lbl_price.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_price.setText("Price €");

        txt_price.setMinimumSize(new java.awt.Dimension(12, 20));
        txt_price.setPreferredSize(new java.awt.Dimension(0, 25));
        txt_price.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_priceKeyPressed(evt);
            }
        });

        txt_qty.setText("1");
        txt_qty.setMinimumSize(new java.awt.Dimension(12, 20));
        txt_qty.setPreferredSize(new java.awt.Dimension(0, 25));
        txt_qty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_qtyKeyPressed(evt);
            }
        });

        lbl_serial_number.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_serial_number.setText("S/N");

        txt_serial_number.setMinimumSize(new java.awt.Dimension(12, 20));
        txt_serial_number.setPreferredSize(new java.awt.Dimension(0, 25));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        editor_pane_refurb_notes.setPreferredSize(new java.awt.Dimension(267, 39));
        jScrollPane1.setViewportView(editor_pane_refurb_notes);

        lbl_refurb_notes.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_refurb_notes.setText("Notes");

        lbl_qty.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_qty.setText("Qty");

        javax.swing.GroupLayout panel_refurb_basic_infoLayout = new javax.swing.GroupLayout(panel_refurb_basic_info);
        panel_refurb_basic_info.setLayout(panel_refurb_basic_infoLayout);
        panel_refurb_basic_infoLayout.setHorizontalGroup(
            panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_price_star)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_price)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_price, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_qty)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_categ_star)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_category)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(combo_box_refurb_category, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_refurb_notes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
                    .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_serial_number)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_serial_number, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_model_star)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_model)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_model, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_brand_star)
                        .addGap(5, 5, 5)
                        .addComponent(lbl_brand)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_brand, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_refurb_basic_infoLayout.setVerticalGroup(
            panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_category)
                        .addComponent(lbl_categ_star, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(combo_box_refurb_category, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_brand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_brand)
                    .addComponent(lbl_brand_star, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_model)
                    .addComponent(txt_model, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_model_star, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_price)
                    .addComponent(lbl_price_star, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_qty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_qty))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_serial_number)
                    .addComponent(txt_serial_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_refurb_notes)
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        panel_refurb_extra_info.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_processor.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_processor.setText("Processor");

        txt_processor.setPreferredSize(new java.awt.Dimension(63, 25));

        lbl_ram.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_ram.setText("RAM");

        txt_ram.setPreferredSize(new java.awt.Dimension(63, 25));

        lbl_storage.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_storage.setText("Storage");

        txt_storage.setPreferredSize(new java.awt.Dimension(63, 25));

        lbl_gpu.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_gpu.setText("GPU");

        txt_gpu.setPreferredSize(new java.awt.Dimension(63, 25));

        lbl_screen.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_screen.setText("Screen");

        txt_screen.setPreferredSize(new java.awt.Dimension(63, 25));

        lbl_battery_health.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_battery_health.setText("Battery Health");

        txt_battery_health.setPreferredSize(new java.awt.Dimension(63, 25));

        javax.swing.GroupLayout panel_refurb_extra_infoLayout = new javax.swing.GroupLayout(panel_refurb_extra_info);
        panel_refurb_extra_info.setLayout(panel_refurb_extra_infoLayout);
        panel_refurb_extra_infoLayout.setHorizontalGroup(
            panel_refurb_extra_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_extra_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurb_extra_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_refurb_extra_infoLayout.createSequentialGroup()
                        .addComponent(lbl_processor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_processor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_refurb_extra_infoLayout.createSequentialGroup()
                        .addComponent(lbl_storage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_storage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_refurb_extra_infoLayout.createSequentialGroup()
                        .addComponent(lbl_ram)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_ram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_refurb_extra_infoLayout.createSequentialGroup()
                        .addComponent(lbl_gpu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_gpu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_refurb_extra_infoLayout.createSequentialGroup()
                        .addComponent(lbl_screen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_screen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_refurb_extra_infoLayout.createSequentialGroup()
                        .addComponent(lbl_battery_health)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_battery_health, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_refurb_extra_infoLayout.setVerticalGroup(
            panel_refurb_extra_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_extra_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurb_extra_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_screen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_screen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_extra_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_processor)
                    .addComponent(txt_processor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_extra_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_ram)
                    .addComponent(txt_ram, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_extra_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_storage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_storage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_extra_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_gpu)
                    .addComponent(txt_gpu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_extra_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_battery_health)
                    .addComponent(txt_battery_health, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panel_refurb_custom_info.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_refurb_custom_info.setPreferredSize(new java.awt.Dimension(329, 210));

        lbl_custom_1.setBackground(new java.awt.Color(214, 217, 223));
        lbl_custom_1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_custom_1.setText("CustomSpec");
        lbl_custom_1.setBorder(null);
        lbl_custom_1.setPreferredSize(new java.awt.Dimension(125, 25));

        txt_custom_1.setPreferredSize(new java.awt.Dimension(182, 25));

        lbl_custom_2.setBackground(new java.awt.Color(214, 217, 223));
        lbl_custom_2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_custom_2.setText("CustomSpec");
        lbl_custom_2.setBorder(null);
        lbl_custom_2.setPreferredSize(new java.awt.Dimension(125, 25));

        txt_custom_2.setPreferredSize(new java.awt.Dimension(182, 25));

        lbl_custom_3.setBackground(new java.awt.Color(214, 217, 223));
        lbl_custom_3.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_custom_3.setText("CustomSpec");
        lbl_custom_3.setBorder(null);
        lbl_custom_3.setPreferredSize(new java.awt.Dimension(125, 25));

        txt_custom_3.setPreferredSize(new java.awt.Dimension(182, 25));

        lbl_custom_4.setBackground(new java.awt.Color(214, 217, 223));
        lbl_custom_4.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_custom_4.setText("CustomSpec");
        lbl_custom_4.setBorder(null);
        lbl_custom_4.setPreferredSize(new java.awt.Dimension(125, 25));

        txt_custom_4.setPreferredSize(new java.awt.Dimension(182, 25));

        lbl_custom_5.setBackground(new java.awt.Color(214, 217, 223));
        lbl_custom_5.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_custom_5.setText("CustomSpec");
        lbl_custom_5.setBorder(null);
        lbl_custom_5.setPreferredSize(new java.awt.Dimension(125, 25));

        txt_custom_5.setPreferredSize(new java.awt.Dimension(182, 25));

        lbl_custom_6.setBackground(new java.awt.Color(214, 217, 223));
        lbl_custom_6.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_custom_6.setText("CustomSpec");
        lbl_custom_6.setBorder(null);
        lbl_custom_6.setPreferredSize(new java.awt.Dimension(125, 25));

        txt_custom_6.setPreferredSize(new java.awt.Dimension(182, 25));

        javax.swing.GroupLayout panel_refurb_custom_infoLayout = new javax.swing.GroupLayout(panel_refurb_custom_info);
        panel_refurb_custom_info.setLayout(panel_refurb_custom_infoLayout);
        panel_refurb_custom_infoLayout.setHorizontalGroup(
            panel_refurb_custom_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_custom_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurb_custom_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_custom_4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_custom_5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_custom_6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_custom_1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_custom_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_custom_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_custom_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_custom_6, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(txt_custom_4, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(txt_custom_3, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(txt_custom_2, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(txt_custom_5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(txt_custom_1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_refurb_custom_infoLayout.setVerticalGroup(
            panel_refurb_custom_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_custom_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurb_custom_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_custom_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_custom_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_custom_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_custom_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_custom_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_custom_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_custom_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_custom_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_custom_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_custom_4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_custom_4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_custom_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_custom_5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_custom_5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_custom_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_custom_6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_custom_6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panel_refurb_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        btn_gen_label.setBackground(new java.awt.Color(21, 76, 121));
        btn_gen_label.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_gen_label.setForeground(new java.awt.Color(255, 255, 255));
        btn_gen_label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icon_print.png"))); // NOI18N
        btn_gen_label.setText("Print");
        btn_gen_label.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_gen_labelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_refurb_buttonsLayout = new javax.swing.GroupLayout(panel_refurb_buttons);
        panel_refurb_buttons.setLayout(panel_refurb_buttonsLayout);
        panel_refurb_buttonsLayout.setHorizontalGroup(
            panel_refurb_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_add)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_update)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_delete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_clear_fields)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_gen_label)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_refurb_buttonsLayout.setVerticalGroup(
            panel_refurb_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_buttonsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panel_refurb_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_clear_fields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_gen_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        lbl_mandatory.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        lbl_mandatory.setForeground(new java.awt.Color(255, 102, 102));
        lbl_mandatory.setText("* mandatory");

        javax.swing.GroupLayout panel_refurbsLayout = new javax.swing.GroupLayout(panel_refurbs);
        panel_refurbs.setLayout(panel_refurbsLayout);
        panel_refurbsLayout.setHorizontalGroup(
            panel_refurbsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurbsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurbsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_refurbsLayout.createSequentialGroup()
                        .addComponent(lbl_search_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_search_refurb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addComponent(panel_refurb_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_refurbsLayout.createSequentialGroup()
                        .addGroup(panel_refurbsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_mandatory)
                            .addGroup(panel_refurbsLayout.createSequentialGroup()
                                .addComponent(panel_refurb_basic_info, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(panel_refurb_extra_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(panel_refurb_custom_info, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_refurbsLayout.setVerticalGroup(
            panel_refurbsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_refurbsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurbsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_search_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_search_refurb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(panel_refurbsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_refurb_extra_info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_refurb_basic_info, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addComponent(panel_refurb_custom_info, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addComponent(lbl_mandatory)
                .addGap(15, 15, 15)
                .addComponent(panel_refurb_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_refurbs, javax.swing.GroupLayout.DEFAULT_SIZE, 1026, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_refurbs, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        setBounds(0, 0, 1050, 650);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        try {
            Refurb addRefurbProd = this.getRefurbFields();

            if (addRefurbProd != null) {
                long idRefurbAdded = this._refurbController.addRefurbProduct(addRefurbProd);

                if (idRefurbAdded > 0) {
                    getItemRefurbProd(idRefurbAdded);
                    cleanBasicInfoPanelFields();
                    cleanExtraInfoPanelFields();
                    cleanCustomInfoPanelFields();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            CommonConstant.ERROR_SAVE,
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // useful during development
            JOptionPane.showMessageDialog(
                    this,
                    "An unexpected error occurred: " + ex.getMessage(),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        try {
            Refurb updateRefurb = getRefurbFields();

            if (updateRefurb != null) {
                int confirmEditing = JOptionPane.showConfirmDialog(
                        this,
                        CommonConstant.CONFIRM_UPDATE,
                        this.getTitle(),
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmEditing == JOptionPane.YES_OPTION) {
                    boolean isUpdated = this._refurbController.updateRefurbProduct(updateRefurb);

                    if (isUpdated) {
                        getItemRefurbProd(updateRefurb.getIdRefurb());
                        cleanBasicInfoPanelFields();
                        cleanExtraInfoPanelFields();
                        cleanCustomInfoPanelFields();
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                CommonConstant.ERROR_UPDATE,
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();  // For debugging, replace with logger in production
            JOptionPane.showMessageDialog(
                    this,
                    "An unexpected error occurred: " + ex.getMessage(),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        try {
            int selectedRow = this.table_view_refurbs.getSelectedRow();

            if (selectedRow >= 0) {
                Refurb deleteRefurbProd = new Refurb();

                deleteRefurbProd.setIdRefurb((long) this._dtmRefurb.getValueAt(selectedRow, 0));
                deleteRefurbProd.setModel(this._dtmRefurb.getValueAt(selectedRow, 2).toString());

                int confirmDeletion = JOptionPane.showConfirmDialog(
                        this,
                        CommonConstant.CONFIRM_DELETE,
                        this.getTitle(),
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmDeletion == JOptionPane.YES_OPTION) {
                    boolean isDeleted = this._refurbController.deleteRefurbProduct(deleteRefurbProd);

                    if (isDeleted) {
                        loadRefurbProdListTable(); // Refresh the table
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                CommonConstant.ERROR_DELETE,
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a row to delete.",
                        this.getTitle(),
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Consider replacing with a logger
            JOptionPane.showMessageDialog(
                    this,
                    "An unexpected error occurred: " + ex.getMessage(),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_clear_fieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clear_fieldsActionPerformed
        cleanBasicInfoPanelFields();
        cleanExtraInfoPanelFields();
        cleanCustomInfoPanelFields();

        loadRefurbProdListTable();
    }//GEN-LAST:event_btn_clear_fieldsActionPerformed

    private void txt_qtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_qtyKeyPressed
        if (Character.isLetter(evt.getKeyChar())) {
            this.txt_qty.setEditable(false);
        } else {
            this.txt_qty.setEditable(true);
        }
    }//GEN-LAST:event_txt_qtyKeyPressed

    private void txt_priceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_priceKeyPressed
        if (Character.isLetter(evt.getKeyChar())) {
            this.txt_price.setEditable(false);
        } else {
            this.txt_price.setEditable(true);
        }
    }//GEN-LAST:event_txt_priceKeyPressed

    private void txt_search_refurbKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search_refurbKeyReleased
        searchProductService(this.txt_search_refurb.getText());
    }//GEN-LAST:event_txt_search_refurbKeyReleased

    private void btn_gen_labelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_gen_labelActionPerformed
        Refurb printRefurb = getRefurbFields();
        new PrintRefurbLabel(printRefurb).setVisible(true);
    }//GEN-LAST:event_btn_gen_labelActionPerformed

    private void combo_box_refurb_categoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_box_refurb_categoryItemStateChanged
        showHideCustomPanel(this.combo_box_refurb_category.getSelectedItem().toString());
    }//GEN-LAST:event_combo_box_refurb_categoryItemStateChanged

    private void table_view_refurbsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_refurbsMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = this.table_view_refurbs.getSelectedRow();

            long refurbId = (long) this._dtmRefurb.getValueAt(selectedRow, 0);
            Refurb updateRefurb = this._refurbController.getRefurbProductById(refurbId);
            setRefurbFields(updateRefurb);
        }
    }//GEN-LAST:event_table_view_refurbsMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_clear_fields;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_gen_label;
    private javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> combo_box_refurb_category;
    private javax.swing.JEditorPane editor_pane_refurb_notes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_battery_health;
    private javax.swing.JLabel lbl_brand;
    private javax.swing.JLabel lbl_brand_star;
    private javax.swing.JLabel lbl_categ_star;
    private javax.swing.JLabel lbl_category;
    private javax.swing.JTextField lbl_custom_1;
    private javax.swing.JTextField lbl_custom_2;
    private javax.swing.JTextField lbl_custom_3;
    private javax.swing.JTextField lbl_custom_4;
    private javax.swing.JTextField lbl_custom_5;
    private javax.swing.JTextField lbl_custom_6;
    private javax.swing.JLabel lbl_gpu;
    private javax.swing.JLabel lbl_mandatory;
    private javax.swing.JLabel lbl_model;
    private javax.swing.JLabel lbl_model_star;
    private javax.swing.JLabel lbl_price;
    private javax.swing.JLabel lbl_price_star;
    private javax.swing.JLabel lbl_processor;
    private javax.swing.JLabel lbl_qty;
    private javax.swing.JLabel lbl_ram;
    private javax.swing.JLabel lbl_refurb_notes;
    private javax.swing.JLabel lbl_screen;
    private javax.swing.JLabel lbl_search_icon;
    private javax.swing.JLabel lbl_serial_number;
    private javax.swing.JLabel lbl_storage;
    private javax.swing.JPanel panel_refurb_basic_info;
    private javax.swing.JPanel panel_refurb_buttons;
    private javax.swing.JPanel panel_refurb_custom_info;
    private javax.swing.JPanel panel_refurb_extra_info;
    private javax.swing.JPanel panel_refurbs;
    private javax.swing.JTable table_view_refurbs;
    private javax.swing.JTextField txt_battery_health;
    private javax.swing.JTextField txt_brand;
    private javax.swing.JTextField txt_custom_1;
    private javax.swing.JTextField txt_custom_2;
    private javax.swing.JTextField txt_custom_3;
    private javax.swing.JTextField txt_custom_4;
    private javax.swing.JTextField txt_custom_5;
    private javax.swing.JTextField txt_custom_6;
    private javax.swing.JTextField txt_gpu;
    private javax.swing.JTextField txt_model;
    private javax.swing.JTextField txt_price;
    private javax.swing.JTextField txt_processor;
    private javax.swing.JTextField txt_qty;
    private javax.swing.JTextField txt_ram;
    private javax.swing.JTextField txt_screen;
    private javax.swing.JTextField txt_search_refurb;
    private javax.swing.JTextField txt_serial_number;
    private javax.swing.JTextField txt_storage;
    // End of variables declaration//GEN-END:variables
}
