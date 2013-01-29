package jpkmn.exe.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

public abstract class JPokemonView extends JPanel {
  public abstract void refresh();

  public abstract Dimension dimension();

  public JPanel spacer() {
    return new JPanel();
  }

  private static final long serialVersionUID = 1L;
}