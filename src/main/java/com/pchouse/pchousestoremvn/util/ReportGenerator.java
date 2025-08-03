package com.pchouse.pchousestoremvn.util;

import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderFault;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {

    public void generateServiceOrderReport(ServiceOrder order,
            List<ServiceOrderFault> faults,
            List<ServiceOrderProdServ> prodServs) {

        try {
            String reportPath = "/com/pchouse/pchousestoremvn/reports/ServiceOrderReport.jasper";
            String subreportDir = "/com/pchouse/pchousestoremvn/reports/";

            // Fonte de dados para produtos/serviços permanece igual
            JRBeanCollectionDataSource prodServDataSource = new JRBeanCollectionDataSource(prodServs);

            // CONCATENA descrições das falhas separadas por vírgula
            String faultsConcatenated = faults.stream()
                    .map(fault -> fault.getFault().getDescription())
                    .collect(Collectors.joining(", "));

            // Parâmetros para o relatório principal
            Map<String, Object> parameters = new HashMap<>();
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
            
            
            // Subreports
            parameters.put("SUBREPORT_DIR", getClass().getResource(subreportDir).toString());

            // PASSAR a string concatenada em vez de DataSource para faults
            parameters.put("faultsConcatenated", faultsConcatenated);

            // Para produtos/serviços ainda usa datasource
            parameters.put("prodServDataSource", prodServDataSource);

            // Carrega e preenche o relatório principal
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource(reportPath));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            // Exibe na tela
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
