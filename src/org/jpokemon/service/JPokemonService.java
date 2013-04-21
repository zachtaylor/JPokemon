package org.jpokemon.service;

import org.jpokemon.battle.Battle;
import org.jpokemon.battle.BattleRegistry;
import org.jpokemon.map.Area;
import org.jpokemon.map.Map;
import org.jpokemon.map.npc.NPC;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JPokemonService {
  private static final String PLAYER_ID_KEY = "id", NPC_NUMBER_KEY = "number", POKEMON_INDEX_KEY = "pokemon_index",
      NPC_OPTION_KEY = "option";

  protected static Player getPlayer(JSONObject request) throws ServiceException {
    int playerID = -1;
    Player player = null;

    try {
      playerID = request.getInt(PLAYER_ID_KEY);
    } catch (JSONException e) {
      throw new ServiceException("Player key not found");
    }

    player = PlayerFactory.get(playerID);

    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    return player;
  }

  protected static Pokemon getPokemon(JSONObject request) throws ServiceException {
    int pokemonIndex = -1;
    Pokemon pokemon = null;
    Player player = null;

    player = getPlayer(request);

    try {
      pokemonIndex = request.getInt(POKEMON_INDEX_KEY);
    } catch (JSONException e) {
      throw new ServiceException("Pokemon index not provided");
    }

    try {
      pokemon = player.party().get(pokemonIndex);
    } catch (IllegalArgumentException e) {
      throw new ServiceException("Pokemon index " + pokemonIndex + " not found");
    }

    return pokemon;
  }

  protected static Battle getBattle(JSONObject request) throws ServiceException {
    Battle battle = null;
    Player player = null;

    player = getPlayer(request);

    battle = BattleRegistry.get(player);

    if (battle == null)
      throw new ServiceException("Battle for player " + player.toString() + " not found");

    return battle;
  }

  protected static Area getArea(JSONObject request) throws ServiceException {
    Area area = null;
    Player player = null;

    player = getPlayer(request);

    area = Map.area(player.area());

    if (area == null)
      throw new ServiceException("Area number " + player.area() + " not found");

    return area;
  }

  protected static NPC getNpc(JSONObject request) throws ServiceException {
    int npcNumber = -1;
    NPC npc = null;
    Area area = null;

    area = getArea(request);

    try {
      npcNumber = request.getInt(NPC_NUMBER_KEY);
    } catch (JSONException e) {
      throw new ServiceException("Npc number not found");
    }

    npc = area.getNpc(npcNumber);

    if (npc == null)
      throw new ServiceException("Npc number " + npcNumber + " not found");

    return npc;
  }

  protected static String getOption(JSONObject request) throws ServiceException {
    String option = null;

    try {
      option = request.getString(NPC_OPTION_KEY);
    } catch (JSONException e) {
      throw new ServiceException("NPC option not found");
    }

    return option;
  }
}