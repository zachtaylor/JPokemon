package org.jpokemon.activity;

import org.jpokemon.JPokemonVisitor;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.stat.Stat;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpgradeDataGenerator extends JPokemonVisitor {
  private JSONArray party, stats;
  private JSONObject data = new JSONObject();

  public UpgradeDataGenerator(Player player) throws JSONException {
    try {
      visit(player);
    } catch (Exception e) {
    }
  }

  @Override
  public void visit_party(PokemonStorageUnit unit) throws JSONException {
    data.put("party", party = new JSONArray());

    try {
      super.visit_party(unit);
    } catch (Exception e) {
    }
  }

  @Override
  public void visit(PokemonStorageUnit unit) throws JSONException { // Nothing
  }

  @Override
  public void visit(Pokemon pokemon) throws JSONException {
    JSONObject json = new JSONObject();

    json.put("name", pokemon.name());
    json.put("number", pokemon.number());
    json.put("points", pokemon.availableStatPoints());
    json.put("stats", stats = new JSONArray());

    party.put(json);

    try {
      super.visit(pokemon);
    } catch (Exception e) {
    }
  }

  @Override
  public void visit(Stat stat) throws JSONException {
    JSONObject json = new JSONObject();

    json.put("name", last_stat_type().toString());
    json.put("max", stat.max());
    json.put("ev", stat.ev());
    json.put("points", stat.points());
    json.put("iv", stat.iv());

    stats.put(json);

    try {
      super.visit(stat);
    } catch (Exception e) {
    }
  }
}