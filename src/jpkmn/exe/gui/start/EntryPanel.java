package jpkmn.exe.gui.start;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class EntryPanel extends JPanel {
  public EntryPanel(StartMenu s, StartEntryValue v) {
    _parent = s;
    _value = v;

    add(new JLabel(v.toString()));

    active(false);
  }

  public void active(boolean a) {
    setBackground(a ? ON : OFF);
  }

  public void action() {
    switch (_value) {
    case UPGRADE:
      _parent.onUpgrade();
      break;
    case SAVE:
      _parent.onSave();
      break;
    case EXIT:
      _parent.onExit();
      break;
    case QUIT:
      _parent.onQuit();
    }
  }

  protected StartMenu _parent;

  private StartEntryValue _value;
  private static final Color ON = Color.cyan, OFF = Color.gray;
  private static final long serialVersionUID = 1L;
}