package org.jpokemon.map.npc;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.action.ActionSet;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class NPC {
  public static NPC get(int number) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      List<NPC> query = SqlStatement.select(NPC.class).where("number").eq(number).getList();

      if (query.size() > 0)
        return query.get(0);
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int n) {
    number = n;
  }

  public NPCType getType() {
    return npctype;
  }

  public int getTypeAsInt() {
    return type;
  }

  public void setType(int t) {
    npctype = NPCType.get(t);
  }

  public String getName() {
    return name.replace("{typename}", getType().getName());
  }

  public void setName(String n) {
    name = n;
  }

  public String getIcon() {
    return getType().getIcon();
  }

  public void addActionSet(ActionSet actionset) {
    _actions.add(actionset);
  }

  public ActionSet actionset(String option) {
    for (ActionSet as : _actions) {
      if (option.equals(as.getOption())) {
        return as;
      }
    }

    return null;
  }

  public JSONObject toJSON(Player p) {
    JSONObject json = new JSONObject();

    try {
      json.put("name", getName());
      json.put("number", getNumber());
      json.put("icon", getIcon());

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
    return "NPC#" + number + ": " + getName();
  }

  private String name;
  private int number, type;
  private NPCType npctype;
  private List<ActionSet> _actions = new ArrayList<ActionSet>();
}