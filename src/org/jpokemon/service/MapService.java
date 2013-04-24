package org.jpokemon.service;

import org.jpokemon.battle.BattleRegistry;
import org.jpokemon.map.Area;
import org.jpokemon.map.Border;
import org.jpokemon.map.npc.NPC;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.Trainer;
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

  public static void border(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Border border = getBorder(request);

    String reason = border.isOkay(player);
    if (reason == null) {
      player.area(border.getNext());
    }
    else {
      throw new ServiceException(reason);
    }
  }

  public static void grass(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Area area = getArea(request);

    Pokemon pokemon = area.pokemon();

    if (pokemon == null)
      throw new ServiceException("No wild pokemon in this area");

    Trainer trainer = new Trainer();
    trainer.add(pokemon);

    BattleRegistry.start(player, trainer);
  }
}