package org.jpokemon.service;

import org.jpokemon.map.Area;
import org.jpokemon.map.Map;
import org.jpokemon.map.npc.NPC;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;
import org.json.JSONObject;

public class MapService extends JPokemonService {
  public static JSONObject info(int playerID) throws ServiceException {
    Player player = PlayerFactory.get(playerID);

    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    Area area = Map.area(player.area());

    if (area == null)
      throw new ServiceException("Player " + player.name() + " in invalid area");

    return area.toJSON(player);
  }

  public static void npc(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    NPC npc = getNpc(request);

    npc.action(player).execute(player);
  }
}