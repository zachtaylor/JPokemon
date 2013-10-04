package org.jpokemon.trainer;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;

public interface PokemonTrainer {
  public String id();

  public PokemonStorageUnit party();

  public boolean add(Pokemon p);
}