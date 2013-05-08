package org.jpokemon.map;

import org.jpokemon.activity.Activity;
import org.jpokemon.service.JPokemonService;
import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class OverworldActivity extends JPokemonService implements Activity {
  private OverworldActivity() {
  }

  public static OverworldActivity getInstance() {
    return instance;
  }

  public String getName() {
    return "overworld";
  }

  @Override
  public MapService getHandler() {
    return MapService.getInstance();
  }

  @Override
  public void appendDataToResponse(JSONObject response, JSONObject request, Player player) throws JSONException, ServiceException {
    Area area = getArea(request);

    response.put(getName(), area.toJSON(player));
  }

  public static OverworldActivity instance = new OverworldActivity();
}