package org.jpokemon.map;

public enum RequirementType {
  EVENT, POKEDEX;

  public static RequirementType valueOf(int rt) {
    return values()[rt];
  }
}