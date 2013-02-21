package jpkmn.exe.gui;

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

  public abstract int width();
  
  public abstract boolean key(KeyEvent arg0);

  private GameWindow _parent;
}