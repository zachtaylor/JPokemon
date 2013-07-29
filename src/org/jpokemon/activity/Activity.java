package org.jpokemon.activity;

import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public interface Activity {
  public void onAdd(Player player);

  public void onRemove(Player player);

  public void handleRequest(Player player, JSONObject data) throws JSONException, ServiceException;
}