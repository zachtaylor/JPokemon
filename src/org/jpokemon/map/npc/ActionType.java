package org.jpokemon.map.npc;

public enum ActionType {
  SPEECH;

  public static ActionType valueOf(int t) {
    return values()[t];
  }
}