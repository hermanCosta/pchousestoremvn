package com.pchouse.pchousestoremvn;

import com.formdev.flatlaf.FlatDarkLaf;
import com.pchouse.pchousestoremvn.common.CommonSetting;
import com.pchouse.pchousestoremvn.views.LoginView;

import javax.swing.*;

public class MainApp {

    private static JFrame janelaAtual;

    public static void main(String[] args) {
        try {
            // Dark theme
            UIManager.setLookAndFeel(new FlatDarkLaf());
            CommonSetting.enableEnterKeyOnButtons();

            // use MainApp.class instead of getClass()
            ImageIcon appIcon = new ImageIcon(MainApp.class.getResource("/icons/icon_logo_xs.png"));

            // This sets the icon used in JInternalFrames
            UIManager.put("InternalFrame.icon", appIcon);

            // Optionally: also keep reference if you want to apply to JFrames/JDialogs later
            UIManager.put("App.icon", appIcon);

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

        // Apply same icon to the main window
        janelaAtual.setIconImage(
            ((ImageIcon) UIManager.get("App.icon")).getImage()
        );

        janelaAtual.setVisible(true);
    }
}
