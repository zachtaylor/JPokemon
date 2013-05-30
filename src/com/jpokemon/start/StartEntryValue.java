package com.jpokemon.start;

public enum StartEntryValue {
  SAVE, QUIT;

  public static StartEntryValue valueOf(int v) {
    return values()[v];
  }
}