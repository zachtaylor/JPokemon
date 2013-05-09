package org.jpokemon.upgrade;

import org.jpokemon.activity.Activity;
import org.jpokemon.service.JPokemonService;
import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class UpgradeActivity extends JPokemonService implements Activity {
  @Override
  public String getName() {
    return "upgrade";
  }

  @Override
  public UpgradeService getHandler() {
    return UpgradeService.getInstance();
  }

  @Override
  public void appendDataToResponse(JSONObject response, JSONObject request, Player player) throws JSONException, ServiceException {
    response.put(getName(), player.toJSON());
  }
}