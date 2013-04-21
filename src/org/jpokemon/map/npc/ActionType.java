package org.jpokemon.map.npc;

public enum ActionType {
  SPEECH, EVENT, ITEM;

  public static ActionType valueOf(int t) {
    return values()[t];
  }
}