package com.pchouse.pchousestoremvn.common;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class PasswordPanel extends JPanel {

    private final JPasswordField passwordField = new JPasswordField(12);
    private boolean gainedFocusBefore;

    /**
     * "Hook" method that causes the JPasswordField to request focus the first
     * time this method is called.
     */
    void gainedFocus() {
        if (!gainedFocusBefore) {
            gainedFocusBefore = true;
            passwordField.requestFocusInWindow();
        }
    }

    public PasswordPanel() {
        super(new FlowLayout());

        add(new JLabel("Enter Password: "));
        add(passwordField);
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }
}
