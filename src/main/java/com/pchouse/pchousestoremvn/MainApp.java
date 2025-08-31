package com.pchouse.pchousestoremvn;

import com.formdev.flatlaf.FlatDarkLaf;
import com.pchouse.pchousestoremvn.controllers.CompanyController;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.views.LoginView;
import com.pchouse.pchousestoremvn.views.MenuViewTest;

import javax.swing.*;

public class MainApp {

    private static JFrame janelaAtual;
    
    public void testMethod() {
        
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(MainApp::carregarTelaInicial);
    }

    public static void carregarTelaInicial() {
        if (janelaAtual != null) {
            janelaAtual.dispose();  
        }

        CompanyController controller = new CompanyController();
        Company company = controller.getCompany("FREDERICKST", "fredst");

        if (company != null) {
            janelaAtual = new MenuViewTest(company);            
        }
        //janelaAtual = new LoginView();
        janelaAtual.setVisible(true);
    }

}
