package org.jpokemon.trainer;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.json.JSONObject;

public interface PokemonTrainer {
  public int id();

  public String name();

  public void name(String s);

  public int cash();

  public void cash(int c);

  public int area();

  public TrainerType type();

  public double xpFactor();

  public PokemonStorageUnit party();

  public boolean add(Pokemon p);

  public void notify(String... message);

  public void state(TrainerState state);

  public JSONObject toJSON(TrainerState state);
}