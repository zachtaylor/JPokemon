package com.jpokemon;

import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public abstract class JPokemonMenu extends JPanel {
  public JPokemonMenu(GameWindow parent) {
    _parent = parent;
  }

  public GameWindow parent() {
    return _parent;
  }

  public void refresh() {
    parent().refresh();
  }

  public int select() {
    return _select;
  }

  public void select(int s) {
    if (entries().length == 0)
      return;

    entries()[_select].active(false);

    _select = s;

    if (_select < 0)
      _select += entries().length;
    else
      _select %= entries().length;

    entries()[_select].active(true);
  }

  public abstract int width();

  public abstract JPokemonMenuEntry[] entries();

  public boolean key(KeyEvent arg0) {
    int keyCode = arg0.getKeyCode();

    if (keyCode == 10)
      entries()[_select].action();
    else if (keyCode == 40)
      select(_select + 1);
    else if (keyCode == 38)
      select(_select - 1);
    else
      return false;

    return true;
  }

  private int _select;
  private GameWindow _parent;

  private static final long serialVersionUID = 1L;
}