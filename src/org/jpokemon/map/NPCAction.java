package org.jpokemon.map;

import org.jpokemon.trainer.Player;

public class NPCAction {
  public void execute(Player p) {
    // TODO execute a npcaction on player
  }

  public int data() {
    return _data;
  }

  public void data(int d) {
    _data = d;
  }

  public NPCActionType type() {
    return _type;
  }

  public void type(NPCActionType t) {
    _type = t;
  }

  private int _data;
  private NPCActionType _type;
}