package org.jpokemon.map.npc;

import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NPC implements JPokemonConstants {
  public NPC(NPCInfo info) {
    _info = info;

    _type = NPCType.get(info.getType());
  }

  public int getNumber() {
    return _info.getNumber();
  }

  public String shortName() {
    return _info.getName();
  }

  public String longName() {
    if (_info.usePrefix())
      return _type.getName() + " " + shortName();
    return shortName();
  }

  public String icon() {
    return _type.getIcon();
  }

  public NPCType type() {
    return _type;
  }

  public void actionsets(List<ActionSet> actions) {
    _actions = actions;
  }

  public ActionSet actionset(String action) {
    ActionSet actions = null;

    for (ActionSet as : _actions) {
      if (action.equals(as.getOption())) {
        actions = as;
        break;
      }
    }

    return actions;
  }

  public JSONObject toJSON(Player p) {
    JSONObject json = new JSONObject();

    try {
      json.put("name", longName());
      json.put("number", _info.getNumber());
      json.put("icon", _type.getIcon());

      JSONArray options = new JSONArray();
      for (ActionSet actionset : _actions) {
        if (actionset.isOkay(p)) {
          options.put(actionset.getOption());
        }
      }
      json.put("options", options);

    } catch (JSONException e) {
      e.printStackTrace();
    }

    return json;
  }

  public String toString() {
    return "NPC#" + _info.getNumber() + ": " + longName();
  }

  private NPCType _type;
  private NPCInfo _info;
  private List<ActionSet> _actions;
}