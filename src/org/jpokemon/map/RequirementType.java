package org.jpokemon.map;

public enum RequirementType {
  POKEDEX_COUNT, POKEMON_MOVE, EVENT;

  public static RequirementType valueOf(int rt) {
    return values()[rt];
  }
}