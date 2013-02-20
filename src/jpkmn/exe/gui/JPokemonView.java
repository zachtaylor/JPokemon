package jpkmn.exe.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.json.JSONObject;

public abstract class JPokemonView extends JPanel {
  public JPokemonView(GameWindow parent) {
    _parent = parent;
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

  public abstract Dimension dimension();

  public abstract void update(JSONObject data);

  private GameWindow _parent;

  private static final long serialVersionUID = 1L;
}