package org.jpokemon.map;

public enum RequirementType {
  EVENT;

  public static RequirementType valueOf(int rt) {
    return values()[rt];
  }
}