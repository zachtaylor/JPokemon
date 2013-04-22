package com.jpokemon;

import javax.swing.Icon;
import javax.swing.JButton;

public class JPokemonButton extends JButton {
  public JPokemonButton(String s) {
    super(s);

  }

  public JPokemonButton(Icon icon) {
    super(icon);

    this.setFocusable(false);
  }

  private static final long serialVersionUID = 1L;
}
