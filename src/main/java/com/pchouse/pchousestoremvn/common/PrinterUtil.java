package com.pchouse.pchousestoremvn.common;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import javax.swing.*;

public class PrinterUtil {

    /**
     * Creates a BufferedImage snapshot of the given JPanel.
     */
    public static BufferedImage createImage(JPanel panel) {
        int width = panel.getWidth();
        int height = panel.getHeight();
        if (width <= 0 || height <= 0) {
            // Size may not be set; fallback to preferred size
            Dimension pref = panel.getPreferredSize();
            width = pref.width;
            height = pref.height;
            panel.setSize(width, height);
        }
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        panel.printAll(g2d);  // ensures full component paint, not just visible parts :contentReference[oaicite:0]{index=0}
        g2d.dispose();
        return bi;
    }

    /**
     * Prints the provided JPanel by rendering it into an image first.
     */
    public static void printPanelAsImage(JPanel panel) {
        BufferedImage image = createImage(panel);
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setJobName("JPanel Image Print");

        printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            double scaleX = pageFormat.getImageableWidth() / image.getWidth();
            double scaleY = pageFormat.getImageableHeight() / image.getHeight();
            double scale = Math.min(scaleX, scaleY);
            g2d.scale(scale, scale);
            g2d.drawImage(image, 0, 0, null);
            return Printable.PAGE_EXISTS;
        });

        boolean doPrint = printerJob.printDialog();
        if (doPrint) {
            try {
                printerJob.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }
}
