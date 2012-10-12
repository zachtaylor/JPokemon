package org.jpokemon.pokedex;

public enum PokedexStatus {
  NONE, SAW, OWN;

  public static PokedexStatus valueOf(int p) {
    return values()[p];
  }
}