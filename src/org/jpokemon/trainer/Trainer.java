package org.jpokemon.trainer;

import jpkmn.exceptions.LoadException;
import jpkmn.game.base.AIInfo;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class Trainer implements PokemonTrainer, JPokemonConstants {
  public Trainer() {
    _id = 0;
    _name = "Mock Player";
    _type = TrainerType.WILD;
    _party = new PokemonStorageUnit(TRAINER_PARTY_SIZE);
  }

  public Trainer(int ai_number) throws LoadException {
    this();
    AIInfo info = AIInfo.get(ai_number);

    _id = -ai_number;
    _name = info.getName();
    _cash = info.getCash();
    _type = TrainerType.valueOf(info.getType());

    for (String entry : info.getPokemon())
      add(Pokemon.load(entry));
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
      return GYM_EXPERIENCE_MODIFIER;
    case RIVAL:
    case TRAINER:
      return TRAINER_EXPERIENCE_MODIFIER;
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

  public void setState(String state) { // Do nothing
  }

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();

    try {
      data.put("id", id());
      data.put("name", name());
      data.put("cash", cash());
      data.put("badges", JSONObject.NULL);
      data.put("area", JSONObject.NULL);
      data.put("bag", JSONObject.NULL);
      data.put("pokedex", JSONObject.NULL);
      data.put("progress", JSONObject.NULL);
      data.put("pokemon", new JSONObject().put("party", party().toJSON()));

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