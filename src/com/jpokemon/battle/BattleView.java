package com.jpokemon.battle;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.ServiceException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.GameWindow;
import com.jpokemon.JPokemonButton;
import com.jpokemon.JPokemonMenu;
import com.jpokemon.JPokemonView;
import com.jpokemon.util.ui.ImageLoader;

public class BattleView extends JPokemonView {
  public BattleView(GameWindow parent) {
    super(parent);

    _teams = new JPanel();

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    _teams.setLayout(new BoxLayout(_teams, BoxLayout.X_AXIS));

    add(_teams);
  }

  public void update(JSONObject data) {
    _data = data;

    _teams.removeAll();

    try {
      _trainerData = _data.getJSONObject("player");

      _enemyTeams = _data.getJSONArray("teams");
      for (int enemyTeamIndex = 0; enemyTeamIndex < _enemyTeams.length(); enemyTeamIndex++) {
        JSONArray enemyTeamJSON = _enemyTeams.getJSONArray(enemyTeamIndex);

        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BoxLayout(teamPanel, BoxLayout.Y_AXIS));

        teamPanel.add(spacer());
        for (int j = 0; j < enemyTeamJSON.length(); j++) {
          JSONObject enemySlotJSON = enemyTeamJSON.getJSONObject(j);
          teamPanel.add(new PartyPanel(this, enemySlotJSON));
        }
        teamPanel.add(spacer());

        _teams.add(teamPanel);
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return;
    }

    enableButtons(true);
    validate();
  }

  public Dimension dimension() {
    return new Dimension(440 + ((_enemyTeams.length() - 1) * 120), 160);
  }

  public boolean key(KeyEvent arg0) {
    return false;
  }

  @Override
  public JPokemonMenu menu() {
    return null;
  }

  public JButton fightButton() {
    if (_fightButton == null) {
      _fightButton = new JPokemonButton("FIGHT");
      _fightButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          fight();
        }
      });
    }

    return _fightButton;
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

      enemySlotID = getTarget(_trainerData.getJSONObject("leader").getJSONArray("moves").getJSONObject(moveIndex).getString("name"));
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

  public JButton swapButton() {
    if (_swapButton == null) {
      _swapButton = new JPokemonButton("SWAP");
      _swapButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          swap();
        }
      });
    }

    return _swapButton;
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

  public JButton itemButton() {
    if (_itemButton == null) {
      _itemButton = new JPokemonButton("ITEM");
      _itemButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          item();
        }
      });
    }

    return _itemButton;
  }

  private void item() {
    String targetSlotID = null;
    int itemID = -1, partyIndex = -1;

    enableButtons(false);

    try {
      itemID = getItemChoice();
      if (itemID == -1) {
        enableButtons(true);
        return;
      }

      String itemName = null;
      for (int i = 0; i < _trainerData.getJSONArray("bag").length(); i++) {
        JSONObject itemJSON = _trainerData.getJSONArray("bag").getJSONObject(i);

        if (itemJSON.getInt("id") == itemID) {
          itemName = itemJSON.getString("name");
        }
      }

      targetSlotID = getTarget("Select a target to use " + itemName + " on");
      if (targetSlotID == null) {
        enableButtons(true);
        return;
      }

      // TODO : party index
      partyIndex = 0;
    } catch (Exception e) {
      enableButtons(true);
      return;
    }

    JSONObject request = new JSONObject();

    try {
      request.put("turn", "ITEM");
      request.put("id", parent().playerID());
      request.put("target", targetSlotID);
      request.put("target_index", partyIndex);
      request.put("item", itemID);
    } catch (JSONException e) {
      e.printStackTrace();
      enableButtons(true);
      return;
    }

    submitTurn(request);
  }

  public JButton runButton() {
    if (_runButton == null) {
      _runButton = new JPokemonButton("RUN");
      _runButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          run();
        }
      });
    }

    return _runButton;
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
      PlayerManager.activityRequest(request);
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
      image = ImageLoader.find("pkmn/" + leader.getInt("number"));
      for (int i = 0; i < moves.length(); i++)
        move_names.add(moves.getJSONObject(i).getString("name"));
    } catch (JSONException e) {
      return -1;
    }

    int answer = JOptionPane.showOptionDialog(parent(), prompt, "MOVE CHOICE", 0, 0, image, move_names.toArray(), null);

    return answer;
  }

  private String getTarget(String prompt) throws JSONException {
    ImageIcon image = null;
    List<String> slotNames = new ArrayList<String>();
    List<String> slotIds = new ArrayList<String>();

    try {
      image = ImageLoader.find("pkmn/" + _trainerData.getJSONObject("leader").getInt("number"));

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
      answer = slotIds.get(JOptionPane.showOptionDialog(parent(), prompt, "SELECT TARGET", 0, 0, image, slotNames.toArray(), null));

    return answer;
  }

  private int getSwapIndex() throws JSONException {
    ImageIcon image = null;
    List<String> names = new ArrayList<String>();

    try {
      JSONObject leader = _trainerData.getJSONObject("leader");
      JSONArray party = _data.getJSONArray("pokemon");

      image = ImageLoader.find("pkmn/" + leader.getInt("number"));
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
    List<String> itemTypes = new ArrayList<String>();
    Map<String, List<JSONObject>> itemsPerType = new HashMap<String, List<JSONObject>>();

    for (int i = 0; i < items.length(); i++) {
      JSONObject itemJSON = items.getJSONObject(i);
      String itemType = itemJSON.getString("type");
      itemTypes.add(itemType);

      if (itemsPerType.get(itemType) == null) {
        itemsPerType.put(itemType, new ArrayList<JSONObject>());
      }

      itemsPerType.get(itemType).add(itemJSON);
    }

    int itemTypeChoice = JOptionPane.showOptionDialog(parent(), "Select an item to use", "ITEM CHOICE", 0, JOptionPane.QUESTION_MESSAGE, null, itemTypes.toArray(), null);

    if (itemTypeChoice == -1)
      return -1;

    List<JSONObject> itemsInSelectedType = itemsPerType.get(itemTypes.get(itemTypeChoice));

    List<ImageIcon> itemIconsInType = new ArrayList<ImageIcon>();

    for (int i = 0; i < itemsInSelectedType.size(); i++) {
      JSONObject itemJSON = itemsInSelectedType.get(i);
      String itemName = itemJSON.getString("name");
      String itemType = itemJSON.getString("type");

      itemIconsInType.add(ImageLoader.item(itemType, itemName));
    }

    int itemChoice = JOptionPane.showOptionDialog(parent(), "Select an item to use", "ITEM CHOICE", 0, JOptionPane.QUESTION_MESSAGE, null, itemIconsInType.toArray(), null);

    if (itemChoice == -1)
      return -1;

    return itemsInSelectedType.get(itemChoice).getInt("id");
  }

  private JPanel _teams;
  private JSONArray _enemyTeams;
  private JSONObject _data, _trainerData;
  private JButton _fightButton, _itemButton, _swapButton, _runButton;
  private static final long serialVersionUID = 1L;

}