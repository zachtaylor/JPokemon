package org.jpokemon.trainer;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class Trainer implements PokemonTrainer {
  public Trainer() {
    _id = 0;
    _name = "Mock Player";
    _type = TrainerType.WILD;
    _party = new PokemonStorageUnit(JPokemonConstants.TRAINER_PARTY_SIZE);
  }

  public int id() {
    return _id;
  }

  public String name() {
    return _name;
  }

  public void name(String name) {
    _name = name;
  }

  public int cash() {
    return _cash;
  }

  public void cash(int cash) {
    _cash = cash;
  }

  public int area() {
	return -1;
  }

  public TrainerType type() {
    return _type;
  }

  public double xpFactor() {
    switch (_type) {
    case GYM:
      return JPokemonConstants.GYM_EXPERIENCE_MODIFIER;
    case RIVAL:
    case TRAINER:
      return JPokemonConstants.TRAINER_EXPERIENCE_MODIFIER;
    default:
      return 1.0;
    }
  }

  public PokemonStorageUnit party() {
    return _party;
  }

  public boolean add(Pokemon p) {
    return party().add(p);
  }

  public void notify(String... message) {
    return;
  }

  public void state(TrainerState state) { // Do nothing
  }

  public JSONObject toJSON(TrainerState state) {
    JSONObject data = new JSONObject();

    try {
      if (state == TrainerState.BATTLE) {
        data.put("id", id());
        data.put("leader", party().get(0).toJSON(state));
        data.put("party", party().toJSON(state));
      }
      
    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  public boolean equals(Object o) {
    if (!(o instanceof Trainer))
      return false;
    return ((Trainer) o)._id == _id;
  }

  private String _name;
  private int _id, _cash;
  private TrainerType _type;
  private PokemonStorageUnit _party;
}