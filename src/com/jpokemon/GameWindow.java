package com.jpokemon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import org.jpokemon.service.ImageService;
import org.jpokemon.service.PlayerService;
import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.battle.BattleView;
import com.jpokemon.inbox.InboxMenu;
import com.jpokemon.pokemon.PokemonView;
import com.jpokemon.start.StartMenu;

public class GameWindow extends JFrame implements KeyListener {
  public GameWindow(int playerID) {
    _playerID = playerID;
    _inbox = new InboxMenu(this);
    _start = new StartMenu(this);
    _battle = new BattleView(this);
    _dialogs = new JPokemonDialog(this);
    _upgrade = new PokemonView(this);

    // setResizable(false);
    setLayout(new BorderLayout());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setIconImage(ImageService.find("main-icon").getImage());

    _menu = _inbox;
    _active = _upgrade;

    addKeyListener(this);
    setFocusable(true);
    setVisible(true);

    align();
  }

  public int playerID() {
    return _playerID;
  }

  public JPokemonDialog dialogs() {
    return _dialogs;
  }

  public InboxMenu inbox() {
    return (InboxMenu) _inbox;
  }

  public void showStart() {
    show(_start);
  }

  public void closeStart() {
    if (_active.hasDependentMenu())
      show(_active.dependentMenu());
    else
      show(_inbox);
  }

  public void refresh() {
    JSONObject data = PlayerService.pull(_playerID);

    try {
      _dialogs.setData(data.getJSONObject("player"));

      if (data.getString("state").equals("BATTLE")) {
        _battle.update(data.getJSONObject("battle"));
        show(_battle);
      }
      else if (data.getString("state").equals("UPGRADE")) {
        _upgrade.update(data.getJSONObject("upgrade"));
        show(_upgrade);
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void align() {
    Dimension d = _active.dimension();
    d.setSize(d.width + _menu.width(), d.height);
    setSize(d);

    validate();
    repaint();
  }

  public void keyPressed(KeyEvent arg0) {
    if (_menu.key(arg0))
      return;

    if (_active.key(arg0))
      return;

    else if (arg0.getKeyCode() == 27)
      if (_menu != _start)
        showStart();
      else
        closeStart();
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  private void show(JPokemonView view) {
    if (view != _active || _active == null) {
      if (_active != null)
        remove(_active);

      _active = view;
      add(_active, BorderLayout.CENTER);

      if (_active.hasDependentMenu())
        show(_active.dependentMenu());

      align();
    }
  }

  private void show(JPokemonMenu menu) {
    if (_menu != null)
      remove(_menu);

    _menu = menu;
    add(_menu, BorderLayout.EAST);

    align();
  }

  private int _playerID;
  private JPokemonDialog _dialogs;
  private JPokemonMenu _menu, _start, _inbox;
  private JPokemonView _active, _battle, _upgrade;

  private static final long serialVersionUID = 1L;
}