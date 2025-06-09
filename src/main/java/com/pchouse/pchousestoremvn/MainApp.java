package com.pchouse.pchousestoremvn;

import com.pchouse.pchousestoremvn.views.LoginView;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Agora LoginView é uma JFrame normal, então podemos chamar diretamente
            new LoginView().setVisible(true);
        });
    }
}
