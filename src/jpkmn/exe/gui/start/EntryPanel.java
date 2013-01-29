package jpkmn.exe.gui.start;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class EntryPanel extends JPanel {
  public EntryPanel(StartView s, StartEntryValue v) {
    _startView = s;
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
      _startView.onUpgrade();
      break;
    case SAVE:
      _startView.onSave();
      break;
    case EXIT:
      _startView.onExit();
      break;
    case QUIT:
      _startView.onQuit();
    }
  }

  protected StartView _startView;

  private StartEntryValue _value;
  private static final Color ON = Color.cyan, OFF = Color.gray;
  private static final long serialVersionUID = 1L;
}