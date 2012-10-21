package org.jpokemon.pokemon.stat;

public enum StatType {
  HEALTH, ATTACK, SPECATTACK, DEFENSE, SPECDEFENSE, SPEED;

  public static StatType valueOf(int s) {
    return StatType.values()[s];
  }
}