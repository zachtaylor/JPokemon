package jpkmn.exe.gui.start;

import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;

import jpkmn.exceptions.ServiceException;
import jpkmn.exe.gui.GameWindow;
import jpkmn.exe.gui.JPokemonMenu;
import jpkmn.game.service.PlayerService;

public class StartMenu extends JPokemonMenu {
  public StartMenu(GameWindow g) {
    super(g);

    _select = 0;
    _entries = new EntryPanel[StartEntryValue.values().length];

    for (int i = 0; i < StartEntryValue.values().length; ++i)
      _entries[i] = new EntryPanel(this, StartEntryValue.valueOf(i));

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    for (EntryPanel entry : _entries)
      add(entry);

    select(_select);

    setFocusable(true);
    setFocusTraversalKeysEnabled(false);
  }

  public void onUpgrade() {
    // try {
    // _gameWindow.showUpgrade(_gameWindow.graphics().getPartyIndex("upgrade"));
    // } catch (DialogCancelException e) {
    // return;
    // }
  }

  public void onSave() {
    try {
      PlayerService.save(parent().playerID());
      refresh();
    } catch (ServiceException e) {
      e.printStackTrace();
      return;
    }
  }

  public void onExit() {
    parent().showInbox();
  }

  public void onQuit() {
    parent().dispose();
  }

  public void select(int s) {
    _entries[_select].active(false);

    _select = s;

    if (_select < 0)
      _select += _entries.length;
    else
      _select %= _entries.length;

    _entries[_select].active(true);
  }

  public int width() {
    return 100;
  }

  @Override
  public boolean key(KeyEvent arg0) {
    int keyCode = arg0.getKeyCode();

    if (keyCode == 10)
      _entries[_select].action();
    else if (keyCode == 40)
      select(_select + 1);
    else if (keyCode == 38)
      select(_select - 1);
    else
      return false;

    return true;
  }

  private int _select;
  private EntryPanel[] _entries;
  private static final long serialVersionUID = 1L;
}