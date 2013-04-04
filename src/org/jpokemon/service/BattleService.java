package org.jpokemon.service;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.battle.Battle;
import org.jpokemon.battle.BattleRegistry;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class BattleService implements JPokemonConstants {
  public static void turn(JSONObject json) {
    int trainerID;

    try {
      trainerID = json.getInt("trainer");
    } catch (JSONException e) {
      e.printStackTrace();
      return;
    }

    Player trainer = PlayerFactory.get(trainerID);
    Battle battle = BattleRegistry.get(trainer);

    battle.createTurn(json);
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