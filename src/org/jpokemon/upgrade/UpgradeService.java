package org.jpokemon.upgrade;

import org.jpokemon.activity.ActivityService;
import org.jpokemon.activity.ActivityTracker;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.stat.StatType;
import org.jpokemon.service.JPokemonService;
import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpgradeService extends JPokemonService implements ActivityService {
  private static final String POKEMON_INDEX_KEY = "pokemon_index";

  private UpgradeService() {
  }

  public static UpgradeService getInstance() {
    return instance;
  }

  @Override
  public void handleRequest(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Pokemon pokemon = getPokemon(player, request);

    try {
      JSONArray stats = request.getJSONArray("stats");
      for (int i = 0; i < stats.length(); i++) {
        StatType s = StatType.valueOf(stats.getJSONObject(i).getString("stat"));
        pokemon.statPoints(s, stats.getJSONObject(i).getInt("amount"));
      }
    } catch (JSONException e) {
    }

    ActivityTracker.clearActivity(player);
  }

  @Override
  public void handleRequestOption(String option, JSONObject request) throws ServiceException {
    throw new ServiceException("Upgrades have no options");
  }

  private static Pokemon getPokemon(Player player, JSONObject request) throws ServiceException {
    int pokemonIndex = -1;
    Pokemon pokemon = null;

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

  private static UpgradeService instance = new UpgradeService();
}