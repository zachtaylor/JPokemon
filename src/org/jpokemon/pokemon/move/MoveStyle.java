package org.jpokemon.pokemon.move;

public enum MoveStyle {
  PHYSICAL, SPECIAL, OHKO, STATUS, REPEAT, DELAYNEXT, DELAYBEFORE, MISC;

  public static MoveStyle valueOf(int style) {
    return values()[style];
  }
}