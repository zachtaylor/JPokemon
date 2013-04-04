package com.jpokemon.battle;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jpokemon.service.BattleService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.DialogCancelException;
import com.jpokemon.GameWindow;
import com.jpokemon.JPokemonButton;
import com.jpokemon.JPokemonView;

public class BattleView extends JPokemonView {
  public BattleView(GameWindow parent) {
    super(parent);

    _user = new JPanel();
    _enemies = new JPanel();
    JPanel userAndButtons = new JPanel();
    JPanel buttonPanel = new JPanel();

    _fightButton = new JPokemonButton("FIGHT");
    buttonPanel.add(_fightButton);
    _fightButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fight();
      }
    });

    _itemButton = new JPokemonButton("ITEM");
    buttonPanel.add(_itemButton);
    _itemButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        item();
      }
    });

    _swapButton = new JPokemonButton("SWAP");
    buttonPanel.add(_swapButton);
    _swapButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        swap();
      }
    });

    _runButton = new JPokemonButton("RUN");
    buttonPanel.add(_runButton);
    _runButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        run();
      }
    });

    userAndButtons.add(_user);
    userAndButtons.add(buttonPanel);
    userAndButtons.add(spacer());

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    _user.setLayout(new BoxLayout(_user, BoxLayout.Y_AXIS));
    _enemies.setLayout(new BoxLayout(_enemies, BoxLayout.X_AXIS));
    userAndButtons.setLayout(new BoxLayout(userAndButtons, BoxLayout.Y_AXIS));

    add(userAndButtons);
    add(_enemies);
  }

  public void update(JSONObject data) {
    _data = data;

    _user.removeAll();
    _enemies.removeAll();

    try {
      _trainerData = _data.getJSONObject("player");
      _user.add(new PartyPanel(_trainerData, true));

      _enemyTeams = _data.getJSONArray("enemies");
      for (int i = 0; i < _enemyTeams.length(); i++) {
        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BoxLayout(teamPanel, BoxLayout.Y_AXIS));

        JSONArray teamData = _enemyTeams.getJSONArray(i);
        for (int j = 0; j < teamData.length(); j++)
          teamPanel.add(new PartyPanel(teamData.getJSONObject(j), false));

        _enemies.add(teamPanel);
      }

    } catch (JSONException e) {
      e.printStackTrace();
      return;
    }

    enableButtons(true);
    validate();
  }

  public Dimension dimension() {
    return new Dimension(625, 200);
  }

  public boolean key(KeyEvent arg0) {
    return false;
  }

  private void fight() {
    int moveIndex, enemySlotID;

    enableButtons(false);

    try {
      moveIndex = parent().dialogs().getMoveIndex();
      enemySlotID = parent().dialogs().getMoveTarget(_enemyTeams, moveIndex);
    } catch (Exception e) {
      if (!(e instanceof DialogCancelException))
        e.printStackTrace();

      enableButtons(true);
      return;
    }

    JSONObject json = new JSONObject();
    try {
      json.put("turn", "ATTACK");
      json.put("trainer", parent().playerID());
      json.put("target", enemySlotID);
      json.put("move", moveIndex);
    } catch (JSONException e) {
      e.printStackTrace();
      enableButtons(true);
      return;
    }

    BattleService.turn(json);
    refresh();
  }

  private void swap() {
    int swapIndex;
    enableButtons(false);

    try {
      swapIndex = parent().dialogs().getSwapIndex();
    } catch (Exception e) {
      if (!(e instanceof DialogCancelException))
        e.printStackTrace();

      enableButtons(true);
      return;
    }

    JSONObject json = new JSONObject();
    try {
      json.put("turn", "SWAP");
      json.put("trainer", parent().playerID());
      json.put("target", parent().playerID());
      json.put("swap", swapIndex);
    } catch (JSONException e) {
      e.printStackTrace();
      enableButtons(true);
      return;
    }

    BattleService.turn(json);
    refresh();
  }

  private void item() {
    int itemID;
    enableButtons(false);

    try {
      itemID = parent().dialogs().getItemChoice();
    } catch (Exception e) {
      if (!(e instanceof DialogCancelException))
        e.printStackTrace();

      enableButtons(true);
      return;
    }

    int targetID = 0, targetIndex = 0; // TODO : target choice

    JSONObject json = new JSONObject();
    try {
      json.put("turn", "ITEM");
      json.put("trainer", parent().playerID());
      json.put("target", targetID);
      json.put("target_index", targetIndex);
      json.put("item", itemID);
    } catch (JSONException e) {
      e.printStackTrace();
      enableButtons(true);
      return;
    }

    BattleService.turn(json);
    refresh();
  }

  private void run() {
    enableButtons(false);

    JSONObject json = new JSONObject();
    try {
      json.put("turn", "RUN");
      json.put("trainer", parent().playerID());
      json.put("target", parent().playerID());
    } catch (JSONException e) {
      e.printStackTrace();
      enableButtons(true);
      return;
    }

    BattleService.turn(json);
    refresh();
  }

  private void enableButtons(boolean enable) {
    _fightButton.setEnabled(enable);
    _itemButton.setEnabled(enable);
    _swapButton.setEnabled(enable);
    _runButton.setEnabled(enable);
  }

  private JPanel _enemies, _user;
  private JSONArray _enemyTeams;
  private JSONObject _data, _trainerData;
  private JButton _fightButton, _itemButton, _swapButton, _runButton;
  private static final long serialVersionUID = 1L;
}