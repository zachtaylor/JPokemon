package org.jpokemon.manager.activity;

import org.jpokemon.manager.Activity;
import org.jpokemon.manager.ServiceException;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.stat.StatType;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpgradeActivity implements Activity {
  @Override
  public void handleRequest(Player player, JSONObject request) throws JSONException, ServiceException {
    Pokemon pokemon = getPokemon(player, request);

    try {
      JSONArray stats = request.getJSONArray("stats");
      for (int i = 0; i < stats.length(); i++) {
        StatType s = StatType.valueOf(stats.getJSONObject(i).getString("stat"));
        pokemon.statPoints(s, stats.getJSONObject(i).getInt("amount"));
      }
    } catch (JSONException e) {
    }

    // PlayerManager.clearActivity(player);
  }

  private static Pokemon getPokemon(Player player, JSONObject request) throws ServiceException {
    int pokemonIndex = -1;
    Pokemon pokemon = null;

    try {
      pokemonIndex = request.getInt("pokemon");
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
}