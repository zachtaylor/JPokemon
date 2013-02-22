package com.jpokemon;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import org.json.JSONObject;

public abstract class JPokemonView extends JPanel {
  public JPokemonView(GameWindow parent) {
    _parent = parent;
    setFocusable(true);
  }

  public GameWindow parent() {
    return _parent;
  }

  public final void refresh() {
    parent().refresh();
  }

  public JPanel spacer() {
    return new JPanel();
  }

  public boolean hasDependentMenu() {
    return false;
  }

  public JPokemonMenu dependentMenu() {
    return null;
  }

  public abstract Dimension dimension();

  public abstract void update(JSONObject data);

  public abstract boolean key(KeyEvent arg0);

  private GameWindow _parent;

  private static final long serialVersionUID = 1L;
}