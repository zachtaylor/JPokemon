package org.jpokemon.service;

import org.jpokemon.battle.Battle;
import org.jpokemon.battle.BattleRegistry;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;
import org.json.JSONObject;

public class BattleService extends JPokemonService {
  public static void turn(JSONObject request) throws ServiceException {
    Battle battle = getBattle(request);

    battle.createTurn(request);
  }

  public static JSONObject info(int playerID) throws ServiceException {
    Player player = PlayerFactory.get(playerID);

    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    Battle battle = BattleRegistry.get(player);

    if (battle == null)
      throw new ServiceException(player.name() + " is not in a battle");

    return battle.toJSON(player);
  }
}