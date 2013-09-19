package org.jpokemon.pokemon;

import org.jpokemon.activity.Activity;
import org.jpokemon.pokemon.stat.StatType;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpgradeActivity implements Activity {
  @Override
  public void onAdd(Player player) {
    // TODO Auto-generated method stub
  }

  @Override
  public void beforeRemove(Player player) {
  }

  @Override
  public void onReturn(Activity activity, Player player) {
    // TODO Auto-generated method stub
  }

  @Override
  public void serve(JSONObject request, Player player) throws ServiceException {
    Pokemon pokemon = getPokemon(player, request);

    try {
      JSONArray stats = request.getJSONArray("stats");

      for (int i = 0; i < stats.length(); i++) {
        StatType s = StatType.valueOf(stats.getJSONObject(i).getString("stat"));
        pokemon.statPoints(s, stats.getJSONObject(i).getInt("amount"));
      }
    }
    catch (JSONException e) {
    }
  }

  private static Pokemon getPokemon(Player player, JSONObject request) throws ServiceException {
    int pokemonIndex = -1;
    Pokemon pokemon = null;

    try {
      pokemonIndex = request.getInt("pokemon");
    }
    catch (JSONException e) {
      throw new ServiceException("Pokemon index not provided");
    }

    try {
      pokemon = player.party().get(pokemonIndex);
    }
    catch (IllegalArgumentException e) {
      throw new ServiceException("Pokemon index " + pokemonIndex + " not found");
    }

    return pokemon;
  }
}