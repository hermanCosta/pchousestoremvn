package com.pchouse.pchousestoremvn.views.modals;

import com.pchouse.pchousestoremvn.common.CommonExtension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CloseTillSummaryDialog extends JDialog {

    public JTextField txtTotalCash;
    public JTextField txtTotalCard;
    public JTextField txtTotalCombined;
    public JTextArea txtNotes;
    public JButton btnCloseTill;
    public JButton btnPrint;
    Window owner;

    public CloseTillSummaryDialog(Window owner) {
        super(owner, "Close Till Summary", ModalityType.APPLICATION_MODAL);
        initDialogComponents();
        owner = owner;
    }

    private void initDialogComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());

        JPanel summaryPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTotalCash = new JTextField(10);
        txtTotalCard = new JTextField(10);
        txtTotalCombined = new JTextField(10);
        txtNotes = new JTextArea(3, 50);

        txtTotalCash.setEditable(false);
        txtTotalCard.setEditable(false);
        txtTotalCombined.setEditable(false);

        // First row: Cash, Card, Combined
        gbc.gridx = 0;
        gbc.gridy = 0;
        summaryPanel.add(new JLabel("Cash Total:"), gbc);
        gbc.gridx = 1;
        summaryPanel.add(txtTotalCash, gbc);
        gbc.gridx = 2;
        summaryPanel.add(new JLabel("Card Total:"), gbc);
        gbc.gridx = 3;
        summaryPanel.add(txtTotalCard, gbc);
        gbc.gridx = 4;
        summaryPanel.add(new JLabel("Combined Total:"), gbc);
        gbc.gridx = 5;
        summaryPanel.add(txtTotalCombined, gbc);

        // Notes label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 6;
        summaryPanel.add(new JLabel("Closing Notes:"), gbc);

        // Notes textarea
        gbc.gridy = 2;
        summaryPanel.add(new JScrollPane(txtNotes), gbc);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonsPanel.setPreferredSize(new Dimension(1000, 50));

        btnCloseTill = new JButton("Close Till", CommonExtension.loadIcon("/icons/icon_save.png"));
        btnCloseTill.setBackground(new Color(21, 76, 121));
        btnCloseTill.setForeground(Color.WHITE);

        btnPrint = new JButton("Print", CommonExtension.loadIcon("/icons/icon_print.png"));
        btnPrint.setBackground(new Color(21, 76, 121));
        btnPrint.setForeground(Color.WHITE);

        buttonsPanel.add(btnCloseTill);
        // Uncomment if needed
        //buttonsPanel.add(btnPrint);

        gbc.gridy = 3;
        summaryPanel.add(buttonsPanel, gbc);

        panel.add(summaryPanel, BorderLayout.CENTER);
        add(panel);

        pack();
        setLocationRelativeTo(owner);
    }
}
