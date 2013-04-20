package org.jpokemon.service;

import org.jpokemon.map.Area;
import org.jpokemon.map.Map;
import org.jpokemon.map.npc.NPC;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class MapService {
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
    try {
      int playerID = request.getInt("id");
      int npcNumber = request.getInt("number");

      Player player = PlayerFactory.get(request.getInt("id"));

      if (player == null)
        throw new ServiceException("PlayerID " + playerID + " not found");

      Area area = Map.area(player.area());

      if (area == null)
        throw new ServiceException("Player " + player.name() + " in invalid area");

      NPC npc = area.getNpc(npcNumber);

      if (npc == null)
        throw new ServiceException("NPC " + npcNumber + " not in current area");

      npc.action(player).execute(player);

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}