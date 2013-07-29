package com.jpokemon.start;

import javax.swing.JLabel;

import org.jpokemon.activity.PlayerManager;
import org.jpokemon.activity.ServiceException;
import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.JPokemonMenuEntry;

public class StartMenuEntry extends JPokemonMenuEntry {
  public StartMenuEntry(StartMenu s, StartEntryValue v) {
    super(s);
    _value = v;

    try {
      _showPokemonPlz = new JSONObject();
      _showPokemonPlz.put("id", parent().parent().playerID());

      _savePlz = new JSONObject();
      _savePlz.put("id", parent().parent().playerID());
      _savePlz.put("option", "save");
    } catch (JSONException e) {
    }

    add(new JLabel(v.toString()));
  }

  public void action() {
    switch (_value) {
    case SAVE:
      try {
        PlayerManager.activityRequest(_savePlz);
        parent().refresh();
      } catch (ServiceException e) {
        e.printStackTrace();
      }
    break;
    case QUIT:
      parent().parent().dispose();
    }
  }

  private StartEntryValue _value;
  private JSONObject _showPokemonPlz, _savePlz;

  private static final long serialVersionUID = 1L;
}