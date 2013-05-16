package com.jpokemon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import org.jpokemon.service.ImageService;
import org.jpokemon.service.PlayerService;
import org.jpokemon.service.ServiceException;
import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.battle.BattleView;
import com.jpokemon.inbox.InboxMenu;
import com.jpokemon.overworld.OverworldView;
import com.jpokemon.start.StartMenu;
import com.jpokemon.store.StoreView;
import com.jpokemon.upgrade.UpgradeView;

public class GameWindow extends JFrame implements KeyListener {
  public GameWindow(String playerID) {
    _playerID = playerID;
    _inbox = new InboxMenu(this);
    _start = new StartMenu(this);

    _battle = new BattleView(this);
    _store = new StoreView(this);
    _upgrade = new UpgradeView(this);
    _world = new OverworldView(this);

    _dialogs = new JPokemonDialog(this);

    // setResizable(false);
    setLayout(new BorderLayout());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setIconImage(ImageService.find("main-icon").getImage());

    _menu = _inbox;
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
    try {
      JSONObject data = PlayerService.pull(_newDataRequest);
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

  private String _playerID;
  private JPokemonDialog _dialogs;
  private JSONObject _newDataRequest;
  private JPokemonMenu _menu, _start, _inbox;
  private JPokemonView _active, _battle, _upgrade, _world, _store;

  private static final long serialVersionUID = 1L;
}