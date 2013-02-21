package jpkmn.exe.gui.inbox;

import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import jpkmn.exe.gui.GameWindow;
import jpkmn.exe.gui.JPokemonMenu;
import jpkmn.exe.gui.JPokemonMenuEntry;

public class InboxMenu extends JPokemonMenu {
  public InboxMenu(GameWindow parent) {
    super(parent);

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
  }

  public void addMessage(String... messages) {
    remove(_spacer);

    if (messages.length != 0)
      add(new MessagePanel(this, messages));

    add(_spacer);

    parent().doLayout();
  }

  public int width() {
    return 200;
  }

  public JPokemonMenuEntry[] entries() {
    return null;
  }

  @Override
  public boolean key(KeyEvent arg0) {
    return false;
  }

  private JPanel _spacer = new JPanel();
}