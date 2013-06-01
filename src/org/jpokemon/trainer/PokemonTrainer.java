package org.jpokemon.trainer;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;

public interface PokemonTrainer {
  public String id();

  public String name();

  public void name(String s);

  public double xpFactor();

  public PokemonStorageUnit party();

  public boolean add(Pokemon p);
}