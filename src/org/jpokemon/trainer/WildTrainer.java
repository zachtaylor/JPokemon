package org.jpokemon.trainer;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;

public class WildTrainer implements PokemonTrainer {
  public String id() {
    return "wild_pokemon";
  }

  public String getName() {
    return "Wild Pokemon";
  }

  public void setName(String s) {
  }

  public PokemonStorageUnit party() {
    return _party;
  }

  public boolean add(Pokemon p) {
    return party().add(p);
  }

  public void notify(String... message) {
  }

  private PokemonStorageUnit _party = new PokemonStorageUnit();
}