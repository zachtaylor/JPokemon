package org.jpokemon.service;

import org.jpokemon.battle.Battle;
import org.jpokemon.trainer.Player;
import org.json.JSONObject;

public class BattleService extends JPokemonService {
  public static void turn(JSONObject request) throws ServiceException {
    Battle battle = getBattle(request);

    battle.createTurn(request);
  }

  public static JSONObject info(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Battle battle = getBattle(request);

    return battle.toJSON(player);
  }
}