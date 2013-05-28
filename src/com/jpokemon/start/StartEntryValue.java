package com.jpokemon.start;

public enum StartEntryValue {
  POKEMON, SAVE, QUIT;

  public static StartEntryValue valueOf(int v) {
    return values()[v];
  }
}