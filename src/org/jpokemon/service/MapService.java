package org.jpokemon.service;

import org.jpokemon.map.Area;
import org.jpokemon.map.npc.NPC;
import org.jpokemon.trainer.Player;
import org.json.JSONObject;

public class MapService extends JPokemonService {
  public static JSONObject info(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Area area = getArea(request);

    return area.toJSON(player);
  }

  public static void npc(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    NPC npc = getNpc(request);
    String option = getOption(request);

    npc.actionset(option).execute(player);
  }
}