package com.jpokemon.launcher;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.jpokemon.service.PlayerService;
import org.jpokemon.service.ServiceException;

public class PlayButton extends JButton implements ActionListener {
  public PlayButton(Launcher l) {
    super("Play");

    _launcher = l;

    setFocusable(false);
    setBorderPainted(false);
    setBounds(550, 60, 110, 30);
    setBackground(new Color(206, 77, 77));

    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    String name = JOptionPane.showInputDialog(_launcher, "Please enter your username", "LOGIN", JOptionPane.QUESTION_MESSAGE);

    if (name == null)
      return;

    try {
      PlayerService.load(name);
    } catch (ServiceException e) {
      JOptionPane.showMessageDialog(_launcher, e.toString(), "LOGIN ERROR", JOptionPane.ERROR_MESSAGE);

      if (JOptionPane.showConfirmDialog(_launcher, "Would you like to start a new game?", "NEW GAME", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == JOptionPane.OK_OPTION) {
        PlayerService.create(name);
      }
      else
        return;
    }

    _launcher.dispose();
  }

  private Launcher _launcher;
  private static final long serialVersionUID = 1L;
}