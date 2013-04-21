package org.jpokemon.trainer;

import org.jpokemon.service.BattleService;
import org.jpokemon.service.MapService;
import org.jpokemon.service.ServiceException;
import org.json.JSONObject;

public enum TrainerState {
  OVERWORLD, BATTLE, UPGRADE;

  public String root() {
    return name().toLowerCase();
  }

  public JSONObject relatedData(JSONObject request, Player p) throws ServiceException {
    switch (this) {
    case BATTLE:
      return BattleService.info(request);
    case UPGRADE:
      return p.toJSON(p.state());
    case OVERWORLD:
      return MapService.info(request);
    }
    return null;
  }
}