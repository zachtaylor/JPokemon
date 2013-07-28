package org.jpokemon.manager.component;

import org.jpokemon.manager.JPokemonServer;
import org.jpokemon.overworld.map.Area;
import org.jpokemon.overworld.map.Border;
import org.jpokemon.overworld.map.Map;
import org.jpokemon.overworld.npc.NPC;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OverworldServer extends JPokemonServer {
  public OverworldServer(Player player) {
    super(player);

    visit(player);
  }

  @Override
  public void visit(Player player) {
    Area area = Map.area(player.getArea());

    npcs_json = new JSONArray();
    for (NPC npc : area.npcs()) {
      visit(npc);
    }

    borders_json = new JSONArray();
    for (Border border : area.borders()) {
      visit(border);
    }

    try {
      data().put("name", area.getName());
      data().put("npcs", npcs_json);
      data().put("borders", borders_json);
      data().put("has_wild_pokemon", area.wildPokemon().size() > 0);
    } catch (JSONException e) {
    }
  }

  private void visit(NPC npc) {
    JSONObject json = new JSONObject();

    try {
      json.put("name", npc.getNameFormatted());
      json.put("number", npc.getNumber());
      json.put("icon", npc.getIcon());
      json.put("options", new JSONArray());

      for (String option : npc.getOptionsForPlayer(get_calling_player())) {
        json.getJSONArray("options").put(option);
      }
    } catch (JSONException e) {
    }

    npcs_json.put(json);
  }

  private void visit(Border border) {
    JSONObject json = new JSONObject();

    Area nextArea = Map.area(border.getNext());

    try {
      json.put("name", nextArea.getName());

      if (border.isOkay(get_calling_player())) {
        json.put("is_okay", true);
      }
      else {
        json.put("is_okay", false);
        json.put("reason", "You cannot go that way");
      }
    } catch (JSONException e) {
    }

    borders_json.put(json);
  }

  private JSONArray npcs_json, borders_json;
}