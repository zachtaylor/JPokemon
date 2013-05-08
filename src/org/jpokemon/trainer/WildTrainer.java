package org.jpokemon.trainer;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class WildTrainer implements PokemonTrainer {
  public String id() {
    return "wild_pokemon";
  }

  public String name() {
    return "Wild Pokemon";
  }

  public void name(String s) {
  }

  public double xpFactor() {
    return 0;
  }

  public PokemonStorageUnit party() {
    return _party;
  }

  public boolean add(Pokemon p) {
    return party().add(p);
  }

  public void notify(String... message) {
  }

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();

    try {
      data.put("id", id());
      data.put("leader", party().get(0).toJSON());
      data.put("party", party().toJSON());

    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  private PokemonStorageUnit _party = new PokemonStorageUnit(JPokemonConstants.TRAINER_PARTY_SIZE);
}