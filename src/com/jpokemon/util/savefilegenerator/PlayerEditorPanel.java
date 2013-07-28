package com.jpokemon.util.savefilegenerator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PlayerEditorPanel extends JPanel {
  public PlayerEditorPanel(SaveGeneratorWindow sgw) {
    parent = sgw;
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(200, 300));

    JPanel namePanel = new JPanel();
    namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
    namePanel.add(new JLabel("Username"));
    nameField.setMinimumSize(new Dimension(100, 16));
    nameField.setMaximumSize(new Dimension(100, 16));
    nameField.addActionListener(new SetNameHandler());
    namePanel.add(nameField);
    add(namePanel);

    JPanel cashPanel = new JPanel();
    cashPanel.setLayout(new BoxLayout(cashPanel, BoxLayout.X_AXIS));
    cashPanel.add(new JLabel("Cash"));
    cashField.setMinimumSize(new Dimension(100, 16));
    cashField.setMaximumSize(new Dimension(100, 16));
    cashField.addActionListener(new SetCashHandler());
    cashPanel.add(cashField);
    add(cashPanel);

    JPanel badgePanel = new JPanel();
    badgePanel.setLayout(new BoxLayout(badgePanel, BoxLayout.X_AXIS));
    badgePanel.add(new JLabel("Badges"));
    badgeField.setMinimumSize(new Dimension(100, 16));
    badgeField.setMaximumSize(new Dimension(100, 16));
    badgeField.addActionListener(new SetBadgeHandler());
    badgePanel.add(badgeField);
    add(badgePanel);
  }

  private SaveGeneratorWindow parent;
  private JTextField nameField = new JTextField();
  private JTextField cashField = new JTextField();
  private JTextField badgeField = new JTextField();

  public static final long serialVersionUID = 1L;

  private class SetNameHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent arg0) {
      parent.loadOrSetName(nameField.getText());
      parent.refresh();
    }
  }

  private class SetCashHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent arg0) {
      parent.getPlayer().setCash(Integer.parseInt(cashField.getText()));
      parent.refresh();
    }
  }

  private class SetBadgeHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent arg0) {
      parent.getPlayer().setBadgeCount(Integer.parseInt(badgeField.getText()));
      parent.refresh();
    }
  }
}