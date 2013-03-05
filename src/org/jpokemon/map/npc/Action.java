package org.jpokemon.map.npc;

import org.jpokemon.trainer.Player;

public class Action {
  public Action(int type, int data) {
    _type = ActionType.valueOf(type);
    _data = data;
  }

  public void execute(Player p) {
    // TODO execute an action on player
  }

  public int data() {
    return _data;
  }

  public ActionType type() {
    return _type;
  }

  private int _data;
  private ActionType _type;
}