package com.jpokemon.launcher;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.jpokemon.service.PlayerService;
import org.jpokemon.service.ServiceException;

import com.jpokemon.GameWindow;

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

    int id = -1;
    
    try {
      id = PlayerService.load(name);
    } catch (ServiceException e) {
      JOptionPane.showMessageDialog(_launcher, e.toString(), "LOGIN ERROR", JOptionPane.ERROR_MESSAGE);

      if (JOptionPane.showConfirmDialog(_launcher, "Would you like to start a new game?", "NEW GAME", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == JOptionPane.OK_OPTION) {
        String rivalName = JOptionPane.showInputDialog("What is your rival's name, again?", null);
        
        if (rivalName != null && !rivalName.isEmpty())
          id = PlayerService.create(name, rivalName);
        else
          return;
      }
      else
        return;
    }
    
    if (id == -1) {
      return;
    }
    _launcher.dispose();
    new GameWindow(id);
  }

  private Launcher _launcher;
  private static final long serialVersionUID = 1L;
}