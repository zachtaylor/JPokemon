package org.jpokemon.trainer;

import org.jpokemon.service.BattleService;
import org.jpokemon.service.MapService;
import org.jpokemon.service.ServiceException;
import org.json.JSONException;
import org.json.JSONObject;

public enum TrainerState {
  OVERWORLD, BATTLE, UPGRADE;

  public void appendDataToResponse(JSONObject response, JSONObject request, Player player) throws JSONException, ServiceException {
    Object extraData = JSONObject.NULL;

    switch (this) {
    case BATTLE:
      extraData = BattleService.info(request);
      break;
    case UPGRADE:
      extraData = player.toJSON(UPGRADE);
      break;
    case OVERWORLD:
      extraData = MapService.info(request);
      break;
    }

    response.put(name().toLowerCase(), extraData);
  }
}