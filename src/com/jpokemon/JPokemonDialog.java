package com.jpokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

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

  private JSONObject _data;
  private GameWindow _parent;
}