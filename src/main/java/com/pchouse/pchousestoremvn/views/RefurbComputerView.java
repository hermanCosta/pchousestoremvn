package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.common.Printer;
import com.pchouse.pchousestoremvn.controllers.RefurbController;
import com.pchouse.pchousestoremvn.models.Refurb;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RefurbComputerView extends javax.swing.JInternalFrame {

    private final DefaultTableModel _dtmRefurb;
    private final RefurbController _refurbController;
    private List<Refurb> _listRefurb;
    private static final String CATEGORY = "COMPUTER";

    public RefurbComputerView() {
        initComponents();

        CommonSetting.requestTxtFocus(this.txt_search);
        CommonSetting.tableSettings(this.table_view_refurb);

        this._dtmRefurb = (DefaultTableModel) this.table_view_refurb.getModel();
        this._listRefurb = new ArrayList<>();
        this._refurbController = new RefurbController();

        loadRefurbListTable();
    }

    private void loadRefurbListTable() {
        this._listRefurb = this._refurbController.getRefurbProductsByCategory(CommonSetting.COMPANY, this.CATEGORY);

        this._dtmRefurb.setRowCount(0);

        if (this._listRefurb != null) {
            _listRefurb.forEach((refurbItem) -> {
                this._dtmRefurb.addRow(
                        new Object[]{
                            refurbItem.getIdRefurb(),
                            refurbItem.getBrand(),
                            refurbItem.getModel(),
                            refurbItem.getQty(),
                            CommonExtension.formatEuroCurrency(refurbItem.getPrice()),
                            refurbItem.getNote()
                        }
                );
            });
        }
    }

    private Refurb getRefurbFields() {
        Refurb getRefurb = null;
        if (this.txt_brand.getText().trim().isEmpty()
                || this.txt_model.getText().trim().isEmpty()
                || this.txt_price.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, this.getTitle(), JOptionPane.ERROR_MESSAGE);

            return getRefurb;
        } else {
            getRefurb = new Refurb(
                    this.CATEGORY,
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
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    CommonSetting.COMPANY);

            int idRefurb = CommonExtension.setIdExtension(this.hdn_txt_refurb_id);
            getRefurb.setIdRefurb(idRefurb);

            return getRefurb;
        }
    }

    private void setRefurbFields(Refurb refurbProd) {
        this.hdn_txt_refurb_id.setText(String.valueOf(refurbProd.getIdRefurb()));
        this.txt_brand.setText(refurbProd.getBrand());
        this.txt_model.setText(refurbProd.getModel());
        this.txt_price.setText(CommonExtension.formatToPriceField(refurbProd.getPrice()));
        this.txt_qty.setText(String.valueOf(refurbProd.getQty()));
        this.txt_serial_number.setText(refurbProd.getSerialNumber());
        this.editor_pane_refurb_notes.setText(refurbProd.getNote());
        this.txt_screen.setText(refurbProd.getScreen());
        this.txt_processor.setText(refurbProd.getProcessor());
        this.txt_ram.setText(refurbProd.getRamMemory());
        this.txt_storage.setText(refurbProd.getStorage());
        this.txt_gpu.setText(refurbProd.getGpuBoard());
        this.txt_battery_health.setText(refurbProd.getBatteryHealth());
    }

    private void setLabelFields(Refurb refurbProd) {
        this.editor_pane_label.setText("");

        this.txt_bran_mod_scr_label.setText(refurbProd.getBrand() + " " + refurbProd.getModel() + " - " + refurbProd.getScreen());
        this.editor_pane_label.setText("\n" + this.editor_pane_label.getText() + "PROCESSOR:\t\t" + refurbProd.getProcessor() + "\n");
        this.editor_pane_label.setText(this.editor_pane_label.getText() + "GRAPHICS: \t\t" + refurbProd.getGpuBoard() + "\n");
        this.editor_pane_label.setText(this.editor_pane_label.getText() + "MEMORY:\t\t" + refurbProd.getRamMemory() + "\n");
        this.editor_pane_label.setText(this.editor_pane_label.getText() + "STORAGE:\t\t" + refurbProd.getStorage() + "\n");
        this.txt_price_label.setText(CommonExtension.formatToPriceField(refurbProd.getPrice()) + " euro");
        this.txt_warranty.setText("6 Months Warranty");
        this.txt_id.setText("#" + String.valueOf(refurbProd.getIdRefurb()));
    }

    private void searchRefurbByCategory(String pSearch) {
        if (!pSearch.trim().isEmpty()) {
            _listRefurb = _refurbController.searchRefurbByCategory(CommonSetting.COMPANY, this.CATEGORY, pSearch);

            this._dtmRefurb.setRowCount(0);
            if (_listRefurb != null) {

                _listRefurb.forEach((refurb) -> {
                    this._dtmRefurb.addRow(
                            new Object[]{
                                refurb.getIdRefurb(),
                                refurb.getBrand(),
                                refurb.getModel(),
                                refurb.getQty(),
                                CommonExtension.formatEuroCurrency(refurb.getPrice()),
                                refurb.getNote()
                            }
                    );
                });
            }
        } else {
            loadRefurbListTable();
        }
    }

    private void getItemRefurbProd(long idRefurbProd) {
        if (idRefurbProd != 0) {
            Refurb refurbProd = _refurbController.getRefurbProductById(idRefurbProd);

            this._dtmRefurb.setRowCount(0);
            this._dtmRefurb.addRow(
                    new Object[]{
                        refurbProd.getIdRefurb(),
                        refurbProd.getBrand(),
                        refurbProd.getModel(),
                        refurbProd.getQty(),
                        refurbProd.getPrice(),
                        refurbProd.getNote()
                    }
            );
        } else {
            loadRefurbListTable();
        }
    }

    private void clearPanelFields() {
        this.hdn_txt_refurb_id.setText("");
        this.txt_brand.setText("");
        this.txt_model.setText("");
        this.txt_price.setText("");
        this.txt_qty.setText("1");
        this.txt_serial_number.setText("");
        this.txt_screen.setText("");
        this.editor_pane_refurb_notes.setText("");

        this.txt_processor.setText("");
        this.txt_ram.setText("");
        this.txt_storage.setText("");
        this.txt_gpu.setText("");
        this.txt_battery_health.setText("");
    }

    private void clearPanelLabel() {
        this.txt_bran_mod_scr_label.setText("");
        this.editor_pane_label.setText("");
        this.txt_price_label.setText("");
        this.txt_warranty.setText("");
        this.txt_id.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_refurbs = new javax.swing.JPanel();
        lbl_search_icon = new javax.swing.JLabel();
        txt_search = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_view_refurb = new javax.swing.JTable();
        panel_refurb_basic_info = new javax.swing.JPanel();
        hdn_txt_refurb_id = new javax.swing.JTextField();
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
        lbl_screen = new javax.swing.JLabel();
        txt_screen = new javax.swing.JTextField();
        panel_refurb_extra_info = new javax.swing.JPanel();
        lbl_processor = new javax.swing.JLabel();
        txt_processor = new javax.swing.JTextField();
        lbl_ram = new javax.swing.JLabel();
        txt_ram = new javax.swing.JTextField();
        lbl_storage = new javax.swing.JLabel();
        txt_storage = new javax.swing.JTextField();
        lbl_gpu = new javax.swing.JLabel();
        txt_gpu = new javax.swing.JTextField();
        lbl_battery_health = new javax.swing.JLabel();
        txt_battery_health = new javax.swing.JTextField();
        panel_label_design = new javax.swing.JPanel();
        panel_refurb_label = new javax.swing.JPanel();
        lbl_icon = new javax.swing.JLabel();
        txt_bran_mod_scr_label = new javax.swing.JTextField();
        separator_header = new javax.swing.JSeparator();
        scroll_editorpane_computer = new javax.swing.JScrollPane();
        editor_pane_label = new javax.swing.JEditorPane();
        separator_footer = new javax.swing.JSeparator();
        txt_price_label = new javax.swing.JTextField();
        txt_warranty = new javax.swing.JTextField();
        txt_id = new javax.swing.JTextField();
        panel_refurb_buttons = new javax.swing.JPanel();
        btn_add = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_clear_fields = new javax.swing.JButton();
        btn_print_label = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Computers List");
        setMaximumSize(new java.awt.Dimension(0, 0));
        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(1050, 650));

        panel_refurbs.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_search_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icon_search_black.png"))); // NOI18N

        txt_search.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        txt_search.setNextFocusableComponent(txt_brand);
        txt_search.setPreferredSize(new java.awt.Dimension(500, 30));
        txt_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchKeyReleased(evt);
            }
        });

        table_view_refurb.setAutoCreateRowSorter(true);
        table_view_refurb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hdn_Id", "Brand", "Model", "Qty", "Price", "Notes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_view_refurb.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        table_view_refurb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_view_refurbMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_view_refurb);
        if (table_view_refurb.getColumnModel().getColumnCount() > 0) {
            table_view_refurb.getColumnModel().getColumn(0).setMinWidth(0);
            table_view_refurb.getColumnModel().getColumn(0).setPreferredWidth(0);
            table_view_refurb.getColumnModel().getColumn(0).setMaxWidth(0);
            table_view_refurb.getColumnModel().getColumn(1).setPreferredWidth(200);
            table_view_refurb.getColumnModel().getColumn(1).setMaxWidth(200);
            table_view_refurb.getColumnModel().getColumn(2).setPreferredWidth(250);
            table_view_refurb.getColumnModel().getColumn(2).setMaxWidth(300);
            table_view_refurb.getColumnModel().getColumn(3).setPreferredWidth(50);
            table_view_refurb.getColumnModel().getColumn(3).setMaxWidth(150);
            table_view_refurb.getColumnModel().getColumn(4).setPreferredWidth(100);
            table_view_refurb.getColumnModel().getColumn(4).setMaxWidth(150);
        }

        panel_refurb_basic_info.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_refurb_basic_info.setPreferredSize(new java.awt.Dimension(330, 210));
        panel_refurb_basic_info.setVerifyInputWhenFocusTarget(false);

        hdn_txt_refurb_id.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        hdn_txt_refurb_id.setEnabled(false);
        hdn_txt_refurb_id.setMinimumSize(new java.awt.Dimension(12, 20));
        hdn_txt_refurb_id.setPreferredSize(new java.awt.Dimension(0, 0));
        hdn_txt_refurb_id.setRequestFocusEnabled(false);

        lbl_brand_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_brand_star.setForeground(java.awt.Color.red);
        lbl_brand_star.setText("*");

        lbl_brand.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_brand.setText("Brand");

        txt_brand.setMinimumSize(new java.awt.Dimension(12, 20));
        txt_brand.setPreferredSize(new java.awt.Dimension(0, 25));
        txt_brand.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_brandKeyPressed(evt);
            }
        });

        lbl_model_star.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbl_model_star.setForeground(java.awt.Color.red);
        lbl_model_star.setText("*");

        lbl_model.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_model.setText("Model");

        txt_model.setMinimumSize(new java.awt.Dimension(12, 20));
        txt_model.setPreferredSize(new java.awt.Dimension(0, 25));
        txt_model.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_modelKeyPressed(evt);
            }
        });

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
        txt_qty.setPreferredSize(new java.awt.Dimension(50, 25));
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

        lbl_screen.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbl_screen.setText("Screen");

        txt_screen.setPreferredSize(new java.awt.Dimension(63, 25));

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
                        .addComponent(txt_qty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_refurb_notes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                    .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_serial_number)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_serial_number, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_screen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_screen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                                .addComponent(lbl_model_star)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_model))
                            .addComponent(hdn_txt_refurb_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_brand_star)
                        .addGap(5, 5, 5)
                        .addComponent(lbl_brand)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txt_model, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txt_brand, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panel_refurb_basic_infoLayout.setVerticalGroup(
            panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                .addComponent(hdn_txt_refurb_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(8, 8, 8)
                .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_screen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_screen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_basic_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                    .addGroup(panel_refurb_basic_infoLayout.createSequentialGroup()
                        .addComponent(lbl_refurb_notes)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panel_refurb_extra_info.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_refurb_extra_info.setPreferredSize(new java.awt.Dimension(275, 196));

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
                        .addComponent(lbl_battery_health)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_battery_health, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_refurb_extra_infoLayout.setVerticalGroup(
            panel_refurb_extra_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_extra_infoLayout.createSequentialGroup()
                .addContainerGap()
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_label_design.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        panel_refurb_label.setBackground(new java.awt.Color(255, 255, 255));
        panel_refurb_label.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_refurb_label.setMaximumSize(new java.awt.Dimension(555, 200));
        panel_refurb_label.setPreferredSize(new java.awt.Dimension(430, 220));

        lbl_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/logo_slogan_small.png"))); // NOI18N

        txt_bran_mod_scr_label.setFont(new java.awt.Font("sansserif", 1, 15)); // NOI18N
        txt_bran_mod_scr_label.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_bran_mod_scr_label.setBorder(null);

        scroll_editorpane_computer.setBorder(null);
        scroll_editorpane_computer.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll_editorpane_computer.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        editor_pane_label.setBorder(null);
        editor_pane_label.setPreferredSize(new java.awt.Dimension(112, 30));
        scroll_editorpane_computer.setViewportView(editor_pane_label);

        txt_price_label.setFont(new java.awt.Font("sansserif", 1, 15)); // NOI18N
        txt_price_label.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_price_label.setBorder(null);
        txt_price_label.setPreferredSize(new java.awt.Dimension(0, 30));

        txt_warranty.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N
        txt_warranty.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_warranty.setBorder(null);
        txt_warranty.setPreferredSize(new java.awt.Dimension(0, 20));

        txt_id.setEditable(false);
        txt_id.setFont(new java.awt.Font("sansserif", 0, 13)); // NOI18N
        txt_id.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_id.setBorder(null);
        txt_id.setPreferredSize(new java.awt.Dimension(50, 20));

        javax.swing.GroupLayout panel_refurb_labelLayout = new javax.swing.GroupLayout(panel_refurb_label);
        panel_refurb_label.setLayout(panel_refurb_labelLayout);
        panel_refurb_labelLayout.setHorizontalGroup(
            panel_refurb_labelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_labelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurb_labelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_refurb_labelLayout.createSequentialGroup()
                        .addComponent(lbl_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_bran_mod_scr_label, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_refurb_labelLayout.createSequentialGroup()
                        .addComponent(separator_footer, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panel_refurb_labelLayout.createSequentialGroup()
                        .addGroup(panel_refurb_labelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(scroll_editorpane_computer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                            .addComponent(separator_header, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_refurb_labelLayout.createSequentialGroup()
                                .addGroup(panel_refurb_labelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_price_label, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                                    .addComponent(txt_warranty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panel_refurb_labelLayout.setVerticalGroup(
            panel_refurb_labelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_labelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurb_labelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_bran_mod_scr_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(separator_header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scroll_editorpane_computer, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(separator_footer, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_price_label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_refurb_labelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_warranty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout panel_label_designLayout = new javax.swing.GroupLayout(panel_label_design);
        panel_label_design.setLayout(panel_label_designLayout);
        panel_label_designLayout.setHorizontalGroup(
            panel_label_designLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_label_designLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_refurb_label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panel_label_designLayout.setVerticalGroup(
            panel_label_designLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_label_designLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_refurb_label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_refurb_buttons.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_add.setBackground(new java.awt.Color(21, 76, 121));
        btn_add.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_add.setForeground(new java.awt.Color(255, 255, 255));
        btn_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icon_add.png"))); // NOI18N
        btn_add.setText("Add");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });

        btn_update.setBackground(new java.awt.Color(21, 76, 121));
        btn_update.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_update.setForeground(new java.awt.Color(255, 255, 255));
        btn_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icon_save_changes.png"))); // NOI18N
        btn_update.setText("Update");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });

        btn_delete.setBackground(new java.awt.Color(21, 76, 121));
        btn_delete.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_delete.setForeground(new java.awt.Color(255, 255, 255));
        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icon_cancel.png"))); // NOI18N
        btn_delete.setText("Delete");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        btn_clear_fields.setBackground(new java.awt.Color(21, 76, 121));
        btn_clear_fields.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_clear_fields.setForeground(new java.awt.Color(255, 255, 255));
        btn_clear_fields.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icon_clear.png"))); // NOI18N
        btn_clear_fields.setText("Clear");
        btn_clear_fields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_fieldsActionPerformed(evt);
            }
        });

        btn_print_label.setBackground(new java.awt.Color(21, 76, 121));
        btn_print_label.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btn_print_label.setForeground(new java.awt.Color(255, 255, 255));
        btn_print_label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icon_print.png"))); // NOI18N
        btn_print_label.setText("Print ");
        btn_print_label.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_print_labelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_refurb_buttonsLayout = new javax.swing.GroupLayout(panel_refurb_buttons);
        panel_refurb_buttons.setLayout(panel_refurb_buttonsLayout);
        panel_refurb_buttonsLayout.setHorizontalGroup(
            panel_refurb_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_add)
                .addGap(18, 18, 18)
                .addComponent(btn_update)
                .addGap(18, 18, 18)
                .addComponent(btn_delete)
                .addGap(18, 18, 18)
                .addComponent(btn_clear_fields)
                .addGap(18, 18, 18)
                .addComponent(btn_print_label)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_refurb_buttonsLayout.setVerticalGroup(
            panel_refurb_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_refurb_buttonsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panel_refurb_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_clear_fields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_print_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

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
                        .addComponent(txt_search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addComponent(panel_refurb_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_refurbsLayout.createSequentialGroup()
                        .addComponent(panel_refurb_basic_info, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel_refurb_extra_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel_label_design, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_refurbsLayout.setVerticalGroup(
            panel_refurbsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_refurbsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_refurbsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_search_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panel_refurbsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panel_refurb_extra_info, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                    .addComponent(panel_label_design, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_refurb_basic_info, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(panel_refurb_buttons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_refurbs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_refurbs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBounds(0, 0, 1050, 650);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeyReleased
        searchRefurbByCategory(this.txt_search.getText());
    }//GEN-LAST:event_txt_searchKeyReleased

    private void btn_print_labelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_print_labelActionPerformed
        Printer.printPanel(this.panel_refurb_label);
    }//GEN-LAST:event_btn_print_labelActionPerformed

    private void table_view_refurbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_view_refurbMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = this.table_view_refurb.getSelectedRow();

            long refurbId = (long) this._dtmRefurb.getValueAt(selectedRow, 0);
            Refurb updateRefurb = this._refurbController.getRefurbProductById(refurbId);

            setRefurbFields(updateRefurb);
            setLabelFields(updateRefurb);
        }
    }//GEN-LAST:event_table_view_refurbMouseClicked

    private void btn_clear_fieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clear_fieldsActionPerformed
        this.txt_search.setText("");
        clearPanelFields();
        clearPanelLabel();
        loadRefurbListTable();
    }//GEN-LAST:event_btn_clear_fieldsActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        int selectedRow = this.table_view_refurb.getSelectedRow();

        if (selectedRow >= 0) {
            Refurb deleteRefurbProd = new Refurb();

            deleteRefurbProd.setIdRefurb((Integer) this._dtmRefurb.getValueAt(selectedRow, 0));
            deleteRefurbProd.setModel(this._dtmRefurb.getValueAt(selectedRow, 2).toString());

            int confirmDeletion = JOptionPane.showConfirmDialog(this, CommonConstant.CONFIRM_DELETE, this.getTitle(), JOptionPane.YES_NO_OPTION);

            if (confirmDeletion == 0) {
                boolean isDeleted = this._refurbController.deleteRefurbProduct(deleteRefurbProd);

                if (isDeleted) {
                    loadRefurbListTable();
                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.ERROR_DELETE, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        Refurb updateRefurb = getRefurbFields();

        if (updateRefurb != null) {
            int confirmEditing = JOptionPane.showConfirmDialog(this, CommonConstant.CONFIRM_UPDATE, this.getTitle(), JOptionPane.YES_NO_OPTION);
            if (confirmEditing == 0) {
                boolean isUpdated = this._refurbController.updateRefurbProduct(updateRefurb);

                if (isUpdated) {
                    getItemRefurbProd(updateRefurb.getIdRefurb());

                    clearPanelFields();
                    clearPanelLabel();
                } else {
                    JOptionPane.showMessageDialog(this, CommonConstant.ERROR_UPDATE, this.getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        Refurb addRefurbProd = this.getRefurbFields();

        if (addRefurbProd != null) {
            long idRefurbAdded = this._refurbController.addRefurbProduct(addRefurbProd);

            if (idRefurbAdded > 0) {
                getItemRefurbProd(idRefurbAdded);

                clearPanelFields();
                clearPanelLabel();
            } else {
                JOptionPane.showMessageDialog(this, CommonConstant.ERROR_SAVE, this.getTitle(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_addActionPerformed

    private void txt_brandKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_brandKeyPressed
        //Suggest autoComplete firstNames from Database
        //        switch (evt.getKeyCode()) {
        //            case KeyEvent.VK_BACK_SPACE:
        //                break;
        //
        //            case KeyEvent.VK_ENTER:
        //                txt_brand.setText(txt_brand.getText());
        //                break;
        //            default:
        //                EventQueue.invokeLater(() -> {
        //                    String text = txt_brand.getText();
        //                    autoCompleteFromDb(brands, text, txt_brand);
        //                });
        //        }
    }//GEN-LAST:event_txt_brandKeyPressed

    private void txt_modelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_modelKeyPressed
        //Sugest autoComplete firstNames from Database
        //        switch (evt.getKeyCode()) {
        //            case KeyEvent.VK_BACK_SPACE:
        //                break;
        //
        //            case KeyEvent.VK_ENTER:
        //                txt_model.setText(txt_model.getText());
        //                break;
        //            default:
        //                EventQueue.invokeLater(() -> {
        //                    String text = txt_model.getText();
        //                    autoCompleteFromDb(models, text, txt_model);
        //                });
        //        }
    }//GEN-LAST:event_txt_modelKeyPressed

    private void txt_priceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_priceKeyPressed
        if (Character.isLetter(evt.getKeyChar())) {
            this.txt_price.setEditable(false);
        } else {
            this.txt_price.setEditable(true);
        }
    }//GEN-LAST:event_txt_priceKeyPressed

    private void txt_qtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_qtyKeyPressed
        if (Character.isLetter(evt.getKeyChar())) {
            this.txt_qty.setEditable(false);
        } else {
            this.txt_qty.setEditable(true);
        }
    }//GEN-LAST:event_txt_qtyKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_clear_fields;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_print_label;
    private javax.swing.JButton btn_update;
    private javax.swing.JEditorPane editor_pane_label;
    private javax.swing.JEditorPane editor_pane_refurb_notes;
    private javax.swing.JTextField hdn_txt_refurb_id;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_battery_health;
    private javax.swing.JLabel lbl_brand;
    private javax.swing.JLabel lbl_brand_star;
    private javax.swing.JLabel lbl_gpu;
    private javax.swing.JLabel lbl_icon;
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
    private javax.swing.JPanel panel_label_design;
    private javax.swing.JPanel panel_refurb_basic_info;
    private javax.swing.JPanel panel_refurb_buttons;
    private javax.swing.JPanel panel_refurb_extra_info;
    private javax.swing.JPanel panel_refurb_label;
    private javax.swing.JPanel panel_refurbs;
    private javax.swing.JScrollPane scroll_editorpane_computer;
    private javax.swing.JSeparator separator_footer;
    private javax.swing.JSeparator separator_header;
    private javax.swing.JTable table_view_refurb;
    private javax.swing.JTextField txt_battery_health;
    private javax.swing.JTextField txt_bran_mod_scr_label;
    private javax.swing.JTextField txt_brand;
    private javax.swing.JTextField txt_gpu;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_model;
    private javax.swing.JTextField txt_price;
    private javax.swing.JTextField txt_price_label;
    private javax.swing.JTextField txt_processor;
    private javax.swing.JTextField txt_qty;
    private javax.swing.JTextField txt_ram;
    private javax.swing.JTextField txt_screen;
    private javax.swing.JTextField txt_search;
    private javax.swing.JTextField txt_serial_number;
    private javax.swing.JTextField txt_storage;
    private javax.swing.JTextField txt_warranty;
    // End of variables declaration//GEN-END:variables
}
