package com.jpokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.jpokemon.service.ImageService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JPokemonDialog {
  public JPokemonDialog(GameWindow parent) {
    _parent = parent;
  }

  public void setData(JSONObject data) {
    _data = data;
  }

  public void showMessages(JSONArray data) {
    try {
      for (int i = 0; i < data.length(); i++) {
        String message = data.getString(i);
        showAlert(message);
      }
    } catch (JSONException e) {
    }
  }

  public void showAlert(String s) {
    JOptionPane.showConfirmDialog(_parent, s, "Title", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
  }

  public int getMoveIndex() throws DialogCancelException, JSONException {

    JSONObject leader = _data.getJSONArray("pokemon").getJSONObject(0);
    JSONArray moves = leader.getJSONArray("moves");

    String prompt = "Select a move for " + leader.getString("name");
    ImageIcon image = ImageService.find("pkmn/" + leader.getInt("number"));

    List<String> move_names = new ArrayList<String>();
    for (int i = 0; i < moves.length(); i++) {
      JSONObject move = moves.getJSONObject(i);
      move_names.add(move.getString("name"));
    }

    int answer = JOptionPane.showOptionDialog(_parent, prompt, "MOVE CHOICE", 0, 0, image, move_names.toArray(), null);

    if (answer == -1)
      throw new DialogCancelException();

    return answer;
  }

  public int getMoveTarget(JSONArray enemyTeams, int moveIndex) throws DialogCancelException, JSONException {
    JSONObject leader = _data.getJSONArray("pokemon").getJSONObject(0);

    String move = leader.getJSONArray("moves").getJSONObject(moveIndex).getString("name");
    ImageIcon image = ImageService.find("pkmn/" + leader.getInt("number"));

    List<String> slot_names = new ArrayList<String>();
    List<Integer> trainerID_key = new ArrayList<Integer>();

    for (int i = 0; i < enemyTeams.length(); i++) {
      JSONArray teamData = enemyTeams.getJSONArray(i);

      for (int j = 0; j < teamData.length(); j++) {
        JSONObject trainerData = teamData.getJSONObject(j);

        slot_names.add(trainerData.getJSONObject("leader").getString("name"));
        trainerID_key.add(trainerData.getInt("id"));
      }
    }

    int answer = JOptionPane.showOptionDialog(_parent, "Select a target for " + move, "MOVE CHOICE", 0, 0, image, slot_names.toArray(), null);

    if (answer == -1)
      throw new DialogCancelException();

    return trainerID_key.get(answer);
  }

  public int getItemChoice() throws DialogCancelException, JSONException {
    JSONArray items = _data.getJSONArray("bag");

    List<String> itemTypeNames = new ArrayList<String>();
    Map<String, List<String>> itemsByType = new HashMap<String, List<String>>();
    Map<String, Integer> answerKey = new HashMap<String, Integer>();

    for (int i = 0; i < items.length(); i++) {
      JSONObject itemData = items.getJSONObject(i);
      String itemType = itemData.getString("type");
      String itemName = itemData.getString("name");
      int itemID = itemData.getInt("id");
      answerKey.put(itemName, itemID);

      if (!itemsByType.keySet().contains(itemType)) {
        itemsByType.put(itemType, new ArrayList<String>());
        itemTypeNames.add(itemType);
      }
      itemsByType.get(itemType).add(itemName);
    }

    int itemTypeChoice = JOptionPane.showOptionDialog(_parent, "Select an item to use", "ITEM CHOICE", 0, 0, null, itemTypeNames.toArray(), null);

    if (itemTypeChoice == -1)
      throw new DialogCancelException();

    String itemType = itemTypeNames.get(itemTypeChoice);
    List<String> itemsInType = itemsByType.get(itemType);

    int itemChoice = JOptionPane.showOptionDialog(_parent, "Select an item to use", "ITEM CHOICE", 0, 0, null, itemsInType.toArray(), null);

    if (itemChoice == -1)
      throw new DialogCancelException();

    String itemName = itemsInType.get(itemChoice);
    return answerKey.get(itemName);
  }

  public int getSwapIndex() throws DialogCancelException, JSONException {
    JSONArray party = _data.getJSONArray("pokemon");
    JSONObject leader = party.getJSONObject(0);

    ImageIcon image = ImageService.find("pkmn/" + leader.getInt("number"));

    List<String> names = new ArrayList<String>();
    for (int i = 1; i < party.length(); i++) {
      JSONObject pokemon = party.getJSONObject(i);
      names.add(pokemon.getString("name"));
    }

    int answer = JOptionPane.showOptionDialog(_parent, "Select a Pokemon", "SWAP CHOICE", 0, 0, image, names.toArray(), null);

    if (answer <= 0)
      throw new DialogCancelException();

    return answer;
  }

  private JSONObject _data;
  private GameWindow _parent;
}