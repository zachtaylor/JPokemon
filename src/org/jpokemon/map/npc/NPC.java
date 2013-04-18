package org.jpokemon.map.npc;

import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class NPC implements JPokemonConstants {
  public NPC(NPCInfo info) {
    _info = info;

    _type = NPCType.get(info.getType());
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

  public void actions(List<ActionSet> actions) {
    _actions = actions;
  }

  public ActionSet action(Player p) {
    ActionSet actions = null;

    for (ActionSet as : _actions) {
      if (as.isOkay(p)) {
        // Pick the last one such that isOkay(p)
        actions = as;
      }
    }

    return actions;
  }

  public JSONObject toJSON(Player p) {
    JSONObject json = new JSONObject();

    try {
      json.put("name", longName());
      json.put("id", _info.getNumber());
      json.put("icon", _type.getIcon());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return json;
  }

  public String toString() {
    return "NPC#"+ _info.getNumber() + ": " + longName();
  }

  private NPCType _type;
  private NPCInfo _info;
  private List<ActionSet> _actions;
}