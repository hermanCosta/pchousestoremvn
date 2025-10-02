package com.pchouse.pchousestoremvn.util;

import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.enums.PayMethod;
import static com.pchouse.pchousestoremvn.enums.PayMethod.CARD;
import static com.pchouse.pchousestoremvn.enums.PayMethod.CASH;
import static com.pchouse.pchousestoremvn.enums.PayMethod.COMBINE;
import com.pchouse.pchousestoremvn.enums.PaymentType;
import com.pchouse.pchousestoremvn.models.CashInRegistry;
import com.pchouse.pchousestoremvn.models.CashOutRegistry;
import com.pchouse.pchousestoremvn.models.PaymentSummary;
import com.pchouse.pchousestoremvn.models.RefurbSale;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.SaleProdServ;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderFault;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

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

    public void generateSaleReceiptReport(Sale sale, List<SaleProdServ> prodServs, List<SalePayment> salePayments) {
        try {
            // Sum amounts by payment method
            double totalCash = 0.0;
            double totalCard = 0.0;
            double totalChange = 0.0;

            for (SalePayment payment : salePayments) {
                if (payment.getPayMethod() != null) {
                    totalChange += payment.getChangeAmount();

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
            parameters.put("createdDate", salePayments.getLast().getDtTransaction());
            parameters.put("total", sale.getTotal());

            // Set summed payment method values
            parameters.put("payMethodCash", totalCash);
            parameters.put("payMethodCard", totalCard);
            parameters.put("change", totalChange);

            parameters.put("prodServDataSource", prodServDataSource);

            // Set subreport directory parameter (useful for subreports inside main report)
            parameters.put("SUBREPORT_DIR", getClass().getResource(subreportDir).getPath());

            // Load main report
            JasperReport mainReport = (JasperReport) JRLoader.loadObject(getClass().getResource(mainReportPath));

            // Fill main report
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, new JREmptyDataSource());

            // View the filled report
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateServiceOrderReceiptReport(ServiceOrder order, List<ServiceOrderProdServ> prodServs, List<ServiceOrderPayment> serviceOrderPayments) {
        try {
            // Sum amounts by payment method
            double totalCash = 0.0;
            double totalCard = 0.0;
            double totalchange = 0.0;

            for (ServiceOrderPayment payment : serviceOrderPayments) {
                if (payment.getPayMethod() != null) {
                    totalchange += payment.getChangeAmount();

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
                            break; // ignore others
                    }
                }
            }

            // Paths to your reports
            String mainReportPath = "/com/pchouse/pchousestoremvn/reports/ServiceOrderReceiptReport.jasper";
            String headerSubreportPath = "/com/pchouse/pchousestoremvn/reports/subreport_header.jasper";
            String subreportDir = "/com/pchouse/pchousestoremvn/reports/";

            // Load the header subreport JasperReport object
            JasperReport headerSubreport = (JasperReport) JRLoader.loadObject(
                    getClass().getResource(headerSubreportPath)
            );

            // Create data source
            JRBeanCollectionDataSource prodServDataSource = new JRBeanCollectionDataSource(prodServs);

            // Prepare parameters
            Map<String, Object> parameters = new HashMap<>();

            // Company info
            parameters.put("companyName", order.getCompany().getName());
            parameters.put("companyAddress", order.getCompany().getAddress());
            parameters.put("companyPhone", order.getCompany().getContactOne());
            parameters.put("companyEmail", order.getCompany().getEmail());

            // Logo
            InputStream logoStream = getClass().getResourceAsStream("/icons/icon_logo_header_lg.png");
            if (logoStream == null) {
                System.err.println("Logo not found!");
            }
            parameters.put("companyLogo", logoStream);

            // Subreport
            parameters.put("subreport_header", headerSubreport);

            // Order fields
            parameters.put("serviceOrderId", order.getIdServiceOrder());
            parameters.put("customerName", order.getCustomer().getPerson().getFirstName() + " " + order.getCustomer().getPerson().getLastName());
            parameters.put("customerPhone", order.getCustomer().getPerson().getContactNo());
            parameters.put("customerEmail", order.getCustomer().getPerson().getEmail());
            parameters.put("brand", order.getDevice().getBrand());
            parameters.put("model", order.getDevice().getModel());
            parameters.put("serialNumber", order.getDevice().getSerialNumber());
            parameters.put("createdDate", order.getCreated());
            parameters.put("createdDate", serviceOrderPayments.getLast().getDtTransaction());
            parameters.put("total", order.getTotal());
            parameters.put("deposit", order.getTotal() - order.getDue());
            parameters.put("remaining", order.getDue());

            // Payment method totals
            parameters.put("payMethodCash", totalCash);
            parameters.put("payMethodCard", totalCard);

            // Change amount (take from first payment safely)
            if (serviceOrderPayments != null && !serviceOrderPayments.isEmpty()) {
                parameters.put("change", totalchange);
            } else {
                parameters.put("change", 0.0);
            }

            // DataSource for products/services
            parameters.put("prodServDataSource", prodServDataSource);

            // Subreport dir
            parameters.put("SUBREPORT_DIR", getClass().getResource(subreportDir).getPath());

            // Load and fill main report
            JasperReport mainReport = (JasperReport) JRLoader.loadObject(getClass().getResource(mainReportPath));
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, new JREmptyDataSource());

            // Show
            JasperViewer.viewReport(jasperPrint, false);

            System.out.println("Service Order Receipt generated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateRefurbSaleReceiptReport(Sale sale, List<RefurbSale> refurbsSale, List<SalePayment> salePayments) {
        try {
            // Sum amounts by payment method
            double totalCash = 0.0;
            double totalCard = 0.0;
            double totalChange = 0.0;

            for (SalePayment payment : salePayments) {
                if (payment.getPayMethod() != null) {
                    totalChange += payment.getChangeAmount();

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
            String mainReportPath = "/com/pchouse/pchousestoremvn/reports/RefurbSaleReceiptReport.jasper";
            String headerSubreportPath = "/com/pchouse/pchousestoremvn/reports/subreport_header.jasper";
            String subreportDir = "/com/pchouse/pchousestoremvn/reports/";

            // Load the header subreport JasperReport object
            JasperReport headerSubreport = (JasperReport) JRLoader.loadObject(
                    getClass().getResource(headerSubreportPath)
            );

            // Create data sources
            JRBeanCollectionDataSource refurbsSaleDataSource = new JRBeanCollectionDataSource(refurbsSale);

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
            parameters.put("createdDate", salePayments.getLast().getDtTransaction());
            parameters.put("total", sale.getTotal());

            // Set summed payment method values
            parameters.put("payMethodCash", totalCash);
            parameters.put("payMethodCard", totalCard);
            parameters.put("change", totalChange);

            parameters.put("refurbsSaleDataSource", refurbsSaleDataSource);

            // Set subreport directory parameter (useful for subreports inside main report)
            parameters.put("SUBREPORT_DIR", getClass().getResource(subreportDir).getPath());

            // Load main report
            JasperReport mainReport = (JasperReport) JRLoader.loadObject(getClass().getResource(mainReportPath));

            // Fill main report
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, new JREmptyDataSource());

            // View the filled report
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateDailyClosingTillReport(
            List<SalePayment> salePayments,
            List<ServiceOrderPayment> serviceOrderPayments,
            List<CashInRegistry> cashInList,
            List<CashOutRegistry> cashOutList,
            String cashierName,
            double openCash,
            double closeCash
    ) {
        // Check if all lists are null or empty
        boolean noDataToPrint = (salePayments == null || salePayments.isEmpty())
                && (serviceOrderPayments == null || serviceOrderPayments.isEmpty())
                && (cashInList == null || cashInList.isEmpty())
                && (cashOutList == null || cashOutList.isEmpty());
        if (noDataToPrint) {
            System.out.println("No data to generate report. Skipping report generation.");
            return;
        }
        // Initialize null lists to empty lists to avoid NullPointerExceptions
        if (salePayments == null) {
            salePayments = Collections.emptyList();
        }
        if (serviceOrderPayments == null) {
            serviceOrderPayments = Collections.emptyList();
        }
        if (cashInList == null) {
            cashInList = Collections.emptyList();
        }
        if (cashOutList == null) {
            cashOutList = Collections.emptyList();
        }
        List<PaymentSummary> saleSummaries = new ArrayList<>();
        List<PaymentSummary> serviceSummaries = new ArrayList<>();
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        // --- Group Sale Payments ---
        Map<String, double[]> groupedSales = new HashMap<>();
        for (SalePayment sp : salePayments) {
            String key = sp.getSale().getIdSale() + "|" + sp.getPaymentType().name() + "|" + sp.getDtTransaction();
            double[] totals = groupedSales.getOrDefault(key, new double[2]);
            totals[0] += sp.getCashAmount() != null ? sp.getCashAmount() - sp.getChangeAmount() : 0;
            totals[1] += sp.getCardAmount() != null ? sp.getCardAmount() : 0;
            groupedSales.put(key, totals);
        }
        for (Map.Entry<String, double[]> entry : groupedSales.entrySet()) {
            String[] parts = entry.getKey().split("\\|");
            long orderNumber = Long.parseLong(parts[0]);
            String paymentType = parts[1];
            LocalDateTime dtTransaction = LocalDateTime.parse(parts[2], inputFormat);
            double[] totals = entry.getValue();
            if (paymentType.equalsIgnoreCase("REFUND")) {
                totals[0] = -totals[0];
                totals[1] = -totals[1];
            }
            saleSummaries.add(new PaymentSummary(
                    orderNumber,
                    paymentType,
                    totals[0],
                    totals[1],
                    CommonExtension.formatDateTimeFromLocalDate(dtTransaction)
            ));
        }

        // --- Group Service Order Payments ---
        Map<String, double[]> groupedServices = new HashMap<>();
        for (ServiceOrderPayment sop : serviceOrderPayments) {
            String key = sop.getServiceOrder().getIdServiceOrder() + "|" + sop.getPaymentType().name() + "|" + sop.getDtTransaction();
            double[] totals = groupedServices.getOrDefault(key, new double[2]);
            totals[0] += sop.getCashAmount() != null ? sop.getCashAmount() - sop.getChangeAmount() : 0;
            totals[1] += sop.getCardAmount() != null ? sop.getCardAmount() : 0;
            groupedServices.put(key, totals);
        }
        for (Map.Entry<String, double[]> entry : groupedServices.entrySet()) {
            String[] parts = entry.getKey().split("\\|");
            long orderNumber = Long.parseLong(parts[0]);
            String paymentType = parts[1];
            LocalDateTime dtTransaction = LocalDateTime.parse(parts[2], inputFormat);
            double[] totals = entry.getValue();
            if (paymentType.equalsIgnoreCase("REFUND")) {
                totals[0] = -totals[0];
                totals[1] = -totals[1];
            }
            serviceSummaries.add(new PaymentSummary(
                    orderNumber,
                    paymentType,
                    totals[0],
                    totals[1],
                    CommonExtension.formatDateTimeFromLocalDate(dtTransaction)
            ));
        }

        // --- Totals Calculation (FIXED) ---
        double totalGrossSale = salePayments.stream()
                .mapToDouble(SalePayment::getAmountDue)
                .sum();
        double totalGrossService = serviceOrderPayments.stream()
                .mapToDouble(ServiceOrderPayment::getAmountDue)
                .sum();

        // Cash: subtract change and refunds
        double totalSaleCash = salePayments.stream()
                .filter(p -> p.getPayMethod() == PayMethod.CASH)
                .mapToDouble(p -> p.getCashAmount() != null ? p.getCashAmount() - p.getChangeAmount() : 0)
                .sum();
        double totalServiceCash = serviceOrderPayments.stream()
                .filter(p -> p.getPayMethod() == PayMethod.CASH)
                .mapToDouble(p -> p.getCashAmount() != null ? p.getCashAmount() - p.getChangeAmount() : 0)
                .sum();

        // Card: no change, just refunds
        double totalSaleCard = salePayments.stream()
                .filter(p -> p.getPayMethod() == PayMethod.CARD)
                .mapToDouble(p -> p.getCardAmount() != null ? p.getCardAmount() : 0)
                .sum();
        double totalServiceCard = serviceOrderPayments.stream()
                .filter(p -> p.getPayMethod() == PayMethod.CARD)
                .mapToDouble(p -> p.getCardAmount() != null ? p.getCardAmount() : 0)
                .sum();

        // Refunds: both cash and card
        double totalSaleRefundCash = salePayments.stream()
                .filter(p -> p.getPaymentType() == PaymentType.REFUND && p.getPayMethod() == PayMethod.CASH)
                .mapToDouble(p -> p.getCashAmount() != null ? p.getCashAmount() - p.getChangeAmount() : 0)
                .sum();
        double totalSaleRefundCard = salePayments.stream()
                .filter(p -> p.getPaymentType() == PaymentType.REFUND && p.getPayMethod() == PayMethod.CARD)
                .mapToDouble(p -> p.getCardAmount() != null ? p.getCardAmount() : 0)
                .sum();
        double totalServiceRefundCash = serviceOrderPayments.stream()
                .filter(p -> p.getPaymentType() == PaymentType.REFUND && p.getPayMethod() == PayMethod.CASH)
                .mapToDouble(p -> p.getCashAmount() != null ? p.getCashAmount() - p.getChangeAmount() : 0)
                .sum();
        double totalServiceRefundCard = serviceOrderPayments.stream()
                .filter(p -> p.getPaymentType() == PaymentType.REFUND && p.getPayMethod() == PayMethod.CARD)
                .mapToDouble(p -> p.getCardAmount() != null ? p.getCardAmount() : 0)
                .sum();

        // Total refunds (cash + card)
        double totalSaleRefund = totalSaleRefundCash + totalSaleRefundCard;
        double totalServiceRefund = totalServiceRefundCash + totalServiceRefundCard;

        // Net totals (after refunds)
        double netTotalCash = (totalSaleCash + totalServiceCash) - (totalSaleRefundCash + totalServiceRefundCash);
        double netTotalCard = (totalSaleCard + totalServiceCard) - (totalSaleRefundCard + totalServiceRefundCard);
        double netTotalGross = (totalGrossSale + totalGrossService) - (totalSaleRefund + totalServiceRefund);

        try {
            String subreportDir = "/com/pchouse/pchousestoremvn/reports/";
            // Load subreports
            JasperReport headerSubreport = (JasperReport) JRLoader.loadObject(getClass().getResource(subreportDir + "subreport_header.jasper"));
            JasperReport saleSubreport = (JasperReport) JRLoader.loadObject(getClass().getResource(subreportDir + "subreport_sale_payments.jasper"));
            JasperReport serviceSubreport = (JasperReport) JRLoader.loadObject(getClass().getResource(subreportDir + "subreport_service_payments.jasper"));
            JasperReport cashInSubreport = (JasperReport) JRLoader.loadObject(getClass().getResource(subreportDir + "subreport_cash_in.jasper"));
            JasperReport cashOutSubreport = (JasperReport) JRLoader.loadObject(getClass().getResource(subreportDir + "subreport_cash_out.jasper"));
            JasperReport mainReport = (JasperReport) JRLoader.loadObject(getClass().getResource(subreportDir + "TillCloseReport.jasper"));

            // Parameters
            Map<String, Object> params = new HashMap<>();
            // Company info
            params.put("companyName", CommonSetting.COMPANY.getName());
            params.put("companyAddress", CommonSetting.COMPANY.getAddress());
            params.put("companyPhone", CommonSetting.COMPANY.getContactOne());
            params.put("companyEmail", CommonSetting.COMPANY.getEmail());
            // Logo
            InputStream logoStream = getClass().getResourceAsStream("/icons/icon_logo_header_lg.png");
            if (logoStream != null) {
                byte[] logoBytes = logoStream.readAllBytes();
                logoStream.close();
                params.put("companyLogo", new ByteArrayInputStream(logoBytes));
            } else {
                System.err.println("Logo not found!");
            }
            // Other report parameters
            params.put("totalGross", netTotalGross);
            params.put("totalGrossSale", totalGrossSale - totalSaleRefund);
            params.put("totalGrossService", totalGrossService - totalServiceRefund);
            params.put("totalCash", netTotalCash);
            params.put("totalCard", netTotalCard);
            params.put("totalRefunds", -(totalSaleRefund + totalServiceRefund));
            params.put("openCash", openCash);
            params.put("closeCash", closeCash);
            params.put("cashierName", cashierName);

            // Subreports
            params.put("subreport_header", headerSubreport);
            params.put("subreport_sale_payments", saleSubreport);
            params.put("subreport_service_payments", serviceSubreport);
            params.put("subreport_cash_in", cashInSubreport);
            params.put("subreport_cash_out", cashOutSubreport);

            // Data sources
            params.put("saleDataSource", new JRBeanCollectionDataSource(saleSummaries));
            params.put("serviceDataSource", new JRBeanCollectionDataSource(serviceSummaries));
            params.put("cashInDataSource", new JRBeanCollectionDataSource(cashInList));
            params.put("cashOutDataSource", new JRBeanCollectionDataSource(cashOutList));

            // Fill and display the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, params, new JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
