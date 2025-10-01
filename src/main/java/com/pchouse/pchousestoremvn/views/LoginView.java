package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.controllers.CompanyController;
import com.pchouse.pchousestoremvn.models.Company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class LoginView extends JFrame {

    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JButton btnSignIn = new JButton("Sign in");

    public LoginView() {
        initialize();
    }

    private void initialize() {
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Ajuste do tamanho da janela, garantindo que ela se ajuste ao conteúdo
        setSize(500, 350); // Define o tamanho da janela

        // Centraliza a janela
        setLocationRelativeTo(null);

        JPanel panelLogin = buildLoginPanel();
        getContentPane().add(panelLogin);

        // Adiciona KeyListener nos campos de texto
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    onSignIn(); // Chama o login ao pressionar Enter no nome de usuário
                }
            }
        });

        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    onSignIn(); // Chama o login ao pressionar Enter na senha
                }
            }
        });

        pack();
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(21, 76, 121));

        JLabel lblUserIcon = createIconLabel("/icons/icon-customer.png");
        JLabel lblPassIcon = createIconLabel("/icons/icon_password.png");        

        configureSignInButton();

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(65)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblUserIcon)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 314, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblPassIcon)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtPassword)))
                                .addGap(50))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(100)
                                .addComponent(btnSignIn, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
                                .addGap(100))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGap(50)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(lblUserIcon)
                                .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                        .addGap(18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(lblPassIcon)
                                .addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                        .addGap(35)
                        .addComponent(btnSignIn, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                        .addGap(50)
        );

        return panel;
    }

    private JLabel createIconLabel(String path) {
        return new JLabel(new ImageIcon(getClass().getResource(path)));
    }

    private void setupTextField(JTextField field, String defaultText) {
        field.setFont(new Font("SansSerif", Font.BOLD, 16));
        field.setText(defaultText);
        field.setFocusTraversalKeysEnabled(true);
    }

    private void setupTextField(JPasswordField field, String defaultText) {
        field.setFont(new Font("SansSerif", Font.BOLD, 16));
        field.setText(defaultText);
        field.setFocusTraversalKeysEnabled(true);
        field.addActionListener(e -> btnSignIn.doClick());
    }

    private void configureSignInButton() {
        btnSignIn.setBackground(new Color(21, 76, 121));
        btnSignIn.setForeground(Color.WHITE);
        btnSignIn.setFont(new Font("Lucida Grande", Font.BOLD, 17));
        btnSignIn.setIcon(new ImageIcon(getClass().getResource("/icons/icon_login.png")));
        btnSignIn.setFocusTraversalKeysEnabled(true);
        btnSignIn.addActionListener(e -> onSignIn());
    }

    private void onSignIn() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, getTitle(), JOptionPane.WARNING_MESSAGE);
            return;
        }

        CompanyController controller = new CompanyController();
        Company company = controller.getCompany(username.toUpperCase(), CommonExtension.encryptPassword(txtPassword));

        if (company != null) {
            new MenuViewTest(company).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, CommonConstant.ERROR_LOGIN, getTitle(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
