package com.jpokemon.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MartButton extends JButton implements ActionListener {
  public MartButton(WorldView view, int areaID) {
    super("Pokemon Mart");

    setFocusable(false);
    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    System.out.println("You clicked Mart");
    // TODO : Generate battle
  }

  private static final long serialVersionUID = 1L;
}