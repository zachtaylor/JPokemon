package org.jpokemon.activity;

import org.jpokemon.service.MapService;
import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class OverworldActivity implements Activity {
  private OverworldActivity() {
  }

  public String getName() {
    return "overworld";
  }

  public void appendDataToResponse(JSONObject response, JSONObject request, Player player) throws JSONException, ServiceException {
    response.put(getName(), MapService.info(request));
  }

  public static OverworldActivity getInstance() {
    if (instance == null)
      instance = new OverworldActivity();

    return instance;
  }

  public static OverworldActivity instance;
}