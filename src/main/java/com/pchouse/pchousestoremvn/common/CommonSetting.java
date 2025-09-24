package com.pchouse.pchousestoremvn.common;

import com.pchouse.pchousestoremvn.models.Company;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class CommonSetting {

    public static final Color DEFAULT_COLOR = new Color(21, 76, 121);
    public static final Color CLICKED_COLOR = new Color(118, 181, 197);
    public static long ID_COMPANY;
    public static Company COMPANY;
    public static JDesktopPane MAIN_MENU_DESKTOP_PANE;

    public static void tableSettings(JTable jTable) {
        jTable.setRowHeight(20);
        jTable.getTableHeader().setFont(new Font("Lucida Grande", Font.BOLD, 12));
    }

    public static void defaultColorPanelAndLabel(JPanel jPanel, JLabel jLabel) {
        Color defaultColor = new Color(21, 76, 121);
        jPanel.setBackground(defaultColor);
        jLabel.setBackground(defaultColor);
    }

    public static void defaultColorLabel(JLabel jLabel) {
        Color defaultColor = new Color(21, 76, 121);
        jLabel.setBackground(defaultColor);
    }

    public static void scrollPaneSettings(JScrollPane jScrollPane) {
        jScrollPane.setOpaque(false);
        jScrollPane.getViewport().setOpaque(false);
    }

    public static void requestTxtFocus(JTextField jTextField) {
        SwingUtilities.invokeLater(() -> {
            jTextField.requestFocus();
        });
    }

    public static void removeInternalFrameBorder(JInternalFrame jInternalFrame) {
        jInternalFrame.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) jInternalFrame.getUI();
        ui.setNorthPane(null);
    }

    public static void setMaxInternalFrame(JInternalFrame jInternalFrame) {
        try {
            jInternalFrame.setMaximum(true);
        } catch (PropertyVetoException e) {
        }
    }

    public static PrinterJob printReport(JPanel jPanel, String pCategory) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setJobName("Print Report " + pCategory);
        PageFormat format = printerJob.getPageFormat(null);

        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D graphics2D = (Graphics2D) graphics;
                jPanel.paint(graphics2D);

                return Printable.PAGE_EXISTS;
            }
        }, format);

        return printerJob;
    }

    public static void fitContentJtable(JTable jTable) {
        jTable = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public class BaseFrame extends JFrame {

        public BaseFrame() {
            setAppIcon();
        }

        protected void setAppIcon() {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/app_icon.png"));
            setIconImage(icon);
        }
    }

    public static void openInternalFrame(JInternalFrame frame, String title) {
        frame.setTitle(title);
        frame.setClosable(true);
        frame.setIconifiable(true);
        frame.setMaximizable(true);
        frame.setResizable(true);

        JDesktopPane desktop = CommonSetting.MAIN_MENU_DESKTOP_PANE;

        for (JInternalFrame openFrame : desktop.getAllFrames()) {
            openFrame.dispose();
        }

        desktop.add(frame);
        desktop.revalidate();
        desktop.repaint();

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

    public static void enableEnterKeyOnButtons() {
        UIManager.put("Button.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{
            "SPACE", "pressed",
            "released SPACE", "released",
            "ENTER", "pressed",
            "released ENTER", "released"
        }));
    }

}
