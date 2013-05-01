package org.jpokemon.action;

import org.jpokemon.trainer.Player;

public class EventAction extends Action {
  public EventAction(String data) {
    super(data);
  }

  public void execute(Player player) {
    player.events().put(Integer.parseInt(data()));
  }
}