package com.jpokemon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.ServiceException;
import org.jpokemon.manager.component.ImageService;
import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.battle.BattleView;
import com.jpokemon.overworld.OverworldView;
import com.jpokemon.store.StoreView;
import com.jpokemon.upgrade.UpgradeView;

public class GameWindow extends JFrame implements KeyListener {
  public GameWindow(String playerID) {
    _playerID = playerID;

    _battle = new BattleView(this);
    _store = new StoreView(this);
    _upgrade = new UpgradeView(this);
    _world = new OverworldView(this);

    _dialogs = new JPokemonDialog(this);

    // setResizable(false);
    setLayout(new BorderLayout());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setIconImage(ImageService.find("main-icon").getImage());

    _active = _upgrade;

    _newDataRequest = new JSONObject();
    try {
      _newDataRequest.put("id", _playerID);
    } catch (JSONException e) {
    }

    addKeyListener(this);
    setFocusable(true);
    setVisible(true);

    refresh();
  }

  public String playerID() {
    return _playerID;
  }

  public JPokemonDialog dialogs() {
    return _dialogs;
  }

  public void refresh() {
    try {
      JSONObject data = PlayerManager.getDataRequest(_newDataRequest);
      _dialogs.showMessages(data.getJSONArray("messages"));

      if (data.getString("state").equals("battle")) {
        _battle.update(data.getJSONObject("battle"));
        show(_battle);
      }
      else if (data.getString("state").equals("upgrade")) {
        _upgrade.update(data.getJSONObject("upgrade"));
        show(_upgrade);
      }
      else if (data.getString("state").equals("overworld")) {
        _world.update(data.getJSONObject("overworld"));
        show(_world);
      }
      else if (data.getString("state").equals("store")) {
        _store.update(data.getJSONObject("store"));
        show(_store);
      }

    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void align() {
    Dimension d = _active.dimension();

    int width = d.width, height = d.height;

    if (_active.menu() != null) {
      width += _active.menu().width();
    }

    d.setSize(width, height);
    setSize(d);

    validate();
    repaint();
  }

  public void keyPressed(KeyEvent arg0) {
    if (arg0.getKeyCode() == 27) {
      toggleMenu();
      return;
    }

    if (_menu != null && _menu.key(arg0)) {
      return;
    }

    if (_active.key(arg0)) {
      return;
    }

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
      hideMenu();

      align();
    }
  }

  private void showMenu() {
    _menu = _active.menu();

    if (_menu != null) {
      add(_menu, BorderLayout.EAST);
    }
  }

  private void hideMenu() {
    if (_menu != null) {
      remove(_menu);
    }

    _menu = null;
  }

  private void toggleMenu() {
    if (_menu == null) {
      showMenu();
    }
    else {
      hideMenu();
    }

    align();
  }

  private String _playerID;
  private JPokemonMenu _menu;
  private JPokemonDialog _dialogs;
  private JSONObject _newDataRequest;
  private JPokemonView _active, _battle, _upgrade, _world, _store;

  private static final long serialVersionUID = 1L;
}