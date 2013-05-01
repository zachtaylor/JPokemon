package org.jpokemon.action;

public enum ActionType {
  SPEECH, EVENT, ITEM, TRANSPORT, POKEMON, BATTLE;

  public static ActionType valueOf(int t) {
    return values()[t];
  }
}