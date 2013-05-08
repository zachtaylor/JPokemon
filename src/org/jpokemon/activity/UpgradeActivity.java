package org.jpokemon.activity;

import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class UpgradeActivity implements Activity {

  @Override
  public String getName() {
    return "upgrade";
  }

  @Override
  public void appendDataToResponse(JSONObject response, JSONObject request, Player player) throws JSONException, ServiceException {
    response.put(getName(), player.toJSON());
  }
}