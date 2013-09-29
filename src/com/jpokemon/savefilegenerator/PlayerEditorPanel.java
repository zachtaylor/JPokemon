package com.jpokemon.savefilegenerator;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jpokemon.savefilegenerator.input.SetBadgeHandler;
import com.jpokemon.savefilegenerator.input.SetCashHandler;
import com.jpokemon.savefilegenerator.input.SetNameHandler;

public class PlayerEditorPanel extends JPanel {
  public PlayerEditorPanel(SaveGeneratorWindow sgw) {
    super();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(200, 300));

    JPanel namePanel = new JPanel();
    namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
    namePanel.add(new JLabel("Username"));
 
    JTextField nameField = new JTextField();
    nameField.setMinimumSize(new Dimension(100, 16));
    nameField.setMaximumSize(new Dimension(100, 16));
    nameField.getDocument().addDocumentListener(new SetNameHandler(sgw, nameField));
    namePanel.add(nameField);
    add(namePanel);

    JPanel cashPanel = new JPanel();
    cashPanel.setLayout(new BoxLayout(cashPanel, BoxLayout.X_AXIS));
    cashPanel.add(new JLabel("Cash"));

    JTextField cashField = new JTextField();
    cashField.setMinimumSize(new Dimension(100, 16));
    cashField.setMaximumSize(new Dimension(100, 16));
    cashField.getDocument().addDocumentListener(new SetCashHandler(sgw, cashField));
    cashPanel.add(cashField);
    add(cashPanel);

    JPanel badgePanel = new JPanel();
    badgePanel.setLayout(new BoxLayout(badgePanel, BoxLayout.X_AXIS));
    badgePanel.add(new JLabel("Badges"));

    JTextField badgeField = new JTextField();
    badgeField.setMinimumSize(new Dimension(100, 16));
    badgeField.setMaximumSize(new Dimension(100, 16));
    badgeField.getDocument().addDocumentListener(new SetBadgeHandler(sgw, badgeField));
    badgePanel.add(badgeField);
    add(badgePanel);
  }

  public static final long serialVersionUID = 1L;
}