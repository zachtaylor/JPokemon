package jpkmn.exe.gui.start;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;

import jpkmn.exceptions.DialogCancelException;
import jpkmn.exceptions.ServiceException;
import jpkmn.exe.gui.GameWindow;
import jpkmn.exe.gui.JPokemonView;
import jpkmn.game.service.PlayerService;

public class StartView extends JPokemonView implements KeyListener {
  public StartView(GameWindow g) {
    _gameWindow = g;
    _select = 0;
    _entries = new EntryPanel[StartEntryValue.values().length];
    _dimension = new Dimension(100, 35 * _entries.length);

    for (int i = 0; i < StartEntryValue.values().length; ++i)
      _entries[i] = new EntryPanel(this, StartEntryValue.valueOf(i));

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    for (EntryPanel entry : _entries)
      add(entry);

    select(_select);

    setFocusable(true);
    setFocusTraversalKeysEnabled(false);
    addKeyListener(this);
  }

  public void onUp() {
    select(_select - 1);
  }

  public void onDown() {
    select(_select + 1);
  }

  public void onEnter() {
    _entries[_select].action();
  }

  public void onUpgrade() {
    try {
      _gameWindow.showUpgrade(_gameWindow.graphics().getPartyIndex("upgrade"));
    } catch (DialogCancelException e) {
      return;
    }
  }

  public void onSave() {
    try {
      PlayerService.savePlayer(_gameWindow.playerID());
      _gameWindow.showMain();
    } catch (ServiceException e) {
      e.printStackTrace();
      return;
    }
  }

  public void onExit() {
    _gameWindow.showMain();
  }

  public void onQuit() {
    _gameWindow.dispose();
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

  @Override
  public void refresh() {
  }

  @Override
  public Dimension dimension() {
    return _dimension;
  }

  @Override
  public void keyPressed(KeyEvent arg0) {
    int keyCode = arg0.getKeyCode();

    if (keyCode == 38)
      onUp();
    else if (keyCode == 40)
      onDown();
    else if (keyCode == 10)
      onEnter();
  }

  @Override
  public void keyReleased(KeyEvent arg0) {
  }

  @Override
  public void keyTyped(KeyEvent arg0) {
  }

  private int _select;
  private EntryPanel[] _entries;
  private Dimension _dimension;
  private GameWindow _gameWindow;
  private static final long serialVersionUID = 1L;
}