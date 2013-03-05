package org.jpokemon.map.npc;

import org.jpokemon.trainer.Player;

public class Action {
  public void execute(Player p) {
    // TODO execute an action on player
  }

  public int data() {
    return _data;
  }

  public void data(int d) {
    _data = d;
  }

  public ActionType type() {
    return _type;
  }

  public void type(ActionType t) {
    _type = t;
  }

  private int _data;
  private ActionType _type;
}