package org.jpokemon.activity;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.stat.StatType;
import org.jpokemon.service.JPokemonService;
import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpgradeActivity extends JPokemonService implements Activity {
  @Override
  public String getName() {
    return "upgrade";
  }

  @Override
  public void handleRequest(JSONObject request) throws ServiceException {
    Pokemon pokemon = getPokemon(request);

    try {
      JSONArray stats = request.getJSONArray("stats");
      for (int i = 0; i < stats.length(); i++) {
        StatType s = StatType.valueOf(stats.getJSONObject(i).getString("stat"));
        pokemon.statPoints(s, stats.getJSONObject(i).getInt("amount"));
      }
    } catch (JSONException e) {
    }
  }

  @Override
  public void appendDataToResponse(JSONObject response, JSONObject request, Player player) throws JSONException,
      ServiceException {
    response.put(getName(), player.toJSON());
  }
}