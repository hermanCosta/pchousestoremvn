package com.pchouse.pchousestoremvn;

import com.formdev.flatlaf.FlatDarkLaf;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.views.LoginView;

import javax.swing.*;

public class MainApp {

    private static JFrame janelaAtual;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            CommonSetting.enableEnterKeyOnButtons();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(MainApp::carregarTelaInicial);
    }

    public static void carregarTelaInicial() {
        if (janelaAtual != null) {
            janelaAtual.dispose();
        }

        janelaAtual = new LoginView();
        janelaAtual.setVisible(true);
    }

}
