package com.pchouse.pchousestoremvn.common;

import java.awt.Graphics2D;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

public class Printer {

    public static void printPanel(JPanel jPanel) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setJobName("Print Report");
        printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return 1;
            }
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            jPanel.paint(graphics2D);
            return 0;
        });
        boolean returningResult = printerJob.printDialog();
        if (returningResult) {
            try {
                printerJob.print();
            } catch (PrinterException ex) {
                Logger.getLogger(Printer.class.getName()).log(Level.SEVERE, (String) null, ex);
            }
        }
    }
}
