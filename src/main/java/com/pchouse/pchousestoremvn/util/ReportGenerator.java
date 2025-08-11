package com.pchouse.pchousestoremvn.util;

import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.SaleProdServ;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderFault;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {

    public void generateServiceOrderReport(ServiceOrder order, List<ServiceOrderFault> faults, List<ServiceOrderProdServ> prodServs) {
        try {
            // Paths to your reports
            String mainReportPath = "/com/pchouse/pchousestoremvn/reports/ServiceOrderReport.jasper";
            String headerSubreportPath = "/com/pchouse/pchousestoremvn/reports/subreport_header.jasper";
            String subreportDir = "/com/pchouse/pchousestoremvn/reports/";

            // Load the header subreport JasperReport object
            JasperReport headerSubreport = (JasperReport) JRLoader.loadObject(
                    getClass().getResource(headerSubreportPath)
            );

            // Create data sources
            JRBeanCollectionDataSource prodServDataSource = new JRBeanCollectionDataSource(prodServs);

            // Concatenate faults descriptions
            String faultsConcatenated = faults.stream()
                    .map(fault -> fault.getFault().getDescription())
                    .collect(Collectors.joining(", "));

            // Prepare parameters for main report (and subreport)
            Map<String, Object> parameters = new HashMap<>();

            // Company info (shared with header subreport)
            parameters.put("companyName", order.getCompany().getName());
            parameters.put("companyAddress", order.getCompany().getAddress());
            parameters.put("companyPhone", order.getCompany().getContactOne());
            parameters.put("companyEmail", order.getCompany().getEmail());

            // Logo InputStream
            InputStream logoStream = getClass().getResourceAsStream("/icons/icon_logo_header_lg.png");
            if (logoStream == null) {
                System.err.println("Logo not found!");
            }
            parameters.put("companyLogo", logoStream);

            // Pass the compiled header subreport to main report
            parameters.put("subreport_header", headerSubreport);

            // Other fields for main report
            parameters.put("orderId", order.getIdServiceOrder());
            parameters.put("customerName", order.getCustomer().getPerson().getFirstName() + " " + order.getCustomer().getPerson().getLastName());
            parameters.put("customerPhone", order.getCustomer().getPerson().getContactNo());
            parameters.put("customerEmail", order.getCustomer().getPerson().getEmail());
            parameters.put("brand", order.getDevice().getBrand());
            parameters.put("model", order.getDevice().getModel());
            parameters.put("serialNumber", order.getDevice().getSerialNumber());
            parameters.put("createdDate", order.getCreated());
            parameters.put("notes", order.getNote());
            parameters.put("total", order.getTotal());
            parameters.put("deposit", order.getTotal() - order.getDue());
            parameters.put("remaining", order.getDue());
            parameters.put("createdDate", order.getCreated());

            parameters.put("faultsConcatenated", faultsConcatenated);
            parameters.put("prodServDataSource", prodServDataSource);

            // Set subreport directory parameter (useful for subreports inside main report)
            parameters.put("SUBREPORT_DIR", getClass().getResource(subreportDir).getPath());

            // Load main report
            JasperReport mainReport = (JasperReport) JRLoader.loadObject(getClass().getResource(mainReportPath));

            // Fill main report with parameters and an empty datasource (or your datasource)
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, new JREmptyDataSource());

            // View the filled report
            JasperViewer.viewReport(jasperPrint, false);

            System.out.println("Report generated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateSaleOrderReport(Sale sale, List<SaleProdServ> prodServs, List<SalePayment> salePayments) {
        try {
            // Sum amounts by payment method
            double totalCash = 0.0;
            double totalCard = 0.0;

            for (SalePayment payment : salePayments) {
                if (payment.getPayMethod() != null) {
                    switch (payment.getPayMethod()) {
                        case CASH:
                            totalCash += payment.getCashAmount();
                            break;
                        case CARD:
                            totalCard += payment.getCardAmount();
                            break;
                        case COMBINE:
                            totalCash += payment.getCashAmount();
                             totalCard += payment.getCardAmount();
                             break;
                        default:
                            break; // ignore other payment methods
                    }
                }
            }

            // Paths to your reports
            String mainReportPath = "/com/pchouse/pchousestoremvn/reports/SaleOrderReport.jasper";
            String headerSubreportPath = "/com/pchouse/pchousestoremvn/reports/subreport_header.jasper";
            String subreportDir = "/com/pchouse/pchousestoremvn/reports/";

            // Load the header subreport JasperReport object
            JasperReport headerSubreport = (JasperReport) JRLoader.loadObject(
                    getClass().getResource(headerSubreportPath)
            );

            // Create data sources
            JRBeanCollectionDataSource prodServDataSource = new JRBeanCollectionDataSource(prodServs);

            // Prepare parameters for main report (and subreport)
            Map<String, Object> parameters = new HashMap<>();

            // Company info (shared with header subreport)
            parameters.put("companyName", sale.getCompany().getName());
            parameters.put("companyAddress", sale.getCompany().getAddress());
            parameters.put("companyPhone", sale.getCompany().getContactOne());
            parameters.put("companyEmail", sale.getCompany().getEmail());

            // Logo InputStream
            InputStream logoStream = getClass().getResourceAsStream("/icons/icon_logo_header_lg.png");
            if (logoStream == null) {
                System.err.println("Logo not found!");
            }
            parameters.put("companyLogo", logoStream);

            // Pass the compiled header subreport to main report
            parameters.put("subreport_header", headerSubreport);

            // Other fields for main report
            parameters.put("saleId", sale.getIdSale());
            parameters.put("customerName", sale.getCustomer().getPerson().getFirstName() + " " + sale.getCustomer().getPerson().getLastName());
            parameters.put("customerPhone", sale.getCustomer().getPerson().getContactNo());
            parameters.put("customerEmail", sale.getCustomer().getPerson().getEmail());
            parameters.put("createdDate", sale.getCreated());
            parameters.put("total", sale.getTotal());

            // Set summed payment method values
            parameters.put("payMethodCash", totalCash);
            parameters.put("payMethodCard", totalCard);
            parameters.put("change", salePayments.get(0).getChangeAmount());

            parameters.put("prodServDataSource", prodServDataSource);

            // Set subreport directory parameter (useful for subreports inside main report)
            parameters.put("SUBREPORT_DIR", getClass().getResource(subreportDir).getPath());

            // Load main report
            JasperReport mainReport = (JasperReport) JRLoader.loadObject(getClass().getResource(mainReportPath));

            // Fill main report
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, new JREmptyDataSource());

            // View the filled report
            JasperViewer.viewReport(jasperPrint, false);

            System.out.println("Report generated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateServiceOrderReportTest() {
        try {
            Map<String, Object> params = new HashMap<>();

            // Company Info
            params.put("companyName", "PCHouse");
            params.put("companyAddress", "123 Tech Street");
            params.put("companyPhone", "+1 555-1234");
            params.put("companyEmail", "info@pchouse.com");

            // Subreport path
            URL subDirUrl = getClass().getResource("/com/pchouse/pchousestoremvn/reports/");
            if (subDirUrl == null) {
                throw new RuntimeException("Subreport directory not found!");
            }

            File subDirFile = new File(subDirUrl.toURI());
            String subreportPath = subDirFile.getAbsolutePath() + File.separator;

            File subreportFile = new File(subreportPath + "subreport_header.jasper");
            System.out.println("Subreport path: " + subreportFile.getAbsolutePath());
            if (!subreportFile.exists()) {
                throw new FileNotFoundException("Subreport file not found: " + subreportFile.getAbsolutePath());
            }

            JasperReport subreport = (JasperReport) JRLoader.loadObject(subreportFile);
            System.out.println("Subreport loaded: " + subreport.getName());
            params.put("SUBREPORT_HEADER", subreport);

            // Load and compile main report
            JasperReport mainReport = (JasperReport) JRLoader.loadObject(
                    getClass().getResource("/com/pchouse/pchousestoremvn/reports/MainReport.jasper")
            );

            System.out.println("Preenchendo o relatório...");
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, params, new JREmptyDataSource(1));
            System.out.println("Relatório preenchido com " + jasperPrint.getPages().size() + " páginas.");

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
