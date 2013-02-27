package com.jpokemon;

import java.awt.Color;

import javax.swing.JPanel;

public abstract class JPokemonMenuEntry extends JPanel {
  private static final Color ON = Color.cyan, OFF = Color.gray;

  public JPokemonMenuEntry(JPokemonMenu parent) {
    _parent = parent;

    active(false);
  }

  public JPokemonMenu parent() {
    return _parent;
  }

  public void active(boolean a) {
    setBackground(a ? ON : OFF);
  }

  public abstract void action();

  private JPokemonMenu _parent;

  private static final long serialVersionUID = 1L;
}