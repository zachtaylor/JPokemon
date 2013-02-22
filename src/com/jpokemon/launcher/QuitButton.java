package com.jpokemon.launcher;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class QuitButton extends JButton implements ActionListener {
  public QuitButton(Launcher l) {
    super("Quit");

    _launcher = l;

    setFocusable(false);
    setBorderPainted(false);
    setBounds(550, 140, 110, 30);
    setBackground(new Color(83, 83, 221));

    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    _launcher.dispose();
  }

  private Launcher _launcher;
  private static final long serialVersionUID = 1L;
}