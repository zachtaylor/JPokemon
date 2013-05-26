package org.jpokemon.action;

import org.jpokemon.map.Event;
import org.jpokemon.trainer.Player;

public class EventAction extends Action {
  public EventAction(String data) {
    super(data);
  }

  public void execute(Player player) {
    Event event = Event.get(Integer.parseInt(data()));
    player.record().putEvent(event.getNumber());
  }
}