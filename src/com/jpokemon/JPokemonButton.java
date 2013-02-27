package com.jpokemon;

import javax.swing.JButton;

public class JPokemonButton extends JButton {
  public JPokemonButton(String s) {
    super(s);

    this.setFocusable(false);
  }

  private static final long serialVersionUID = 1L;
}
