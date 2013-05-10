package org.jpokemon.upgrade;

import org.jpokemon.activity.ActivityServer;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.stat.Stat;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpgradeServer extends ActivityServer {
  public UpgradeServer(Player player) {
    super(player);
  }

  public void visit_party(PokemonStorageUnit unit) {
    try {
      data().put("party", party = new JSONArray());
    } catch (JSONException e) {
      return;
    }

    super.visit_storage_unit(unit);
  }

  public void visit_storage_unit(PokemonStorageUnit unit) { // Nothing
  }

  public void visit_pokemon(Pokemon pokemon) {
    JSONObject json = new JSONObject();

    try {
      json.put("name", pokemon.name());
      json.put("number", pokemon.number());
      json.put("points", pokemon.availableStatPoints());
      json.put("stats", stats = new JSONArray());
    } catch (JSONException e) {
      return;
    }

    party.put(json);

    super.visit_pokemon(pokemon);
  }

  public void visit_stat(Stat stat) {
    JSONObject json = new JSONObject();

    try {
      json.put("name", last_stat_type().toString());
      json.put("max", stat.max());
      json.put("ev", stat.ev());
      json.put("points", stat.points());
      json.put("iv", stat.iv());
    } catch (JSONException e) {
      return;
    }

    stats.put(json);

    super.visit_stat(stat);
  }

  private JSONArray party, stats;
}