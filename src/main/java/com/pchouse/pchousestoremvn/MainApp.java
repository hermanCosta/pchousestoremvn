package com.pchouse.pchousestoremvn;

import com.formdev.flatlaf.FlatDarkLaf;
import com.pchouse.pchousestoremvn.views.LoginView;

import javax.swing.*;

public class MainApp {

    private static JFrame janelaAtual;

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
            janelaAtual.dispose();  // Fecha a janela anterior se existir
        }

        janelaAtual = new LoginView(); // Pode ser qualquer JFrame: LoginView, MainMenu, etc.
        janelaAtual.setVisible(true);
    }
}