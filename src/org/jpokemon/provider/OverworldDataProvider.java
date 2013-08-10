package org.jpokemon.provider;

import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class OverworldDataProvider {
  private OverworldDataProvider() {
  }

  public static JSONObject generate(Player player) {
    JSONObject json = new JSONObject();

    try {
      json.put("action", "overworld");
      json.put("map", "myarea");
    } catch (JSONException e) {
    }

    return json;
  }
}