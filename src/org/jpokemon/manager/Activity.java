package org.jpokemon.manager;

import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public interface Activity {
  public void handleRequest(Player player, JSONObject data) throws JSONException, ServiceException;
}