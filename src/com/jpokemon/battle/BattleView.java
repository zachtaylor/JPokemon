package com.jpokemon.battle;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jpokemon.service.BattleService;
import org.jpokemon.service.ImageService;
import org.jpokemon.service.ServiceException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    int moveIndex;
    String enemySlotID;

    enableButtons(false);

    try {
      moveIndex = getMoveIndex();
      if (moveIndex == -1) {
        enableButtons(true);
        return;
      }

      enemySlotID = getMoveTarget(moveIndex);
      if (enemySlotID == null) {
        enableButtons(true);
        return;
      }
    } catch (Exception e) {
      enableButtons(true);
      return;
    }

    JSONObject request = new JSONObject();
    try {
      request.put("turn", "ATTACK");
      request.put("id", parent().playerID());
      request.put("target", enemySlotID);
      request.put("move", moveIndex);
    } catch (JSONException e) {
      e.printStackTrace();
      enableButtons(true);
      return;
    }

    submitTurn(request);
  }

  private void swap() {
    int swapIndex;
    enableButtons(false);

    try {
      swapIndex = getSwapIndex();
      if (swapIndex == -1) {
        enableButtons(true);
        return;
      }
    } catch (Exception e) {
      enableButtons(true);
      return;
    }

    JSONObject request = new JSONObject();
    try {
      request.put("turn", "SWAP");
      request.put("id", parent().playerID());
      request.put("target", parent().playerID());
      request.put("swap", swapIndex);
    } catch (JSONException e) {
      e.printStackTrace();
      enableButtons(true);
      return;
    }

    submitTurn(request);
  }

  private void item() {
    int itemID;
    enableButtons(false);

    try {
      itemID = getItemChoice();
      if (itemID == -1) {
        enableButtons(true);
        return;
      }
    } catch (Exception e) {
      enableButtons(true);
      return;
    }

    String targetID = parent().playerID();// TODO : target choice
    int targetIndex = 0;

    JSONObject request = new JSONObject();
    try {
      request.put("turn", "ITEM");
      request.put("id", parent().playerID());
      request.put("target", targetID);
      request.put("target_index", targetIndex);
      request.put("item", itemID);
    } catch (JSONException e) {
      e.printStackTrace();
      enableButtons(true);
      return;
    }

    submitTurn(request);
  }

  private void run() {
    enableButtons(false);

    JSONObject request = new JSONObject();
    try {
      request.put("turn", "RUN");
      request.put("id", parent().playerID());
      request.put("target", parent().playerID());
    } catch (JSONException e) {
      e.printStackTrace();
      enableButtons(true);
      return;
    }

    submitTurn(request);
  }

  private void submitTurn(JSONObject request) {
    try {
      BattleService.turn(request);
      refresh();
    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }

  private void enableButtons(boolean enable) {
    _fightButton.setEnabled(enable);
    _itemButton.setEnabled(enable);
    _swapButton.setEnabled(enable);
    _runButton.setEnabled(enable);
  }

  private int getMoveIndex() throws JSONException {
    String prompt = null;
    ImageIcon image = null;
    List<String> move_names = new ArrayList<String>();

    try {
      JSONObject leader = _trainerData.getJSONObject("leader");
      JSONArray moves = leader.getJSONArray("moves");

      prompt = "Select a move for " + leader.getString("name");
      image = ImageService.find("pkmn/" + leader.getInt("number"));
      for (int i = 0; i < moves.length(); i++)
        move_names.add(moves.getJSONObject(i).getString("name"));
    } catch (JSONException e) {
      return -1;
    }

    int answer = JOptionPane.showOptionDialog(parent(), prompt, "MOVE CHOICE", 0, 0, image, move_names.toArray(), null);

    return answer;
  }

  private String getMoveTarget(int moveIndex) throws JSONException {
    String move = null;
    ImageIcon image = null;
    List<String> slotNames = new ArrayList<String>();
    List<String> slotIds = new ArrayList<String>();

    try {
      move = _trainerData.getJSONObject("leader").getJSONArray("moves").getJSONObject(moveIndex).getString("name");
      image = ImageService.find("pkmn/" + _trainerData.getJSONObject("leader").getInt("number"));

      for (int i = 0; i < _enemyTeams.length(); i++) {
        for (int j = 0; j < _enemyTeams.getJSONArray(i).length(); j++) {
          slotNames.add(_enemyTeams.getJSONArray(i).getJSONObject(j).getJSONObject("leader").getString("name"));
          slotIds.add(_enemyTeams.getJSONArray(i).getJSONObject(j).getString("id"));
        }
      }
    } catch (JSONException e) {
    }

    String answer = slotIds.get(0);

    if (slotIds.size() > 1)
      answer = slotIds.get(JOptionPane.showOptionDialog(parent(), "Select a target for " + move, "MOVE CHOICE", 0, 0, image, slotNames.toArray(), null));

    return answer;
  }

  private int getSwapIndex() throws JSONException {
    ImageIcon image = null;
    List<String> names = new ArrayList<String>();

    try {
      JSONObject leader = _trainerData.getJSONObject("leader");
      JSONArray party = _data.getJSONArray("pokemon");

      image = ImageService.find("pkmn/" + leader.getInt("number"));
      for (int i = 1; i < party.length(); i++) {
        names.add(party.getJSONObject(i).getString("name"));
      }
    } catch (JSONException e) {
    }

    int answer = JOptionPane.showOptionDialog(parent(), "Select a Pokemon", "SWAP CHOICE", 0, JOptionPane.QUESTION_MESSAGE, image, names.toArray(), null);

    return answer;
  }

  private int getItemChoice() throws JSONException {
    JSONArray items = _trainerData.getJSONArray("bag");
    JSONObject itemType = null;

    List<String> itemTypes = new ArrayList<String>();
    List<ImageIcon> itemsInType = new ArrayList<ImageIcon>();

    for (int i = 0; i < items.length(); i++) {
      itemTypes.add(items.getJSONObject(i).getString("type"));
    }

    int itemTypeChoice = JOptionPane.showOptionDialog(parent(), "Select an item to use", "ITEM CHOICE", 0, JOptionPane.QUESTION_MESSAGE, null,
        itemTypes.toArray(), null);

    if (itemTypeChoice == -1)
      return -1;
    else
      itemType = items.getJSONObject(itemTypeChoice);

    for (int i = 0; i < itemType.getJSONArray("items").length(); i++) {
      String itemName = itemType.getJSONArray("items").getJSONObject(i).getString("name");
      itemsInType.add(ImageService.item(itemType.getString("type"), itemName));
    }

    int itemChoice = JOptionPane.showOptionDialog(parent(), "Select an item to use", "ITEM CHOICE", 0, JOptionPane.QUESTION_MESSAGE, null,
        itemsInType.toArray(), null);

    if (itemChoice == -1)
      return -1;

    return itemType.getJSONArray("items").getJSONObject(itemChoice).getInt("id");
  }

  private JPanel _enemies, _user;
  private JSONArray _enemyTeams;
  private JSONObject _data, _trainerData;
  private JButton _fightButton, _itemButton, _swapButton, _runButton;
  private static final long serialVersionUID = 1L;
}