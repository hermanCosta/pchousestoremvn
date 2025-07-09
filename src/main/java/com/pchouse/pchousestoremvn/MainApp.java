package com.pchouse.pchousestoremvn;

import com.formdev.flatlaf.FlatDarkLaf;
import com.pchouse.pchousestoremvn.views.LoginView;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // ou FlatLightLaf()
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //logo_slogan_small
        SwingUtilities.invokeLater(() -> {
            // Agora LoginView é uma JFrame normal, então podemos chamar diretamente
            new LoginView().setVisible(true);
        });
    }
}
