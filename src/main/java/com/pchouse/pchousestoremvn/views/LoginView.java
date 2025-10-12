package com.pchouse.pchousestoremvn.views;

import com.pchouse.pchousestoremvn.common.CommonConstant;
import com.pchouse.pchousestoremvn.common.CommonExtension;
import com.pchouse.pchousestoremvn.controllers.CompanyController;
import com.pchouse.pchousestoremvn.models.Company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;

public class LoginView extends JFrame {

    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JButton btnSignIn = new JButton("Sign in");
    private JDialog loadingDialog;

    public LoginView() {
        initialize();
    }

    private void initialize() {
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panelLogin = buildLoginPanel();
        getContentPane().add(panelLogin);

        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    onSignIn();
                }
            }
        });

        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    onSignIn();
                }
            }
        });

        pack();
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(21, 76, 121));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === LOGO HEADER ===
        JLabel lblLogoHeader = new JLabel(new ImageIcon(getClass().getResource("/icons/icon_logo_header_md.png")));
        lblLogoHeader.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblUserIcon = createIconLabel("/icons/icon_customer.png");
        JLabel lblPassIcon = createIconLabel("/icons/icon_password.png");

        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        configureSignInButton();

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(lblLogoHeader, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
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
                                                .addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 314, GroupLayout.PREFERRED_SIZE)))
                                .addGap(50))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(100)
                                .addComponent(btnSignIn, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
                                .addGap(100))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGap(20)
                        .addComponent(lblLogoHeader, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                        .addGap(20)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(lblUserIcon)
                                .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                        .addGap(18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(lblPassIcon)
                                .addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                        .addGap(35)
                        .addComponent(btnSignIn, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                        .addGap(40)
        );

        return panel;
    }

    private JLabel createIconLabel(String path) {
        return new JLabel(new ImageIcon(getClass().getResource(path)));
    }

    private void showLoading() {
        loadingDialog = new JDialog(this, false); // Non-modal
        loadingDialog.setUndecorated(true);
        loadingDialog.setSize(160, 90);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setBackground(new Color(0, 0, 0, 0)); // Transparent background for shadow effect

        // Outer panel (for shadow effect)
        JPanel outerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 50)); // subtle shadow
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        outerPanel.setOpaque(false);
        outerPanel.setLayout(new GridBagLayout());

        // Inner panel
        JPanel innerPanel = new JPanel(new BorderLayout(0, 10));
        innerPanel.setBackground(Color.WHITE);
        innerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        innerPanel.setPreferredSize(new Dimension(140, 70));
        innerPanel.setOpaque(true);

        // Modern spinner (progress bar)
        JProgressBar spinner = new JProgressBar();
        spinner.setIndeterminate(true);
        spinner.setBorderPainted(false);
        spinner.setPreferredSize(new Dimension(100, 6));
        spinner.setForeground(new Color(33, 150, 243)); // Material blue

        // Text label
        JLabel label = new JLabel("Signing in...", JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(new Color(80, 80, 80));

        innerPanel.add(label, BorderLayout.CENTER);
        innerPanel.add(spinner, BorderLayout.SOUTH);

        outerPanel.add(innerPanel);
        loadingDialog.add(outerPanel);

        loadingDialog.setAlwaysOnTop(true);
        loadingDialog.setVisible(true);
    }

    private void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dispose();
        }
    }

    private void configureSignInButton() {
        btnSignIn.setBackground(new Color(0, 123, 255));
        btnSignIn.setForeground(Color.WHITE);
        btnSignIn.setFont(new Font("Lucida Grande", Font.BOLD, 17));
        btnSignIn.setIcon(new ImageIcon(getClass().getResource("/icons/icon_login.png")));
        btnSignIn.setFocusTraversalKeysEnabled(true);
        btnSignIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSignIn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnSignIn.setOpaque(true);

        // Rounded corners + subtle shadow
        btnSignIn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 105, 217), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Hover effect
        btnSignIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSignIn.setBackground(new Color(0, 136, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSignIn.setBackground(new Color(0, 123, 255));
            }
        });

        btnSignIn.addActionListener(e -> onSignIn());
    }

    private void onSignIn() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, CommonConstant.WARN_EMPTY_FIELDS, getTitle(), JOptionPane.WARNING_MESSAGE);
            return;
        }

        SwingWorker<Company, Void> worker = new SwingWorker<Company, Void>() {
            @Override
            protected Company doInBackground() throws Exception {
                showLoading();
                CompanyController controller = new CompanyController();
                return controller.getCompany(username.toUpperCase(), CommonExtension.encryptPassword(txtPassword));
            }

            @Override
            protected void done() {
                hideLoading();
                try {
                    Company company = get();
                    if (company != null) {
                        new MainMenuView(company).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginView.this, CommonConstant.ERROR_LOGIN, getTitle(), JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    JOptionPane.showMessageDialog(LoginView.this, "An error occurred: " + e.getCause().getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }
}
